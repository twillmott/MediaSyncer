package uk.org.willmott.mediasyncer.data.access;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

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
        return new Episode(
                model.getTvdbId(),
                model.getTraktId(),
                null,
                model.getEpisodeNumber(),
                model.getTitle(),
                model.getOverview(),
                model.getBannerUrl(),
                model.getThumbnailUrl()
        );
    }

    @Override
    public uk.org.willmott.mediasyncer.model.Episode getModelForDao(Episode dao) {
        return new uk.org.willmott.mediasyncer.model.Episode(
                dao.getId(),
                dao.getTvdbId(),
                dao.getTraktId(),
                dao.getTitle(),
                dao.getEpisodeNumber(),
                dao.getBannerUrl(),
                dao.getThumbnailUrl(),
                dao.getOverview()
        );
    }
}
