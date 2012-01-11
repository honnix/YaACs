/**
 * ACServerImpl.java
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
package com.honnix.yaacs.core.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.honnix.yaacs.adapter.data.model.AlbumCoverRaw;
import com.honnix.yaacs.auth.Authentication;
import com.honnix.yaacs.auth.SessionManager;
import com.honnix.yaacs.constant.ACConstant;
import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.core.data.model.Album;
import com.honnix.yaacs.core.data.model.AlbumCover;
import com.honnix.yaacs.core.manager.ACFileInfoManagerFactory;
import com.honnix.yaacs.core.manager.ACManager;
import com.honnix.yaacs.core.manager.ACManagerFactory;
import com.honnix.yaacs.error.ACError;
import com.honnix.yaacs.exception.ACException;
import com.honnix.yaacs.util.StreamUtil;
import com.honnix.yaacs.util.StringUtil;

/**
 *
 */
public class ACServerImpl
    implements ACServer
{

    private static final Log LOG = LogFactory.getLog(ACServerImpl.class);

    private SessionManager sessionManager;

    private Authentication authentication;

    private ACManager acManager;

    public ACServerImpl()
        throws ACException
    {
        super();

        sessionManager = new SessionManager();
        authentication = new Authentication();

        try
        {
            acManager =
                    ((ACManagerFactory) Class.forName(
                            ACPropertiesConstant.AC_MANAGER_FACTORY)
                            .newInstance()).createACManager();
        }
        catch (ACException e)
        {
            LOG.fatal("Error creating ACManager.", e);

            throw e;
        }
        catch (Exception e)
        {
            StringBuilder sb =
                    new StringBuilder("Error instantiating ACManagerFactory")
                            .append(" defined in yaacs.properties.").append(
                                    " Default factory (file) will be used.");

            LOG.error(sb.toString());

            acManager = new ACFileInfoManagerFactory().createACManager();
        }
    }

    private List<AlbumCoverRaw> buildACRawList(List<AlbumCover> acList)
    {
        List<AlbumCoverRaw> acRawList =
                new ArrayList<AlbumCoverRaw>(acList.size());

        for (AlbumCover albumCover : acList)
        {
            Album album = albumCover.getAlbum();
            AlbumCoverRaw albumCoverRaw =
                    new AlbumCoverRaw(album.getDiscId(), album.getArtist(),
                            album.getYear(), album.getAlbumName());

            albumCoverRaw.setAcFileContent(StringUtil.encodeBase64(StreamUtil
                    .inputStream2ByteArray(albumCover.getInputStream())));
            StreamUtil.closeStream(albumCover.getInputStream());

            acRawList.add(albumCoverRaw);
        }

        return acRawList;
    }

    private boolean hasNotLoggedIn(String sessionId)
    {
        return !sessionManager.isValidSession(sessionId);
    }

    public List<AlbumCoverRaw> getACListByAlbum(AlbumCoverRaw albumCoverRaw,
            String sessionId)
        throws ACException
    {
        if (hasNotLoggedIn(sessionId))
        {
            throw new ACException(ACError.USER_NOT_LOGGED_IN_MSG,
                    ACError.USER_NOT_LOGGED_IN);
        }

        List<AlbumCover> acList =
                acManager.getACList(new Album(albumCoverRaw.getArtist(),
                        albumCoverRaw.getYear(), albumCoverRaw.getAlbumName()));

        return buildACRawList(acList);
    }

    public List<AlbumCoverRaw> getACListByDiscId(AlbumCoverRaw albumCoverRaw,
            String sessionId)
        throws ACException
    {
        if (hasNotLoggedIn(sessionId))
        {
            throw new ACException(ACError.USER_NOT_LOGGED_IN_MSG,
                    ACError.USER_NOT_LOGGED_IN);
        }

        List<AlbumCover> acList =
                acManager.getACList(new Album(albumCoverRaw.getDiscId()));

        return buildACRawList(acList);
    }

    /* (non-Javadoc)
     * @see com.honnix.yaacs.core.server.ACServer#login(java.lang.String, java.lang.String)
     */
    public String login(String userId, String password)
        throws ACException
    {
        if (!authentication.isValidUser(userId, password))
        {
            throw new ACException(
                    ACError.USER_NOT_REGISTERED_OR_PASSWORD_WRONG_MSG,
                    ACError.USER_NOT_REGISTERED_OR_PASSWORD_WRONG);
        }

        return sessionManager.login(userId, password);
    }

    /* (non-Javadoc)
     * @see com.honnix.yaacs.core.server.ACServer#logout(java.lang.String)
     */
    public void logout(String sessionId)
        throws ACException
    {
        sessionManager.logout(sessionId);
    }

    public void putAC(AlbumCoverRaw albumCoverRaw, String sessionId)
        throws ACException
    {
        if (hasNotLoggedIn(sessionId))
        {
            throw new ACException(ACError.USER_NOT_LOGGED_IN_MSG,
                    ACError.USER_NOT_LOGGED_IN);
        }

        Album album =
                new Album(albumCoverRaw.getDiscId(), albumCoverRaw.getArtist(),
                        albumCoverRaw.getYear(), albumCoverRaw.getAlbumName());

        acManager.putAC(new AlbumCover(album, ACConstant.HIDDEN_FILE_PATH,
                StreamUtil.byteArray2InputStream(StringUtil
                        .decodeBase64(albumCoverRaw.getAcFileContent()))));
    }

    /* (non-Javadoc)
     * @see com.honnix.yaacs.core.server.ACServer#start()
     */
    public void start()
    {
        sessionManager.startSessionCleaner();
    }

    /* (non-Javadoc)
     * @see com.honnix.yaacs.core.server.ACServer#stop()
     */
    public void stop()
    {
        sessionManager.stopSessionCleaner();
    }

}
