package uk.org.willmott.mediasyncer.model;

import org.parceler.Parcel;

/**
 * Created by tomw on 11/10/2016.
 */
@Parcel
public class Episode {

    private int episodeNumber;
    private String bannerUrl;
    private String thumbnailUrl;
    private String overview;

    public Episode(int episodeNumber, String bannerUrl, String thumbnailUrl, String overview) {
        this.episodeNumber = episodeNumber;
        this.bannerUrl = bannerUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.overview = overview;
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
}
