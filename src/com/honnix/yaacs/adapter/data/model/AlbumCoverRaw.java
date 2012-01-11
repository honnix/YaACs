/**
 * AlbumCoverRaw.java
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
package com.honnix.yaacs.adapter.data.model;

/**
 *
 */
public class AlbumCoverRaw
{

    private String discId;

    private String artist;

    private String year;

    private String albumName;

    private String acFileContent;

    public AlbumCoverRaw()
    {
        super();
    }

    public AlbumCoverRaw(String discId)
    {
        super();

        this.discId = discId;
    }

    public AlbumCoverRaw(String artist, String year, String albumName)
    {
        super();

        this.artist = artist;
        this.year = year;
        this.albumName = albumName;
    }

    public AlbumCoverRaw(String discId, String artist, String year,
            String albumName)
    {
        super();

        this.discId = discId;
        this.artist = artist;
        this.year = year;
        this.albumName = albumName;
    }

    public AlbumCoverRaw(String discId, String artist, String year,
            String albumName, String acFileContent)
    {
        super();

        this.discId = discId;
        this.artist = artist;
        this.year = year;
        this.albumName = albumName;
        this.acFileContent = acFileContent;
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

    /**
     * @return
     */
    public String getAcFileContent()
    {
        return acFileContent;
    }

    /**
     * @return the discId
     */
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

    /**
     * 
     * @param acFileContent
     */
    public void setAcFileContent(String acFileContent)
    {
        this.acFileContent = acFileContent;
    }

    /**
     * @param discId the discId to set
     */
    public void setDiscId(String discId)
    {
        this.discId = discId;
    }

    /**
     * @param year the year to set
     */
    public void setYear(String year)
    {
        this.year = year;
    }

}
