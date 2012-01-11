/**
 * ACRequestHandler.java
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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.AbstractHandler;

import com.honnix.yaacs.adapter.data.model.AlbumCoverRaw;
import com.honnix.yaacs.adapter.http.constant.ACHttpConstant;
import com.honnix.yaacs.adapter.http.constant.ACHttpPropertiesConstant;
import com.honnix.yaacs.adapter.http.error.ACHttpError;
import com.honnix.yaacs.adapter.http.util.ACHttpBodyUtil;
import com.honnix.yaacs.constant.ACConstant;
import com.honnix.yaacs.core.server.ACServer;
import com.honnix.yaacs.error.ACError;
import com.honnix.yaacs.exception.ACException;
import com.honnix.yaacs.util.StreamUtil;

/**
 *
 */
public class ACRequestHandler
    extends AbstractHandler
{

    private static final Log LOG = LogFactory.getLog(ACRequestHandler.class);

    private ACServer acServer;

    public ACRequestHandler(ACServer acServer)
    {
        super();

        this.acServer = acServer;
    }

    private void createHeaderAndStatusResponse(HttpServletResponse response,
            ACException exception)
    {
        setHttpHeader(response);
        writeResponse(response, ACHttpBodyUtil.buildStatusBody(exception));
    }

    private void endOfResponse(HttpServletResponse response)
    {
        writeResponse(response, ACHttpConstant.BODY_LINE_SEPARATOR);
    }

    private Map<String, String> getParameterMap(HttpServletRequest request)
    {
        Map<String, String> paramMap = null;
        InputStream inputStream = getRequestInputStream(request);

        if (inputStream != null)
        {
            try
            {
                paramMap =
                        ACHttpBodyUtil.buildParameterMap(request
                                .getInputStream(),
                                ACHttpPropertiesConstant.HTTP_CHARSET);
            }
            catch (IOException e)
            {
                LOG.warn(ACError.CORRUPTED_REQUEST, e);
            }
            finally
            {
                StreamUtil.closeStream(inputStream);
            }
        }

        return paramMap;
    }

    private InputStream getRequestInputStream(HttpServletRequest request)
    {
        InputStream inputStream = null;

        try
        {
            inputStream = request.getInputStream();
        }
        catch (IOException e)
        {
            LOG.warn("Error retrieving input stream from request.", e);
        }

        return inputStream;
    }

    private void handleAC(HttpServletRequest request,
            HttpServletResponse response)
    {
        Map<String, String> paramMap = getParameterMap(request);

        if (paramMap != null)
        {
            String action = paramMap.get(ACHttpConstant.ACTION_KEY);

            if (action == null)
            {
                createHeaderAndStatusResponse(response, new ACException(
                        ACError.MISSING_ACTION_MSG, ACError.MISSING_ACTION));
                endOfResponse(response);
            }
            else if (ACHttpConstant.ACTION_PUT_AC.equals(action))
            {
                handlePutAC(paramMap, response);
            }
            else if (ACHttpConstant.ACTION_GET_AC_LIST.equals(action))
            {
                handleGetACList(paramMap, response);
            }
            else
            {
                createHeaderAndStatusResponse(response, new ACException(
                        ACError.WRONG_ACTION_MSG, ACError.WRONG_ACTION));
                endOfResponse(response);
            }
        }
        else
        {
            createHeaderAndStatusResponse(response, new ACException(
                    ACError.CORRUPTED_REQUEST_MSG, ACError.CORRUPTED_REQUEST));
            endOfResponse(response);
        }
    }

    private void handleGetACList(Map<String, String> paramMap,
            HttpServletResponse response)
    {
        String sessionId = paramMap.get(ACHttpConstant.SESSION_ID_KEY);

        if (sessionId == null)
        {
            createHeaderAndStatusResponse(response, new ACException(
                    ACError.EMPTY_SESSION_ID_MSG, ACError.EMPTY_SESSION_ID));
            endOfResponse(response);
        }
        else
        {
            String searchFilter =
                    paramMap.get(ACHttpConstant.SEARCH_FILTER_KEY);

            if (searchFilter == null)
            {
                createHeaderAndStatusResponse(response, new ACException(
                        ACError.MISSING_MENDATORY_PARAMETERS_MSG,
                        ACError.MISSING_MENDATORY_PARAMETERS));
                endOfResponse(response);
            }
            else if (ACHttpConstant.SEARCH_FILTER_ALBUM.equals(searchFilter))
            {
                handleGetACListByAlbum(paramMap, response);
            }
            else if (ACHttpConstant.SEARCH_FILTER_DISC_ID.equals(searchFilter))
            {
                handleGetACListByDiscId(paramMap, response);
            }
            else
            {
                createHeaderAndStatusResponse(response, new ACException(
                        ACError.BAD_SEARCH_FILTER_MSG,
                        ACError.BAD_SEARCH_FILTER));
                endOfResponse(response);
            }
        }
    }

    private void handleGetACListByAlbum(Map<String, String> paramMap,
            HttpServletResponse response)
    {
        String sessionId = paramMap.get(ACHttpConstant.SESSION_ID_KEY);
        String artist = paramMap.get(ACHttpConstant.ARTIST_KEY);
        String year = paramMap.get(ACHttpConstant.YEAR_KEY);
        String albumName = paramMap.get(ACHttpConstant.ALBUM_NAME_KEY);

        if (artist == null || year == null || albumName == null)
        {
            createHeaderAndStatusResponse(response, new ACException(
                    ACError.MISSING_MENDATORY_PARAMETERS_MSG,
                    ACError.MISSING_MENDATORY_PARAMETERS));
            endOfResponse(response);
        }
        else
        {
            List<AlbumCoverRaw> acRawList = null;

            try
            {
                acRawList =
                        acServer.getACListByAlbum(new AlbumCoverRaw(artist,
                                year, albumName), sessionId);
            }
            catch (ACException e)
            {
                createHeaderAndStatusResponse(response, e);
                endOfResponse(response);
            }

            if (acRawList != null)
            {
                createHeaderAndStatusResponse(response, new ACException(
                        ACError.SUCCESS_MSG, ACError.SUCCESS));
                writeACList(acRawList, response);
                endOfResponse(response);
            }
        }
    }

    private void handleGetACListByDiscId(Map<String, String> paramMap,
            HttpServletResponse response)
    {
        String sessionId = paramMap.get(ACHttpConstant.SESSION_ID_KEY);
        String discId = paramMap.get(ACHttpConstant.ALBUM_DISC_ID_KEY);

        if (discId == null)
        {
            createHeaderAndStatusResponse(response, new ACException(
                    ACError.MISSING_MENDATORY_PARAMETERS_MSG,
                    ACError.MISSING_MENDATORY_PARAMETERS));
            endOfResponse(response);
        }
        else
        {
            List<AlbumCoverRaw> acRawList = null;

            try
            {
                acRawList =
                        acServer.getACListByDiscId(new AlbumCoverRaw(discId),
                                sessionId);
            }
            catch (ACException e)
            {
                createHeaderAndStatusResponse(response, e);
                endOfResponse(response);
            }

            if (acRawList != null)
            {
                createHeaderAndStatusResponse(response, new ACException(
                        ACError.SUCCESS_MSG, ACError.SUCCESS));
                writeACList(acRawList, response);
                endOfResponse(response);
            }
        }
    }

    private void handlePutAC(Map<String, String> paramMap,
            HttpServletResponse response)
    {
        String sessionId = paramMap.get(ACHttpConstant.SESSION_ID_KEY);

        if (sessionId == null)
        {
            createHeaderAndStatusResponse(response, new ACException(
                    ACError.EMPTY_SESSION_ID_MSG, ACError.EMPTY_SESSION_ID));
            endOfResponse(response);
        }
        else
        {
            String artist = paramMap.get(ACHttpConstant.ARTIST_KEY);
            String year = paramMap.get(ACHttpConstant.YEAR_KEY);
            String albumName = paramMap.get(ACHttpConstant.ALBUM_NAME_KEY);
            String discId = paramMap.get(ACHttpConstant.ALBUM_DISC_ID_KEY);
            String acFileContentEncode =
                    paramMap.get(ACHttpConstant.AC_FILE_CONTENT_ENCODE_KEY);
            String acFileContent =
                    paramMap.get(ACHttpConstant.AC_FILE_CONTENT_KEY);

            if (artist == null || year == null || albumName == null
                    || discId == null || acFileContentEncode == null
                    || acFileContent == null)
            {
                createHeaderAndStatusResponse(response, new ACException(
                        ACError.MISSING_MENDATORY_PARAMETERS_MSG,
                        ACError.MISSING_MENDATORY_PARAMETERS));
                endOfResponse(response);
            }
            else if (ACConstant.AC_FILE_CONTENT_ENCODE
                    .equals(acFileContentEncode))
            {
                boolean success = false;

                try
                {
                    AlbumCoverRaw albumCoverRaw =
                            new AlbumCoverRaw(discId, artist, year, albumName,
                                    acFileContent);

                    acServer.putAC(albumCoverRaw, sessionId);

                    success = true;
                }
                catch (ACException e)
                {
                    createHeaderAndStatusResponse(response, e);
                    endOfResponse(response);
                }

                if (success)
                {
                    createHeaderAndStatusResponse(response, new ACException(
                            ACError.SUCCESS_MSG, ACError.SUCCESS));
                    endOfResponse(response);
                }
            }
            else
            {
                createHeaderAndStatusResponse(response, new ACException(
                        ACError.BAD_AC_FILE_CONTENT_ENCODE_MSG,
                        ACError.BAD_AC_FILE_CONTENT_ENCODE));
                endOfResponse(response);
            }
        }
    }

    private void handleSession(HttpServletRequest request,
            HttpServletResponse response)
    {
        Map<String, String> paramMap = getParameterMap(request);

        if (paramMap != null)
        {
            String action = paramMap.get(ACHttpConstant.ACTION_KEY);

            if (action == null)
            {
                createHeaderAndStatusResponse(response, new ACException(
                        ACError.MISSING_ACTION_MSG, ACError.MISSING_ACTION));
                endOfResponse(response);
            }
            else if (ACHttpConstant.ACTION_LOGIN.equals(action))
            {
                handleSessionLogin(paramMap, response);
            }
            else if (ACHttpConstant.ACTION_LOGOUT.equals(action))
            {
                handleSessionLogout(paramMap, response);
            }
            else
            {
                createHeaderAndStatusResponse(response, new ACException(
                        ACError.WRONG_ACTION_MSG, ACError.WRONG_ACTION));
                endOfResponse(response);
            }
        }
        else
        {
            createHeaderAndStatusResponse(response, new ACException(
                    ACError.CORRUPTED_REQUEST_MSG, ACError.CORRUPTED_REQUEST));
            endOfResponse(response);
        }
    }

    private void handleSessionLogin(Map<String, String> paramMap,
            HttpServletResponse response)
    {
        String userId = paramMap.get(ACHttpConstant.USER_ID_KEY);
        String password = paramMap.get(ACHttpConstant.PASSWORD_KEY);

        if (userId == null || password == null)
        {
            createHeaderAndStatusResponse(response, new ACException(
                    ACError.EMPTY_USER_ID_OR_PASSWORD_MSG,
                    ACError.EMPTY_USER_ID_OR_PASSWORD));
            endOfResponse(response);
        }
        else
        {
            String sessionId = null;

            try
            {
                sessionId = acServer.login(userId, password);
            }
            catch (ACException e)
            {
                createHeaderAndStatusResponse(response, e);
                endOfResponse(response);
            }

            if (sessionId != null)
            {
                createHeaderAndStatusResponse(response, new ACException(
                        ACError.SUCCESS_MSG, ACError.SUCCESS));
                writeResponse(response, ACHttpBodyUtil
                        .buildSessionIDResponseBody(sessionId));
                endOfResponse(response);
            }
        }
    }

    private void handleSessionLogout(Map<String, String> paramMap,
            HttpServletResponse response)
    {
        String sessionId = paramMap.get(ACHttpConstant.SESSION_ID_KEY);

        if (sessionId == null)
        {
            createHeaderAndStatusResponse(response, new ACException(
                    ACError.EMPTY_SESSION_ID_MSG, ACError.EMPTY_SESSION_ID));
            endOfResponse(response);
        }
        else
        {
            boolean success = false;

            try
            {
                acServer.logout(sessionId);
                success = true;
            }
            catch (ACException e)
            {
                createHeaderAndStatusResponse(response, e);
                endOfResponse(response);
            }

            if (success)
            {
                createHeaderAndStatusResponse(response, new ACException(
                        ACError.SUCCESS_MSG, ACError.SUCCESS));
                endOfResponse(response);
            }
        }
    }

    private void setHttpHeader(HttpServletResponse response)
    {
        response.setContentType(ACHttpConstant.HTTP_CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding(ACHttpPropertiesConstant.HTTP_CHARSET);
    }

    private void writeACList(List<AlbumCoverRaw> acRawList,
            HttpServletResponse response)
    {
        writeResponse(response, ACHttpBodyUtil
                .buildACListCountReponseBody(acRawList.size()));

        for (int i = 0; i < acRawList.size(); ++i)
        {
            AlbumCoverRaw albumCoverRaw = acRawList.get(i);

            writeResponse(response, ACHttpBodyUtil.buildGetACListResponseBody(
                    i, albumCoverRaw.getArtist(), albumCoverRaw.getYear(),
                    albumCoverRaw.getAlbumName(),
                    ACConstant.AC_FILE_CONTENT_ENCODE, albumCoverRaw
                            .getAcFileContent()));
        }
    }

    private void writeResponse(HttpServletResponse response, String content)
    {
        try
        {
            response.getWriter().print(content);
        }
        catch (IOException e)
        {
            LOG.error("Error creating error HTTP response.", e);
        }
    }

    public void handle(String target, HttpServletRequest request,
            HttpServletResponse response, int dispatch)
        throws IOException, ServletException
    {
        if (!ACHttpConstant.SUPPORTED_METHOD.equals(request.getMethod()))
        {
            createHeaderAndStatusResponse(response, new ACException(
                    ACHttpError.WRONG_METHOD_MSG, ACHttpError.WRONG_METHOD));
            endOfResponse(response);
        }
        else if (ACHttpPropertiesConstant.HTTP_SESSION_CONTROL_URL
                .equals(target))
        {
            handleSession(request, response);
        }
        else if (ACHttpPropertiesConstant.HTTP_AC_REQUEST_URL.equals(target))
        {
            handleAC(request, response);
        }
        else
        {
            createHeaderAndStatusResponse(response, new ACException(
                    ACHttpError.WRONG_ULR_MSG, ACHttpError.WRONG_URL));
            endOfResponse(response);
        }

        ((Request) request).setHandled(true);
    }

}
