/**
 * TestACFileInfoLoader.java
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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import junit.framework.TestCase;

import com.honnix.yaacs.constant.ACConstant;
import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.core.data.model.Album;
import com.honnix.yaacs.core.data.model.AlbumCover;
import com.honnix.yaacs.exception.ACException;

/**
 *
 */
public class TestACFileInfoHandler
    extends TestCase
{

    private static final String ARTIST = "Honnix";

    private static final String YEAR = "2008";

    private static final String ALBUM_NAME = "Shadow of Light";

    private ACInfoHandler acFileInfoHandler;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();

        acFileInfoHandler = new ACFileInfoHandler();
    }

    public void testRead()
    {
        List<AlbumCover> acList = null;

        try
        {
            acList =
                    acFileInfoHandler.read(new Album(ARTIST, YEAR, ALBUM_NAME));
        }
        catch (ACException e)
        {
            fail("Error reading.");
        }

        assertEquals("Wrong album cover list size.", 1, acList.size());
        assertEquals("Wrong album cover file path.", ACPropertiesConstant.REPO_PATH
                + ACConstant.FILE_SEPARATOR
                + "f04b85d32f91df435c439f5723e0efbb.jpg", acList.get(0)
                .getFilePath());
    }

    public void testSave()
    {
        try
        {
            acFileInfoHandler.save(new AlbumCover(new Album(
                    "aac6400f48249cc9763771cc626a3571", ARTIST, YEAR,
                    ALBUM_NAME), ACPropertiesConstant.REPO_PATH
                    + ACConstant.FILE_SEPARATOR + "111111.jpg"));
        }
        catch (ACException e)
        {
            fail("Error saving.");
        }

        List<AlbumCover> acList = null;

        try
        {
            acList =
                    acFileInfoHandler.read(new Album(ARTIST, YEAR, ALBUM_NAME));
        }
        catch (ACException e)
        {
            fail("Error reading.");
        }

        assertEquals("Wrong album cover list size.", 2, acList.size());

        try
        {
            PrintWriter pw = new PrintWriter(ACPropertiesConstant.INFO_FILE_PATH);

            pw.print("aac6400f48249cc9763771cc626a3571"
                    + ACConstant.AC_SEPARATOR + ARTIST
                    + ACConstant.AC_SEPARATOR + YEAR + ACConstant.AC_SEPARATOR
                    + ALBUM_NAME + ACConstant.AC_SEPARATOR
                    + ACPropertiesConstant.REPO_PATH + ACConstant.FILE_SEPARATOR
                    + "f04b85d32f91df435c439f5723e0efbb.jpg");
            pw.print(ACConstant.AC_LINE_SEPARATOR);

            pw.flush();
        }
        catch (FileNotFoundException e)
        {
            fail("Could not write info file.");
        }
    }
}
