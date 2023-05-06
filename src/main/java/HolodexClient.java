import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import constants.ExtraInfo;
import constants.Language;
import constants.SortOrder;
import model.Channel;
import model.GetQueryParameters;
import model.PostQueryParameters;
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

    public List<Video> getLiveAndUpcomingVideos(GetQueryParameters getQueryParameters)
            throws UnirestException, JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder(URL + "live?");

        buildRequest(getQueryParameters, stringBuilder);

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

    public List<Video> getVideos(GetQueryParameters getQueryParameters)
            throws UnirestException, JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder(URL + "videos?");

        buildRequest(getQueryParameters, stringBuilder);

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

    public List<Video> getVideosRelatedToChannel(GetQueryParameters getQueryParameters)
            throws UnirestException, JsonProcessingException {
        if(getQueryParameters.getChannelId() == null) {
            throw new RuntimeException("Channel ID can't be null");
        }

        if(getQueryParameters.getVideoType() == null) {
            throw new RuntimeException("Video Type can't be null");
        }

        if(getQueryParameters.getLanguages() != null && getQueryParameters.getVideoType().equals("videos")) {
            throw new RuntimeException("Can't filter VIDEOS by language");
        }

        StringBuilder stringBuilder = new StringBuilder(URL + "channels/");

        stringBuilder
                .append(getQueryParameters.getChannelId())
                .append("/")
                .append(getQueryParameters.getVideoType())
                .append("?");

        getQueryParameters.setChannelId(null);
        getQueryParameters.setVideoType(null);

        buildRequest(getQueryParameters, stringBuilder);

        System.out.println(stringBuilder);

        HttpResponse<String> response = Unirest.get(stringBuilder.toString())
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Video>>() {});
    }

    public List<Video> getLiveOrUpcomingVideosForSetOfChannels(GetQueryParameters getQueryParameters)
            throws UnirestException, JsonProcessingException {
        if(getQueryParameters.getChannelIds() == null) {
            throw new RuntimeException("Channel IDs can't be null");
        }

        StringBuilder stringBuilder = new StringBuilder(URL + "users/live?channels=");

        buildChannelsParameter(getQueryParameters.getChannelIds(), stringBuilder);

        System.out.println(stringBuilder);

        HttpResponse<String> response = Unirest.get(stringBuilder.toString())
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Video>>() {});
    }

    public Video getVideoMetadata(String videoId,
                                  Integer timestampComments,
                                  String[] languages)
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

    public List<Channel> listChannels(GetQueryParameters getQueryParameters)
            throws UnirestException, JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder(URL + "channels?");

        buildRequest(getQueryParameters, stringBuilder);

        System.out.println(stringBuilder);

        HttpResponse<String> response = Unirest.get(stringBuilder.toString())
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .asString();

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Channel>>() {
        });
    }

    public List<Video> searchVideos(PostQueryParameters postQueryParameters)
            throws UnirestException, JsonProcessingException {

        if(postQueryParameters.getSort() == null) {
            postQueryParameters.setSort(SortOrder.NEWEST);
        }

        if(postQueryParameters.getOffset() == null) {
            postQueryParameters.setOffset(0);
        }

        if(postQueryParameters.getLimit() == null) {
            postQueryParameters.setLimit(30);
        }

        HttpResponse<String> response = Unirest.post(URL + "search/videoSearch")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("X-APIKEY", HOLODEX_API_KEY)
                .body(objectMapper.writeValueAsString(postQueryParameters))
                .asString();

        System.out.println(objectMapper.writeValueAsString(postQueryParameters));

        return objectMapper.readValue(response.getBody(), new TypeReference<List<Video>>() {
        });
    }

    private void buildRequest(GetQueryParameters getQueryParameters, StringBuilder stringBuilder) {
        if (getQueryParameters.getLanguages() != null) {
            stringBuilder.append("&lang=");
            buildLangParameter(getQueryParameters.getLanguages(), stringBuilder);
        }

        if (getQueryParameters.getLimit() != null) {
            stringBuilder
                    .append("&limit=")
                    .append(getQueryParameters.getLimit());
        }

        if (getQueryParameters.getOffset() != null) {
            stringBuilder
                    .append("&offset=")
                    .append(getQueryParameters.getOffset());
        }

        if (getQueryParameters.getSortOrder() != null) {
            stringBuilder
                    .append("&order=")
                    .append(getQueryParameters.getSortOrder());
        }

        if (getQueryParameters.getOrganization() != null) {
            stringBuilder
                    .append("&org=")
                    .append(getQueryParameters.getOrganization());
        }

        if (getQueryParameters.getSort() != null) {
            stringBuilder
                    .append("&sort=")
                    .append(getQueryParameters.getSort());
        }

        if (getQueryParameters.getChannelType() != null) {
            stringBuilder
                    .append("&type=")
                    .append(getQueryParameters.getChannelType());
        }

        if (getQueryParameters.getChannelId() != null) {
            stringBuilder
                    .append("&channel_id=")
                    .append(getQueryParameters.getChannelId());
        }

        if (getQueryParameters.getVideoId() != null) {
            stringBuilder
                    .append("&id=")
                    .append(getQueryParameters.getVideoId());
        }

        if (getQueryParameters.getExtraInfo() != null) {
            stringBuilder.append("&include=");
            buildIncludeParameter(getQueryParameters.getExtraInfo(), stringBuilder);
        }

        if (getQueryParameters.getMaxUpcomingHours() != null) {
            stringBuilder
                    .append("&max_upcoming_hours=")
                    .append(getQueryParameters.getMaxUpcomingHours());
        }

        if(getQueryParameters.getMentionedChannelId() != null) {
            stringBuilder
                    .append("&mentioned_channel_id=")
                    .append(getQueryParameters.getMentionedChannelId());
        }

        if(getQueryParameters.getStatus() != null) {
            stringBuilder
                    .append("&status=")
                    .append(getQueryParameters.getStatus());
        }

        if(getQueryParameters.getTopic() != null) {
            stringBuilder
                    .append("&topic=")
                    .append(getQueryParameters.getTopic());
        }

        if(getQueryParameters.getVideoType() != null) {
            stringBuilder
                    .append("&type=")
                    .append(getQueryParameters.getVideoType());
        }

        if(getQueryParameters.getFrom() != null) {
            stringBuilder
                    .append("&from=")
                    .append(getQueryParameters.getFrom());
        }

        if(getQueryParameters.getTo() != null) {
            stringBuilder.append("&to")
                    .append(getQueryParameters.getTo());
        }
    }

    private void buildChannelsParameter(String[] channelIds, StringBuilder stringBuilder) {
        for (int i = 0; i < channelIds.length; i++) {
            stringBuilder.append(channelIds[i]);

            if (i < channelIds.length - 1) {
                stringBuilder.append(",");
            }
        }
    }

    private void buildIncludeParameter(String[] extraInfo, StringBuilder stringBuilder) {
        for (int i = 0; i < extraInfo.length; i++) {
            stringBuilder.append(extraInfo[i]);

            if (i < extraInfo.length - 1) {
                stringBuilder.append(",");
            }
        }
    }

    private void buildLangParameter(String[] languages, StringBuilder stringBuilder) {
        for (int i = 0; i < languages.length; i++) {
            stringBuilder.append(languages[i]);

            if (i < languages.length - 1) {
                stringBuilder.append(",");
            }
        }
    }

}
