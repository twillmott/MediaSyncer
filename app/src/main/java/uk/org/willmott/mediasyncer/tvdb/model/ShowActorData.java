
package uk.org.willmott.mediasyncer.tvdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "seriesId",
        "name",
        "role",
        "sortOrder",
        "image",
        "imageAuthor",
        "imageAdded",
        "lastUpdated"
})
public class ShowActorData {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("seriesId")
    private Integer seriesId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("role")
    private String role;
    @JsonProperty("sortOrder")
    private Integer sortOrder;
    @JsonProperty("image")
    private String image;
    @JsonProperty("imageAuthor")
    private Integer imageAuthor;
    @JsonProperty("imageAdded")
    private String imageAdded;
    @JsonProperty("lastUpdated")
    private String lastUpdated;

    /**
     * @return The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The seriesId
     */
    @JsonProperty("seriesId")
    public Integer getSeriesId() {
        return seriesId;
    }

    /**
     * @param seriesId The seriesId
     */
    @JsonProperty("seriesId")
    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    /**
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The role
     */
    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    /**
     * @param role The role
     */
    @JsonProperty("role")
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return The sortOrder
     */
    @JsonProperty("sortOrder")
    public Integer getSortOrder() {
        return sortOrder;
    }

    /**
     * @param sortOrder The sortOrder
     */
    @JsonProperty("sortOrder")
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * @return The image
     */
    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    /**
     * @param image The image
     */
    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return The imageAuthor
     */
    @JsonProperty("imageAuthor")
    public Integer getImageAuthor() {
        return imageAuthor;
    }

    /**
     * @param imageAuthor The imageAuthor
     */
    @JsonProperty("imageAuthor")
    public void setImageAuthor(Integer imageAuthor) {
        this.imageAuthor = imageAuthor;
    }

    /**
     * @return The imageAdded
     */
    @JsonProperty("imageAdded")
    public String getImageAdded() {
        return imageAdded;
    }

    /**
     * @param imageAdded The imageAdded
     */
    @JsonProperty("imageAdded")
    public void setImageAdded(String imageAdded) {
        this.imageAdded = imageAdded;
    }

    /**
     * @return The lastUpdated
     */
    @JsonProperty("lastUpdated")
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated The lastUpdated
     */
    @JsonProperty("lastUpdated")
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
