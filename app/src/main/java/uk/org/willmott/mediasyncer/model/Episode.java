package uk.org.willmott.mediasyncer.model;

import org.parceler.Parcel;

/**
 * Created by tomw on 11/10/2016.
 */
@Parcel
public class Episode {

    Integer id;
    int episodeNumber;
    Integer tmdbId;
    Integer traktId;
    Integer tvdbId;
    Integer tvrageId;
    String imdbId;
    String title;
    String bannerUrl;
    String thumbnailUrl;
    String overview;
    Long lastTmdbUpdate;
    Long lastWatched;
    Long lastCollected;
    Long airedOn;

    public Episode() {
    } //Required for parcel

    public Episode(Integer id, Integer tmdbId, Integer traktId, Integer tvdbId, Integer tvrageId, String imdbId, String title, int episodeNumber, String bannerUrl, String thumbnailUrl, String overview) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.traktId = traktId;
        this.episodeNumber = episodeNumber;
        this.bannerUrl = bannerUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.overview = overview;
        this.title = title;
        this.tvdbId = tvdbId;
        this.tvrageId = tvrageId;
        this.imdbId = imdbId;
    }

    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
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

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public Integer getTraktId() {
        return traktId;
    }

    public void setTraktId(Integer traktId) {
        this.traktId = traktId;
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

    public Long getAiredOn() {
        return airedOn;
    }

    public void setAiredOn(Long airedOn) {
        this.airedOn = airedOn;
    }
}
