
package uk.org.willmott.mediasyncer.imdb.model.photos;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private String id;
    private String type;
    private String title;
    private String description;
    private String image;
    private List<String> mediaLinks = new ArrayList<String>();
    private List<Filmography> filmography = new ArrayList<Filmography>();
    private List<String> occupation = new ArrayList<String>();

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return The mediaLinks
     */
    public List<String> getMediaLinks() {
        return mediaLinks;
    }

    /**
     * @param mediaLinks The mediaLinks
     */
    public void setMediaLinks(List<String> mediaLinks) {
        this.mediaLinks = mediaLinks;
    }

    /**
     * @return The filmography
     */
    public List<Filmography> getFilmography() {
        return filmography;
    }

    /**
     * @param filmography The filmography
     */
    public void setFilmography(List<Filmography> filmography) {
        this.filmography = filmography;
    }

    /**
     * @return The occupation
     */
    public List<String> getOccupation() {
        return occupation;
    }

    /**
     * @param occupation The occupation
     */
    public void setOccupation(List<String> occupation) {
        this.occupation = occupation;
    }

}
