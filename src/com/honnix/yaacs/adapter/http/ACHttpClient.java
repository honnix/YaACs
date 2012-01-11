/**
 * ACHttpClient.java
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.honnix.yaacs.adapter.AdapterClient;
import com.honnix.yaacs.adapter.data.model.AlbumCoverRaw;
import com.honnix.yaacs.adapter.http.constant.ACHttpConstant;
import com.honnix.yaacs.adapter.http.error.ACHttpError;
import com.honnix.yaacs.adapter.http.util.ACHttpBodyUtil;
import com.honnix.yaacs.error.ACError;
import com.honnix.yaacs.exception.ACException;

/**
 * 
 */
public class ACHttpClient
    implements AdapterClient
{

    private static final String FILE_CONTENT_ENCODE = "Base64";

    private static final String CHARSET = "utf-8";

    private String sessionControlUrl;

    private String acRequestUrl;

    private String host;

    private int port;

    private HttpClient httpClient;

    private String sessionId;

    public ACHttpClient(String host, int port, String sessionControlUrl,
            String acRequestUrl)
    {
        super();

        this.host = host;
        this.port = port;
        this.sessionControlUrl = sessionControlUrl;
        this.acRequestUrl = acRequestUrl;

        httpClient = new HttpClient();
    }

    /**
     * @param responseMap
     */
    private List<AlbumCoverRaw> buildACRawList(Map<String, String> responseMap)
    {
        int count =
                Integer.parseInt(responseMap.get(ACHttpConstant.AC_COUNT_KEY));
        List<AlbumCoverRaw> acRawList = new ArrayList<AlbumCoverRaw>(count);

        for (int i = 0; i < count; ++i)
        {
            String suffix =
                    new StringBuilder(ACHttpConstant.AC_AT).append(i)
                            .toString();

            if (FILE_CONTENT_ENCODE.equals(responseMap.get(new StringBuilder(
                    ACHttpConstant.AC_FILE_CONTENT_ENCODE_KEY).append(suffix)
                    .toString())))
            {
                String discId =
                        responseMap.get(new StringBuilder(
                                ACHttpConstant.ALBUM_DISC_ID_KEY)
                                .append(suffix).toString());
                String artist =
                        responseMap.get(new StringBuilder(
                                ACHttpConstant.ARTIST_KEY).append(suffix)
                                .toString());
                String year =
                        responseMap.get(new StringBuilder(
                                ACHttpConstant.YEAR_KEY).append(suffix)
                                .toString());
                String albumName =
                        responseMap.get(new StringBuilder(
                                ACHttpConstant.ALBUM_NAME_KEY).append(suffix)
                                .toString());
                String acFileContent =
                        responseMap.get(new StringBuilder(
                                ACHttpConstant.AC_FILE_CONTENT_KEY).append(
                                suffix).toString());

                acRawList.add(new AlbumCoverRaw(discId, artist, year,
                        albumName, acFileContent));
            }
        }

        return acRawList;
    }

    private PostMethod buildPostMethod(String url, String request)
    {
        StringBuilder sb =
                new StringBuilder("http://").append(host).append(':').append(
                        port).append(url);
        PostMethod postMethod = new PostMethod(sb.toString());

        sb =
                new StringBuilder(ACHttpConstant.HTTP_CONTENT_TYPE).append(
                        " charset=").append(CHARSET);
        postMethod.setRequestHeader("Content-Type", sb.toString());

        try
        {
            StringRequestEntity requestEntity =
                    new StringRequestEntity(request, null, CHARSET);

            postMethod.setRequestEntity(requestEntity);
        }
        catch (UnsupportedEncodingException e)
        {
            postMethod = null;
        }

        return postMethod;
    }

    private void checkResponse(Map<String, String> responseMap)
        throws ACException
    {
        if (!String.valueOf(ACError.SUCCESS).equals(
                responseMap.get(ACHttpConstant.RESPONSE_CODE_KEY)))
        {
            throw ACHttpBodyUtil.buildACException(responseMap);
        }
    }

    public List<AlbumCoverRaw> getACListByAlbum(AlbumCoverRaw albumCoverRaw)
        throws ACException
    {
        Map<String, String> responseMap =
                sendAndReceive(acRequestUrl, ACHttpBodyUtil
                        .buildGetACListByAlbumRequestBody(sessionId,
                                albumCoverRaw.getArtist(), albumCoverRaw
                                        .getYear(), albumCoverRaw
                                        .getAlbumName()));

        return buildACRawList(responseMap);
    }

    public List<AlbumCoverRaw> getACListByDiscId(AlbumCoverRaw albumCoverRaw)
        throws ACException
    {
        Map<String, String> responseMap =
                sendAndReceive(acRequestUrl, ACHttpBodyUtil
                        .buildGetACListByDiscIdRequestBody(sessionId,
                                albumCoverRaw.getDiscId()));

        return buildACRawList(responseMap);
    }

    public void login(String userId, String password)
        throws ACException
    {
        Map<String, String> responseMap =
                sendAndReceive(sessionControlUrl, ACHttpBodyUtil
                        .buildLoginRequestBody(userId, password));

        sessionId = responseMap.get(ACHttpConstant.SESSION_ID_KEY);
    }

    public void logout()
        throws ACException
    {
        sendAndReceive(sessionControlUrl, ACHttpBodyUtil
                .buildLogoutRequestBody(sessionId));
    }

    public void putAC(AlbumCoverRaw albumCoverRaw)
        throws ACException
    {
        sendAndReceive(acRequestUrl, ACHttpBodyUtil.buildPutACRequestBody(
                sessionId, albumCoverRaw.getDiscId(),
                albumCoverRaw.getArtist(), albumCoverRaw.getYear(),
                albumCoverRaw.getAlbumName(), FILE_CONTENT_ENCODE,
                albumCoverRaw.getAcFileContent()));
    }

    private Map<String, String> sendAndReceive(String url, String request)
        throws ACException
    {
        Map<String, String> responseMap = null;

        PostMethod postMethod = buildPostMethod(url, request);

        if (postMethod == null)
        {
            throw new ACException(ACHttpError.INTERNAL_ERROR_MSG,
                    ACHttpError.INTERNAL_ERROR);
        }

        int statusCode = -1;

        try
        {
            statusCode = httpClient.executeMethod(postMethod);
        }
        catch (Exception e)
        {
            throw new ACException(ACHttpError.INTERNAL_ERROR_MSG,
                    ACHttpError.INTERNAL_ERROR, e);
        }

        if (statusCode == HttpStatus.SC_OK)
        {
            try
            {
                responseMap =
                        ACHttpBodyUtil.buildParameterMap(postMethod
                                .getResponseBodyAsStream(), postMethod
                                .getResponseCharSet());
            }
            catch (IOException e)
            {
                throw new ACException(ACHttpError.INTERNAL_ERROR_MSG,
                        ACHttpError.INTERNAL_ERROR, e);
            }
            finally
            {
                postMethod.releaseConnection();
            }
        }
        else
        {
            throw new ACException(ACHttpError.BAD_STATUS_CODE_MSG,
                    ACHttpError.BAD_STATUS_CODE);
        }

        checkResponse(responseMap);

        return responseMap;
    }

}
