/**
 * TestACFileHandler.java
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
package com.honnix.yaacs.core.data.processing.ac;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

import com.honnix.yaacs.constant.ACConstant;
import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.error.ACError;
import com.honnix.yaacs.exception.ACException;

/**
 *
 */
public class TestACFileHandler
    extends TestCase
{

    private ACFileHandler acFileHandler;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();

        acFileHandler = new ACFileHandler();
    }

    public void testSaveAndRead()
    {
        File file = new File("etc/yaacs.properties");
        BufferedInputStream bis = null;
        StringBuilder sb = new StringBuilder();
        String input = null;

        try
        {
            bis = new BufferedInputStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e)
        {
            fail("Could not open file etc/yaacs.properties.");
        }

        try
        {
            int length = -1;
            byte[] buffer = new byte[255];

            while ((length = bis.read(buffer)) != -1)
            {
                sb.append(new String(buffer, 0, length));
            }

            input = sb.toString();
        }
        catch (IOException e)
        {
            fail("Error reading file.");
        }

        try
        {
            bis = new BufferedInputStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e)
        {
            fail("Could not open file etc/yaacs.properties.");
        }

        boolean saveSuccess = true;

        try
        {
            acFileHandler.save(ACPropertiesConstant.REPO_PATH + ACConstant.FILE_SEPARATOR
                    + "f04b85d32f91df435c439f5723e0efbb.jpg", bis);
        }
        catch (ACException e)
        {
            assertEquals("Should be converting error.",
                    ACError.AC_FILE_CONVERTING_ERROR, e.getErrorCode());

            saveSuccess = false;
        }

        if (saveSuccess)
        {
            try
            {
                bis =
                        new BufferedInputStream(
                                acFileHandler
                                        .read(ACPropertiesConstant.REPO_PATH
                                                + ACConstant.FILE_SEPARATOR
                                                + "f04b85d32f91df435c439f5723e0efbb.jpg"));
            }
            catch (ACException e)
            {
                fail("ACException");
            }

            String output = null;

            try
            {
                int length = -1;
                byte[] buffer = new byte[255];
                sb = new StringBuilder();

                while ((length = bis.read(buffer)) != -1)
                {
                    sb.append(new String(buffer, 0, length));
                }

                output = sb.toString();
            }
            catch (IOException e)
            {
                fail("Error reading file.");
            }

            assertEquals("Not the same output and input.", output, input);
        }
    }
}
