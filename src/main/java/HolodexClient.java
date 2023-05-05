import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import constants.ChannelType;
import constants.Languages;
import constants.Order;
import model.Channel;

import java.util.List;

public class HolodexClient {

    private final String URL = "https://holodex.net/api/v2/";
    private final String HOLODEX_API_KEY;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public HolodexClient(String holodexApiKey) {
        this.HOLODEX_API_KEY = holodexApiKey;
    }

    public Channel getChannelInformation(String channelId) throws UnirestException, JsonProcessingException {
        HttpResponse<String> response = Unirest.get(URL + "channels/" + channelId)
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), Channel.class);
    }

    public List<Channel> listChannels(Languages languages,
                                      Integer limit,
                                      Integer offset,
                                      Order order,
                                      String org,
                                      String sort,
                                      ChannelType channelType)
            throws UnirestException, JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder(URL + "channels?");

        if(languages != null) {
            stringBuilder
                    .append("&lang=")
                    .append(languages.toString().toLowerCase());
        }

        if(limit != null) {
            stringBuilder
                    .append("&limit=")
                    .append(limit);
        }

        if(offset != null) {
            stringBuilder
                    .append("&offset=")
                    .append(offset);
        }

        if(order != null) {
            stringBuilder
                    .append("&order=")
                    .append(order.toString().toLowerCase());
        }

        if(org != null) {
            stringBuilder
                    .append("&org=")
                    .append(org);
        }

        if(sort != null) {
            stringBuilder
                    .append("&sort=")
                    .append(sort);
        }

        if(channelType != null) {
            stringBuilder
                    .append("&type=")
                    .append(channelType.toString().toLowerCase());
        }

        HttpResponse<String> response = Unirest.get(stringBuilder.toString())
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Channel>>() {});
    }
}
