/**
 * Authentication.java
 * 
 * Copyright : (C) 2008 by Honnix
 * Email     : hxliang1982@gmail.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.honnix.yaacs.auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.honnix.yaacs.constant.ACConstant;
import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.util.MD5;
import com.honnix.yaacs.util.StreamUtil;
import com.honnix.yaacs.util.StringUtil;

/**
 *
 */
public class Authentication
{

    private static final Log LOG = LogFactory.getLog(Authentication.class);

    private File passwdFile;

    public Authentication()
    {
        super();

        checkPasswdFile();
    }

    private void checkPasswdFile()
    {
        File file = null;
        boolean isFound = false;

        for (String entry : ACConstant.CLASS_PATH
                .split(ACConstant.PATH_SEPARATOR))
        {
            file = new File(entry, ACPropertiesConstant.PASSWD_FILE_NAME);

            if (file.exists())
            {
                isFound = true;

                break;
            }
        }

        if (isFound)
        {
            passwdFile = file;
        }
        else
        {
            passwdFile = null;
        }
    }

    private boolean handleUsingPasswdFile(String userId, String password)
    {
        boolean valid = false;
        String encodedPassword = MD5.getMD5Sum(password);

        synchronized (passwdFile)
        {
            BufferedReader br = null;

            try
            {
                br = new BufferedReader(new FileReader(passwdFile));
            }
            catch (FileNotFoundException e)
            {
                StringBuilder sb =
                        new StringBuilder("This should not happen.")
                                .append(" passwd file was deleted by someone.");

                LOG.error(sb.toString());
            }

            if (br != null)
            {
                try
                {
                    String line = null;

                    while ((line = br.readLine()) != null)
                    {
                        if (StringUtil.EMPTY_STRING.equals(line)
                                || line.charAt(0) == '#')
                        {
                            continue;
                        }

                        String[] tmp = line.split(",");

                        if (tmp != null && tmp.length == 2)
                        {
                            String savedUserId = tmp[0];
                            String savedPassword = tmp[1];

                            if (savedUserId.equals(userId)
                                    && savedPassword.equals(encodedPassword))
                            {
                                valid = true;

                                break;
                            }
                        }
                    }
                }
                catch (IOException e)
                {
                    StringBuilder sb =
                            new StringBuilder("Read passwd file error.")
                                    .append(" Make sure the file was not corrupted.");

                    LOG.error(sb.toString());
                }
                finally
                {
                    StreamUtil.closeStream(br);
                }
            }
        }

        return valid;
    }

    public boolean isValidUser(String userId, String password)
    {
        boolean valid = false;

        // If there is not passwd file, no authentication check at all.
        if (passwdFile == null)
        {
            valid = true;
        }
        else
        {
            valid = handleUsingPasswdFile(userId, password);
        }

        return valid;
    }

}
