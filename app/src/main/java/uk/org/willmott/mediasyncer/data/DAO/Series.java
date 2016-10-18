package uk.org.willmott.mediasyncer.data.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * DAO for the series database table.
 * Created by tomwi on 07/10/2016.
 */
@DatabaseTable
public class Series {

    public static final String TRAKT_ID_COLUMN = "traktId";

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(index = true, canBeNull = false)
    private String title;
    @DatabaseField(canBeNull = false, unique = true, columnName = TRAKT_ID_COLUMN)
    private Integer traktId;
    @DatabaseField
    private Integer tmdbId;
    @DatabaseField
    private Integer tvdbId;
    @DatabaseField
    private Integer tvrageId;
    @DatabaseField
    String imdbId;
    @DatabaseField
    private String seriesThumbnail;
    @DatabaseField
    private String seriesBanner;
    @DatabaseField
    private Integer nextEpisode;
    @DatabaseField
    private String overview;

    public Series() {
    } // No args constructor for ormlite

    public Series(String title, Integer traktId, Integer tmdbId, Integer tvdbId, Integer tvrageId, String imdbId, String seriesThumbnail, String seriesBanner, Integer nextEpisode, String overview) {
        this.title = title;
        this.traktId = traktId;
        this.tmdbId = tmdbId;
        this.seriesThumbnail = seriesThumbnail;
        this.seriesBanner = seriesBanner;
        this.nextEpisode = nextEpisode;
        this.overview = overview;
        this.tvdbId = tvdbId;
        this.tvrageId = tvrageId;
        this.imdbId = imdbId;
    }

    public Integer getTvdbId() {
        return tvdbId;
    }

    public void setTvdbId(Integer tvdbId) {
        this.tvdbId = tvdbId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public Integer getTvrageId() {
        return tvrageId;
    }

    public void setTvrageId(Integer tvrageId) {
        this.tvrageId = tvrageId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTraktId() {
        return traktId;
    }

    public void setTraktId(Integer traktId) {
        this.traktId = traktId;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getSeriesThumbnail() {
        return seriesThumbnail;
    }

    public void setSeriesThumbnail(String seriesThumbnail) {
        this.seriesThumbnail = seriesThumbnail;
    }

    public String getSeriesBanner() {
        return seriesBanner;
    }

    public void setSeriesBanner(String seriesBanner) {
        this.seriesBanner = seriesBanner;
    }

    public Integer getNextEpisode() {
        return nextEpisode;
    }

    public void setNextEpisode(Integer nextEpisode) {
        this.nextEpisode = nextEpisode;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}