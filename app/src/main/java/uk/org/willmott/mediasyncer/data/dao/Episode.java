package uk.org.willmott.mediasyncer.data.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by tomwi on 11/10/2016.
 */
@DatabaseTable
public class Episode {

    public static final String SEASON_COLUMN = "seasonId";

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(canBeNull = false, columnName = SEASON_COLUMN)
    private Integer season;
    @DatabaseField(canBeNull = false)
    private int episodeNumber;
    @DatabaseField(canBeNull = false, unique = true)
    private Integer traktId;
    @DatabaseField
    private Integer tmdbId;
    @DatabaseField
    private Integer tvdbId;
    @DatabaseField
    private Integer tvrageId;
    @DatabaseField
    private String imdbId;
    @DatabaseField(canBeNull = false)
    private String title;
    @DatabaseField
    private String overview;
    @DatabaseField
    private String bannerUrl;
    @DatabaseField
    private String thumbnailUrl;
    @DatabaseField
    Long lastTmdbUpdate;
    @DatabaseField
    Long lastWatched;
    @DatabaseField
    Long lastCollected;

    public Episode() {
    } // Required by ormlite.

    public Episode(Integer tmdbId, Integer traktId, Integer tvdbId, Integer tvrageId, String imdbId, Integer season, int episodeNumber, String title, String overview, String bannerUrl, String thumbnailUrl) {
        this.tmdbId = tmdbId;
        this.traktId = traktId;
        this.season = season;
        this.episodeNumber = episodeNumber;
        this.title = title;
        this.overview = overview;
        this.bannerUrl = bannerUrl;
        this.thumbnailUrl = thumbnailUrl;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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

    public Long getLastWatched() {
        return lastWatched;
    }

    public void setLastWatched(Long lastWatched) {
        this.lastWatched = lastWatched;
    }

    public Long getLastCollected() {
        return lastCollected;
    }

    public void setLastCollected(Long lastCollected) {
        this.lastCollected = lastCollected;
    }
}
