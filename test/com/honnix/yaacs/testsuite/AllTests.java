/**
 * AllTests.java
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
package com.honnix.yaacs.testsuite;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.honnix.yaacs.adapter.http.TestACHttpServer;
import com.honnix.yaacs.auth.TestAuthentication;
import com.honnix.yaacs.auth.TestSessionManager;
import com.honnix.yaacs.core.data.processing.ac.TestACFileConverterMagick;
import com.honnix.yaacs.core.data.processing.ac.TestACFileHandler;
import com.honnix.yaacs.core.data.processing.info.TestACDBInfoHandler;
import com.honnix.yaacs.core.data.processing.info.TestACFileInfoHandler;
import com.honnix.yaacs.core.manager.TestACManagerImpl;
import com.honnix.yaacs.core.server.TestACServerImpl;
import com.honnix.yaacs.util.TestStringUtil;

/**
 * 
 */
public final class AllTests
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.honnix.yaacs.testsuite");

        //$JUnit-BEGIN$
        suite.addTestSuite(TestACFileHandler.class);
        suite.addTestSuite(TestACFileInfoHandler.class);
        suite.addTestSuite(TestACManagerImpl.class);
        suite.addTestSuite(TestACFileConverterMagick.class);
        suite.addTestSuite(TestSessionManager.class);
        suite.addTestSuite(TestACServerImpl.class);
        suite.addTestSuite(TestAuthentication.class);
        suite.addTestSuite(TestACHttpServer.class);
        suite.addTestSuite(TestStringUtil.class);
        suite.addTestSuite(TestACDBInfoHandler.class);
        //$JUnit-END$

        return suite;
    }

    private AllTests()
    {

    }

}
