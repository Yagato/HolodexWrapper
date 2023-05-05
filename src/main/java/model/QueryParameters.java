package model;

import constants.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryParameters {

    String channelId;
    String videoId;
    String id;
    Languages[] languages;
    Integer limit;
    Integer maxUpcomingHours;
    String mentionedChannelId;
    Integer offset;
    SortOrder sortOrder;
    String organization;
    String sort;
    String topic;
    String type;
    ChannelType channelType;
    ExtraInfo[] extraInfo;
    Status status;
    VideoType videoType;
    String from;

}
