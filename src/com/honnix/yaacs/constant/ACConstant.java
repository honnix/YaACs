/**
 * ACConstant.java
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
package com.honnix.yaacs.constant;

/**
 *
 */
public final class ACConstant
{

    public static final String CLASS_PATH =
            System.getProperty("java.class.path");

    public static final char AC_LINE_SEPARATOR = '\n';

    public static final String PATH_SEPARATOR =
            System.getProperty("path.separator");

    public static final String FILE_SEPARATOR =
            System.getProperty("file.separator");

    public static final String USER_HOME = System.getProperty("user.home");

    public static final String FILE_INFO_LOADER_TYPE = "FILE";

    public static final String DB_INFO_LOADER_TYPE = "DB";

    public static final String HIDDEN_FILE_PATH = "******";

    public static final String AC_SEPARATOR = "@@";

    public static final String AC_FILE_CONTENT_ENCODE = "Base64";

    private ACConstant()
    {
        super();
    }

}
