/**
 * PropertiesLoader.java
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 */
public final class PropertiesLoader
{

    private static final Log LOG = LogFactory.getLog(PropertiesLoader.class);

    private static Map<String, Properties> propertiesMap = new HashMap<String, Properties>();

    public static Properties loadProperties(final String fileName)
    {
        Properties properties = null;

        synchronized (PropertiesLoader.class)
        {
            properties = propertiesMap.get(fileName);

            if (properties == null)
            {
                properties = new Properties();

                try
                {
                    properties.load(PropertiesLoader.class.getClassLoader()
                            .getResourceAsStream(fileName));
                    propertiesMap.put(fileName, properties);
                }
                catch (IOException e)
                {
                    StringBuilder sb = new StringBuilder("Could not find ")
                            .append(fileName).append(" in class loading path.");

                    LOG.fatal(sb.toString());

                    properties = null;
                }
            }
        }

        return properties;
    }

    private PropertiesLoader()
    {
        super();
    }

}
