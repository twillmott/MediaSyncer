package uk.org.willmott.mediasyncer.data.access;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.data.TvDbHelper;
import uk.org.willmott.mediasyncer.data.dao.Episode;
import uk.org.willmott.mediasyncer.data.dao.Season;

/**
 * Created by tomwi on 11/10/2016.
 */

public class EpisodeAccessor implements Accessor<Episode, uk.org.willmott.mediasyncer.model.Episode> {

    private static String LOG_TAG = EpisodeAccessor.class.getSimpleName();

    Context context;

    public EpisodeAccessor(Context context) {
        this.context = context;
    }

    TvDbHelper helper = new TvDbHelper(context);
    Dao<Episode, Integer> episodeDao = helper.getEpisodeDao();

    SeasonAccessor seasonAccessor = new SeasonAccessor(context);


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


    @Override
    public Episode getDaoForModel(uk.org.willmott.mediasyncer.model.Episode model) {
        return new Episode(
                seasonAccessor.getSeasonById(model.getId()),
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
                dao.getTitle(),
                dao.getEpisodeNumber(),
                dao.getBannerUrl(),
                dao.getThumbnailUrl(),
                dao.getOverview()
        );
    }
}
