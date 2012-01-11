/**
 * ACFileInfoHandler.java
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.honnix.yaacs.constant.ACConstant;
import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.core.data.model.Album;
import com.honnix.yaacs.core.data.model.AlbumCover;
import com.honnix.yaacs.error.ACError;
import com.honnix.yaacs.exception.ACException;
import com.honnix.yaacs.util.StreamUtil;
import com.honnix.yaacs.util.StringUtil;

/**
 *
 */
public final class ACFileInfoHandler
    extends AbstractACInfoHandler
{

    private static final Log LOG = LogFactory.getLog(ACFileInfoHandler.class);

    private static final String MATCH = ".+";

    private static final String RECORD_REGEX =
            new StringBuilder(MATCH).append(ACConstant.AC_SEPARATOR).append(
                    MATCH).append(ACConstant.AC_SEPARATOR).append(MATCH)
                    .append(ACConstant.AC_SEPARATOR).append(MATCH).append(
                            ACConstant.AC_SEPARATOR).append(MATCH).toString();

    private Map<String, List<AlbumCover>> acAlbumMap;

    private Map<String, List<AlbumCover>> acDiscIdMap;

    public ACFileInfoHandler()
        throws ACException
    {
        super();

        acAlbumMap = new HashMap<String, List<AlbumCover>>();
        acDiscIdMap = new HashMap<String, List<AlbumCover>>();

        initialize();
    }

    private String buildRecord(AlbumCover albumCover)
    {
        return new StringBuilder(albumCover.getAlbum().toString()).append(
                ACConstant.AC_SEPARATOR).append(albumCover.getFilePath())
                .toString();
    }

    private void initialize()
        throws ACException
    {
        BufferedReader br = null;

        try
        {
            br =
                    new BufferedReader(new InputStreamReader(
                            new FileInputStream(ACPropertiesConstant.INFO_FILE_PATH),
                            ACPropertiesConstant.INFO_FILE_CHARSET));
        }
        catch (FileNotFoundException e)
        {
            LOG.fatal(ACError.INFO_FILE_FINDING_ERROR_MSG, e);

            throw new ACException(ACError.INFO_FILE_FINDING_ERROR_MSG,
                    ACError.INFO_FILE_FINDING_ERROR, e);
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.fatal("This should not happen anyway.", e);

            throw new ACException(ACError.INFO_FILE_READING_ERROR_MSG,
                    ACError.INFO_FILE_READING_ERROR, e);
        }

        String line = null;

        try
        {
            while ((line = br.readLine()) != null)
            {
                if (StringUtil.EMPTY_STRING.equals(line)
                        || line.charAt(0) == '#')
                {
                    continue;
                }

                updateMaps(line);
            }
        }
        catch (IOException e)
        {
            LOG.fatal(ACError.INFO_FILE_READING_ERROR_MSG, e);

            throw new ACException(ACError.INFO_FILE_READING_ERROR_MSG,
                    ACError.INFO_FILE_READING_ERROR, e);
        }
        finally
        {
            StreamUtil.closeStream(br);
        }
    }

    private void updateMaps(String line)
    {
        if (line.matches(RECORD_REGEX))
        {
            // The content by order is discid, artist, year, 
            // album name, file path.
            String[] elems = line.split(ACConstant.AC_SEPARATOR);

            Album album = new Album(elems[0], elems[1], elems[2], elems[3]);
            AlbumCover albumCover = new AlbumCover(album, elems[4]);
            String albumKey = album.toStringWithoutDiscId();
            String discIdKey = album.getDiscId();

            if (acAlbumMap.containsKey(albumKey))
            {
                List<AlbumCover> acList = acAlbumMap.get(albumKey);

                acList.add(albumCover);
            }
            else
            {
                List<AlbumCover> acList = new ArrayList<AlbumCover>();

                acList.add(albumCover);
                acAlbumMap.put(albumKey, acList);
            }

            if (acDiscIdMap.containsKey(discIdKey))
            {
                List<AlbumCover> acList = acDiscIdMap.get(discIdKey);

                acList.add(albumCover);
            }
            else
            {
                List<AlbumCover> acList = new ArrayList<AlbumCover>();

                acList.add(albumCover);
                acDiscIdMap.put(discIdKey, acList);
            }
        }
        else
        {
            StringBuilder sb =
                    new StringBuilder(
                            "Wrong format of album cover information \"")
                            .append(line).append("\".");

            LOG.error(sb.toString());
        }
    }

    private void writeBack()
        throws ACException
    {
        PrintWriter pw = null;

        try
        {
            pw =
                    new PrintWriter(ACPropertiesConstant.INFO_FILE_PATH,
                            ACPropertiesConstant.INFO_FILE_CHARSET);
        }
        catch (FileNotFoundException e)
        {
            LOG.fatal(ACError.INFO_FILE_FINDING_ERROR_MSG, e);

            throw new ACException(ACError.INFO_FILE_FINDING_ERROR_MSG,
                    ACError.INFO_FILE_FINDING_ERROR);
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.fatal("This should not happen anyway.", e);

            throw new ACException(ACError.INFO_FILE_SAVING_ERROR_MSG,
                    ACError.INFO_FILE_SAVING_ERROR);
        }

        for (String albumKey : acAlbumMap.keySet())
        {
            for (AlbumCover albumCover : acAlbumMap.get(albumKey))
            {
                pw.print(buildRecord(albumCover));
                pw.print(ACConstant.AC_LINE_SEPARATOR);
            }
        }

        pw.flush();
    }

    @Override
    protected List<AlbumCover> readByAlbum(Album album)
        throws ACException
    {
        List<AlbumCover> acList = acAlbumMap.get(album.toStringWithoutDiscId());

        if (acList == null)
        {
            acList = new ArrayList<AlbumCover>();
        }

        return acList;
    }

    @Override
    protected List<AlbumCover> readByDiscId(String discId)
        throws ACException
    {
        List<AlbumCover> acList = acDiscIdMap.get(discId);

        if (acList == null)
        {
            acList = new ArrayList<AlbumCover>();
        }

        return acList;
    }

    /*
     * (non-Javadoc)
     * @see com.honnix.yaacs.core.data.processing.info.ACInfoHandler#save(com.honnix.yaacs.core.data.model.AlbumCover)
     */
    public void save(AlbumCover albumCover)
        throws ACException
    {
        updateMaps(buildRecord(albumCover));
        writeBack();
    }

}
