/**
 * ACPropertiesConstant.java
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

import com.honnix.yaacs.util.PropertiesLoader;

/**
 *
 */
public final class ACPropertiesConstant
{

    private static final String MAIN_PROPERTIES_FILE_NAME = "yaacs.properties";

    public static final String ADAPTER_PROPERTIES_FILE_NAME =
            "adapter.properties";

    public static final String REPO_PATH =
            PropertiesLoader.loadProperties(MAIN_PROPERTIES_FILE_NAME)
                    .getProperty("repo.path");

    public static final String PASSWD_FILE_NAME =
            PropertiesLoader.loadProperties(MAIN_PROPERTIES_FILE_NAME)
                    .getProperty("passwd.file.name");

    public static final String INFO_FILE_PATH =
            PropertiesLoader.loadProperties(MAIN_PROPERTIES_FILE_NAME)
                    .getProperty("info.file.path");

    public static final String INFO_FILE_CHARSET =
            PropertiesLoader.loadProperties(MAIN_PROPERTIES_FILE_NAME)
                    .getProperty("info.file.charset");

    public static final String INFO_DB_PATH =
            PropertiesLoader.loadProperties(MAIN_PROPERTIES_FILE_NAME)
                    .getProperty("info.db.path");

    public static final String INFO_DB_CHARSET =
            PropertiesLoader.loadProperties(MAIN_PROPERTIES_FILE_NAME)
                    .getProperty("info.db.charset");

    public static final String RUN_FILE_PATH =
            PropertiesLoader.loadProperties(MAIN_PROPERTIES_FILE_NAME)
                    .getProperty("run.file.path");

    public static final String AC_FILE_EXT =
            PropertiesLoader.loadProperties(MAIN_PROPERTIES_FILE_NAME)
                    .getProperty("ac.file.ext");

    public static final String AC_FILE_CONVERTER =
            PropertiesLoader.loadProperties(MAIN_PROPERTIES_FILE_NAME)
                    .getProperty("ac.file.converter");

    public static final String AC_FILE_CONVERTER_MAGICK_PATH =
            PropertiesLoader.loadProperties(MAIN_PROPERTIES_FILE_NAME)
                    .getProperty("ac.file.converter.magick.path");

    public static final String AC_FILE_SCALE_WIDTH =
            PropertiesLoader.loadProperties(MAIN_PROPERTIES_FILE_NAME)
                    .getProperty("ac.file.scale.width");

    public static final int MAX_SESSION_NUM =
            Integer.parseInt(PropertiesLoader.loadProperties(
                    MAIN_PROPERTIES_FILE_NAME)
                    .getProperty("max.session.number"));

    public static final int SESSION_TIMEOUT =
            Integer.parseInt(PropertiesLoader.loadProperties(
                    MAIN_PROPERTIES_FILE_NAME).getProperty("session.timeout"));

    public static final String AC_MANAGER_FACTORY =
            PropertiesLoader.loadProperties(MAIN_PROPERTIES_FILE_NAME)
                    .getProperty("ac.manager.factory");

    private ACPropertiesConstant()
    {
        super();
    }

}
