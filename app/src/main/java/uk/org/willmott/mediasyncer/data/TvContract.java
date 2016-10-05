package uk.org.willmott.mediasyncer.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines the table and column names for the TV Show database.
 *
 * Created by tomw on 05/10/2016.
 */
public class TvContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "uk.org.willmott.mediasyncer";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_SERIES = "series";
    public static final String PATH_SEASON = "season";
    public static final String PATH_EPISODE = "episode";

    /**
     * Inner class that defines the table contents of the series table.
     */
    public static final class SeriesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SERIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SERIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SERIES;

        // Table name
        public static final String TABLE_NAME = "series";

        // The title of the series in string format.
        public static final String COLUMN_TITLE = "title";

        // The trakt id of the series
        public static final String COLUMN_TRAKT_ID = "trakt_id";

        // The tvdb id of the series
        public static final String COLUMN_TVDB_ID = "tvdb_id";

        // The thumbnail image url for the series in string format.
        public static final String COLUMN_SERIES_THUMBNAIL = "series_thumbnail";

        // The banner image url for the series in string format.
        public static final String COLUMN_SERIES_BANNER = "series_banner";

        // The season number of the next episode to watch as an integer
        public static final String COLUMN_NEXT_EPISODE_SEASON_NUMBER = "next_episode_season_number";

        // The episode number of the next episode to watch as an integer
        public static final String COLUMN_NEXT_EPISODE_EPISODE_NUMBER = "next_episode_episode_number";

        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * Inner class that defines the table contents of the season table.
     */
    public static final class SeasonEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SEASON).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEASON;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEASON;

        public static final String TABLE_NAME = "season";

        // The foreign key for the id of the series table to which this season relates.
        public static final String COLUMN_SERIES_KEY = "series_id";

        // The number of the season as an integer
        public static final String COLUMN_SEASON_NUMBER = "season_number";
    }


}
