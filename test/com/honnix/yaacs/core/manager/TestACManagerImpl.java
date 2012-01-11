/**
 * TestACManagerImpl.java
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import junit.framework.TestCase;

import com.honnix.yaacs.constant.ACConstant;
import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.core.data.model.Album;
import com.honnix.yaacs.core.data.model.AlbumCover;
import com.honnix.yaacs.exception.ACException;
import com.honnix.yaacs.util.StringUtil;

/**
 *
 */
public class TestACManagerImpl
    extends TestCase
{

    private ACManager acManager;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();

        acManager = new ACFileInfoManagerFactory().createACManager();
    }

    public void testGetACList()
    {
        List<AlbumCover> acList = null;

        try
        {
            acList =
                    acManager.getACList(new Album(
                            "aac6400f48249cc9763771cc626a3571", "Honnix",
                            "2008", "Shadow of Light"));
        }
        catch (ACException e)
        {
            fail("ACException");
        }

        assertEquals("Wrong album cover list size.", 1, acList.size());
        assertEquals("Wrong album cover file path.",
                ACConstant.HIDDEN_FILE_PATH, acList.get(0).getFilePath());
        assertNotNull("Wrong input stream.", acList.get(0).getInputStream());
    }

    public void testPutAC()
    {
        File file = new File("etc/yaacs.properties");
        BufferedInputStream bis = null;

        try
        {
            bis = new BufferedInputStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e)
        {
            fail("Could not open etc/yaacs.properties.");
        }

        try
        {
            acManager.putAC(new AlbumCover(new Album(
                    "aac6400f48249cc9763771cc626a3573", "Honnix", "2008",
                    "Shadow of Light"), StringUtil.EMPTY_STRING, bis));
        }
        catch (ACException e)
        {
            fail("ACException");
        }

        file = new File(ACPropertiesConstant.REPO_PATH);
        File[] fileArray = file.listFiles();

        for (int i = 0; i < fileArray.length; i++)
        {
            fileArray[i].delete();
        }

        try
        {
            PrintWriter pw = new PrintWriter(ACPropertiesConstant.INFO_FILE_PATH);

            pw.println("aac6400f48249cc9763771cc626a3571"
                    + ACConstant.AC_SEPARATOR + "Honnix"
                    + ACConstant.AC_SEPARATOR + "2008"
                    + ACConstant.AC_SEPARATOR + "Shadow of Light"
                    + ACConstant.AC_SEPARATOR + ACPropertiesConstant.REPO_PATH
                    + ACConstant.FILE_SEPARATOR
                    + "f04b85d32f91df435c439f5723e0efbb.jpg");
            pw.print(ACConstant.AC_LINE_SEPARATOR);

            pw.flush();
        }
        catch (FileNotFoundException e)
        {
            fail("Could not open " + ACPropertiesConstant.INFO_FILE_PATH);
        }

        PrintWriter pw = null;

        try
        {
            pw =
                    new PrintWriter(ACPropertiesConstant.REPO_PATH
                            + ACConstant.FILE_SEPARATOR
                            + "f04b85d32f91df435c439f5723e0efbb.jpg");
        }
        catch (FileNotFoundException e)
        {
            fail("Could not open test1.jpg to write.");
        }

        pw.flush();
    }
}
