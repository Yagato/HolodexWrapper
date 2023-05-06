import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.exceptions.UnirestException;
import constants.*;
import io.github.cdimascio.dotenv.Dotenv;
import model.Channel;
import model.QueryParameters;
import model.Video;
import org.junit.jupiter.api.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HolodexClientTest {

    private static HolodexClient holodexClient;

    private static final Dotenv dotenv = Dotenv.load();

    private final static String HOLODEX_API_KEY = dotenv.get("HOLODEX_API_KEY");

    private static ObjectMapper objectMapper = new ObjectMapper();

    QueryParameters queryParameters;

    @BeforeAll
    public static void setup() {
        holodexClient = new HolodexClient(HOLODEX_API_KEY);
        objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    @BeforeEach
    public void beforeEach() {
        queryParameters = new QueryParameters();
    }

    /*
     *
     * Tests for Get Channel Information (https://holodex.net/api/v2/channels/{channelId})
     *
     * */
    @Test
    @DisplayName("Get Channel Information")
    @Order(1)
    public void getChannelInformationTest() throws UnirestException, JsonProcessingException {
        Channel channel = holodexClient.getChannelInformation("UC5CwaMl1eIgY8h02uZw7u8A");
        assertEquals(channel.getName(), "Suisei Channel");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channel));
    }

    /*
     *
     * Tests for List Channels (https://holodex.net/api/v2/channels)
     *
     * */
    @Test
    @DisplayName("List Channels")
    @Order(2)
    public void listChannelsTest() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels();
        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels by Type (Vtuber)")
    @Order(3)
    public void listChannelsVtuberTypeTest() throws UnirestException, JsonProcessingException {
        queryParameters.setChannelType(ChannelType.VTUBER);

        List<Channel> channels = holodexClient.listChannels(queryParameters);

        assertEquals(channels.get(0).getType(), "vtuber");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels by Type (Subber)")
    @Order(4)
    public void listChannelsSubberTypeTest() throws UnirestException, JsonProcessingException {
        queryParameters.setChannelType(ChannelType.SUBBER);

        List<Channel> channels = holodexClient.listChannels(queryParameters);

        assertEquals(channels.get(0).getType(), "subber");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels by Language")
    @Order(5)
    public void listChannelsByLanguageTest() throws UnirestException, JsonProcessingException {
        queryParameters.setLanguages(new Languages[]{Languages.ES});
        queryParameters.setChannelType(ChannelType.SUBBER);

        List<Channel> channels = holodexClient.listChannels(queryParameters);

        assertEquals(channels.get(0).getName(), "F F Traducciones");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels with a Limit")
    @Order(6)
    public void listChannelsLimitTest() throws UnirestException, JsonProcessingException {
        queryParameters.setLimit(5);

        List<Channel> channels = holodexClient.listChannels(queryParameters);

        assertEquals(5, channels.size());
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels with an Offset")
    @Order(7)
    public void listChannelsOffsetTest() throws UnirestException, JsonProcessingException {
        queryParameters.setOffset(6);

        List<Channel> channels = holodexClient.listChannels(queryParameters);

        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels in Ascending SortOrder")
    @Order(8)
    public void listChannelsOrderAscTest() throws UnirestException, JsonProcessingException {
        queryParameters.setSortOrder(SortOrder.ASC);
        List<Channel> channels = holodexClient.listChannels(queryParameters);

        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels in Descending SortOrder")
    @Order(9)
    public void listChannelsOrderDescTest() throws UnirestException, JsonProcessingException {
        queryParameters.setSortOrder(SortOrder.DESC);

        List<Channel> channels = holodexClient.listChannels(queryParameters);

        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels by Organization")
    @Order(10)
    public void listChannelsByOrgTest() throws UnirestException, JsonProcessingException {
        queryParameters.setOrganization(Organizations.HOLOLIVE);

        List<Channel> channels = holodexClient.listChannels(queryParameters);

        assertEquals(channels.get(0).getOrg(), "Hololive");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels sorting them by field")
    @Order(11)
    public void listChannelsSortTest() throws UnirestException, JsonProcessingException {
        queryParameters.setSort("subscriber_count");

        List<Channel> channels = holodexClient.listChannels(queryParameters);

        int channelsIndexZeroSubscriberCount = Integer.parseInt(channels.get(0).getSubscriberCount());
        int channelsIndexOneSubscriberCount = Integer.parseInt(channels.get(1).getSubscriberCount());

        assertTrue(channelsIndexZeroSubscriberCount <= channelsIndexOneSubscriberCount);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    /*
     *
     * Tests for Get a single Video's metadata (https://holodex.net/api/v2/videos/{videoId})
     *
     * */
    @Test
    @DisplayName("Get Video Metadata")
    @Order(12)
    public void getVideoMetadataTest() throws UnirestException, JsonProcessingException {
        Video video = holodexClient.getVideoMetadata("IKKar5SS29E", null, null);
        assertEquals(video.getTitle(), "GHOST / 星街すいせい(official)");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(video));
    }

    @Test
    @DisplayName("Get Video Metadata with a null Video ID")
    @Order(13)
    public void getVideoMetadataNullVideoIdTest() {
        assertThrows(RuntimeException.class, () -> {
            holodexClient.getVideoMetadata(null, null, null);
        });
    }

    @Test
    @DisplayName("Get Video Metadata with Timestamp Comments")
    @Order(14)
    public void getVideoMetadataTimestampCommentsTest() throws UnirestException, JsonProcessingException {
        Video video = holodexClient.getVideoMetadata("urMWdWlzDCw", 1, null);
        assertEquals(video.getComments().get(0).getCommentKey(), "Ugzehy9JSmcAGL8VQU94AaABAg");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(video));
    }

    @Test
    @DisplayName("Get Video Metadata filter with Language")
    @Order(14)
    // Note: This endpoint doesn't seem to be working correctly (it doesn't filter any comments)
    public void getVideoMetadataFilterLanguageTest() throws UnirestException, JsonProcessingException {
        Video video = holodexClient.getVideoMetadata("Puyd1d445IM", null,
                new Languages[]{Languages.EN, Languages.ES});
        assertTrue(video.getClips().size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(video));
    }

    /*
    *
    * Tests for Query Live and Upcoming Videos (https://holodex.net/api/v2/live)
    *
    * */
    @Test
    @DisplayName("Get Live and Upcoming Videos with Channel ID")
    @Order(15)
    // This test will fail if the stream ends
    public void getLiveAndUpcomingVideosChannelIdTest() throws UnirestException, JsonProcessingException {
        String title = "【 睡眠導入 】聞くと眠くなるお姉さんの朗読 ラジオ 【 無人配信 】24/7 Unmanned Japanese story reading aloud 【 VTuber 河崎翆 作業用 安眠用 】";
        List<Video> videos = holodexClient.getLiveAndUpcomingVideos("UCs86f6tbWatcKVt7emv9hfQ");
        assertEquals(videos.get(0).getTitle(), title);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Video ID")
    @Order(16)
    // This test will fail if the stream ends
    public void getLiveAndUpcomingVideosVideoIdTest() throws UnirestException, JsonProcessingException {
        queryParameters.setVideoId("EheqWg4LOLA");
        String title = "【 睡眠導入 】聞くと眠くなるお姉さんの朗読 ラジオ 【 無人配信 】24/7 Unmanned Japanese story reading aloud 【 VTuber 河崎翆 作業用 安眠用 】";

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertEquals(videos.get(0).getTitle(), title);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }
    @Test
    @DisplayName("Get Live and Upcoming Videos with Video ID")
    @Order(17)
    public void getLiveAndUpcomingVideosIncludeTest() throws UnirestException, JsonProcessingException {
        queryParameters.setVideoId("EheqWg4LOLA");
        queryParameters.setExtraInfo(new ExtraInfo[]{ExtraInfo.DESCRIPTION});

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertNotNull(videos.get(0).getDescription());
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Language Array")
    @Order(18)
    public void getLiveAndUpcomingVideosLanguageTest() throws UnirestException, JsonProcessingException {
        queryParameters.setLanguages(new Languages[]{Languages.ID, Languages.JA});

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertTrue(videos.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Limit")
    @Order(19)
    public void getLiveAndUpcomingVideosLimitTest() throws UnirestException, JsonProcessingException {
        queryParameters.setLimit(5);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertEquals(5, videos.size());
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Max Upcoming Hours")
    @Order(20)
    public void getLiveAndUpcomingVideosMaxUpcomingHoursTest() throws UnirestException, JsonProcessingException {
        queryParameters.setMaxUpcomingHours(50);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertTrue(videos.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Mentioned Channel ID")
    @Order(21)
    // This test is not easily reproducible due to the nature of this endpoint
    // You'll need to provide a new title every few days
    public void getLiveAndUpcomingVideosMentionedChannelIdTest() throws UnirestException, JsonProcessingException {
        String title = "【#ホロライブワールド】GWマリオメーカー大会本番!!!!!!!!!!!!!!!ぺこ!【ホロライブ/兎田ぺこら】";
        queryParameters.setMentionedChannelId("UCO_aKKYxn4tvrqPjcTzZ6EQ");

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertEquals(videos.get(0).getTitle(), title);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos Offset")
    @Order(22)
    public void getLiveAndUpcomingVideosOffsetTest() throws UnirestException, JsonProcessingException {
        queryParameters.setOffset(6);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertTrue(videos.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos in Ascending Order")
    @Order(23)
    public void getLiveAndUpcomingVideosAscTest() throws UnirestException, JsonProcessingException {
        queryParameters.setSortOrder(SortOrder.ASC);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertTrue(videos.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos in Descending Order")
    @Order(24)
    public void getLiveAndUpcomingVideosDescTest() throws UnirestException, JsonProcessingException {
        queryParameters.setSortOrder(SortOrder.DESC);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertTrue(videos.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Organization")
    @Order(25)
    public void getLiveAndUpcomingByOrgTest() throws UnirestException, JsonProcessingException {
        queryParameters.setOrganization(Organizations.NIJISANJI);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertEquals(videos.get(0).getChannel().getOrg(), "Nijisanji");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos Sort by Field")
    @Order(26)
    public void getLiveAndUpcomingSortByFieldTest() throws UnirestException, JsonProcessingException {
        queryParameters.setSort("live_viewers");

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        int videosIndexZeroLiveViewers = videos.get(0).getLiveViewers();
        int videosIndexOneLiveViewers = videos.get(1).getLiveViewers();

        assertTrue(videosIndexZeroLiveViewers <= videosIndexOneLiveViewers);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Status")
    @Order(27)
    public void getLiveAndUpcomingByStatusTest() throws UnirestException, JsonProcessingException {
        queryParameters.setStatus(Status.UPCOMING);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertEquals(videos.get(0).getStatus(), "upcoming");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Topic")
    @Order(28)
    public void getLiveAndUpcomingByTopicTest() throws UnirestException, JsonProcessingException {
        queryParameters.setTopic("singing");

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertEquals(videos.get(0).getTopicId(), "singing");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Video Type (Clip)")
    @Order(29)
    public void getLiveAndUpcomingByVideoTypeClipTest() throws UnirestException, JsonProcessingException {
        queryParameters.setVideoType(VideoType.CLIP);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertEquals(videos.get(0).getType(), "clip");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Video Type (Stream)")
    @Order(29)
    public void getLiveAndUpcomingByVideoTypeStreamTest() throws UnirestException, JsonProcessingException {
        queryParameters.setVideoType(VideoType.STREAM);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(queryParameters);

        assertEquals(videos.get(0).getType(), "stream");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    /*
    *
    * Tests for Query Videos (https://holodex.net/api/v2/videos)
    *
    * */
    @Test
    @DisplayName("Get Videos")
    @Order(30)
    public void getVideosTest() throws UnirestException, JsonProcessingException {
        List<Video> videos = holodexClient.getVideos();

        assertTrue(videos.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos by Channel ID")
    @Order(31)
    public void getVideosByChannelIdTest() throws UnirestException, JsonProcessingException {
        queryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");

        List<Video> videos = holodexClient.getVideos(queryParameters);

        assertEquals(videos.get(0).getChannel().getName(), "Suisei Channel");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

//    @Test
//    @DisplayName("Get Videos by Minimum Date")
//    @Order(32)
//    public void getVideosByMinimumDateTest() throws UnirestException, JsonProcessingException, ParseException {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        Date date = simpleDateFormat.parse("2023-05-14T13:40:00.000Z");
//        String formattedDate = simpleDateFormat.format(date);
//
//        queryParameters.setFrom(formattedDate);
//
//        List<Video> videos = holodexClient.getVideos(queryParameters);
//
//        assertTrue(videos.get(0).getAvailableAt().compareTo(date) >= 0);
//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
//    }

    @Test
    @DisplayName("Get Video by Video ID")
    @Order(33)
    public void getVideosByVideoIdTest() throws UnirestException, JsonProcessingException {
        queryParameters.setVideoId("8ZdLXELdF9Q");

        List<Video> videos = holodexClient.getVideos(queryParameters);

        assertEquals(videos.get(0).getTitle(), "『VIOLET』 - Ninomae Ina'nis");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos by Extra Info")
    @Order(34)
    public void getVideosByExtraInfoTest() throws UnirestException, JsonProcessingException {
        queryParameters.setExtraInfo(new ExtraInfo[]{ExtraInfo.DESCRIPTION});

        List<Video> videos = holodexClient.getVideos(queryParameters);

        assertNotNull(videos.get(0).getDescription());
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos by Language")
    @Order(35)
    public void getVideosByLanguageTest() throws UnirestException, JsonProcessingException {
        queryParameters.setLanguages(new Languages[]{Languages.JA});

        List<Video> videos = holodexClient.getVideos(queryParameters);

        List<String> languages = new ArrayList<>();

        for (Video video : videos) {
            languages.add(video.getLang());
        }

        assertTrue(languages.stream().allMatch("ja"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos with a Limit")
    @Order(36)
    public void getVideosLimitTest() throws UnirestException, JsonProcessingException {
        queryParameters.setLimit(5);

        List<Video> videos = holodexClient.getVideos(queryParameters);

        assertEquals(videos.size(), 5);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos By Maximum Upcoming Hours")
    @Order(37)
    public void getVideosByMaximumUpcomingHoursTest() throws UnirestException, JsonProcessingException {
        queryParameters.setMaxUpcomingHours(24);

        List<Video> videos = holodexClient.getVideos(queryParameters);

        assertTrue(videos.size() >= 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos By Mentioned Channel ID")
    @Order(38)
    public void getVideosByMentionedChannelIdTest() throws UnirestException, JsonProcessingException {
        queryParameters.setMentionedChannelId("UC1DCedRgGHBdm81E1llLhOQ");

        List<Video> videos = holodexClient.getVideos(queryParameters);

        assertEquals(videos.get(0).getId(), "HMV6xHHumVg");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

}
