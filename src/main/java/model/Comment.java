package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment {

    @JsonProperty("comment_key")
    private String commentKey;

    @JsonProperty("video_id")
    private String videoId;

    @JsonProperty("message")
    private String message;

}
