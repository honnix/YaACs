/**
 * ACServer.java
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

import java.util.List;

import com.honnix.yaacs.adapter.data.model.AlbumCoverRaw;
import com.honnix.yaacs.exception.ACException;

/**
 *
 */
public interface ACServer
{

    List<AlbumCoverRaw> getACListByAlbum(AlbumCoverRaw albumCoverRaw,
            String sessionId)
        throws ACException;

    List<AlbumCoverRaw> getACListByDiscId(AlbumCoverRaw albumCoverRaw,
            String sessionId)
        throws ACException;

    String login(String userId, String password)
        throws ACException;

    void logout(String sessionId)
        throws ACException;

    void putAC(AlbumCoverRaw albumCoverRaw, String sessionId)
        throws ACException;

    void start();

    void stop();

}
