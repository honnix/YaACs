/**
 * StreamUtil.java
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
package com.honnix.yaacs.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public final class StreamUtil
{

    private static final Log LOG = LogFactory.getLog(StreamUtil.class);

    public static InputStream byteArray2InputStream(byte[] byteArray)
    {
        return new ByteArrayInputStream(byteArray);
    }

    public static void closeStream(Closeable stream)
    {
        try
        {
            stream.close();
        }
        catch (IOException e)
        {
            LOG.warn("Error closing stream.", e);
        }
    }

    public static byte[] inputStream2ByteArray(InputStream inputStream)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[255];
        byte[] rawData = null;

        try
        {
            int length = -1;

            while ((length = inputStream.read(buffer)) != -1)
            {
                baos.write(buffer, 0, length);
            }

            rawData = baos.toByteArray();
        }
        catch (IOException e)
        {
            LOG.error("Error reading input album cover data.", e);

            rawData = new byte[0];
        }

        return rawData;
    }

    private StreamUtil()
    {
        super();
    }

}
