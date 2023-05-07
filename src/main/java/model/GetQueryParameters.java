package model;

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
    String[] languages;
    Integer limit;
    Integer maxUpcomingHours;
    String mentionedChannelId;
    Integer offset;
    String sortOrder;
    String organization;
    String sortByField;
    String topic;
    String channelType;
    String[] extraInfo;
    String status;
    String videoType;
    OffsetDateTime from;
    OffsetDateTime to;
    String[] channelIds;

}
