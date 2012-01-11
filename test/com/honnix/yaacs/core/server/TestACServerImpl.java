/**
 * TestACServerImpl.java
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
package com.honnix.yaacs.core.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import junit.framework.TestCase;

import com.honnix.yaacs.adapter.data.model.AlbumCoverRaw;
import com.honnix.yaacs.constant.ACConstant;
import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.error.ACError;
import com.honnix.yaacs.exception.ACException;
import com.honnix.yaacs.util.StringUtil;

/**
 *
 */
public class TestACServerImpl
    extends TestCase
{

    private static final String USER_ID = "honnix";

    private static final String PASSWORD = "honnix";

    private static final String EXCEPTION_THROWN =
            "An exception should have been thrown.";

    private static final String INVALID_SESSION_ID = "Invalid session ID.";

    private static final String LOGIN_FAILED = "Login failed.";

    private ACServer acServer;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();

        try
        {
            acServer = new ACServerImpl();
        }
        catch (ACException e)
        {
            fail("Should be no exception.");
        }

        acServer.start();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown()
        throws Exception
    {
        acServer.stop();

        super.tearDown();
    }

    public void testGetACList()
    {
        String sessionId = null;

        try
        {
            sessionId = acServer.login(USER_ID, PASSWORD);
        }
        catch (ACException e)
        {
            fail(LOGIN_FAILED);
        }

        List<AlbumCoverRaw> acRawList = null;

        try
        {
            acRawList =
                    acServer.getACListByDiscId(new AlbumCoverRaw(
                            "aac6400f48249cc9763771cc626a3571"), sessionId);
        }
        catch (ACException e)
        {
            fail("ACException");
        }

        assertEquals("Wrong album cover raw list size.", 1, acRawList.size());
        assertNotNull("At least an empty string.", acRawList.get(0)
                .getAcFileContent());

        try
        {
            acServer.logout(sessionId);
        }
        catch (ACException e)
        {
            fail("Logout failed.");
        }
    }

    public void testLoginDuplicate()
    {
        String sessionId = null;

        try
        {
            sessionId = acServer.login(USER_ID, PASSWORD);
        }
        catch (ACException e)
        {
            fail(LOGIN_FAILED);
        }

        assertNotNull(INVALID_SESSION_ID, sessionId);

        try
        {
            sessionId = acServer.login(USER_ID, PASSWORD);
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
            sessionId = acServer.login(USER_ID, PASSWORD);
        }
        catch (ACException e)
        {
            fail(LOGIN_FAILED);
        }

        assertNotNull(INVALID_SESSION_ID, sessionId);

        try
        {
            acServer.logout(sessionId);
        }
        catch (ACException e)
        {
            fail("Should be no exception.");
        }

        try
        {
            acServer.putAC(new AlbumCoverRaw(), sessionId);
            fail(EXCEPTION_THROWN);
        }
        catch (ACException e)
        {
            assertEquals("This use shall not be able to put album cover.",
                    ACError.USER_NOT_LOGGED_IN, e.getErrorCode());
        }
    }

    public void testLogoutFailed()
    {
        try
        {
            acServer.logout(StringUtil.EMPTY_STRING);
            fail(EXCEPTION_THROWN);
        }
        catch (ACException e)
        {
            assertEquals("This user should not have logged in anyway.",
                    ACError.USER_NOT_LOGGED_IN, e.getErrorCode());
        }
    }

    public void testPutAC()
    {
        File file = new File("etc/yaacs.properties");
        InputStream inputStream = null;

        try
        {
            inputStream = new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            fail("Could not open etc/yaacs.properties.");
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[255];
        byte[] rawData = null;

        try
        {
            int length = -1;

            while ((length = inputStream.read(buffer)) != -1)
            {
                bos.write(buffer, 0, length);
            }

            rawData = bos.toByteArray();
        }
        catch (IOException e)
        {
            fail("Failed read etc/yaacs.properties.");
        }

        String sessionId = null;

        try
        {
            sessionId = acServer.login(USER_ID, PASSWORD);
        }
        catch (ACException e)
        {
            fail(LOGIN_FAILED);
        }

        try
        {
            AlbumCoverRaw albumCoverRaw =
                    new AlbumCoverRaw("aac6400f48249cc9763771cc626a3571",
                            "Honnix", "2008", "Shadow of Light");

            albumCoverRaw.setAcFileContent(StringUtil.encodeBase64(rawData));

            acServer.putAC(albumCoverRaw, sessionId);
        }
        catch (ACException e)
        {
            fail("ACException");
        }

        file = new File(ACPropertiesConstant.REPO_PATH);
        File[] fileArray = file.listFiles();

        for (int i = 0; i < fileArray.length; i++)
        {
            fileArray[i].delete();
        }

        try
        {
            PrintWriter pw =
                    new PrintWriter(ACPropertiesConstant.INFO_FILE_PATH);

            pw.println("aac6400f48249cc9763771cc626a3571"
                    + ACConstant.AC_SEPARATOR + "Honnix"
                    + ACConstant.AC_SEPARATOR + "2008"
                    + ACConstant.AC_SEPARATOR + "Shadow of Light"
                    + ACConstant.AC_SEPARATOR + ACPropertiesConstant.REPO_PATH
                    + ACConstant.FILE_SEPARATOR
                    + "f04b85d32f91df435c439f5723e0efbb.jpg");
            pw.print(ACConstant.AC_LINE_SEPARATOR);

            pw.flush();
        }
        catch (FileNotFoundException e)
        {
            fail("Could not open " + ACPropertiesConstant.INFO_FILE_PATH);
        }

        PrintWriter pw = null;

        try
        {
            pw =
                    new PrintWriter(ACPropertiesConstant.REPO_PATH
                            + ACConstant.FILE_SEPARATOR
                            + "f04b85d32f91df435c439f5723e0efbb.jpg");

            pw.write("abc");
        }
        catch (FileNotFoundException e)
        {
            fail("Could not open test1.jpg to write.");
        }

        pw.flush();

        try
        {
            acServer.logout(sessionId);
        }
        catch (ACException e)
        {
            fail("Logout failed.");
        }
    }

    public void testRemoveSession()
    {
        String sessionId = null;

        try
        {
            sessionId = acServer.login(USER_ID, PASSWORD);
        }
        catch (ACException e)
        {
            fail(LOGIN_FAILED);
        }

        assertNotNull(INVALID_SESSION_ID, sessionId);

        try
        {
            Thread.sleep(1000 * 8);
        }
        catch (InterruptedException e)
        {
            fail("Sleep has been interrupted.");
        }

        try
        {
            acServer.putAC(new AlbumCoverRaw(), sessionId);
            fail(EXCEPTION_THROWN);
        }
        catch (ACException e)
        {
            assertEquals("This use shall not be able to put album cover.",
                    ACError.USER_NOT_LOGGED_IN, e.getErrorCode());
        }

        try
        {
            acServer.logout(sessionId);
            fail(EXCEPTION_THROWN);
        }
        catch (ACException e)
        {
            assertEquals("This user should have already logged out.",
                    ACError.USER_NOT_LOGGED_IN, e.getErrorCode());
        }
    }

}
