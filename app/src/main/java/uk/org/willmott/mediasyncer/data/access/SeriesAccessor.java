package uk.org.willmott.mediasyncer.data.access;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.data.TvDbHelper;
import uk.org.willmott.mediasyncer.data.dao.Series;

/**
 * The class to handle all interaction with the series table of the database.
 * Created by tomwi on 11/10/2016.
 */

public class SeriesAccessor implements Accessor<Series, uk.org.willmott.mediasyncer.model.Series> {

    private static String LOG_TAG = SeriesAccessor.class.getSimpleName();

    Context context;
    TvDbHelper helper;
    Dao<Series, Integer> seriesDao;
    SeasonAccessor seasonAccessor;
    EpisodeAccessor episodeAccessor;

    public SeriesAccessor(Context context) {
        this.context = context;
        helper = new TvDbHelper(context);
        seriesDao = helper.getSeriesDao();
        seasonAccessor = new SeasonAccessor(context);
        episodeAccessor = new EpisodeAccessor(context);
    }


    protected Series getSeriesForId(int id) {
        try {
            return seriesDao.queryForId(id);
        } catch (SQLException e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }

    /**
     * Get all series's data from the database including seasons and episodes. Note that this may not
     * be complete (with the trakt api).
     */
    public List<uk.org.willmott.mediasyncer.model.Series> getAllSeriesAsModel() {
        try {
            List<uk.org.willmott.mediasyncer.model.Series> seriesModelList = new ArrayList<>();
            for (Series series : seriesDao.queryForAll()) {
                seriesModelList.add(getModelForDao(series));
            }
            return seriesModelList;
        } catch (SQLException e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }


    /**
     * Writes a list of series models (including seasons and episodes) to the databse.
     */
    public void writeAllSeriesToDatabase(List<uk.org.willmott.mediasyncer.model.Series> allSeries) {
        for (uk.org.willmott.mediasyncer.model.Series series : allSeries) {
            try {
                // Try getting the series from the database and updating it
                Series databaseSeries = seriesDao.queryBuilder().where().eq(Series.TRAKT_ID_COLUMN, series.getTraktId()).queryForFirst();
                if (databaseSeries == null) {
                    seriesDao.create(getDaoForModel(series));
                    Log.i(LOG_TAG, series.getTitle() + " created in the database.");
                } else {
                    Series newSeries = getDaoForModel(series);
                    newSeries.setId(databaseSeries.getId());
                    seriesDao.update(newSeries);
                    Log.i(LOG_TAG, series.getTitle() + " updated in the database.");
                }

                // Now add all the seasons to the dbizzle
                try {
                    seasonAccessor.writeToDatabase(series.getSeasons(), seriesDao.queryBuilder().where().eq("traktId", series.getTraktId()).queryForFirst());
                } catch (Exception e2) {
                    Log.e(LOG_TAG, "Error writing " + series.getTitle() + " seasons to the database. " + e2.getMessage());
                }

                // Now that we've hopefully saved all seasons and episodes to the database, we can
                // update the episode ID on the series
                if (series.getNextEpisode() != null) {
                    databaseSeries = seriesDao.queryBuilder().where().eq(Series.TRAKT_ID_COLUMN, series.getTraktId()).queryForFirst();
                    try {
                        databaseSeries.setNextEpisode(episodeAccessor.getForTraktId(series.getNextEpisode().getTraktId().toString()).getId());
                        seriesDao.update(databaseSeries);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error writing next episode for " + series.getTitle() + " to the database. " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error writing " + series.getTitle() + " to the database. " + e.getMessage());
            }
        }
    }


    @Override
    public Series getDaoForModel(uk.org.willmott.mediasyncer.model.Series model) {
        return new Series(
                model.getTitle(),
                model.getTraktId(),
                model.getTmdbId(),
                model.getTvdbId(),
                model.getTvrageId(),
                model.getImdbId(),
                model.getThumbnailUrl(),
                model.getBannerUrl(),
                model.getNextEpisode() == null ? null : model.getNextEpisode().getId(),
                model.getOverview()
        );
    }

    @Override
    public uk.org.willmott.mediasyncer.model.Series getModelForDao(Series dao) {
        return new uk.org.willmott.mediasyncer.model.Series(
                dao.getId(),
                dao.getTitle(),
                dao.getTraktId(),
                dao.getTmdbId(),
                dao.getTvdbId(),
                dao.getTvrageId(),
                dao.getImdbId(),
                dao.getSeriesBanner(),
                dao.getSeriesThumbnail(),
                dao.getNextEpisode() == null ? null : episodeAccessor.getModelForDao(episodeAccessor.getById(dao.getNextEpisode())), //
                seasonAccessor.getSeasonsForSeries(dao),
                dao.getOverview()
        );
    }
}
