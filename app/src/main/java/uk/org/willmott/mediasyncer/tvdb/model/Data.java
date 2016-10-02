
package uk.org.willmott.mediasyncer.tvdb.model;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private Integer id;
    private Integer airedSeason;
    private Integer airedSeasonID;
    private Integer airedEpisodeNumber;
    private String episodeName;
    private String firstAired;
    private List<String> guestStars = new ArrayList<String>();
    private String director;
    private List<String> directors = new ArrayList<String>();
    private List<String> writers = new ArrayList<String>();
    private String overview;
    private Language language;
    private String productionCode;
    private String showUrl;
    private Integer lastUpdated;
    private String dvdDiscid;
    private Object dvdSeason;
    private Object dvdEpisodeNumber;
    private Object dvdChapter;
    private Integer absoluteNumber;
    private String filename;
    private Integer seriesId;
    private Integer lastUpdatedBy;
    private Object airsAfterSeason;
    private Object airsBeforeSeason;
    private Object airsBeforeEpisode;
    private Integer thumbAuthor;
    private String thumbAdded;
    private String thumbWidth;
    private String thumbHeight;
    private String imdbId;
    private Double siteRating;
    private Integer siteRatingCount;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The airedSeason
     */
    public Integer getAiredSeason() {
        return airedSeason;
    }

    /**
     * @param airedSeason The airedSeason
     */
    public void setAiredSeason(Integer airedSeason) {
        this.airedSeason = airedSeason;
    }

    /**
     * @return The airedSeasonID
     */
    public Integer getAiredSeasonID() {
        return airedSeasonID;
    }

    /**
     * @param airedSeasonID The airedSeasonID
     */
    public void setAiredSeasonID(Integer airedSeasonID) {
        this.airedSeasonID = airedSeasonID;
    }

    /**
     * @return The airedEpisodeNumber
     */
    public Integer getAiredEpisodeNumber() {
        return airedEpisodeNumber;
    }

    /**
     * @param airedEpisodeNumber The airedEpisodeNumber
     */
    public void setAiredEpisodeNumber(Integer airedEpisodeNumber) {
        this.airedEpisodeNumber = airedEpisodeNumber;
    }

    /**
     * @return The episodeName
     */
    public String getEpisodeName() {
        return episodeName;
    }

    /**
     * @param episodeName The episodeName
     */
    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    /**
     * @return The firstAired
     */
    public String getFirstAired() {
        return firstAired;
    }

    /**
     * @param firstAired The firstAired
     */
    public void setFirstAired(String firstAired) {
        this.firstAired = firstAired;
    }

    /**
     * @return The guestStars
     */
    public List<String> getGuestStars() {
        return guestStars;
    }

    /**
     * @param guestStars The guestStars
     */
    public void setGuestStars(List<String> guestStars) {
        this.guestStars = guestStars;
    }

    /**
     * @return The director
     */
    public String getDirector() {
        return director;
    }

    /**
     * @param director The director
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * @return The directors
     */
    public List<String> getDirectors() {
        return directors;
    }

    /**
     * @param directors The directors
     */
    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    /**
     * @return The writers
     */
    public List<String> getWriters() {
        return writers;
    }

    /**
     * @param writers The writers
     */
    public void setWriters(List<String> writers) {
        this.writers = writers;
    }

    /**
     * @return The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * @param overview The overview
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     * @return The language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * @param language The language
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * @return The productionCode
     */
    public String getProductionCode() {
        return productionCode;
    }

    /**
     * @param productionCode The productionCode
     */
    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }

    /**
     * @return The showUrl
     */
    public String getShowUrl() {
        return showUrl;
    }

    /**
     * @param showUrl The showUrl
     */
    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    /**
     * @return The lastUpdated
     */
    public Integer getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated The lastUpdated
     */
    public void setLastUpdated(Integer lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return The dvdDiscid
     */
    public String getDvdDiscid() {
        return dvdDiscid;
    }

    /**
     * @param dvdDiscid The dvdDiscid
     */
    public void setDvdDiscid(String dvdDiscid) {
        this.dvdDiscid = dvdDiscid;
    }

    /**
     * @return The dvdSeason
     */
    public Object getDvdSeason() {
        return dvdSeason;
    }

    /**
     * @param dvdSeason The dvdSeason
     */
    public void setDvdSeason(Object dvdSeason) {
        this.dvdSeason = dvdSeason;
    }

    /**
     * @return The dvdEpisodeNumber
     */
    public Object getDvdEpisodeNumber() {
        return dvdEpisodeNumber;
    }

    /**
     * @param dvdEpisodeNumber The dvdEpisodeNumber
     */
    public void setDvdEpisodeNumber(Object dvdEpisodeNumber) {
        this.dvdEpisodeNumber = dvdEpisodeNumber;
    }

    /**
     * @return The dvdChapter
     */
    public Object getDvdChapter() {
        return dvdChapter;
    }

    /**
     * @param dvdChapter The dvdChapter
     */
    public void setDvdChapter(Object dvdChapter) {
        this.dvdChapter = dvdChapter;
    }

    /**
     * @return The absoluteNumber
     */
    public Integer getAbsoluteNumber() {
        return absoluteNumber;
    }

    /**
     * @param absoluteNumber The absoluteNumber
     */
    public void setAbsoluteNumber(Integer absoluteNumber) {
        this.absoluteNumber = absoluteNumber;
    }

    /**
     * @return The filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename The filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return The seriesId
     */
    public Integer getSeriesId() {
        return seriesId;
    }

    /**
     * @param seriesId The seriesId
     */
    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    /**
     * @return The lastUpdatedBy
     */
    public Integer getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy The lastUpdatedBy
     */
    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return The airsAfterSeason
     */
    public Object getAirsAfterSeason() {
        return airsAfterSeason;
    }

    /**
     * @param airsAfterSeason The airsAfterSeason
     */
    public void setAirsAfterSeason(Object airsAfterSeason) {
        this.airsAfterSeason = airsAfterSeason;
    }

    /**
     * @return The airsBeforeSeason
     */
    public Object getAirsBeforeSeason() {
        return airsBeforeSeason;
    }

    /**
     * @param airsBeforeSeason The airsBeforeSeason
     */
    public void setAirsBeforeSeason(Object airsBeforeSeason) {
        this.airsBeforeSeason = airsBeforeSeason;
    }

    /**
     * @return The airsBeforeEpisode
     */
    public Object getAirsBeforeEpisode() {
        return airsBeforeEpisode;
    }

    /**
     * @param airsBeforeEpisode The airsBeforeEpisode
     */
    public void setAirsBeforeEpisode(Object airsBeforeEpisode) {
        this.airsBeforeEpisode = airsBeforeEpisode;
    }

    /**
     * @return The thumbAuthor
     */
    public Integer getThumbAuthor() {
        return thumbAuthor;
    }

    /**
     * @param thumbAuthor The thumbAuthor
     */
    public void setThumbAuthor(Integer thumbAuthor) {
        this.thumbAuthor = thumbAuthor;
    }

    /**
     * @return The thumbAdded
     */
    public String getThumbAdded() {
        return thumbAdded;
    }

    /**
     * @param thumbAdded The thumbAdded
     */
    public void setThumbAdded(String thumbAdded) {
        this.thumbAdded = thumbAdded;
    }

    /**
     * @return The thumbWidth
     */
    public String getThumbWidth() {
        return thumbWidth;
    }

    /**
     * @param thumbWidth The thumbWidth
     */
    public void setThumbWidth(String thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    /**
     * @return The thumbHeight
     */
    public String getThumbHeight() {
        return thumbHeight;
    }

    /**
     * @param thumbHeight The thumbHeight
     */
    public void setThumbHeight(String thumbHeight) {
        this.thumbHeight = thumbHeight;
    }

    /**
     * @return The imdbId
     */
    public String getImdbId() {
        return imdbId;
    }

    /**
     * @param imdbId The imdbId
     */
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    /**
     * @return The siteRating
     */
    public Double getSiteRating() {
        return siteRating;
    }

    /**
     * @param siteRating The siteRating
     */
    public void setSiteRating(Double siteRating) {
        this.siteRating = siteRating;
    }

    /**
     * @return The siteRatingCount
     */
    public Integer getSiteRatingCount() {
        return siteRatingCount;
    }

    /**
     * @param siteRatingCount The siteRatingCount
     */
    public void setSiteRatingCount(Integer siteRatingCount) {
        this.siteRatingCount = siteRatingCount;
    }

}
