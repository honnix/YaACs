/**
 * TestACHttpServer.java
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
package com.honnix.yaacs.adapter.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.honnix.yaacs.adapter.AdapterServer;
import com.honnix.yaacs.adapter.http.constant.ACHttpConstant;
import com.honnix.yaacs.adapter.http.constant.ACHttpPropertiesConstant;
import com.honnix.yaacs.adapter.http.util.ACHttpBodyUtil;
import com.honnix.yaacs.constant.ACConstant;
import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.core.server.ACServer;
import com.honnix.yaacs.core.server.ACServerImpl;
import com.honnix.yaacs.exception.ACException;
import com.honnix.yaacs.util.StreamUtil;
import com.honnix.yaacs.util.StringUtil;

/**
 *
 */
public class TestACHttpServer
    extends TestCase
{

    private static final String SUCCESS_RESP = "Response-Code: 0";

    private static final String ERROR_CONVERTING_FILE = "Response-Code: 4099";

    private static final String USER_ID = "honnix";

    private static final String PASSWORD = "honnix";

    private static final String FORCED_FAILURE = "Forced failure.";

    private static final String SHOULD_BE_OK = "Should be an OK response.";

    private static final String RESPONSE_ERROR = "Response error.";

    private static final String CONTENT_TYPE_KEY = "Content-Type";

    private static final String CONTENT_TYPE =
            "text/html; charset=" + ACHttpPropertiesConstant.HTTP_CHARSET;

    private static final String HOST = "http://localhost:";

    private AdapterServer acHttpServer;

    private PostMethod postMethod;

    private HttpClient client;

    public TestACHttpServer()
    {
        super();
    }

    private Map<String, String> login()
        throws HttpException, IOException
    {
        StringBuilder sb =
                new StringBuilder(HOST).append(
                        ACHttpPropertiesConstant.HTTP_LISTENING_PORT).append(
                        ACHttpPropertiesConstant.HTTP_SESSION_CONTROL_URL);
        postMethod = new PostMethod(sb.toString());

        postMethod.setRequestHeader(CONTENT_TYPE_KEY, CONTENT_TYPE);

        StringRequestEntity requestEntity =
                new StringRequestEntity(ACHttpBodyUtil.buildLoginRequestBody(
                        USER_ID, PASSWORD), null,
                        ACHttpPropertiesConstant.HTTP_CHARSET);
        postMethod.setRequestEntity(requestEntity);

        int statusCode = client.executeMethod(postMethod);

        assertEquals(SHOULD_BE_OK, HttpStatus.SC_OK, statusCode);

        return ACHttpBodyUtil.buildParameterMap(postMethod
                .getResponseBodyAsStream(),
                ACHttpPropertiesConstant.HTTP_CHARSET);
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
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

        acHttpServer = new ACHttpServer(acServer);
        acHttpServer.start();

        client = new HttpClient();
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown()
        throws Exception
    {
        acHttpServer.stop();

        super.tearDown();
    }

    public void testGetACList()
    {
        Map<String, String> paramMap = null;
        String sessionId = null;

        try
        {
            paramMap = login();
            sessionId = paramMap.get(ACHttpConstant.SESSION_ID_KEY);

            assertNotNull(RESPONSE_ERROR, sessionId);
        }
        catch (HttpException e)
        {
            fail(FORCED_FAILURE);
        }
        catch (IOException e)
        {
            fail(FORCED_FAILURE);
        }

        StringBuilder sb =
                new StringBuilder(HOST).append(
                        ACHttpPropertiesConstant.HTTP_LISTENING_PORT).append(
                        ACHttpPropertiesConstant.HTTP_AC_REQUEST_URL);
        postMethod = new PostMethod(sb.toString());

        postMethod.setRequestHeader(CONTENT_TYPE_KEY, CONTENT_TYPE);

        try
        {
            StringRequestEntity requestEntity =
                    new StringRequestEntity(ACHttpBodyUtil
                            .buildGetACListByAlbumRequestBody(sessionId,
                                    "Honnix", "2008", "Shadow of Light"), null,
                            ACHttpPropertiesConstant.HTTP_CHARSET);

            postMethod.setRequestEntity(requestEntity);

            int statusCode = client.executeMethod(postMethod);

            assertEquals(SHOULD_BE_OK, HttpStatus.SC_OK, statusCode);

            String response = postMethod.getResponseBodyAsString();

            assertTrue(RESPONSE_ERROR, response.startsWith(SUCCESS_RESP));

            byte[] byteArray = new byte[3];

            byteArray[0] = 97;
            byteArray[1] = 98;
            byteArray[2] = 99;
            assertNotSame(RESPONSE_ERROR, -1, response.indexOf(StringUtil
                    .encodeBase64(byteArray)));
        }
        catch (HttpException e)
        {
            fail(FORCED_FAILURE);
        }
        catch (IOException e)
        {
            fail(FORCED_FAILURE);
        }

        try
        {
            StringRequestEntity requestEntity =
                    new StringRequestEntity(ACHttpBodyUtil
                            .buildGetACListByDiscIdRequestBody(sessionId,
                                    "aac6400f48249cc9763771cc626a3571"), null,
                            ACHttpPropertiesConstant.HTTP_CHARSET);
            postMethod = new PostMethod(sb.toString());

            postMethod.setRequestHeader(CONTENT_TYPE_KEY, CONTENT_TYPE);
            postMethod.setRequestEntity(requestEntity);

            int statusCode = client.executeMethod(postMethod);

            assertEquals(SHOULD_BE_OK, HttpStatus.SC_OK, statusCode);

            String response = postMethod.getResponseBodyAsString();

            assertTrue(RESPONSE_ERROR, response.startsWith(SUCCESS_RESP));

            byte[] byteArray = new byte[3];

            byteArray[0] = 97;
            byteArray[1] = 98;
            byteArray[2] = 99;
            assertNotSame(RESPONSE_ERROR, -1, response.indexOf(StringUtil
                    .encodeBase64(byteArray)));
        }
        catch (HttpException e)
        {
            fail(FORCED_FAILURE);
        }
        catch (IOException e)
        {
            fail(FORCED_FAILURE);
        }
    }

    public void testLogin()
    {
        try
        {
            Map<String, String> paramMap = login();

            assertNotNull(RESPONSE_ERROR, paramMap
                    .get(ACHttpConstant.SESSION_ID_KEY));
        }
        catch (HttpException e)
        {
            fail(FORCED_FAILURE);
        }
        catch (IOException e)
        {
            fail(FORCED_FAILURE);
        }
    }

    public void testPutAC()
    {
        Map<String, String> paramMap = null;
        String sessionId = null;

        try
        {
            paramMap = login();
            sessionId = paramMap.get(ACHttpConstant.SESSION_ID_KEY);

            assertNotNull(RESPONSE_ERROR, sessionId);
        }
        catch (HttpException e)
        {
            fail(FORCED_FAILURE);
        }
        catch (IOException e)
        {
            fail(FORCED_FAILURE);
        }

        StringBuilder sb =
                new StringBuilder(HOST).append(
                        ACHttpPropertiesConstant.HTTP_LISTENING_PORT).append(
                        ACHttpPropertiesConstant.HTTP_AC_REQUEST_URL);
        postMethod = new PostMethod(sb.toString());

        postMethod.setRequestHeader(CONTENT_TYPE_KEY, CONTENT_TYPE);

        File file = new File("etc/http.properties");
        byte[] byteArray = null;

        try
        {
            byteArray =
                    StreamUtil.inputStream2ByteArray(new FileInputStream(file));
        }
        catch (FileNotFoundException e1)
        {
            fail(FORCED_FAILURE);
        }

        assertNotNull("Should not be null.", byteArray);

        try
        {
            StringRequestEntity requestEntity =
                    new StringRequestEntity(ACHttpBodyUtil
                            .buildPutACRequestBody(sessionId,
                                    "aac6400f48249cc9763771cc626a3571",
                                    "Honnix", "2008", "Shadow of Light",
                                    ACConstant.AC_FILE_CONTENT_ENCODE,
                                    StringUtil.encodeBase64(byteArray)), null,
                            ACHttpPropertiesConstant.HTTP_CHARSET);

            postMethod.setRequestEntity(requestEntity);

            int statusCode = client.executeMethod(postMethod);

            assertEquals(SHOULD_BE_OK, HttpStatus.SC_OK, statusCode);

            String response = postMethod.getResponseBodyAsString();

            assertTrue(RESPONSE_ERROR, response
                    .startsWith(ERROR_CONVERTING_FILE)
                    || response.startsWith(SUCCESS_RESP));
        }
        catch (HttpException e)
        {
            fail(FORCED_FAILURE);
        }
        catch (IOException e)
        {
            fail(FORCED_FAILURE);
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

            pw.print("aac6400f48249cc9763771cc626a3571"
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
    }
}
