package model;

import constants.ChannelType;
import constants.Languages;
import constants.Organizations;
import constants.SortOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryParameters {

    String channelId;
    String id;
    String[] include;
    Languages[] languages;
    Integer limit;
    Integer maxUpcomingHours;
    String mentionedChannelId;
    Integer offset;
    SortOrder sortOrder;
    String organization;
    String sort;
    String status;
    String topic;
    String type;
    ChannelType channelType;

}
