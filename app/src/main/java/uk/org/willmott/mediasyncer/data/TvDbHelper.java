package uk.org.willmott.mediasyncer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for TV show/series data.
 * Created by tomw on 05/10/2016.
 */
public class TvDbHelper extends SQLiteOpenHelper {

    // If the database schema is changes, the database version needs to be incremented.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "tv.db";

    public TvDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold tv series data.
        final String SQL_CREATE_SERIES_TABLE = "CREATE TABLE " + TvContract.SeriesEntry.TABLE_NAME + "(" +
                TvContract.SeriesEntry._ID + " INTEGER PRIMARY KEY," +
                TvContract.SeriesEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                TvContract.SeriesEntry.COLUMN_TRAKT_ID + " INTEGER NOT NULL, " +
                TvContract.SeriesEntry.COLUMN_TVDB_ID + " INTEGER NOT NULL, " +
                TvContract.SeriesEntry.COLUMN_SERIES_THUMBNAIL + " TEXT NOT NULL " +
                TvContract.SeriesEntry.COLUMN_SERIES_BANNER + " TEXT NOT NULL " +
                TvContract.SeriesEntry.COLUMN_NEXT_EPISODE_SEASON_NUMBER + " INTEGER NOT NULL " +
                TvContract.SeriesEntry.COLUMN_NEXT_EPISODE_EPISODE_NUMBER + " INTEGER NOT NULL " +
                " );";

        // Create a table to hold tv season data.
        final String SQL_CREATE_SEASON_TABLE = "CREATE TABLE " + TvContract.SeasonEntry.TABLE_NAME + "(" +
                TvContract.SeasonEntry._ID + " INTEGER PRIMARY KEY," +
                TvContract.SeasonEntry.COLUMN_SEASON_NUMBER + " INTEGER NOT NULL, " +

                // Set up the series key column as a foreign key to series table.
                " FOREIGN KEY (" + TvContract.SeasonEntry.COLUMN_SERIES_KEY + ") REFERENCES " +
                TvContract.SeriesEntry.TABLE_NAME + " (" + TvContract.SeriesEntry._ID + "), " +

                // Ensure that the season tables only has one entry for each season number for a given episode.
                " UNIQUE (" + TvContract.SeasonEntry.COLUMN_SERIES_KEY + ", " +
                        TvContract.SeasonEntry.COLUMN_SEASON_NUMBER + ") ON CONFLICT REPLACE " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_SERIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SEASON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TvContract.SeriesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TvContract.SeasonEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
