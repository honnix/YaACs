/**
 * ACFileHandler.java
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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.error.ACError;
import com.honnix.yaacs.exception.ACException;
import com.honnix.yaacs.util.StreamUtil;

/**
 *
 */
public class ACFileHandler
{

    public ACFileHandler()
    {
        super();
    }

    private void convert(String filePath)
        throws ACException
    {
        ACFileConverter acFileConverter = null;

        try
        {
            acFileConverter =
                    (ACFileConverter) Class.forName(
                            ACPropertiesConstant.AC_FILE_CONVERTER).newInstance();
        }
        catch (Exception e)
        {
            throw new ACException(ACError.AC_FILE_CONVERTING_ERROR_MSG,
                    ACError.AC_FILE_CONVERTING_ERROR, e);
        }

        acFileConverter.convert(filePath);
    }

    public InputStream read(String filePath)
        throws ACException
    {
        File file = new File(filePath);
        InputStream inputStream = null;

        try
        {
            inputStream = new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            throw new ACException(ACError.AC_FILE_READING_ERROR_MSG,
                    ACError.AC_FILE_READING_ERROR, e);
        }

        return inputStream;
    }

    public void save(String filePath, InputStream is)
        throws ACException
    {
        BufferedOutputStream bos = null;

        try
        {
            bos = new BufferedOutputStream(new FileOutputStream(filePath));
        }
        catch (FileNotFoundException e)
        {
            throw new ACException(ACError.AC_FILE_SAVING_ERROR_MSG,
                    ACError.AC_FILE_SAVING_ERROR, e);
        }

        BufferedInputStream bis = new BufferedInputStream(is);

        try
        {
            byte[] buffer = new byte[255];
            int length = -1;

            while ((length = bis.read(buffer)) != -1)
            {
                bos.write(buffer, 0, length);
            }

            bos.flush();
        }
        catch (IOException e)
        {
            throw new ACException(ACError.AC_FILE_SAVING_ERROR_MSG,
                    ACError.AC_FILE_SAVING_ERROR, e);
        }
        finally
        {
            StreamUtil.closeStream(bos);
            StreamUtil.closeStream(bis);
        }

        try
        {
            convert(filePath);
        }
        catch (ACException ex)
        {
            File file = new File(filePath);

            file.delete();

            throw ex;
        }
    }

}
