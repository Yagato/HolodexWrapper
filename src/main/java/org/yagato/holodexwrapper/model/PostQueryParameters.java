package org.yagato.holodexwrapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.yagato.holodexwrapper.constants.SortOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostQueryParameters {

    @JsonProperty("sort")
    private String sort = SortOrder.NEWEST;

    @JsonProperty("lang")
    private String[] language;

    @JsonProperty("target")
    private String[] videoTypes;

    @JsonProperty("conditions")
    private String[] conditions;

    @JsonProperty("comment")
    private String[] comment;

    @JsonProperty("topic")
    private String[] topics;

    @JsonProperty("vch")
    private String[] channelIds;

    @JsonProperty("org")
    private String[] organizations;

    @JsonProperty("offset")
    private Integer offset = 0;

    @JsonProperty("limit")
    private Integer limit = 30;

}
