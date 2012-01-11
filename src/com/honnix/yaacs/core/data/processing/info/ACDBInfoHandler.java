/**
 * ACDBInfoHandler.java
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.core.data.model.Album;
import com.honnix.yaacs.core.data.model.AlbumCover;
import com.honnix.yaacs.error.ACError;
import com.honnix.yaacs.exception.ACException;

/**
 *
 */
public final class ACDBInfoHandler
    extends AbstractACInfoHandler
{

    private static final Log LOG = LogFactory.getLog(ACDBInfoHandler.class);

    private static final String DISC_ID_COLUMN = "discid";

    private static final String ARTIST_COLUMN = "artist";

    private static final String YEAR_COLUMN = "year";

    private static final String ALBUM_NAME_COLUMN = "albumname";

    private static final String PATH_COLUMN = "path";

    private static final String CREATE_TABLE_SQL =
            new StringBuilder("create table if not exists ac ").append(
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT,").append(
                    " discid TEXT NOT NULL,").append(" artist TEXT NOT NULL,")
                    .append(" year TEXT NOT NULL,").append(
                            " albumname TEXT NOT NULL,").append(
                            " path TEXT NOT NULL);").toString();

    private static final String SELECT_BY_ALBUM_SQL =
            new StringBuilder("select * from ac where artist=?").append(
                    " and year=?").append(" and albumname=?;").toString();

    private static final String SELECT_BY_DISC_ID_SQL =
            "select * from ac where discid=?;";

    private static final String INSERT_RECORD_SQL =
            new StringBuilder(
                    "insert into ac (discid, artist, year, albumname, path) ")
                    .append("values (?, ?, ?, ?, ?);").toString();

    private static final String ERROR_CLOSING_STATEMENT =
            "Error closing statement.";

    private Connection connection;

    public ACDBInfoHandler()
        throws ACException
    {
        super();

        initialize();
    }

    private void closeStatement(Statement statement)
    {
        try
        {
            statement.close();
        }
        catch (SQLException e)
        {
            LOG.warn(ERROR_CLOSING_STATEMENT, e);
        }
    }

    private void initialize()
        throws ACException
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException e)
        {
            LOG.fatal(ACError.INFO_DB_CLASS_FINDING_ERROR_MSG, e);

            throw new ACException(ACError.INFO_DB_CLASS_FINDING_ERROR_MSG,
                    ACError.INFO_DB_CLASS_FINDING_ERROR);
        }

        StringBuilder sbConn =
                new StringBuilder("jdbc:sqlite:")
                        .append(ACPropertiesConstant.INFO_DB_PATH);
        StringBuilder sbPragma =
                new StringBuilder("pragma encoding=\"").append(
                        ACPropertiesConstant.INFO_DB_CHARSET).append("\";");
        Statement statement = null; // NOPMD by ehonlia on 4/10/08 1:26 PM

        try
        {
            connection = DriverManager.getConnection(sbConn.toString());

            statement = connection.createStatement();

            statement.executeUpdate(sbPragma.toString());
            statement.executeUpdate(CREATE_TABLE_SQL);
        }
        catch (SQLException e)
        {
            LOG.fatal(ACError.INFO_DB_INIT_ERROR_MSG, e);

            throw new ACException(ACError.INFO_DB_INIT_ERROR_MSG,
                    ACError.INFO_DB_INIT_ERROR);
        }
        finally
        {
            if (statement != null)
            {
                closeStatement(statement);
            }
        }
    }

    private List<AlbumCover> query(PreparedStatement prepStatement)
        throws SQLException
    {
        List<AlbumCover> acList = new ArrayList<AlbumCover>();
        ResultSet resultSet = null; // NOPMD by ehonlia on 4/10/08 1:26 PM

        resultSet = prepStatement.executeQuery();

        while (resultSet.next())
        {
            acList.add(new AlbumCover(new Album(resultSet
                    .getString(DISC_ID_COLUMN), resultSet
                    .getString(ARTIST_COLUMN),
                    resultSet.getString(YEAR_COLUMN), resultSet
                            .getString(ALBUM_NAME_COLUMN)), resultSet
                    .getString(PATH_COLUMN)));
        }

        resultSet.close();

        return acList;
    }

    @Override
    protected List<AlbumCover> readByAlbum(Album album)
        throws ACException
    {
        List<AlbumCover> acList = null;
        PreparedStatement prepStatement = null;

        try
        {
            prepStatement = connection.prepareStatement(SELECT_BY_ALBUM_SQL);

            prepStatement.setString(1, album.getArtist());
            prepStatement.setString(2, album.getYear());
            prepStatement.setString(3, album.getAlbumName());

            acList = query(prepStatement);
        }
        catch (SQLException e)
        {
            LOG.fatal(ACError.INFO_DB_READING_ERROR_MSG, e);

            throw new ACException(ACError.INFO_DB_READING_ERROR_MSG,
                    ACError.INFO_DB_READING_ERROR);
        }
        finally
        {
            if (prepStatement != null)
            {
                closeStatement(prepStatement);
            }
        }

        return acList;
    }

    @Override
    protected List<AlbumCover> readByDiscId(String discId)
        throws ACException
    {
        List<AlbumCover> acList = null;
        PreparedStatement prepStatement = null;

        try
        {
            prepStatement = connection.prepareStatement(SELECT_BY_DISC_ID_SQL);

            prepStatement.setString(1, discId);

            acList = query(prepStatement);
        }
        catch (SQLException e)
        {
            LOG.fatal(ACError.INFO_DB_READING_ERROR_MSG, e);

            throw new ACException(ACError.INFO_DB_READING_ERROR_MSG,
                    ACError.INFO_DB_READING_ERROR);
        }
        finally
        {
            if (prepStatement != null)
            {
                closeStatement(prepStatement);
            }
        }

        return acList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.honnix.yaacs.core.data.processing.info.ACInfoHandler#save(com.honnix
     * .yaacs.core.data.model.AlbumCover)
     */
    public void save(AlbumCover albumCover)
        throws ACException
    {
        PreparedStatement prepStatement = null;

        try
        {
            prepStatement = connection.prepareStatement(INSERT_RECORD_SQL);

            prepStatement.setString(1, albumCover.getAlbum().getDiscId());
            prepStatement.setString(2, albumCover.getAlbum().getArtist());
            prepStatement.setString(3, albumCover.getAlbum().getYear());
            prepStatement.setString(4, albumCover.getAlbum().getAlbumName());
            prepStatement.setString(5, albumCover.getFilePath());

            prepStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            LOG.fatal(ACError.INFO_DB_SAVING_ERROR_MSG, e);

            throw new ACException(ACError.INFO_DB_SAVING_ERROR_MSG,
                    ACError.INFO_DB_SAVING_ERROR);
        }
        finally
        {
            if (prepStatement != null)
            {
                closeStatement(prepStatement);
            }
        }
    }

}
