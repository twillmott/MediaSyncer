package uk.org.willmott.mediasyncer.data.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * DAO class for the Season table.
 * Created by tomwi on 10/10/2016.
 */
@DatabaseTable
public class Season {

    // Foreign key to the series table. Cannot be null.
    @DatabaseField(generatedId = true)
    private int seriesId;
    @DatabaseField(canBeNull = false)
    private int seasonNumber;
    @DatabaseField(foreign = true)
    private Series series;
    @DatabaseField
    private String thumbnail;
    @DatabaseField
    private String banner;


    Season() {
    } // No args constructor for ormlite.

    public Season(int seasonNumber, Series series, String thumbnail, String banner) {
        this.seasonNumber = seasonNumber;
        this.series = series;
        this.thumbnail = thumbnail;
        this.banner = banner;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
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
}
