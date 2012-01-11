/**
 * ACManagerImpl.java
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
package com.honnix.yaacs.core.manager;

import java.util.Calendar;
import java.util.List;

import com.honnix.yaacs.constant.ACConstant;
import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.core.data.model.Album;
import com.honnix.yaacs.core.data.model.AlbumCover;
import com.honnix.yaacs.core.data.processing.ac.ACFileHandler;
import com.honnix.yaacs.core.data.processing.info.ACInfoHandler;
import com.honnix.yaacs.exception.ACException;
import com.honnix.yaacs.util.MD5;

/**
 *
 */
class ACManagerImpl
    implements ACManager
{

    private ACFileHandler acFileHandler;

    private ACInfoHandler acInfoHandler;

    public ACManagerImpl(ACFileHandler acFileHandler, ACInfoHandler acInfoLoader)
    {
        super();

        this.acFileHandler = acFileHandler;
        this.acInfoHandler = acInfoLoader;
    }

    /**
     * @param album
     * @return
     */
    private String buildACFilePath(Album album)
    {
        return new StringBuilder(ACPropertiesConstant.REPO_PATH).append(
                ACConstant.FILE_SEPARATOR).append(
                MD5.getMD5Sum(new StringBuilder(album.toString()).append(
                        Calendar.getInstance().getTimeInMillis()).toString()))
                .append(ACPropertiesConstant.AC_FILE_EXT).toString();
    }

    public List<AlbumCover> getACList(Album album)
        throws ACException
    {
        List<AlbumCover> acList = acInfoHandler.read(album);

        for (AlbumCover albumCover : acList)
        {
            albumCover.setInputStream(acFileHandler.read(albumCover
                    .getFilePath()));
            albumCover.setFilePath(ACConstant.HIDDEN_FILE_PATH);
        }

        return acList;
    }

    public void putAC(AlbumCover albumCover)
        throws ACException
    {
        Album album = albumCover.getAlbum();
        String acFilePath = buildACFilePath(album);

        acFileHandler.save(acFilePath, albumCover.getInputStream());

        albumCover.setFilePath(acFilePath);
        acInfoHandler.save(albumCover);
    }
}
