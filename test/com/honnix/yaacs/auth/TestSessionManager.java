/**
 * TestSessionManager.java
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

import com.honnix.yaacs.error.ACError;
import com.honnix.yaacs.exception.ACException;
import com.honnix.yaacs.util.StringUtil;

/**
 *
 */
public class TestSessionManager
    extends TestCase
{

    private static final String USER_ID = "honnix";

    private static final String PASSWORD = "honnix";

    private static final String EXCEPTION_THROWN =
            "An exception should have been thrown.";

    private static final String LOGIN_FAILED = "Login failed.";

    private static final String INVALID_SESSION_ID = "Invalid session ID.";

    private SessionManager sessionManager;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();

        sessionManager = new SessionManager();
        sessionManager.startSessionCleaner();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown()
        throws Exception
    {
        sessionManager.stopSessionCleaner();

        super.tearDown();
    }

    public void testLoginDuplicate()
    {
        String sessionId = null;

        try
        {
            sessionId = sessionManager.login(USER_ID, PASSWORD);
        }
        catch (ACException e)
        {
            fail(LOGIN_FAILED);
        }

        assertNotNull(INVALID_SESSION_ID, sessionId);

        try
        {
            sessionId = sessionManager.login(USER_ID, PASSWORD);
        }
        catch (ACException e)
        {
            fail(LOGIN_FAILED);
        }

        assertNotNull(INVALID_SESSION_ID, sessionId);
    }

    public void testLoginSuccess()
    {
        String sessionId = null;

        try
        {
            sessionId = sessionManager.login(USER_ID, PASSWORD);
        }
        catch (ACException e)
        {
            fail(LOGIN_FAILED);
        }

        assertNotNull(INVALID_SESSION_ID, sessionId);
        assertTrue("Should be a valid session anyway.", sessionManager
                .isValidSession(sessionId));

        try
        {
            sessionManager.logout(sessionId);
        }
        catch (ACException e)
        {
            fail("Should be no exception.");
        }

        assertFalse("Should be an invalid session anyway.", sessionManager
                .isValidSession(sessionId));
    }

    public void testLogoutFailed()
    {
        try
        {
            sessionManager.logout(StringUtil.EMPTY_STRING);
            fail(EXCEPTION_THROWN);
        }
        catch (ACException e)
        {
            assertEquals("This user should not have logged in anyway.",
                    ACError.USER_NOT_LOGGED_IN, e.getErrorCode());
        }
    }

    public void testRemoveSession()
    {
        String sessionId = null;

        try
        {
            sessionId = sessionManager.login(USER_ID, PASSWORD);
        }
        catch (ACException e)
        {
            fail(LOGIN_FAILED);
        }

        assertNotNull(INVALID_SESSION_ID, sessionId);
        assertTrue("Should be a valid session anyway.", sessionManager
                .isValidSession(sessionId));

        try
        {
            Thread.sleep(1000 * 8);
        }
        catch (InterruptedException e)
        {
            fail("Sleep has been interrupted.");
        }

        assertFalse("Should be an invalid session anyway.", sessionManager
                .isValidSession(sessionId));

        try
        {
            sessionManager.logout(sessionId);
            fail(EXCEPTION_THROWN);
        }
        catch (ACException e)
        {
            assertEquals("This session should have been removed anyway.",
                    ACError.USER_NOT_LOGGED_IN, e.getErrorCode());
        }
    }

}
