/**
 * AlbumCover.java
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
package com.honnix.yaacs.core.data.model;

import java.io.InputStream;

/**
 *
 */
public class AlbumCover
{

    private Album album;

    private String filePath;

    private InputStream inputStream;

    public AlbumCover()
    {
        super();
    }

    public AlbumCover(Album album, String filePath)
    {
        super();

        this.album = album;
        this.filePath = filePath;
    }

    public AlbumCover(Album album, String filePath, InputStream inputStream)
    {
        super();

        this.album = album;
        this.filePath = filePath;
        this.inputStream = inputStream;
    }

    public AlbumCover(AlbumCover albumCover)
    {
        super();

        album = new Album(albumCover.album);
        filePath = albumCover.filePath;
        inputStream = albumCover.inputStream;
    }

    /**
     * @return the album
     */
    public Album getAlbum()
    {
        return album;
    }

    /**
     * @return the filePath
     */
    public String getFilePath()
    {
        return filePath;
    }

    /**
     * @return the is
     */
    public InputStream getInputStream()
    {
        return inputStream;
    }

    /**
     * @param album the album to set
     */
    public void setAlbum(Album album)
    {
        this.album = album;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    /**
     * @param inputStream the is to set
     */
    public void setInputStream(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

}
