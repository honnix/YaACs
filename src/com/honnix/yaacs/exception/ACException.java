/**
 * ACException.java
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
package com.honnix.yaacs.exception;

/**
 * Well, this is the one and only one exception defined in our system.
 */
public class ACException
    extends Exception
{

    /**
     * Generated UID 
     */
    private static final long serialVersionUID = -8026648646318561593L;

    private long errorCode;

    public ACException()
    {
        super();
    }

    public ACException(String message, long errorCode)
    {
        super(message);

        this.errorCode = errorCode;
    }

    public ACException(String message, long errorCode, Throwable cause)
    {
        super(message, cause);

        this.errorCode = errorCode;
    }

    /**
     * @return the errorCode
     */
    public long getErrorCode()
    {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(long errorCode)
    {
        this.errorCode = errorCode;
    }

    @Override
    public String toString()
    {
        StringBuilder sb =
                new StringBuilder(Long.toHexString(errorCode)).append('@')
                        .append(getMessage());

        return sb.toString();
    }

}
