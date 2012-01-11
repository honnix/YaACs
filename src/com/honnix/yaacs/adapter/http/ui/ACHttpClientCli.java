/**
 * AHttpClientCli.java
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
package com.honnix.yaacs.adapter.http.ui;

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

import org.apache.commons.httpclient.HttpException;

import com.honnix.yaacs.adapter.data.model.AlbumCoverRaw;
import com.honnix.yaacs.adapter.http.ACHttpClient;
import com.honnix.yaacs.constant.ACConstant;
import com.honnix.yaacs.exception.ACException;
import com.honnix.yaacs.util.StreamUtil;
import com.honnix.yaacs.util.StringUtil;

/**
 * 
 */
public final class ACHttpClientCli
{

    private static final String FAILURE_RESPONSE = "Received failure response.";

    private static final String BAD_INPUT = "Bad input.";

    public static void main(String[] args)
    {
        ACHttpClientCli acHttpClientCli = new ACHttpClientCli();

        BufferedReader br =
                new BufferedReader(new InputStreamReader(System.in));

        while (true)
        {
            acHttpClientCli.help();

            String cmd = null;

            try
            {
                cmd = br.readLine();
            }
            catch (IOException e)
            {
                e.printStackTrace(); // NOPMD by honnix on 3/29/08 10:35 PM
                System.err.println(BAD_INPUT); // NOPMD by honnix on 3/29/08 10:35 PM

                continue;
            }

            if ("li".equals(cmd))
            {
                try
                {
                    acHttpClientCli.handleLogin(br);
                }
                catch (Exception e)
                {
                    e.printStackTrace(); // NOPMD by honnix on 3/29/08 10:35 PM
                    System.err.println("Error logging in."); // NOPMD by honnix on 3/29/08 10:35 PM
                }
            }
            else if ("lo".equals(cmd))
            {
                try
                {
                    acHttpClientCli.handleLogout();
                }
                catch (Exception e)
                {
                    e.printStackTrace(); // NOPMD by honnix on 3/29/08 10:35 PM
                    System.err.println("Error logging out."); // NOPMD by honnix on 3/29/08 10:35 PM
                }
            }
            if ("p".equals(cmd))
            {
                try
                {
                    acHttpClientCli.handlePut(br);
                }
                catch (Exception e)
                {
                    e.printStackTrace(); // NOPMD by honnix on 3/29/08 10:35 PM
                    System.err.println("Error putting album cover."); // NOPMD by honnix on 3/29/08 10:36 PM
                }
            }
            else if ("r".equals(cmd))
            {
                try
                {
                    acHttpClientCli.handleRetrieve(br);
                }
                catch (Exception e)
                {
                    e.printStackTrace(); // NOPMD by honnix on 3/29/08 10:35 PM
                    System.err.println("Error retrieving album cover list."); // NOPMD by honnix on 3/29/08 10:36 PM
                }
            }
            else if ("q".equals(cmd))
            {
                break;
            }
        }
    }

    private ACHttpClient acHttpClient;

    private ACHttpClientCli()
    {
        super();
    }

    private void handleLogin(BufferedReader br)
        throws HttpException, IOException
    {
        System.out.println("Host: "); // NOPMD by honnix on 3/29/08 10:36 PM
        String host = br.readLine();

        System.out.println("Port: "); // NOPMD by honnix on 3/29/08 10:36 PM
        int port = Integer.parseInt(br.readLine());

        String userId = null;
        System.out.println("User ID: "); // NOPMD by honnix on 3/29/08 10:36 PM
        userId = br.readLine();

        String password = null;
        System.out.println("Password: "); // NOPMD by honnix on 3/29/08 11:46 PM
        password = br.readLine();

        acHttpClient = new ACHttpClient(host, port, "/session", "/ac");

        try
        {
            acHttpClient.login(userId, password);
        }
        catch (ACException e)
        {
            e.printStackTrace(); // NOPMD by ehonlia on 4/20/08 3:50 PM
        }
    }

    private void handleLogout()
        throws HttpException, IOException
    {
        try
        {
            acHttpClient.logout();
        }
        catch (ACException e)
        {
            e.printStackTrace(); // NOPMD by ehonlia on 4/20/08 3:50 PM
        }
    }

