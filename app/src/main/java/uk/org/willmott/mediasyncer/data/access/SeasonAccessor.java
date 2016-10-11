package uk.org.willmott.mediasyncer.data.access;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import uk.org.willmott.mediasyncer.data.TvDbHelper;
import uk.org.willmott.mediasyncer.data.dao.Season;
import uk.org.willmott.mediasyncer.data.dao.Series;

/**
 * Created by tomwi on 11/10/2016.
 */

public class SeasonAccessor implements Accessor<Season, uk.org.willmott.mediasyncer.model.Season> {

    private static String LOG_TAG = SeasonAccessor.class.getSimpleName();

    Context context;

    public SeasonAccessor(Context context) {
        this.context = context;
    }

    TvDbHelper helper = new TvDbHelper(context);
    Dao<Season, Integer> seasonDao = helper.getSeasonDao();

    EpisodeAccessor episodeAccessor = new EpisodeAccessor(context);
    SeriesAccessor seriesAccessor = new SeriesAccessor(context);

    protected List<uk.org.willmott.mediasyncer.model.Season> getSeasonsForSeries(Series series) {
        return null;
    }

    protected Season getSeasonById(int id) {
        try {
            return seasonDao.queryForId(id);
        } catch (SQLException e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }

    @Override
    public Season getDaoForModel(uk.org.willmott.mediasyncer.model.Season model) {
        return new Season(
                model.getSeasonNumber(),
                seriesAccessor.getSeriesForId(model.getId()), // Series
                model.getThumbnailUrl(),
                model.getBannerUrl());
    }

    @Override
    public uk.org.willmott.mediasyncer.model.Season getModelForDao(Season dao) {
        return new uk.org.willmott.mediasyncer.model.Season(
                dao.getId(),
                dao.getSeasonNumber(),
                dao.getBanner(),
                dao.getThumbnail(),
                episodeAccessor.getEpisodesForSeason(dao)); // List of episodes
    }
}
