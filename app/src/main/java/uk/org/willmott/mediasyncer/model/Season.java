package uk.org.willmott.mediasyncer.model;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by tomw on 11/10/2016.
 */
@Parcel
public class Season {
    Integer id;
    int seasonNumber;
    Integer tmdbId;
    Integer traktId;
    Integer tvdbId;
    Integer tvrageId;
    String imdbId;
    String bannerUrl;
    String thumbnailUrl;
    List<Episode> episodes;
    Long lastTmdbUpdate;

    public Season() {
    } // Required for parceler

    public Season(Integer id, Integer tmdbId, Integer traktId, Integer tvdbId, Integer tvrageId, String imdbId, int seasonNumber, String bannerUrl, String thumbnailUrl, List<Episode> episodes) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.traktId = traktId;
        this.seasonNumber = seasonNumber;
        this.bannerUrl = bannerUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.episodes = episodes;
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

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
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

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
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
}
