/**
 * ACHttpBodyUtil.java
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
package com.honnix.yaacs.adapter.http.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.honnix.yaacs.adapter.http.constant.ACHttpConstant;
import com.honnix.yaacs.error.ACError;
import com.honnix.yaacs.exception.ACException;

/**
 *
 */
public final class ACHttpBodyUtil
{

    public static String buildACListCountReponseBody(int size)
    {
        return new StringBuilder(ACHttpConstant.AC_COUNT_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(size).append(
                ACHttpConstant.BODY_LINE_SEPARATOR).toString();
    }

    public static String buildGetACListByAlbumRequestBody(String sessionId,
            String artist, String year, String albumName)
    {
        StringBuilder sb =
                new StringBuilder(ACHttpConstant.SESSION_ID_KEY).append(
                        ACHttpConstant.KEY_VALUE_SEPARATOR).append(sessionId)
                        .append(ACHttpConstant.BODY_LINE_SEPARATOR);

        sb.append(ACHttpConstant.ACTION_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(
                ACHttpConstant.ACTION_GET_AC_LIST).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.SEARCH_FILTER_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(
                ACHttpConstant.SEARCH_FILTER_ALBUM).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.ARTIST_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(artist).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.YEAR_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(year).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.ALBUM_NAME_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(albumName).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.BODY_LINE_SEPARATOR);

        return sb.toString();
    }

    public static String buildGetACListByDiscIdRequestBody(String sessionId,
            String discID)
    {
        StringBuilder sb =
                new StringBuilder(ACHttpConstant.SESSION_ID_KEY).append(
                        ACHttpConstant.KEY_VALUE_SEPARATOR).append(sessionId)
                        .append(ACHttpConstant.BODY_LINE_SEPARATOR);

        sb.append(ACHttpConstant.ACTION_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(
                ACHttpConstant.ACTION_GET_AC_LIST).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.SEARCH_FILTER_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(
                ACHttpConstant.SEARCH_FILTER_DISC_ID).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.ALBUM_DISC_ID_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(discID).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.BODY_LINE_SEPARATOR);

        return sb.toString();
    }

    public static String buildGetACListResponseBody(int id, String artist,
            String year, String albumName, String fileContentEncode,
            String fileContent)
    {
        StringBuilder sb =
                new StringBuilder(ACHttpConstant.ARTIST_KEY).append(
                        ACHttpConstant.AC_AT).append(id).append(
                        ACHttpConstant.KEY_VALUE_SEPARATOR).append(artist)
                        .append(ACHttpConstant.BODY_LINE_SEPARATOR);

        sb.append(ACHttpConstant.YEAR_KEY).append(ACHttpConstant.AC_AT).append(
                id).append(ACHttpConstant.KEY_VALUE_SEPARATOR).append(year)
                .append(ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.ALBUM_NAME_KEY).append(ACHttpConstant.AC_AT)
                .append(id).append(ACHttpConstant.KEY_VALUE_SEPARATOR).append(
                        albumName).append(ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.AC_FILE_CONTENT_ENCODE_KEY).append(
                ACHttpConstant.AC_AT).append(id).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(fileContentEncode)
                .append(ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.AC_FILE_CONTENT_KEY).append(
                ACHttpConstant.AC_AT).append(id).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(fileContent).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);

        return sb.toString();
    }

    public static String buildLoginRequestBody(String userId, String password)
    {
        StringBuilder sb =
                new StringBuilder(ACHttpConstant.ACTION_KEY).append(
                        ACHttpConstant.KEY_VALUE_SEPARATOR).append(
                        ACHttpConstant.ACTION_LOGIN).append(
                        ACHttpConstant.BODY_LINE_SEPARATOR);

        sb.append(ACHttpConstant.USER_ID_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(userId).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.PASSWORD_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(password).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.BODY_LINE_SEPARATOR);

        return sb.toString();
    }

    public static String buildLogoutRequestBody(String sessionId)
    {
        StringBuilder sb =
                new StringBuilder(ACHttpConstant.SESSION_ID_KEY).append(
                        ACHttpConstant.KEY_VALUE_SEPARATOR).append(sessionId)
                        .append(ACHttpConstant.BODY_LINE_SEPARATOR);

        sb.append(ACHttpConstant.ACTION_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(
                ACHttpConstant.ACTION_LOGOUT).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);

        sb.append(ACHttpConstant.BODY_LINE_SEPARATOR);

        return sb.toString();
    }

    public static Map<String, String> buildParameterMap(
            InputStream inputStream, String charset)
        throws IOException
    {
        Map<String, String> paramMap = new HashMap<String, String>();
        BufferedReader br =
                new BufferedReader(new InputStreamReader(inputStream, charset));
        String line = null;

        while ((line = br.readLine()) != null)
        {
            String[] keyValue = line.split(ACHttpConstant.KEY_VALUE_SEPARATOR);

            if (keyValue.length == 2)
            {
                paramMap.put(keyValue[0], keyValue[1]);
            }
        }

        return paramMap;
    }

    public static String buildPutACRequestBody(String sessionId, String discId,
            String artist, String year, String albumName,
            String fileContentEncode, String fileContent)
    {
        StringBuilder sb =
                new StringBuilder(ACHttpConstant.SESSION_ID_KEY).append(
                        ACHttpConstant.KEY_VALUE_SEPARATOR).append(sessionId)
                        .append(ACHttpConstant.BODY_LINE_SEPARATOR);

        sb.append(ACHttpConstant.ACTION_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(
                ACHttpConstant.ACTION_PUT_AC).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.ALBUM_DISC_ID_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(discId).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.ARTIST_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(artist).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.YEAR_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(year).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.ALBUM_NAME_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(albumName).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.AC_FILE_CONTENT_ENCODE_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(fileContentEncode)
                .append(ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.AC_FILE_CONTENT_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(fileContent).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);
        sb.append(ACHttpConstant.BODY_LINE_SEPARATOR);

        return sb.toString();
    }

    public static String buildSessionIDResponseBody(String sessionId)
    {
        return new StringBuilder(ACHttpConstant.SESSION_ID_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(sessionId).append(
                ACHttpConstant.BODY_LINE_SEPARATOR).toString();
    }

    public static String buildStatusBody(ACException exception)
    {
        StringBuilder sb =
                new StringBuilder(ACHttpConstant.RESPONSE_CODE_KEY).append(
                        ACHttpConstant.KEY_VALUE_SEPARATOR).append(
                        exception.getErrorCode()).append(
                        ACHttpConstant.BODY_LINE_SEPARATOR);

        sb.append(ACHttpConstant.RESPONSE_MSG_KEY).append(
                ACHttpConstant.KEY_VALUE_SEPARATOR).append(
                exception.getMessage()).append(
                ACHttpConstant.BODY_LINE_SEPARATOR);

        return sb.toString();
    }

    public static ACException buildACException(Map<String, String> paramMap)
    {
        ACException acException = null;
        String errorMsg = paramMap.get(ACHttpConstant.RESPONSE_MSG_KEY);
        String errorCode = paramMap.get(ACHttpConstant.RESPONSE_CODE_KEY);

        if (errorMsg == null || errorCode == null)
        {
            acException =
                    new ACException(ACError.UNKNOWN_ERROR_MSG,
                            ACError.UNKNOWN_ERROR);
        }
        else
        {
            acException = new ACException(errorMsg, Long.parseLong(errorCode));
        }

        return acException;
    }

    private ACHttpBodyUtil()
    {
        super();
    }

}
