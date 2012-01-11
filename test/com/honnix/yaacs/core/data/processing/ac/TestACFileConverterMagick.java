/**
 * TestACFileConverterMagick.java
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

import junit.framework.TestCase;

import com.honnix.yaacs.exception.ACException;

/**
 *
 */
public class TestACFileConverterMagick
    extends TestCase
{

    private ACFileConverter acFileConverter;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();

        acFileConverter = new ACFileConverterMagick();
    }

    /**
     * Test method for {@link com.honnix.yaacs.core.data.processing.ac.ACFileConverterMagick#convert(java.lang.String)}.
     */
    public void testConvert()
    {
        try
        {
            acFileConverter.convert("C:\\Documents and Settings\\"
                    + "ehonlia\\My Documents\\My Pictures\\a.jpg");
        }
        catch (ACException e)
        {
            fail("ACException");
        }
    }

}
