package uk.org.willmott.mediasyncer.data.access;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

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
        return null;
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
