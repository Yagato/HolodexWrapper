package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("original_artist")
    private String originalArtist;

    @JsonProperty("art")
    private String art;

    @JsonProperty("start")
    private int start;

    @JsonProperty("end")
    private int end;

    @JsonProperty("itunesid")
    private int iTunesId;

}
