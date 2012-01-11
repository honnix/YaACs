/**
 * ACHttpError.java
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
package com.honnix.yaacs.adapter.http.error;

/**
 * This class defines error code and message related to HTTP adapter.
 */
public final class ACHttpError
{

    public static final long WRONG_URL = 0x11001;

    public static final String WRONG_ULR_MSG = "Wrong URL.";

    public static final long WRONG_METHOD = 0x11002;

    public static final String WRONG_METHOD_MSG =
            "Wrong Http method. Should be POST.";

    public static final long BAD_STATUS_CODE = 0x11003;

    public static final String BAD_STATUS_CODE_MSG =
            "Bad status code returned.";

    public static final long INTERNAL_ERROR = 0x19001;

    public static final String INTERNAL_ERROR_MSG =
            "Http adapter internal error.";

    private ACHttpError()
    {
        super();
    }

}
