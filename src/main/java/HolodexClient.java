import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import constants.ExtraInfo;
import constants.Languages;
import constants.VideoType;
import model.Channel;
import model.QueryParameters;
import model.Video;

import java.util.List;

public class HolodexClient {

    private final String URL = "https://holodex.net/api/v2/";
    private final String HOLODEX_API_KEY;

    private final ObjectMapper objectMapper;

    public HolodexClient(String holodexApiKey) {
        this.HOLODEX_API_KEY = holodexApiKey;
        objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
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

    public List<Video> getLiveAndUpcomingVideos(QueryParameters queryParameters)
            throws UnirestException, JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder(URL + "live?");

        buildRequest(queryParameters, stringBuilder);

        System.out.println(stringBuilder);

        HttpResponse<String> response = Unirest.get(stringBuilder.toString())
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Video>>() {
        });
    }

    public List<Video> getVideos() throws UnirestException, JsonProcessingException {
        HttpResponse<String> response = Unirest.get(URL + "videos")
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Video>>() {});
    }

    public List<Video> getVideos(QueryParameters queryParameters)
            throws UnirestException, JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder(URL + "videos?");

        buildRequest(queryParameters, stringBuilder);

        System.out.println(stringBuilder);

        HttpResponse<String> response = Unirest.get(stringBuilder.toString())
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Video>>() {});
    }

    public Channel getChannelInformation(String channelId) throws UnirestException, JsonProcessingException {
        HttpResponse<String> response = Unirest.get(URL + "channels/" + channelId)
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), Channel.class);
    }

    public List<Video> getVideosRelatedToChannel(QueryParameters queryParameters)
            throws UnirestException, JsonProcessingException {
        if(queryParameters.getChannelId() == null) {
            throw new RuntimeException("Channel ID can't be null");
        }

        if(queryParameters.getVideoType() == null) {
            throw new RuntimeException("Video Type can't be null");
        }

        if(queryParameters.getLanguages() != null && queryParameters.getVideoType().toString().equals("VIDEOS")) {
            throw new RuntimeException("Can't filter VIDEOS by language");
        }

        StringBuilder stringBuilder = new StringBuilder(URL + "channels/");

        stringBuilder
                .append(queryParameters.getChannelId())
                .append("/")
                .append(queryParameters.getVideoType().toString().toLowerCase())
                .append("?");

        queryParameters.setChannelId(null);
        queryParameters.setVideoType(null);

        buildRequest(queryParameters, stringBuilder);

        System.out.println(stringBuilder);

        HttpResponse<String> response = Unirest.get(stringBuilder.toString())
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Video>>() {});
    }

    public Video getVideoMetadata(String videoId,
                                  Integer timestampComments,
                                  Languages[] languages)
            throws UnirestException, JsonProcessingException {
        if (videoId == null) {
            throw new RuntimeException("videoId can't be null");
        }

        StringBuilder stringBuilder = new StringBuilder(URL + "videos/");

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
            buildLangParameter(languages, stringBuilder);
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

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Channel>>() {});
    }

    public List<Channel> listChannels(QueryParameters queryParameters)
            throws UnirestException, JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder(URL + "channels?");

        buildRequest(queryParameters, stringBuilder);

        System.out.println(stringBuilder);

        HttpResponse<String> response = Unirest.get(stringBuilder.toString())
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Channel>>() {
        });
    }

    private void buildRequest(QueryParameters queryParameters, StringBuilder stringBuilder) {
        if (queryParameters.getLanguages() != null) {
            stringBuilder.append("&lang=");
            buildLangParameter(queryParameters.getLanguages(), stringBuilder);
        }

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
                    .append("&order=")
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

        if (queryParameters.getChannelId() != null) {
            stringBuilder
                    .append("&channel_id=")
                    .append(queryParameters.getChannelId());
        }

        if (queryParameters.getVideoId() != null) {
            stringBuilder
                    .append("&id=")
                    .append(queryParameters.getVideoId());
        }

        if (queryParameters.getExtraInfo() != null) {
            stringBuilder.append("&include=");
            buildIncludeParameter(queryParameters.getExtraInfo(), stringBuilder);
        }

        if (queryParameters.getMaxUpcomingHours() != null) {
            stringBuilder
                    .append("&max_upcoming_hours=")
                    .append(queryParameters.getMaxUpcomingHours());
        }

        if(queryParameters.getMentionedChannelId() != null) {
            stringBuilder
                    .append("&mentioned_channel_id=")
                    .append(queryParameters.getMentionedChannelId());
        }

        if(queryParameters.getStatus() != null) {
            stringBuilder
                    .append("&status=")
                    .append(queryParameters.getStatus().toString().toLowerCase());
        }

        if(queryParameters.getTopic() != null) {
            stringBuilder
                    .append("&topic=")
                    .append(queryParameters.getTopic());
        }

        if(queryParameters.getVideoType() != null) {
            stringBuilder
                    .append("&type=")
                    .append(queryParameters.getVideoType().toString().toLowerCase());
        }

        if(queryParameters.getFrom() != null) {
            stringBuilder
                    .append("&from=")
                    .append(queryParameters.getFrom());
        }

        if(queryParameters.getTo() != null) {
            stringBuilder.append("&to")
                    .append(queryParameters.getTo());
        }
    }

    private void buildVideoTypeParameter(String videoType, StringBuilder stringBuilder) {
        if(videoType.equals("CLIPS") || videoType.equals("VIDEOS") || videoType.equals("COLLABS")) {
            stringBuilder
                    .append("/")
                    .append(videoType.toLowerCase())
                    .append("?");
        }
        else {
            stringBuilder
                    .append("&type=")
                    .append(videoType.toString().toLowerCase());
        }
    }

    private void buildIncludeParameter(ExtraInfo[] extraInfo, StringBuilder stringBuilder) {
        for (int i = 0; i < extraInfo.length; i++) {
            stringBuilder.append(extraInfo[i].toString().toLowerCase());

            if (i < extraInfo.length - 1) {
                stringBuilder.append(",");
            }
        }
    }

    private void buildLangParameter(Languages[] languages, StringBuilder stringBuilder) {
        for (int i = 0; i < languages.length; i++) {
            stringBuilder.append(languages[i].toString().toLowerCase());

            if (i < languages.length - 1) {
                stringBuilder.append(",");
            }
        }
    }

}
