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

    private Integer id;
    private String title;
    private String traktId;
    private Integer tvdbId;
    private String bannerUrl;
    private String thumbnailUrl;
    private Episode nextEpisode;
    private List<Season> seasons;
    private String overview;

    public Series(Integer id, String title, String traktId, int tvdbId, String bannerUrl, String thumbnailUrl, Episode nextEpisode, List<Season> seasons, String overview) {
        this.id = id;
        this.title = title;
        this.traktId = traktId;
        this.tvdbId = tvdbId;
        this.bannerUrl = bannerUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.nextEpisode = nextEpisode;
        this.seasons = seasons;
        this.overview = overview;
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
}
