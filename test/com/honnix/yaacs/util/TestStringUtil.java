/**
 * TestStringUtil.java
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

import junit.framework.TestCase;

/**
 *
 */
public class TestStringUtil
    extends TestCase
{

    public void testByteArray2String()
    {
        byte[] byteArray = { 1, 2, 3, 10, 11, 12, 13, 14, 15, 16, -1 };
        String s = StringUtil.encodeBase64(byteArray);

        assertFalse("Should not empty.", StringUtil.EMPTY_STRING.equals(s));
    }

    public void testEncodeDecode()
        throws UnsupportedEncodingException
    {
        String raw = "abcdefg";
        String encodedStr = StringUtil.encodeBase64(raw.getBytes("US-ASCII"));
        byte[] decodedByteArray = StringUtil.decodeBase64(encodedStr);
        String decodedStr = new String(decodedByteArray, "US-ASCII");

        assertEquals("Should equal.", raw, decodedStr);
    }

    public void testString2ByteArray()
    {
        byte[] byteArray = StringUtil
                .decodeBase64("800102030a0b0c0d0e0f10fffe7f8000");

        assertNotSame("The length should not be zero.", byteArray.length == 0);
    }

}
