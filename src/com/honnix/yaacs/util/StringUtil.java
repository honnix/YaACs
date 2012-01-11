/**
 * StringUtil.java
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

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

/**
 *
 */
public final class StringUtil
{

    public static final String EMPTY_STRING = "";

    private static final String UNDERLYING_CHARSET = "US-ASCII";

    public static byte[] decodeBase64(String str)
    {
        byte[] decodedByteArray = null;

        try
        {
            decodedByteArray = Base64.decodeBase64(str
                    .getBytes(UNDERLYING_CHARSET));
        }
        catch (UnsupportedEncodingException e)
        {
            // Should not happen.
            decodedByteArray = new byte[0];
        }

        return decodedByteArray;
    }

    public static String encodeBase64(byte[] byteArray)
    {
        byte[] encodedByteArray = Base64.encodeBase64(byteArray);
        String encodedString = null;

        try
        {
            encodedString = new String(encodedByteArray, UNDERLYING_CHARSET);
        }
        catch (UnsupportedEncodingException e)
        {
            // Should not happen.
            encodedString = "";
        }

        return encodedString;
    }

    private StringUtil()
    {
        super();
    }

}
