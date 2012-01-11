/**
 * MD5.java
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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public final class MD5
{

    private static final Log LOG = LogFactory.getLog(MD5.class);

    /**
     * Get MD5 sum of the input data.
     * 
     * @param data
     *            input data used to generate MD5 sum
     * @return MD5 sum of the input data, or null if no MD5 algorithm could be
     *         found
     */
    public static String getMD5Sum(String data)
    {
        String encodedData = null;

        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.update(data.getBytes("US-ASCII"), 0, data.length());
            encodedData = new BigInteger(1, messageDigest.digest())
                    .toString(16);
        }
        catch (NoSuchAlgorithmException e)
        {
            StringBuilder sb = new StringBuilder("No MD5 algorithm.")
                    .append(" Could not apply validation check.");

            LOG.error(sb.toString(), e);
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.error("This should not happen anyway.", e);
        }

        return encodedData;
    }

    private MD5()
    {
        super();
    }

}
