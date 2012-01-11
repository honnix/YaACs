/**
 * ACFileConverter.java
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

import com.honnix.yaacs.exception.ACException;

/**
 *
 */
public interface ACFileConverter
{
    /**
     * Convert an Album Cover file to a normalized one.
     * <br>
     * <b>NOTE: implementation of this method should be asynchronous.</b>
     * 
     * @param filePath
     * @throws ACException
     */
    void convert(String filePath)
        throws ACException;
}
