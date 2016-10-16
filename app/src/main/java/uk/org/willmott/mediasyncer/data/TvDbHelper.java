package uk.org.willmott.mediasyncer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import uk.org.willmott.mediasyncer.R;
import uk.org.willmott.mediasyncer.data.dao.Episode;
import uk.org.willmott.mediasyncer.data.dao.Season;
import uk.org.willmott.mediasyncer.data.dao.Series;

/**
 * Manages a local database for TV show/series data.
 * Created by tomw on 05/10/2016.
 */
public class TvDbHelper extends OrmLiteSqliteOpenHelper {

    // If the database schema is changes, the database version needs to be incremented.
    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "tv.db";

    private final String LOG_TAG = getClass().getName();

    private Dao<Series, Integer> seriesDao = null;
    private Dao<Season, Integer> seasonDao = null;
    private Dao<Episode, Integer> episodeDao = null;

    public TvDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Series.class);
            TableUtils.createTable(connectionSource, Season.class);
            TableUtils.createTable(connectionSource, Episode.class);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Error creating new tables", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Series.class, true);
            TableUtils.dropTable(connectionSource, Season.class, true);
            TableUtils.dropTable(connectionSource, Episode.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Error upgrading database tables.", e);

        }
    }

    public Dao<Series, Integer> getSeriesDao() {
        if (seriesDao == null) {
            try {
                seriesDao = getDao(Series.class);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error creating dao " + e);
            }
        }
        return seriesDao;
    }

    public Dao<Season, Integer> getSeasonDao() {
        if (seasonDao == null) {
            try {
                seasonDao = getDao(Season.class);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error creating dao " + e);
            }
        }
        return seasonDao;
    }

    public Dao<Episode, Integer> getEpisodeDao() {
        if (seasonDao == null) {
            try {
                episodeDao = getDao(Episode.class);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error creating dao " + e);
            }
        }
        return episodeDao;
    }
}
