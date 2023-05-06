package model;

import constants.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetQueryParameters {

    String channelId;
    String videoId;
    String id;
    String[] languages;
    Integer limit;
    Integer maxUpcomingHours;
    String mentionedChannelId;
    Integer offset;
    String sortOrder;
    String organization;
    String sort;
    String topic;
    String type;
    String channelType;
    String[] extraInfo;
    Status status;
    VideoType videoType;
    OffsetDateTime from;
    OffsetDateTime to;
    String[] channelIds;

}