    private void handlePut(BufferedReader br)
        throws IOException
    {
        String discId = null;
        System.out.println("Disc ID: "); // NOPMD by ehonlia on 3/20/08 4:27 PM
        discId = br.readLine();

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

        InputStream inputStream = null;
        inputStream = new FileInputStream(acFilePath);

        String acFileContent =
                StringUtil.encodeBase64(StreamUtil
                        .inputStream2ByteArray(inputStream));

        StreamUtil.closeStream(inputStream);

        try
        {
            acHttpClient.putAC(new AlbumCoverRaw(discId, artist, year,
                    albumName, acFileContent));
        }
        catch (ACException e)
        {
            e.printStackTrace(); // NOPMD by ehonlia on 4/20/08 3:50 PM
        }
    }

    private void handleRetrieve(BufferedReader br)
        throws IOException
    {
        String filter = null;
        System.out.println("To retrieve by (1. album, 2. Disc ID): "); // NOPMD by honnix on 4/4/08 12:24 AM
        filter = br.readLine();

        List<AlbumCoverRaw> acRawList = null;

        if ("1".equals(filter))
        {
            acRawList = handleRetrieveByAlbum(br);
        }
        else if ("2".equals(filter))
        {
            acRawList = handleRetrieveByDiscId(br);
        }

        if (acRawList != null)
        {
            String destDir = null;
            System.out.println("Destination directory:"); // NOPMD by ehonlia on 4/20/08 3:50 PM
            destDir = br.readLine();

            for (AlbumCoverRaw albumCoverRaw : acRawList)
            {
                StringBuilder sb =
                        new StringBuilder(destDir).append(
                                ACConstant.FILE_SEPARATOR).append(
                                Calendar.getInstance().getTimeInMillis())
                                .append(".jpg");

                save(sb.toString(), StreamUtil.byteArray2InputStream(StringUtil
                        .decodeBase64(albumCoverRaw.getAcFileContent())));
            }
        }
        else
        {
            System.err.println(FAILURE_RESPONSE); // NOPMD by honnix on 3/29/08 11:51 PM
        }
    }

    private List<AlbumCoverRaw> handleRetrieveByAlbum(BufferedReader br)
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

        List<AlbumCoverRaw> acRawList = null;

        try
        {
            acRawList =
                    acHttpClient.getACListByAlbum(new AlbumCoverRaw(artist,
                            year, albumName));
        }
        catch (ACException e)
        {
            e.printStackTrace(); // NOPMD by ehonlia on 4/20/08 3:50 PM
        }

        return acRawList;
    }

    private List<AlbumCoverRaw> handleRetrieveByDiscId(BufferedReader br)
        throws IOException
    {
        String discId = null;
        System.out.print("Disc ID: "); // NOPMD by ehonlia on 3/20/08 4:27 PM
        discId = br.readLine();

        List<AlbumCoverRaw> acRawList = null;

        try
        {
            acRawList =
                    acHttpClient.getACListByDiscId(new AlbumCoverRaw(discId));
        }
        catch (ACException e)
        {
            e.printStackTrace(); // NOPMD by ehonlia on 4/20/08 3:50 PM
        }

        return acRawList;
    }

    private void help()
    {
        System.out.println("------------------------------"); // NOPMD by ehonlia on 3/20/08 4:27 PM
        System.out.println("\"li\" to login"); // NOPMD by honnix on 3/29/08 10:35 PM
        System.out.println("\"lo\" to logout"); // NOPMD by honnix on 3/29/08 10:35 PM
        System.out.println("\"p\" to put a new album cover"); // NOPMD by ehonlia on 3/20/08 4:27 PM
        System.out.println("\"r\" to retrieve album cover list"); // NOPMD by ehonlia on 3/20/08 4:27 PM
        System.out.println("\"q\" to quit"); // NOPMD by ehonlia on 3/20/08 4:27 PM
        System.out.println("------------------------------"); // NOPMD by ehonlia on 3/20/08 4:27 PM
        System.out.print("Enter your choice: "); // NOPMD by ehonlia on 3/20/08 4:27 PM
    }

    private void save(String filePath, InputStream is)
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

}
