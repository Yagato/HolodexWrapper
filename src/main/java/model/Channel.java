package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel extends ChannelMin {

    @JsonProperty("suborg")
    private String suborg;

    @JsonProperty("banner")
    private String banner;

    @JsonProperty("thumbnail")
    private String thumbnail;

    @JsonProperty("twitter")
    private String twitter;

    @JsonProperty("video_count")
    private String videoCount;

    @JsonProperty("subscriber_count")
    private String subscriberCount;

    @JsonProperty("comments_crawled_at")
    private String commentsCrawledAt;

    @JsonProperty("view_count")
    private String viewCount;

    @JsonProperty("clip_count")
    private String clipCount;

    @JsonProperty("top_topics")
    private List<String> topTopics;

    @JsonProperty("lang")
    private String lang;

    @JsonProperty("published_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date publishedAt;

    @JsonProperty("inactive")
    private boolean inactive;

    @JsonProperty("description")
    private String description;

    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date updatedAt;

    @JsonProperty("yt_uploads_id")
    private String ytUploadsId;

    @JsonProperty("crawled_at")
    private String crawledAt;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date createdAt;

    @JsonProperty("yt_handle")
    private List<String> ytHandle;

    @JsonProperty("twitch")
    private String twitch;

    @JsonProperty("yt_name_history")
    private List<String> ytNameHistory;

    @JsonProperty("group")
    private String group;

}
