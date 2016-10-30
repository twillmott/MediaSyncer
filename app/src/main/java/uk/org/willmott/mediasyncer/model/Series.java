package uk.org.willmott.mediasyncer.model;

import org.parceler.Parcel;

import java.util.List;

/**
 * Model to hold a series object.
 *
 * Created by tomw on 11/10/2016.
 */
@Parcel
public class Series {

    Integer id;
    String title;
    Integer traktId;
    Integer tmdbId;
    Integer tvdbId;
    Integer tvrageId;
    String imdbId;
    String bannerUrl;
    String thumbnailUrl;
    Episode nextEpisode;
    List<Season> seasons;
    String overview;
    Long lastTmdbUpdate;

    public Series() {
    } // Required for parceler.

    public Series(Integer id, String title, Integer traktId, Integer tmdbId, Integer tvdbId, Integer tvrageId, String imdbId, String bannerUrl, String thumbnailUrl, Episode nextEpisode, List<Season> seasons, String overview) {
        this.id = id;
        this.title = title;
        this.traktId = traktId;
        this.tmdbId = tmdbId;
        this.bannerUrl = bannerUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.nextEpisode = nextEpisode;
        this.seasons = seasons;
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

    public int getId() {
        return id;
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

    public Episode getNextEpisode() {
        return nextEpisode;
    }

    public void setNextEpisode(Episode nextEpisode) {
        this.nextEpisode = nextEpisode;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Long getLastTmdbUpdate() {
        return lastTmdbUpdate;
    }

    public void setLastTmdbUpdate(Long lastTmdbUpdate) {
        this.lastTmdbUpdate = lastTmdbUpdate;
    }
}
