/**
 * ACHttpConstant.java
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
package com.honnix.yaacs.adapter.http.constant;


/**
 *
 */
public final class ACHttpConstant
{

    public static final String HTTP_CONTENT_TYPE = "text/html;";

    public static final String RESPONSE_CODE_KEY = "Response-Code";

    public static final String RESPONSE_MSG_KEY = "Response-Message";

    public static final String ACTION_KEY = "Action";

    public static final String ACTION_LOGIN = "login";

    public static final String ACTION_LOGOUT = "logout";

    public static final String ACTION_PUT_AC = "putac";

    public static final String SEARCH_FILTER_ALBUM = "album";

    public static final String SEARCH_FILTER_DISC_ID = "discid";

    public static final String ACTION_GET_AC_LIST = "getaclist";

    public static final String USER_ID_KEY = "User-ID";

    public static final String PASSWORD_KEY = "Password";

    public static final String SESSION_ID_KEY = "Session-ID";

    public static final String AC_COUNT_KEY = "Count";

    public static final String ARTIST_KEY = "Artist";

    public static final String YEAR_KEY = "Year";

    public static final String ALBUM_NAME_KEY = "Album-Name";

    public static final String ALBUM_DISC_ID_KEY = "Album-Disc-ID";

    public static final String AC_FILE_CONTENT_ENCODE_KEY =
            "File-Content-Encode";

    public static final String AC_FILE_CONTENT_KEY = "File-Content";

    public static final String SEARCH_FILTER_KEY = "Search-Filter";

    public static final String AC_AT = "@";

    public static final String KEY_VALUE_SEPARATOR = ": ";

    public static final String SUPPORTED_METHOD = "POST";

    public static final String BODY_LINE_SEPARATOR = "\r\n";

    private ACHttpConstant()
    {
        super();
    }

}
