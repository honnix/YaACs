/**
 * Main.java
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
package com.honnix.yaacs.adapter.cli;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import com.honnix.yaacs.constant.ACConstant;
import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.core.data.model.Album;
import com.honnix.yaacs.core.data.model.AlbumCover;
import com.honnix.yaacs.core.manager.ACFileInfoManagerFactory;
import com.honnix.yaacs.core.manager.ACManager;
import com.honnix.yaacs.exception.ACException;
import com.honnix.yaacs.util.StreamUtil;

/**
 * This class is used for test purpose only, so no session control
 * at all. All requests are sent to ACManager directly.
 */
public final class Main
{

    private static AlbumCover handleAdd(BufferedReader br)
        throws IOException
    {
        String artist = null;
        System.out.print("Artist: "); // NOPMD by ehonlia on 3/20/08 4:27 PM
        artist = br.readLine();

        String year = null;
        System.out.print("Year: "); // NOPMD by ehonlia on 3/20/08 4:27 PM
        year = br.readLine();

        String albumName = null;
        System.out.print("Album name: "); // NOPMD by ehonlia on 3/20/08 4:27 PM
        albumName = br.readLine();

        String acFilePath = null;
        System.out.print("Album cover file path: "); // NOPMD by ehonlia on 3/20/08 4:27 PM
        acFilePath = br.readLine();

        InputStream is = null;
        is = new FileInputStream(acFilePath);

        return new AlbumCover(new Album(artist, year, albumName),
                ACConstant.HIDDEN_FILE_PATH, is);
    }

    private static Album handleRetrieve(BufferedReader br)
        throws IOException
    {
        String artist = null;
        System.out.print("Artist: "); // NOPMD by ehonlia on 3/20/08 4:27 PM
        artist = br.readLine();

        String year = null;
        System.out.print("Year: "); // NOPMD by ehonlia on 3/20/08 4:27 PM
        year = br.readLine();

        String albumName = null;
        System.out.print("Album name: "); // NOPMD by ehonlia on 3/20/08 4:27 PM
        albumName = br.readLine();

        return new Album(artist, year, albumName);
    }

    private static void help()
    {
        System.out.println("------------------------------"); // NOPMD by ehonlia on 3/20/08 4:27 PM
        System.out.println("\"a\" to add a new cover album"); // NOPMD by ehonlia on 3/20/08 4:27 PM
        System.out.println("\"r\" to retrieve cover albums"); // NOPMD by ehonlia on 3/20/08 4:27 PM
        System.out.println("\"q\" to quit"); // NOPMD by ehonlia on 3/20/08 4:27 PM
        System.out.println("------------------------------"); // NOPMD by ehonlia on 3/20/08 4:27 PM
        System.out.print("Enter your choice: "); // NOPMD by ehonlia on 3/20/08 4:27 PM
    }

    private static void save(String filePath, InputStream is)
        throws IOException
    {
        File file = new File(filePath);
        File directory = new File(file.getParent());

        if (!directory.exists())
        {
            directory.mkdirs();
        }

        BufferedOutputStream bos =
                new BufferedOutputStream(new FileOutputStream(file));
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[255];
        int length = -1;

        while ((length = bis.read(buffer)) != -1)
        {
            bos.write(buffer, 0, length);
        }

        bos.flush();
        StreamUtil.closeStream(bos);
        StreamUtil.closeStream(bis);
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        ACManager acManager = null;

        try
        {
            acManager = new ACFileInfoManagerFactory().createACManager();
        }
        catch (ACException e)
        {
            e.printStackTrace(); // NOPMD by ehonlia on 3/20/08 4:27 PM

            System.exit(1);
        }

        BufferedReader br =
                new BufferedReader(new InputStreamReader(System.in));

        while (true)
        {
            help();

            String cmd = null;

            try
            {
                cmd = br.readLine();
            }
            catch (IOException e)
            {
                e.printStackTrace(); // NOPMD by ehonlia on 3/20/08 4:27 PM
            }

            if ("a".equals(cmd))
            {
                AlbumCover albumCover = null;

                try
                {
                    albumCover = handleAdd(br);
                }
                catch (IOException e)
                {
                    e.printStackTrace(); // NOPMD by ehonlia on 3/20/08 4:27 PM

                    continue;
                }

                try
                {
                    acManager.putAC(albumCover);
                }
                catch (ACException e)
                {
                    e.printStackTrace(); // NOPMD by ehonlia on 3/20/08 4:27 PM
                }
            }
            else if ("r".equals(cmd))
            {
                Album album = null;

                try
                {
                    album = handleRetrieve(br);
                }
                catch (IOException e)
                {
                    e.printStackTrace(); // NOPMD by ehonlia on 3/20/08 4:27 PM

                    continue;
                }

                List<AlbumCover> acList = null;

                try
                {
                    acList = acManager.getACList(album);
                }
                catch (ACException e)
                {
                    e.printStackTrace(); // NOPMD by ehonlia on 3/20/08 4:27 PM
                }

                if (acList.isEmpty())
                {
                    System.out.println("No such album in repository"); // NOPMD by ehonlia on 3/20/08 4:27 PM

                    continue;
                }

                System.out.print("Destination directory: "); // NOPMD by ehonlia on 3/20/08 4:27 PM
                String destDir = null;

                try
                {
                    destDir = br.readLine();
                }
                catch (IOException e)
                {
                    e.printStackTrace(); // NOPMD by ehonlia on 3/20/08 4:27 PM
                }

                for (AlbumCover albumCover : acList)
                {
                    try
                    {
                        save(destDir + ACConstant.FILE_SEPARATOR
                                + Calendar.getInstance().getTimeInMillis()
                                + ACPropertiesConstant.AC_FILE_EXT, albumCover
                                .getInputStream());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace(); // NOPMD by ehonlia on 3/20/08 4:27 PM
                    }
                }
            }
            else if ("q".equals(cmd))
            {
                break;
            }
        }
    }

    private Main()
    {

    }

}
