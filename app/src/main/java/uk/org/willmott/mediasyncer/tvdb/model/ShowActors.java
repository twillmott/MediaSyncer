
package uk.org.willmott.mediasyncer.tvdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data"
})
public class ShowActors {

    @JsonProperty("data")
    private List<ShowActorData
            > data = new ArrayList<ShowActorData>();

    /**
     * @return The data
     */
    @JsonProperty("data")
    public List<ShowActorData> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    @JsonProperty("data")
    public void setData(List<ShowActorData> data) {
        this.data = data;
    }

}
