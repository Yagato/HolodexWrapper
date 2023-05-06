package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Video {

    @JsonProperty("id")
    private String id;

    @JsonProperty("lang")
    private String lang;

    @JsonProperty("title")
    private String title;

    @JsonProperty("type")
    private String type;

    @JsonProperty("topic_id")
    private String topicId;

    @JsonProperty("published_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime publishedAt;

    @JsonProperty("available_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime availableAt;

    @JsonProperty("duration")
    private int duration;

    @JsonProperty("status")
    private String status;

    @JsonProperty("start_scheduled")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime startScheduled;

    @JsonProperty("start_actual")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime startActual;

    @JsonProperty("end_actual")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime endActual;

    @JsonProperty("live_viewers")
    private int liveViewers;

    @JsonProperty("live_tl_count")
    private LiveTranslator liveTlCount;

    @JsonProperty("recent_live_tls")
    private List<String> recentLiveTls;

    @JsonProperty("description")
    private String description;

    @JsonProperty("songcount")
    private int songCount;

    @JsonProperty("channel_id")
    private String channelId;

    @JsonProperty("channel")
    private Channel channel;

    @JsonProperty("clips")
    private List<Clip> clips;

    @JsonProperty("sources")
    private List<Video> sources;

    @JsonProperty("refers")
    private List<Video> refers;

    @JsonProperty("simulcasts")
    private List<Video> simulcasts;

    @JsonProperty("mentions")
    private List<Channel> mentions;

    @JsonProperty("songs")
    private List<Song> songs;

    @JsonProperty("comments")
    private List<Comment> comments;

    @JsonProperty("jp_name")
    private String jpName;

    @JsonProperty("link")
    private String link;

    @JsonProperty("thumbnail")
    private String thumbnail;

    @JsonProperty("placeholderType")
    private String placeholderType;

    @JsonProperty("certainty")
    private String certainty;

    @JsonProperty("credits")
    private Credits credits;
}
