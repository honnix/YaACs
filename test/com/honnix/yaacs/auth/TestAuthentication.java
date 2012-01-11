/**
 * TestAuthentication.java
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
package com.honnix.yaacs.auth;

import junit.framework.TestCase;

/**
 *
 */
public class TestAuthentication
    extends TestCase
{

    private static final String HONNIX = "honnix";

    private static final String HONNIX1 = "honnix1";

    private Authentication authentication;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();

        authentication = new Authentication();
    }

    /**
     * Test method for {@link com.honnix.yaacs.auth.Authentication#isValidUser(java.lang.String, java.lang.String)}.
     */
    public void testIsValidUser()
    {
        assertTrue("This user should be valid.", authentication.isValidUser(
                HONNIX, HONNIX));

        assertFalse("This user should be invalid.", authentication.isValidUser(
                HONNIX1, HONNIX));

        assertFalse("This user should be invalid.", authentication.isValidUser(
                HONNIX, HONNIX1));
    }

}
