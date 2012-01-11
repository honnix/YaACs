/**
 * ACCoreError.java
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
package com.honnix.yaacs.error;

/**
 * This class defines error code and message.
 */
public final class ACError
{

    public static final long SUCCESS = 0x0000;

    public static final String SUCCESS_MSG = "Success";

    public static final long AC_FILE_SAVING_ERROR = 0x1001;

    public static final String AC_FILE_SAVING_ERROR_MSG =
            "Error saving album cover.";

    public static final long AC_FILE_READING_ERROR = 0x1002;

    public static final String AC_FILE_READING_ERROR_MSG =
            "Error reading album cover.";

    public static final long AC_FILE_CONVERTING_ERROR = 0x1003;

    public static final String AC_FILE_CONVERTING_ERROR_MSG =
            "Error converting album cover.";

    public static final long INFO_FILE_FINDING_ERROR = 0x2001;

    public static final String INFO_FILE_FINDING_ERROR_MSG =
            "Error finding information file.";

    public static final long INFO_FILE_READING_ERROR = 0x2002;

    public static final String INFO_FILE_READING_ERROR_MSG =
            "Error reading information file.";

    public static final long INFO_FILE_SAVING_ERROR = 0x2003;

    public static final String INFO_FILE_SAVING_ERROR_MSG =
            "Error saving information file.";

    public static final long INFO_DB_CLASS_FINDING_ERROR = 0x2004;

    public static final String INFO_DB_CLASS_FINDING_ERROR_MSG =
            "Error finding class org.sqlite.JDBC.";

    public static final long INFO_DB_INIT_ERROR = 0x2005;

    public static final String INFO_DB_INIT_ERROR_MSG =
            "Error initializing information db.";

    public static final long INFO_DB_READING_ERROR = 0x2006;

    public static final String INFO_DB_READING_ERROR_MSG =
            "Error reading information db.";

    public static final long INFO_DB_SAVING_ERROR = 0x2007;

    public static final String INFO_DB_SAVING_ERROR_MSG =
            "Error saving information db.";

    public static final long MAX_SESSION_NUM_EXCEEDED = 0x3001;

    public static final String MAX_SESSION_NUM_EXCEEDED_MSG =
            "Max number of session exceeded.";

    public static final long USER_NOT_LOGGED_IN = 0x3002;

    public static final String USER_NOT_LOGGED_IN_MSG =
            "This user has not logged in.";

    public static final long USER_NOT_REGISTERED_OR_PASSWORD_WRONG = 0x3003;

    public static final String USER_NOT_REGISTERED_OR_PASSWORD_WRONG_MSG =
            "This user has not registered or password is wrong.";

    public static final long EMPTY_USER_ID_OR_PASSWORD = 0x3004;

    public static final String EMPTY_USER_ID_OR_PASSWORD_MSG =
            "Empty user ID or password.";

    public static final long EMPTY_SESSION_ID = 0x3005;

    public static final String EMPTY_SESSION_ID_MSG = "Empty session ID.";

    public static final long CORRUPTED_CONTENT = 0x4001;

    public static final String CORRUPTED_CONTENT_MSG =
            "Corrupted album cover content.";

    public static final long BAD_AC_FILE_CONTENT_ENCODE = 0x4002;

    public static final String BAD_AC_FILE_CONTENT_ENCODE_MSG =
            "Bad album cover content encode.";

    public static final long CORRUPTED_REQUEST = 0x5001;

    public static final String CORRUPTED_REQUEST_MSG = "Corrupted request.";

    public static final long MISSING_MENDATORY_PARAMETERS = 0x5002;

    public static final String MISSING_MENDATORY_PARAMETERS_MSG =
            "Missing mendatory parameters.";

    public static final long MISSING_ACTION = 0x5003;

    public static final String MISSING_ACTION_MSG = "Missing action type.";

    public static final long WRONG_ACTION = 0x5004;

    public static final String WRONG_ACTION_MSG = "Wrong action type.";

    public static final long BAD_SEARCH_FILTER = 0x5005;

    public static final String BAD_SEARCH_FILTER_MSG = "Bad search filter.";

    public static final long UNKNOWN_ERROR = 0x9001;

    public static final String UNKNOWN_ERROR_MSG = "Unknown error.";

    private ACError()
    {
        super();
    }

}
