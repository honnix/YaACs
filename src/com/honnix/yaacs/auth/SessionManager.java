/**
 * SessionManager.java
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.error.ACError;
import com.honnix.yaacs.exception.ACException;
import com.honnix.yaacs.util.MD5;

/**
 *
 */
public class SessionManager
{

    class SessionCleaner
        extends TimerTask
    {

        public SessionCleaner()
        {
            super();
        }

        /* (non-Javadoc)
         * @see java.util.TimerTask#run()
         */
        @Override
        public void run()
        {
            Calendar tmp = Calendar.getInstance();

            tmp.add(Calendar.SECOND, 0 - ACPropertiesConstant.SESSION_TIMEOUT);

            Date checkPoint = tmp.getTime();

            synchronized (sessionList)
            {
                for (int i = 0; i < sessionList.size(); ++i)
                {
                    Session session = sessionList.get(i);
                    if (checkPoint.after(session.getUpdateDate()))
                    {
                        sessionList.remove(i);
                        --i;

                        if (LOG.isDebugEnabled())
                        {
                            StringBuilder sb =
                                    new StringBuilder("Session for user \"")
                                            .append(session.getUserId())
                                            .append("\" has been removed.");

                            LOG.debug(sb.toString());
                        }
                    }
                }
            }
        }
    }

    private static final Log LOG = LogFactory.getLog(SessionManager.class);

    private static final long PERIOD = 1000;

    private List<Session> sessionList;

    private SessionCleaner sessionCleaner;

    private Timer timer;

    public SessionManager()
    {
        super();

        sessionList = new ArrayList<Session>(ACPropertiesConstant.MAX_SESSION_NUM / 2);
        sessionCleaner = new SessionCleaner();
        timer = new Timer("SessionCleaner", true);
    }

    private String generateSessionId(String userId, String password, Date date)
    {
        String tmp =
                new StringBuilder(userId).append(password).append(
                        date.getTime()).toString();
        String sessionId = String.valueOf(tmp.hashCode());
        String encoded = MD5.getMD5Sum(tmp);

        if (encoded != null)
        {
            sessionId = encoded;
        }

        return sessionId;
    }

    public boolean isValidSession(String sessionId)
    {
        boolean isValid = false;

        synchronized (sessionList)
        {
            for (Session session : sessionList)
            {
                if (session.getSessionId().equals(sessionId))
                {
                    isValid = true;
                    session.setUpdateDate(Calendar.getInstance().getTime());

                    break;
                }
            }
        }

        return isValid;
    }

    public String login(String userId, String password)
        throws ACException
    {
        Date now = Calendar.getInstance().getTime();
        String sessionId = generateSessionId(userId, password, now);
        Session session = new Session(userId, password, sessionId, now);

        synchronized (sessionList)
        {
            if (sessionList.size() == ACPropertiesConstant.MAX_SESSION_NUM)
            {
                throw new ACException(ACError.MAX_SESSION_NUM_EXCEEDED_MSG,
                        ACError.MAX_SESSION_NUM_EXCEEDED);
            }

            sessionList.add(session);
        }

        if (LOG.isDebugEnabled())
        {
            StringBuilder sb =
                    new StringBuilder("User \"").append(userId).append(
                            "\" logged in with session ID \"")
                            .append(sessionId).append("\".");

            LOG.debug(sb.toString());
        }

        return sessionId;
    }

    public void logout(String sessionId)
        throws ACException
    {
        boolean hasFound = false;

        synchronized (sessionList)
        {
            for (Session session : sessionList)
            {
                if (session.getSessionId().equals(sessionId))
                {
                    hasFound = true;
                    sessionList.remove(session);

                    break;
                }
            }
        }

        if (!hasFound)
        {
            throw new ACException(ACError.USER_NOT_LOGGED_IN_MSG,
                    ACError.USER_NOT_LOGGED_IN);
        }

        if (LOG.isDebugEnabled())
        {
            StringBuilder sb =
                    new StringBuilder("User with session ID \"").append(
                            sessionId).append("\" logged out.");

            LOG.debug(sb.toString());
        }
    }

    public void startSessionCleaner()
    {
        timer.schedule(sessionCleaner, 0, PERIOD);
    }

    public void stopSessionCleaner()
    {
        timer.cancel();
    }

}
