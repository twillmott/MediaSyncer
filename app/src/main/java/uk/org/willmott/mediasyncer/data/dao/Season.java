package uk.org.willmott.mediasyncer.data.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * DAO class for the Season table.
 * Created by tomwi on 10/10/2016.
 */
@DatabaseTable
public class Season {

    public static final String SERIES_COLUMN = "seriesId";

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false)
    private int seasonNumber;
    // Foreign key to the series table. Cannot be null.
    @DatabaseField(foreign = true, canBeNull = false, columnName = SERIES_COLUMN)
    private Series series;
    @DatabaseField
    private String thumbnail;
    @DatabaseField
    private String banner;


    public Season() {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
