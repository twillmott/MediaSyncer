package uk.org.willmott.mediasyncer.data.access;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.data.TvDbHelper;
import uk.org.willmott.mediasyncer.data.dao.Season;
import uk.org.willmott.mediasyncer.data.dao.Series;

/**
 * The class to handle all interaction with the season table of the database.
 * Created by tomwi on 11/10/2016.
 */

public class SeasonAccessor implements Accessor<Season, uk.org.willmott.mediasyncer.model.Season> {

    private static String LOG_TAG = SeasonAccessor.class.getSimpleName();

    Context context;
    TvDbHelper helper;
    Dao<Season, Integer> seasonDao;
    EpisodeAccessor episodeAccessor;

    public SeasonAccessor(Context context) {
        this.context = context;
        helper = new TvDbHelper(context);
        seasonDao = helper.getSeasonDao();
        episodeAccessor = new EpisodeAccessor(context);
    }

    /**
     * Get all the fully populated season (including episodes) for a given series.
     */
    protected List<uk.org.willmott.mediasyncer.model.Season> getSeasonsForSeries(Series series) {
        try {
            List<uk.org.willmott.mediasyncer.model.Season> seasons = new ArrayList<>();

            for (Season season : seasonDao.queryBuilder().where().eq(Season.SERIES_COLUMN, series.getId()).query()) {
                seasons.add(getModelForDao(season));
            }
            return seasons;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }

    protected Season getSeasonById(int id) {
        try {
            return seasonDao.queryForId(id);
        } catch (SQLException e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }


    /**
     * Write a list of seasons for a given show to the database.
     *
     * @param seasons All seasons must be of the same series
     * @param series  The series that the season belongs to.
     */
    protected void writeToDatabase(List<uk.org.willmott.mediasyncer.model.Season> seasons, Series series) {
        for (uk.org.willmott.mediasyncer.model.Season season : seasons) {
            try {
                // Try getting the series from the database
                Season databaseSeason = seasonDao.queryBuilder().where().eq("traktId", season.getTraktId()).queryForFirst();
                if (databaseSeason == null) {
                    Season newSeason = getDaoForModel(season);
                    newSeason.setSeries(series.getId());
                    seasonDao.create(newSeason);
                } else {
                    Season newSeason = getDaoForModel(season);
                    newSeason.setSeries(series.getId());
                    newSeason.setId(databaseSeason.getId());
                    seasonDao.update(newSeason);
                }

                // Now add all the episodes to the dbizzle
                try {
                    episodeAccessor.writeToDatabase(season.getEpisodes(), seasonDao.queryBuilder().where().eq("traktId", season.getTraktId()).queryForFirst());
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error writing " + series.getTitle() + ", season " + season.getSeasonNumber() + " episodes to the database. " + e.getMessage());
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error writing " + series.getTitle() + ", season " + season.getSeasonNumber() + " to the database. " + e.getMessage());
            }
        }
    }


    @Override
    public Season getDaoForModel(uk.org.willmott.mediasyncer.model.Season model) {
        return new Season(
                model.getTvdbId(),
                model.getTraktId(),
                model.getSeasonNumber(),
                null,
                model.getThumbnailUrl(),
                model.getBannerUrl());
    }

    @Override
    public uk.org.willmott.mediasyncer.model.Season getModelForDao(Season dao) {
        return new uk.org.willmott.mediasyncer.model.Season(
                dao.getId(),
                dao.getTvdbId(),
                dao.getTraktId(),
                dao.getSeasonNumber(),
                dao.getBanner(),
                dao.getThumbnail(),
                episodeAccessor.getEpisodesForSeason(dao)); // List of episodes
    }
}
