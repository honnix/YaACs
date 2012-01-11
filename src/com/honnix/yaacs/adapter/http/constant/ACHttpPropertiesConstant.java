/**
 * ACHttpPropertiesConstant.java
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
package com.honnix.yaacs.adapter.http.constant;

import com.honnix.yaacs.util.PropertiesLoader;

/**
 *
 */
public final class ACHttpPropertiesConstant
{

    private static final String PROPERTIES_FILE_NAME = "http.properties";

    public static final int HTTP_LISTENING_PORT =
            Integer.valueOf(PropertiesLoader.loadProperties(
                    PROPERTIES_FILE_NAME).getProperty("http.connection.port"));

    public static final String HTTP_AC_REQUEST_URL =
            PropertiesLoader.loadProperties(PROPERTIES_FILE_NAME).getProperty(
                    "http.ac.request.url");

    public static final String HTTP_SESSION_CONTROL_URL =
            PropertiesLoader.loadProperties(PROPERTIES_FILE_NAME).getProperty(
                    "http.session.control.url");

    public static final String HTTP_CHARSET =
            PropertiesLoader.loadProperties(PROPERTIES_FILE_NAME).getProperty(
                    "http.charset");

    private ACHttpPropertiesConstant()
    {
        super();
    }

}
