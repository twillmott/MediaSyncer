package uk.org.willmott.mediasyncer.tvdb.model;

public class AuthRequest {

    private String apikey;
    private String username;
    private String userkey;

    public AuthRequest(String apikey, String username, String userkey) {
        this.apikey = apikey;
        this.userkey = userkey;
        this.username = username;
    }


    /**
     * @return The apikey
     */
    public String getApikey() {
        return apikey;
    }

    /**
     * @param apikey The apikey
     */
    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The userkey
     */
    public String getUserkey() {
        return userkey;
    }

    /**
     * @param userkey The userkey
     */
    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

}