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
    Integer tvdbId;
    String traktId;
    String bannerUrl;
    String thumbnailUrl;
    List<Episode> episodes;

    public Season() {
    } // Required for parceler

    public Season(Integer id, Integer tvdbId, String traktId, int seasonNumber, String bannerUrl, String thumbnailUrl, List<Episode> episodes) {
        this.id = id;
        this.tvdbId = tvdbId;
        this.traktId = traktId;
        this.seasonNumber = seasonNumber;
        this.bannerUrl = bannerUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.episodes = episodes;
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

    public Integer getTvdbId() {
        return tvdbId;
    }

    public void setTvdbId(Integer tvdbId) {
        this.tvdbId = tvdbId;
    }

    public String getTraktId() {
        return traktId;
    }

    public void setTraktId(String traktId) {
        this.traktId = traktId;
    }
}
