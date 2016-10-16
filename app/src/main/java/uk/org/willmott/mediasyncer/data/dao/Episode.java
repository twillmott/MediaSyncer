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
    private String traktId;
    @DatabaseField
    private Integer tvdbId;
    @DatabaseField(canBeNull = false)
    private String title;
    @DatabaseField
    private String overview;
    @DatabaseField
    private String bannerUrl;
    @DatabaseField
    private String thumbnailUrl;

    public Episode() {
    } // Required by ormlite.

    public Episode(Integer tvdbId, String traktId, Integer season, int episodeNumber, String title, String overview, String bannerUrl, String thumbnailUrl) {
        this.tvdbId = tvdbId;
        this.traktId = traktId;
        this.season = season;
        this.episodeNumber = episodeNumber;
        this.title = title;
        this.overview = overview;
        this.bannerUrl = bannerUrl;
        this.thumbnailUrl = thumbnailUrl;
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
}
