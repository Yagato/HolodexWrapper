package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import constants.SortOrder;
import constants.VideoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostQueryParameters {

    @JsonProperty("sort")
    private String sort;

    @JsonProperty("lang")
    private String language;

    @JsonProperty("target")
    private VideoType[] videoTypes;

    @JsonProperty("conditions")
    private String[] conditions;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("topic")
    private String[] topics;

    @JsonProperty("vch")
    private String[] channelIds;

    @JsonProperty("org")
    private String[] organizations;

    @JsonProperty("offset")
    private Integer offset;

    @JsonProperty("limit")
    private Integer limit;

}
