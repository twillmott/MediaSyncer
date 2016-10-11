package uk.org.willmott.mediasyncer.data.access;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import uk.org.willmott.mediasyncer.data.TvDbHelper;
import uk.org.willmott.mediasyncer.data.dao.Series;

/**
 * Created by tomwi on 11/10/2016.
 */

public class SeriesAccessor implements Accessor<Series, uk.org.willmott.mediasyncer.model.Series> {

    private static String LOG_TAG = SeriesAccessor.class.getSimpleName();

    Context context;

    public SeriesAccessor(Context context) {
        this.context = context;
    }

    TvDbHelper helper = new TvDbHelper(context);
    Dao<Series, Integer> seriesDao = helper.getSeriesDao();

    SeasonAccessor seasonAccessor = new SeasonAccessor(context);
    EpisodeAccessor episodeAccessor = new EpisodeAccessor(context);

    protected Series getSeriesForId(int id) {
        try {
            return seriesDao.queryForId(id);
        } catch (SQLException e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }


    @Override
    public Series getDaoForModel(uk.org.willmott.mediasyncer.model.Series model) {
        return new Series(
                model.getTitle(),
                model.getTraktId(),
                model.getTvdbId(),
                model.getThumbnailUrl(),
                model.getBannerUrl(),
                episodeAccessor.getDaoForModel(model.getNextEpisode()),
                model.getOverview()
        );
    }

    @Override
    public uk.org.willmott.mediasyncer.model.Series getModelForDao(Series dao) {
        return new uk.org.willmott.mediasyncer.model.Series(
                dao.getId(),
                dao.getTitle(),
                dao.getTraktId(),
                dao.getTvdbId(),
                dao.getSeriesBanner(),
                dao.getSeriesThumbnail(),
                episodeAccessor.getModelForDao(dao.getNextEpisode()),
                seasonAccessor.getSeasonsForSeries(dao),
                dao.getOverview()
        );
    }
}
