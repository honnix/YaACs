/**
 * ACFileConverterMagick.java
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

import java.io.IOException;

import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.error.ACError;
import com.honnix.yaacs.exception.ACException;

/**
 *
 */
public class ACFileConverterMagick
    implements ACFileConverter
{

    public ACFileConverterMagick()
    {
        super();
    }

    /* (non-Javadoc)
     * @see com.honnix.yaacs.core.data.processing.ac.ACFileConverter#convert(java.lang.String)
     */
    public void convert(String filePath)
        throws ACException
    {
        ProcessBuilder pb =
                new ProcessBuilder(ACPropertiesConstant.AC_FILE_CONVERTER_MAGICK_PATH,
                        "-scale", ACPropertiesConstant.AC_FILE_SCALE_WIDTH, filePath,
                        filePath);

        try
        {
            pb.start();
        }
        catch (IOException e)
        {
            throw new ACException(ACError.AC_FILE_CONVERTING_ERROR_MSG,
                    ACError.AC_FILE_CONVERTING_ERROR, e);
        }
    }

}
