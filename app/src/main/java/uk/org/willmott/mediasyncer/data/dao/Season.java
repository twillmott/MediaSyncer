package uk.org.willmott.mediasyncer.data.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * DAO class for the Season table.
 * Created by tomwi on 10/10/2016.
 */
@DatabaseTable
public class Season {

    public static final String SERIES_COLUMN = "seriesId";

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(unique = true)
    private Integer traktId;
    @DatabaseField
    private Integer tmdbId;
    @DatabaseField
    private Integer tvdbId;
    @DatabaseField
    private Integer tvrageId;
    @DatabaseField
    String imdbId;
    @DatabaseField(canBeNull = false)
    private int seasonNumber;
    // Foreign key to the series table. Cannot be null.
    @DatabaseField(canBeNull = false, columnName = SERIES_COLUMN)
    private Integer series;
    @DatabaseField
    private String thumbnail;
    @DatabaseField
    private String banner;
    @DatabaseField
    Long lastTmdbUpdate;
    @DatabaseField
    Integer episodeCount;


    public Season() {
    } // No args constructor for ormlite.

    public Season(Integer tmdbId, Integer traktId, Integer tvdbId, Integer tvrageId, String imdbId, int seasonNumber, Integer series, String thumbnail, String banner) {
        this.tmdbId = tmdbId;
        this.traktId = traktId;
        this.seasonNumber = seasonNumber;
        this.series = series;
        this.thumbnail = thumbnail;
        this.banner = banner;
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

    public Integer getTvrageId() {
        return tvrageId;
    }

    public void setTvrageId(Integer tvrageId) {
        this.tvrageId = tvrageId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
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


    public Long getLastTmdbUpdate() {
        return lastTmdbUpdate;
    }

    public void setLastTmdbUpdate(Long lastTmdbUpdate) {
        this.lastTmdbUpdate = lastTmdbUpdate;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }
}
