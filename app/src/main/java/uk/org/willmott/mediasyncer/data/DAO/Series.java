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
    private long id;
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
    // Null indicates no next episode.
    private Integer nextEpisodeSeasonNumber;
    @DatabaseField
    private Integer nextEpisodeEpisodeNumber;

    Series() {
    } // No args constructor for ormlite

    public Series(String title, String traktId, Integer tvdbId, String seriesThumbnail, String seriesBanner, Integer nextEpisodeSeasonNumber, Integer nextEpisodeEpisodeNumber) {
        this.title = title;
        this.traktId = traktId;
        this.tvdbId = tvdbId;
        this.seriesThumbnail = seriesThumbnail;
        this.seriesBanner = seriesBanner;
        this.nextEpisodeSeasonNumber = nextEpisodeSeasonNumber;
        this.nextEpisodeEpisodeNumber = nextEpisodeEpisodeNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Integer getNextEpisodeSeasonNumber() {
        return nextEpisodeSeasonNumber;
    }

    public void setNextEpisodeSeasonNumber(Integer nextEpisodeSeasonNumber) {
        this.nextEpisodeSeasonNumber = nextEpisodeSeasonNumber;
    }

    public Integer getNextEpisodeEpisodeNumber() {
        return nextEpisodeEpisodeNumber;
    }

    public void setNextEpisodeEpisodeNumber(Integer nextEpisodeEpisodeNumber) {
        this.nextEpisodeEpisodeNumber = nextEpisodeEpisodeNumber;
    }
}