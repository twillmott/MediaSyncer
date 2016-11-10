package uk.org.willmott.mediasyncer.data.access;

import android.content.Context;
import android.util.Log;

import com.google.common.collect.Lists;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.data.TvDbHelper;
import uk.org.willmott.mediasyncer.data.dao.Episode;
import uk.org.willmott.mediasyncer.data.dao.Season;
import uk.org.willmott.mediasyncer.data.dao.Series;

/**
 * The class to handle all interaction with the episode table of the database.
 * Created by tomwi on 11/10/2016.
 */

public class EpisodeAccessor implements Accessor<Episode, uk.org.willmott.mediasyncer.model.Episode> {

    private static String LOG_TAG = EpisodeAccessor.class.getSimpleName();

    Context context;
    TvDbHelper helper;
    Dao<Episode, Integer> episodeDao;

    public EpisodeAccessor(Context context) {
        this.context = context;
        helper = new TvDbHelper(context);
        episodeDao = helper.getEpisodeDao();
    }



    protected List<uk.org.willmott.mediasyncer.model.Episode> getEpisodesForSeason(Season season) {
        try {
            List<uk.org.willmott.mediasyncer.model.Episode> episodes = new ArrayList<>();

            for (Episode episode : episodeDao.queryBuilder().where().eq(Episode.SEASON_COLUMN, season.getId()).query()) {
                episodes.add(getModelForDao(episode));
            }
            return episodes;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }


    /**
     * Write a list of episodes for a given show to the database.
     *
     * @param episodes All episodes must be of the same season
     * @param season   The episode that the season belongs to.
     */
    protected void writeToDatabase(List<uk.org.willmott.mediasyncer.model.Episode> episodes, Season season) {
        for (uk.org.willmott.mediasyncer.model.Episode episode : episodes) {
            try {
                // Try getting the series from the database
                Episode databaseEpisode = episodeDao.queryBuilder().where().eq("traktId", episode.getTraktId()).queryForFirst();
                if (databaseEpisode == null) {
                    Episode newEpisode = getDaoForModel(episode);
                    newEpisode.setSeason(season.getId());
                    episodeDao.create(newEpisode);
                } else {
                    Episode newEpisode = getDaoForModel(episode);
                    newEpisode.setSeason(season.getId());
                    newEpisode.setId(databaseEpisode.getId());
                    episodeDao.update(newEpisode);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error writing episode: " + episode.getTitle() + " to the database. " + e.getMessage());
            }
        }
    }

    public List<Episode> getAllUnwatched() {
        try {
            return episodeDao.queryBuilder().where().isNull("lastWatched").query();
        } catch (Exception e) {
            Log.e(LOG_TAG, "No unwatched episodes found, should this happen? " + e.getMessage());
            return Lists.newArrayList();
        }
    }


    /**
     * Update an episodes collected time based on the show, season number and episode number.
     *
     * @param showId
     * @param seasonNumber
     * @param episodeNumber
     * @param collectedTime
     */
    public void markEpisodeAsCollectedAndAired(Integer showId, Integer seasonNumber, Integer episodeNumber, Long collectedTime) {
        try {
            String sqlStatement =
                    "UPDATE Episode SET lastCollected=?" +
                            "WHERE seasonId IN (SELECT id FROM Season WHERE seriesId = ? AND seasonNumber = ?) AND episodeNumber = ?";

            episodeDao.updateRaw(
                    sqlStatement,
                    collectedTime.toString(),
                    showId.toString(),
                    seasonNumber.toString(),
                    episodeNumber.toString());
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error marking episode for show " + showId.toString() + "as collected." + e.getMessage());
        }
    }


    /**
     * Get the time last watched episode in the database. Will return null if there isn't one.
     *
     * @return
     */
    public Long getLastWatchedEpisode() {
        try {
            List<Episode> episodes = episodeDao.queryBuilder().orderBy("lastWatched", false).limit(1L).query();
            if (!episodes.isEmpty()) {
                return episodes.get(0).getLastWatched();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "No unwatched episodes found, should this happen? " + e.getMessage());
        }
        return null;
    }


    /**
     * Update the last watched field for an episode using its traktId.
     */
    public void updateTraktWatchedEpisode(uk.org.willmott.mediasyncer.model.Episode episode) {
        try {
            UpdateBuilder<Episode, Integer> updateBuilder = episodeDao.updateBuilder();
            updateBuilder.updateColumnValue("lastWatched", episode.getLastWatched());
            updateBuilder.where().eq("traktId", episode.getTraktId());
            updateBuilder.update();
        } catch (Exception e) {
            Log.e(LOG_TAG, "No unwatched episodes found, should this happen? " + e.getMessage());
        }
    }


    protected Episode getById(Integer id) {
        if (id == null) {
            return null;
        }
        try {
            return episodeDao.queryForId(id);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error fetching episode with id :[" + id + "] from the databse.");
            return null;
        }
    }


    protected Episode getForTraktId(String traktId) {
        if (traktId == null) {
            return null;
        }
        try {
            return episodeDao.queryBuilder().where().eq("traktId", traktId).queryForFirst();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error fetching episode with trakt id :[" + traktId + "] from the databse.");
            return null;
        }
    }


    @Override
    public Episode getDaoForModel(uk.org.willmott.mediasyncer.model.Episode model) {
        Episode episode = new Episode(
                model.getTmdbId(),
                model.getTraktId(),
                model.getTvdbId(),
                model.getTvrageId(),
                model.getImdbId(),
                null,
                model.getEpisodeNumber(),
                model.getTitle(),
                model.getOverview(),
                model.getBannerUrl(),
                model.getThumbnailUrl()
        );
        episode.setLastTmdbUpdate(model.getLastTmdbUpdate());
        episode.setLastWatched(model.getLastWatched());
        episode.setLastCollected(model.getLastCollected());
        episode.setAiredOn(model.getAiredOn());

        return episode;
    }

    @Override
    public uk.org.willmott.mediasyncer.model.Episode getModelForDao(Episode dao) {
        uk.org.willmott.mediasyncer.model.Episode episode = new uk.org.willmott.mediasyncer.model.Episode(
                dao.getId(),
                dao.getTmdbId(),
                dao.getTraktId(),
                dao.getTvdbId(),
                dao.getTvrageId(),
                dao.getImdbId(),
                dao.getTitle(),
                dao.getEpisodeNumber(),
                dao.getBannerUrl(),
                dao.getThumbnailUrl(),
                dao.getOverview()
        );
        episode.setLastTmdbUpdate(dao.getLastTmdbUpdate());
        episode.setLastWatched(dao.getLastWatched());
        episode.setLastCollected(dao.getLastCollected());
        episode.setAiredOn(dao.getAiredOn());

        return episode;
    }
}
