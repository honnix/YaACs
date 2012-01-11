/**
 * AbstractACInfoHandler.java
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
package com.honnix.yaacs.core.data.processing.info;

import java.util.ArrayList;
import java.util.List;

import com.honnix.yaacs.core.data.model.Album;
import com.honnix.yaacs.core.data.model.AlbumCover;
import com.honnix.yaacs.exception.ACException;

/**
 *
 */
public abstract class AbstractACInfoHandler
    implements ACInfoHandler
{

    protected abstract List<AlbumCover> readByAlbum(Album album)
        throws ACException;

    protected abstract List<AlbumCover> readByDiscId(String discId)
        throws ACException;

    /* (non-Javadoc)
     * @see com.honnix.yaacs.core.data.processing.info.ACInfoHandler#read(com.honnix.yaacs.core.data.model.Album)
     */
    public List<AlbumCover> read(Album album)
        throws ACException
    {
        List<AlbumCover> tmpList = null;

        // Read by disc ID is always the first choice.
        if (album.getDiscId() != null)
        {
            tmpList = readByDiscId(album.getDiscId());
        }
        else
        {
            tmpList = readByAlbum(album);
        }

        List<AlbumCover> acList = new ArrayList<AlbumCover>(tmpList.size());

        // We have to copy here because album cover will be modified later.
        for (AlbumCover albumCover : tmpList)
        {
            acList.add(new AlbumCover(albumCover));
        }

        return acList;
    }

}