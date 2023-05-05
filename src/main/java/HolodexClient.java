import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import constants.ExtraInfo;
import constants.Languages;
import model.Channel;
import model.QueryParameters;
import model.Video;

import java.util.List;

public class HolodexClient {

    private final String URL = "https://holodex.net/api/v2/";
    private final String HOLODEX_API_KEY;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public HolodexClient(String holodexApiKey) {
        this.HOLODEX_API_KEY = holodexApiKey;
    }

    public List<Video> getLiveAndUpcomingVideos(String channelId)
            throws UnirestException, JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder(URL + "live?");

        if (channelId == null) {
            throw new RuntimeException("videoId can't be null");
        }

        stringBuilder
                .append("&channel_id=")
                .append(channelId);

        HttpResponse<String> response = Unirest.get(stringBuilder.toString())
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Video>>() {
        });
    }

    public List<Video> getLiveAndUpcomingVideos(String channelId,
                                                String videoId,
                                                ExtraInfo[] extraInfo,
                                                Languages[] languages,
                                                Integer limit,
                                                Integer maxUpcomingHours,
                                                String mentionedChannelId)
            throws UnirestException, JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder(URL + "live?");

        if (channelId != null) {
            stringBuilder
                    .append("&channel_id=")
                    .append(channelId);
        }

        if (videoId != null) {
            stringBuilder
                    .append("&id=")
                    .append(videoId);
        }

        if (extraInfo != null) {
            stringBuilder.append("&include=");

            for (int i = 0; i < extraInfo.length; i++) {
                stringBuilder.append(extraInfo[i].toString().toLowerCase());

                if (i < extraInfo.length - 1) {
                    stringBuilder.append(",");
                }
            }
        }

        if (languages != null) {
            stringBuilder.append("&lang=");

            for (int i = 0; i < languages.length; i++) {
                stringBuilder.append(languages[i].toString().toLowerCase());

                if (i < languages.length - 1) {
                    stringBuilder.append(",");
                }
            }
        }

        if (limit != null) {
            stringBuilder
                    .append("&limit=")
                    .append(limit);
        }

        if (maxUpcomingHours != null) {
            stringBuilder
                    .append("&max_upcoming_hours=")
                    .append(maxUpcomingHours);
        }

        HttpResponse<String> response = Unirest.get(stringBuilder.toString())
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Video>>() {
        });
    }

    public Channel getChannelInformation(String channelId) throws UnirestException, JsonProcessingException {
        HttpResponse<String> response = Unirest.get(URL + "channels/" + channelId)
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), Channel.class);
    }

    public Video getVideoMetadata(String videoId,
                                  Integer timestampComments,
                                  Languages[] languages)
            throws UnirestException, JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder(URL + "videos/");

        if (videoId == null) {
            throw new RuntimeException("videoId can't be null");
        }

        stringBuilder
                .append(videoId)
                .append("?");

        if (timestampComments != null && (timestampComments == 0 || timestampComments == 1)) {
            stringBuilder
                    .append("&c=")
                    .append(timestampComments);
        }

        if (languages != null) {
            stringBuilder.append("&lang=");

            for (int i = 0; i < languages.length; i++) {
                stringBuilder.append(languages[i].toString().toLowerCase());

                if (i < languages.length - 1) {
                    stringBuilder.append(",");
                }
            }
        }

        HttpResponse<String> response = Unirest.get(stringBuilder.toString())
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), Video.class);
    }

    public List<Channel> listChannels() throws UnirestException, JsonProcessingException {
        HttpResponse<String> response = Unirest.get(URL + "channels")
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Channel>>() {
        });
    }

    public List<Channel> listChannels(QueryParameters queryParameters)
            throws UnirestException, JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder(URL + "channels?");

        if (queryParameters.getLanguages() != null)
            buildLangParameter(queryParameters, stringBuilder);

        if (queryParameters.getLimit() != null) {
            stringBuilder
                    .append("&limit=")
                    .append(queryParameters.getLimit());
        }

        if (queryParameters.getOffset() != null) {
            stringBuilder
                    .append("&offset=")
                    .append(queryParameters.getOffset());
        }

        if (queryParameters.getSortOrder() != null) {
            stringBuilder
                    .append("&sortOrder=")
                    .append(queryParameters.getSortOrder().toString().toLowerCase());
        }

        if (queryParameters.getOrganization() != null) {
            stringBuilder
                    .append("&org=")
                    .append(queryParameters.getOrganization());
        }

        if (queryParameters.getSort() != null) {
            stringBuilder
                    .append("&sort=")
                    .append(queryParameters.getSort());
        }

        if (queryParameters.getChannelType() != null) {
            stringBuilder
                    .append("&type=")
                    .append(queryParameters.getChannelType().toString().toLowerCase());
        }

        System.out.println(stringBuilder);

        HttpResponse<String> response = Unirest.get(stringBuilder.toString())
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Channel>>() {
        });
    }

    private void buildLangParameter(QueryParameters queryParameters, StringBuilder stringBuilder) {
        stringBuilder.append("&lang=");

        Languages[] languagesEnum = queryParameters.getLanguages();
        String[] languages = new String[queryParameters.getLanguages().length];

        for(int i = 0; i < languages.length; i++) {
            languages[i] = languagesEnum[i].toString().toLowerCase();
        }

        for (int i = 0; i < languages.length; i++) {
            stringBuilder.append(languages[i].toLowerCase());

            if (i < languages.length - 1) {
                stringBuilder.append(",");
            }
        }
    }

}
