/**
 * Session.java
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

import java.util.Date;

/**
 *
 */
public class Session
{

    private String userId;

    private String password;

    private String sessionId;

    private Date updateDate;

    public Session()
    {
        super();
    }

    public Session(String userId, String password)
    {
        super();

        this.userId = userId;
        this.password = password;
    }

    public Session(String userId, String password, String sessionId,
            Date loginDate)
    {
        super();

        this.userId = userId;
        this.password = password;
        this.sessionId = sessionId;
        this.updateDate = loginDate;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true; // NOPMD by ehonlia on 3/20/08 2:11 PM
        }
        if (obj == null)
        {
            return false; // NOPMD by ehonlia on 3/20/08 2:11 PM
        }
        if (getClass() != obj.getClass())
        {
            return false; // NOPMD by ehonlia on 3/20/08 2:11 PM
        }
        final Session other = (Session) obj;
        if (password == null)
        {
            if (other.password != null)
            {
                return false; // NOPMD by ehonlia on 3/20/08 2:11 PM
            }
        }
        else if (!password.equals(other.password))
        {
            return false; // NOPMD by ehonlia on 3/20/08 2:11 PM
        }
        if (userId == null)
        {
            if (other.userId != null)
            {
                return false; // NOPMD by ehonlia on 3/20/08 2:11 PM
            }
        }
        else if (!userId.equals(other.userId))
        {
            return false; // NOPMD by ehonlia on 3/20/08 2:12 PM
        }

        return true;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId()
    {
        return sessionId;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate()
    {
        return updateDate;
    }

    /**
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((password == null)
                ? 0
                : password.hashCode());
        result = prime * result + ((userId == null)
                ? 0
                : userId.hashCode());

        return result;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

}
