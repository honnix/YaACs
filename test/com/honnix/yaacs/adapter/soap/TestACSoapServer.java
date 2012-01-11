/**
 * TestACSoapServer.java
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
package com.honnix.yaacs.adapter.soap;

import junit.framework.TestCase;

import com.honnix.yaacs.adapter.AdapterServer;
import com.honnix.yaacs.core.server.ACServer;
import com.honnix.yaacs.core.server.ACServerImpl;
import com.honnix.yaacs.exception.ACException;

/**
 *
 */
public class TestACSoapServer
    extends TestCase
{

    private AdapterServer acSoapServer;

    public TestACSoapServer()
    {
        super();
    }

    protected void setUp()
        throws Exception
    {
        super.setUp();

        ACServer acServer = null;

        try
        {
            acServer = new ACServerImpl();
        }
        catch (ACException e)
        {
            fail("Error creating ACServer.");
        }

        acSoapServer = new ACSoapServer(acServer);
        acSoapServer.start();
    }

    protected void tearDown()
        throws Exception
    {
        acSoapServer.stop();

        super.tearDown();
    }

}
