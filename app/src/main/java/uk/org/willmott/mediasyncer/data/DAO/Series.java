package uk.org.willmott.mediasyncer.data.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * DAO for the series database table.
 * Created by tomwi on 07/10/2016.
 */
@DatabaseTable
public class Series {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(index = true, canBeNull = false)
    private String title;
    @DatabaseField(canBeNull = false)
    private String traktId;
    @DatabaseField(canBeNull = false)
    private Integer tvdbId;
    @DatabaseField
    private String seriesThumbnail;
    @DatabaseField
    private String seriesBanner;
    @DatabaseField
    private Episode nextEpisode;
    @DatabaseField
    private String overview;

    public Series() {
    } // No args constructor for ormlite

    public Series(String title, String traktId, Integer tvdbId, String seriesThumbnail, String seriesBanner, Episode nextEpisode, String overview) {
        this.title = title;
        this.traktId = traktId;
        this.tvdbId = tvdbId;
        this.seriesThumbnail = seriesThumbnail;
        this.seriesBanner = seriesBanner;
        this.nextEpisode = nextEpisode;
        this.overview = overview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTraktId() {
        return traktId;
    }

    public void setTraktId(String traktId) {
        this.traktId = traktId;
    }

    public Integer getTvdbId() {
        return tvdbId;
    }

    public void setTvdbId(Integer tvdbId) {
        this.tvdbId = tvdbId;
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

    public Episode getNextEpisode() {
        return nextEpisode;
    }

    public void setNextEpisode(Episode nextEpisode) {
        this.nextEpisode = nextEpisode;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}