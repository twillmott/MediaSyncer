package uk.org.willmott.mediasyncer.model;

/**
 * Model to hold info about an actor.
 * Created by tomwi on 29/09/2016.
 */

public class Actor {

    private String imageUrl;
    private String name;

    public Actor(String imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public Actor() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
