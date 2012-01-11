/**
 * Album.java
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

import com.honnix.yaacs.constant.ACConstant;

/**
 *
 */
public class Album
{

    private String discId;

    private String albumName;

    private String artist;

    private String year;

    public Album()
    {
        super();
    }

    public Album(Album album)
    {
        super();

        discId = album.discId;
        artist = album.artist;
        year = album.year;
        albumName = album.albumName;
    }

    public Album(String discId)
    {
        super();

        this.discId = discId.toLowerCase();
    }

    public Album(String artist, String year, String albumName)
    {
        super();

        this.artist = artist;
        this.year = year;
        this.albumName = albumName;
    }

    public Album(String discId, String artist, String year, String albumName)
    {
        super();

        this.discId = discId.toLowerCase();
        this.artist = artist;
        this.year = year;
        this.albumName = albumName;
    }

    /**
     * @return the albumName
     */
    public String getAlbumName()
    {
        return albumName;
    }

    /**
     * @return the artist
     */
    public String getArtist()
    {
        return artist;
    }

    public String getDiscId()
    {
        return discId;
    }

    /**
     * @return the year
     */
    public String getYear()
    {
        return year;
    }

    /**
     * @param albumName the albumName to set
     */
    public void setAlbumName(String albumName)
    {
        this.albumName = albumName;
    }

    /**
     * @param artist the artist to set
     */
    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public void setDiscId(String discId)
    {
        this.discId = discId.toLowerCase();
    }

    /**
     * @param year the year to set
     */
    public void setYear(String year)
    {
        this.year = year;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return new StringBuilder(discId).append(ACConstant.AC_SEPARATOR)
                .append(artist).append(ACConstant.AC_SEPARATOR).append(year)
                .append(ACConstant.AC_SEPARATOR).append(albumName).toString();
    }

    public String toStringWithoutDiscId()
    {
        return new StringBuilder(artist).append(ACConstant.AC_SEPARATOR)
                .append(year).append(ACConstant.AC_SEPARATOR).append(albumName)
                .toString();
    }

}
