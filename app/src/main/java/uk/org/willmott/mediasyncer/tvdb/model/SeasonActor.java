
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
public class SeasonActor {

    @JsonProperty("data")
    private List<SeasonActorData
            > data = new ArrayList<SeasonActorData>();

    /**
     * @return The data
     */
    @JsonProperty("data")
    public List<SeasonActorData> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    @JsonProperty("data")
    public void setData(List<SeasonActorData> data) {
        this.data = data;
    }

}
