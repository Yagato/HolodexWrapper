package io.github.yagato.holodexwrapper.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Channel {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("english_name")
    private String englishName;

    @JsonProperty("type")
    private String type;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("org")
    private String org;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime commentsCrawledAt;

    @JsonProperty("view_count")
    private String viewCount;

    @JsonProperty("clip_count")
    private String clipCount;

    @JsonProperty("top_topics")
    private List<String> topTopics;

    @JsonProperty("lang")
    private String lang;

    @JsonProperty("published_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime publishedAt;

    @JsonProperty("inactive")
    private boolean inactive;

    @JsonProperty("description")
    private String description;

    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime updatedAt;

    @JsonProperty("yt_uploads_id")
    private String ytUploadsId;

    @JsonProperty("crawled_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime crawledAt;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime createdAt;

    @JsonProperty("yt_handle")
    private List<String> ytHandle;

    @JsonProperty("twitch")
    private String twitch;

    @JsonProperty("yt_name_history")
    private List<String> ytNameHistory;

    @JsonProperty("group")
    private String group;

}
