package uk.org.willmott.mediasyncer.model;

import org.parceler.Parcel;

/**
 * Created by tomw on 11/10/2016.
 */
@Parcel
public class Episode {

    private Integer id;
    private int episodeNumber;
    private int tvdbId;
    private String traktId;
    private String title;
    private String bannerUrl;
    private String thumbnailUrl;
    private String overview;

    public Episode(Integer id, int tvdbId, String traktId, String title, int episodeNumber, String bannerUrl, String thumbnailUrl, String overview) {
        this.id = id;
        this.tvdbId = tvdbId;
        this.traktId = traktId;
        this.episodeNumber = episodeNumber;
        this.bannerUrl = bannerUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.overview = overview;
        this.title = title;
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

    public int getTvdbId() {
        return tvdbId;
    }

    public void setTvdbId(int tvdbId) {
        this.tvdbId = tvdbId;
    }

    public String getTraktId() {
        return traktId;
    }

    public void setTraktId(String traktId) {
        this.traktId = traktId;
    }
}
