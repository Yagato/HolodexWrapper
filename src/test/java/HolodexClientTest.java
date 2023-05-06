import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.exceptions.UnirestException;
import constants.*;
import io.github.cdimascio.dotenv.Dotenv;
import model.*;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.jupiter.api.*;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HolodexClientTest {

    private static HolodexClient holodexClient;

    private static final Dotenv dotenv = Dotenv.load();

    private final static String HOLODEX_API_KEY = dotenv.get("HOLODEX_API_KEY");

    private static ObjectMapper objectMapper = new ObjectMapper();

    private GetQueryParameters getQueryParameters;

    private PostQueryParameters postQueryParameters;


    @BeforeAll
    public static void setup() {
        holodexClient = new HolodexClient(HOLODEX_API_KEY);
        objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    @BeforeEach
    public void beforeEach() {
        getQueryParameters = new GetQueryParameters();
        postQueryParameters = new PostQueryParameters();
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
        getQueryParameters.setChannelType(ChannelType.VTUBER);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        List<String> channelTypes = new ArrayList<>();

        for(Channel channel : channels) {
            channelTypes.add(channel.getType());
        }

        assertTrue(channelTypes.stream().allMatch("vtuber"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels by Type (Subber)")
    @Order(4)
    public void listChannelsSubberTypeTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelType(ChannelType.SUBBER);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        List<String> channelTypes = new ArrayList<>();

        for(Channel channel : channels) {
            channelTypes.add(channel.getType());
        }

        assertTrue(channelTypes.stream().allMatch("subber"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels by Language")
    @Order(5)
    public void listChannelsByLanguageTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setLanguages(new String[]{Language.ES});
        getQueryParameters.setChannelType(ChannelType.SUBBER);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        assertEquals(channels.get(0).getName(), "F F Traducciones");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels with a Limit")
    @Order(6)
    public void listChannelsLimitTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setLimit(5);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        assertEquals(5, channels.size());
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels with an Offset")
    @Order(7)
    public void listChannelsOffsetTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setOffset(6);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels in Ascending SortOrder")
    @Order(8)
    public void listChannelsOrderAscTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSortOrder(SortOrder.ASC);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels in Descending SortOrder")
    @Order(9)
    public void listChannelsOrderDescTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSortOrder(SortOrder.DESC);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels by Organization")
    @Order(10)
    public void listChannelsByOrgTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setOrganization(Organizations.HOLOLIVE);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        List<String> organizations = new ArrayList<>();

        for(Channel channel : channels) {
            organizations.add(channel.getOrg());
        }

        assertTrue(organizations.stream().allMatch("Hololive"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels sorting them by field")
    @Order(11)
    public void listChannelsSortTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSort("subscriber_count");

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        int channelsIndexZeroSubscriberCount = Integer.parseInt(channels.get(0).getSubscriberCount());
        int channelsIndexOneSubscriberCount = Integer.parseInt(channels.get(1).getSubscriberCount());

        assertTrue(channelsIndexZeroSubscriberCount <= channelsIndexOneSubscriberCount);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels All Parameters")
    @Order(12)
    public void listChannelsAllParametersTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setLanguages(new String[]{Language.JA});
        getQueryParameters.setLimit(5);
        getQueryParameters.setOffset(5);
        getQueryParameters.setSortOrder(SortOrder.ASC);
        getQueryParameters.setOrganization(Organizations.HOLOLIVE);
        getQueryParameters.setSort("clip_count");
        getQueryParameters.setChannelType(ChannelType.VTUBER);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        assertNotNull(channels);
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
                new String[]{Language.EN, Language.ES});
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
        getQueryParameters.setVideoId("EheqWg4LOLA");
        String title = "【 睡眠導入 】聞くと眠くなるお姉さんの朗読 ラジオ 【 無人配信 】24/7 Unmanned Japanese story reading aloud 【 VTuber 河崎翆 作業用 安眠用 】";

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        assertEquals(videos.get(0).getTitle(), title);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }
    @Test
    @DisplayName("Get Live and Upcoming Videos with Video ID")
    @Order(17)
    public void getLiveAndUpcomingVideosIncludeTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setVideoId("EheqWg4LOLA");
        getQueryParameters.setExtraInfo(new String[]{ExtraInfo.DESCRIPTION});

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        assertNotNull(videos.get(0).getDescription());
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Language Array")
    @Order(18)
    public void getLiveAndUpcomingVideosLanguageTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setLanguages(new String[]{Language.ID, Language.JA});

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        assertTrue(videos.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Limit")
    @Order(19)
    public void getLiveAndUpcomingVideosLimitTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setLimit(5);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        assertEquals(5, videos.size());
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Max Upcoming Hours")
    @Order(20)
    public void getLiveAndUpcomingVideosMaxUpcomingHoursTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setMaxUpcomingHours(50);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

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
        getQueryParameters.setMentionedChannelId("UCO_aKKYxn4tvrqPjcTzZ6EQ");

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        assertEquals(videos.get(0).getTitle(), title);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos Offset")
    @Order(22)
    public void getLiveAndUpcomingVideosOffsetTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setOffset(6);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        assertTrue(videos.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos in Ascending Order")
    @Order(23)
    public void getLiveAndUpcomingVideosAscTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSortOrder(SortOrder.ASC);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        assertTrue(indexZeroAvailableAtDate.isBefore(lastIndexAvailableAtDate));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos in Descending Order")
    @Order(24)
    public void getLiveAndUpcomingVideosDescTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSortOrder(SortOrder.DESC);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        assertTrue(indexZeroAvailableAtDate.isAfter(lastIndexAvailableAtDate));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Organization")
    @Order(25)
    // This endpoint includes other organizations that are collabing with people from the
    // specified organization, that's why I didn't use assertTrue(organizations.stream().allMatch("org"::equals));
    public void getLiveAndUpcomingByOrgTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setOrganization(Organizations.HOLOLIVE);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        assertEquals(videos.get(0).getChannel().getOrg(), "Hololive");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos Sort by Field")
    @Order(26)
    public void getLiveAndUpcomingSortByFieldTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSort("live_viewers");

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        int videosIndexZeroLiveViewers = videos.get(0).getLiveViewers();
        int videosIndexOneLiveViewers = videos.get(1).getLiveViewers();

        assertTrue(videosIndexZeroLiveViewers <= videosIndexOneLiveViewers);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Status")
    @Order(27)
    public void getLiveAndUpcomingByStatusTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setStatus(Status.UPCOMING);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        List<String> statuses = new ArrayList<>();

        for(Video video : videos) {
            statuses.add(video.getStatus());
        }

        assertTrue(statuses.stream().allMatch("upcoming"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Topic")
    @Order(28)
    public void getLiveAndUpcomingByTopicTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setTopic("singing");

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        List<String> topics = new ArrayList<>();

        for(Video video : videos) {
            topics.add(video.getTopicId());
        }

        assertTrue(topics.stream().allMatch("singing"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Video Type (Clip)")
    @Order(29)
    public void getLiveAndUpcomingByVideoTypeClipTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setVideoType(VideoType.CLIP);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        assertTrue(videoTypes.stream().allMatch("clip"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Video Type (Stream)")
    @Order(29)
    public void getLiveAndUpcomingByVideoTypeStreamTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setVideoType(VideoType.STREAM);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        assertTrue(videoTypes.stream().allMatch("stream"::equals));
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
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        assertEquals(videos.get(0).getChannel().getName(), "Suisei Channel");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos by Minimum Date")
    @Order(32)
    public void getVideosByMinimumDateTest() throws UnirestException, JsonProcessingException, ParseException {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse("2019-08-24T14:15:22Z");

        getQueryParameters.setFrom(offsetDateTime);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        assertTrue(videos.get(0).getAvailableAt().isAfter(offsetDateTime));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Video by Video ID")
    @Order(33)
    public void getVideosByVideoIdTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setVideoId("8ZdLXELdF9Q");

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        assertEquals(videos.get(0).getTitle(), "『VIOLET』 - Ninomae Ina'nis");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos by Extra Info")
    @Order(34)
    public void getVideosByExtraInfoTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setExtraInfo(new String[]{ExtraInfo.DESCRIPTION});

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        assertNotNull(videos.get(0).getDescription());
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos by Language")
    @Order(35)
    public void getVideosByLanguageTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setLanguages(new String[]{Language.JA});

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

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
        getQueryParameters.setLimit(5);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        assertEquals(videos.size(), 5);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos By Maximum Upcoming Hours")
    @Order(37)
    public void getVideosByMaximumUpcomingHoursTest() throws UnirestException, JsonProcessingException {
        OffsetDateTime tomorrow = OffsetDateTime.now(ZoneOffset.UTC).plusDays(1);

        getQueryParameters.setMaxUpcomingHours(24);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        assertTrue(videos.get(0).getAvailableAt().isBefore(tomorrow));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos By Mentioned Channel ID")
    @Order(38)
    public void getVideosByMentionedChannelIdTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setMentionedChannelId("UC1DCedRgGHBdm81E1llLhOQ");

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        assertEquals(videos.get(0).getId(), "HMV6xHHumVg");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos with Offset")
    @Order(39)
    public void getVideosOffsetTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setOffset(6);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        assertEquals(videos.get(0).getId(), "9MVcIlxP0eI");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos Sorting them in Ascending Order")
    @Order(40)
    public void getVideosAscOrderTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSortOrder(SortOrder.ASC);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        assertTrue(indexZeroAvailableAtDate.isBefore(lastIndexAvailableAtDate));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos Sorting them in Descending Order")
    @Order(41)
    public void getVideosDescOrderTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSortOrder(SortOrder.DESC);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        assertTrue(indexZeroAvailableAtDate.isAfter(lastIndexAvailableAtDate));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos by Organization")
    @Order(42)
    public void getVideosByOrganizationTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setOrganization(Organizations.INDEPENDENTS);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        List<String> organizations = new ArrayList<>();

        for(Video video : videos) {
            organizations.add(video.getChannel().getOrg());
        }

        assertTrue(organizations.stream().allMatch("Independents"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos Sorting them by Field")
    @Order(43)
    public void getVideosSortByFieldTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSort("duration");

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        int indexZeroDuration = videos.get(0).getDuration();
        int indexOneDuration = videos.get(1).getDuration();

        assertTrue(indexZeroDuration >= indexOneDuration);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos by Status")
    @Order(44)
    public void getVideosByStatusTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setStatus(Status.LIVE);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        List<String> statuses = new ArrayList<>();

        for(Video video : videos) {
            statuses.add(video.getStatus());
        }

        assertTrue(statuses.stream().allMatch("live"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos by Maximum Date")
    @Order(45)
    public void getVideosByMaximumDateTest() throws UnirestException, JsonProcessingException {
        OffsetDateTime maximumDate = OffsetDateTime.parse("2019-08-24T14:15:22Z");

        getQueryParameters.setTo(maximumDate);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        assertTrue(videos.get(0).getAvailableAt().isBefore(maximumDate));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos by Topic")
    @Order(46)
    public void getVideosByTopicTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setTopic("singing");

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        List<String> topics = new ArrayList<>();

        for(Video video : videos) {
            topics.add(video.getTopicId());
        }

        assertTrue(topics.stream().allMatch("singing"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos by Video Type (Stream)")
    @Order(47)
    public void getVideosByVideoTypeStreamTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setVideoType(VideoType.STREAM);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        assertTrue(videoTypes.stream().allMatch("stream"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos by Video Type (Clip)")
    @Order(48)
    public void getVideosByVideoTypeClipTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setVideoType(VideoType.CLIP);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        assertTrue(videoTypes.stream().allMatch("clip"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    /*
    *
    * Tests for Query Videos Related to Channel (https://holodex.net/api/v2/channels/{channelId}/{type})
    *
    * */
    @Test
    @DisplayName("Get Videos Related to Channel (Clips)")
    @Order(49)
    public void getVideosRelatedToChannelVideoTypeClipsTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");
        getQueryParameters.setVideoType(VideoType.CLIPS);

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        assertTrue(videoTypes.stream().allMatch("clip"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos Related to Channel (Videos)")
    @Order(50)
    public void getVideosRelatedToChannelVideoTypeVideosTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");
        getQueryParameters.setVideoType(VideoType.VIDEOS);

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        List<String> channelNames = new ArrayList<>();

        for(Video video : videos) {
            channelNames.add(video.getChannel().getName());
        }

        assertTrue(channelNames.stream().allMatch("Suisei Channel"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos Related to Channel (Collabs)")
    @Order(51)
    public void getVideosRelatedToChannelVideoTypeCollabsTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");
        getQueryParameters.setVideoType(VideoType.COLLABS);

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        List<String> channelNames = new ArrayList<>();

        for(Video video : videos) {
            channelNames.add(video.getChannel().getName());
        }

        assertFalse(channelNames.stream().allMatch("Suisei Channel"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos Related to Channel Null Channel ID")
    @Order(52)
    public void getVideosRelatedToChannelNullChannelIdTest() {
        getQueryParameters.setVideoType(VideoType.COLLABS);

        assertThrows(RuntimeException.class, () -> {
            holodexClient.getVideosRelatedToChannel(getQueryParameters);
        });
    }

    @Test
    @DisplayName("Get Videos Related to Channel Null Video Type")
    @Order(53)
    public void getVideosRelatedToChannelNullVideoTypeTest() {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");

        assertThrows(RuntimeException.class, () -> {
            holodexClient.getVideosRelatedToChannel(getQueryParameters);
        });
    }

    @Test
    @DisplayName("Get Videos Related to Channel with Include parameter")
    @Order(54)
    public void getVideosRelatedToChannelInludeParameterTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");
        getQueryParameters.setVideoType(VideoType.CLIPS);
        getQueryParameters.setExtraInfo(new String[]{ExtraInfo.DESCRIPTION});

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        assertNotNull(videos.get(0).getDescription());
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos Related to Channel Filter Clips by Language")
    @Order(55)
    public void getVideosRelatedToChannelFilterClipsByLanguageTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");
        getQueryParameters.setVideoType(VideoType.CLIPS);
        getQueryParameters.setLanguages(new String[]{Language.JA, Language.ES});

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        int total = 0;

        for (Video video : videos) {
            if (video.getLang().equals("es") || video.getLang().equals("ja")) {
                total++;
            }
        }

        assertEquals(videos.size(), total);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos Related to Channel Filter Collabs by Language")
    @Order(56)
    public void getVideosRelatedToChannelFilterCollabsByLanguageTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");
        getQueryParameters.setVideoType(VideoType.COLLABS);
        getQueryParameters.setLanguages(new String[]{Language.JA, Language.ES});

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        assertTrue(videos.size() > 0);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos Related to Channel Filter Collabs by Language")
    @Order(57)
    public void getVideosRelatedToChannelFilterVideosByLanguageTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");
        getQueryParameters.setVideoType(VideoType.VIDEOS);
        getQueryParameters.setLanguages(new String[]{Language.JA, Language.ES});

        assertThrows(RuntimeException.class, () -> {
            holodexClient.getVideosRelatedToChannel(getQueryParameters);
        });
    }

    @Test
    @DisplayName("Get Videos Related to Channel with Limit")
    @Order(57)
    public void getVideosRelatedToChannelLimitTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");
        getQueryParameters.setVideoType(VideoType.VIDEOS);
        getQueryParameters.setLimit(5);

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        assertEquals(videos.size(), 5);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos Related to Channel with Offset")
    @Order(58)
    public void getVideosRelatedToChannelOffsetTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");
        getQueryParameters.setVideoType(VideoType.VIDEOS);
        getQueryParameters.setOffset(5);

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        assertTrue(videos.size() > 0);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    /*
    *
    * Tests for Quickly Access Live / Upcoming for a set of Channels (https://holodex.net/api/v2/users/live)
    *
    * */
    @Test
    @DisplayName("Get Live / Upcoming Videos for a set of Channels")
    @Order(59)
    public void getLiveOrUpcomingVideosForSetOfChannelsTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelIds(new String[]{
                "UC5CwaMl1eIgY8h02uZw7u8A", // Suisei
                "UC1DCedRgGHBdm81E1llLhOQ", // Pekora
                "UCO_aKKYxn4tvrqPjcTzZ6EQ", // Fauna
                "UCMwGHR0BTZuLsmjY_NT5Pwg" // Ina
        });

        List<Video> videos = holodexClient.getLiveOrUpcomingVideosForSetOfChannels(getQueryParameters);

        assertNotNull(videos);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Live / Upcoming Videos for a set of Channels")
    @Order(60)
    public void getLiveOrUpcomingVideosForSetOfChannelsNullChannelsTest() {
        assertThrows(RuntimeException.class, () -> {
            holodexClient.getLiveOrUpcomingVideosForSetOfChannels(getQueryParameters);
        });
    }


    /*
    *
    * Tests for post-search-videoSearch (https://holodex.net/api/v2/search/videoSearch)
    *
    * */
    @Test
    @DisplayName("Search a List of Videos")
    @Order(61)
    public void searchVideosTest() throws UnirestException, JsonProcessingException {
        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        assertNotNull(videos);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Search a List of Videos Sort by Newest")
    @Order(62)
    public void searchVideosSortByNewestTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setSort(SortOrder.NEWEST);

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        assertTrue(indexZeroAvailableAtDate.isAfter(lastIndexAvailableAtDate));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Search a List of Videos Sort by Oldest")
    @Order(63)
    public void searchVideosSortByOldestTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setSort(SortOrder.OLDEST);

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        assertTrue(indexZeroAvailableAtDate.isBefore(lastIndexAvailableAtDate));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Search a List of Videos Filter by Video Type (Clip)")
    @Order(64)
    public void searchVideosFilterByVideoTypeClipTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setVideoTypes(new String[]{VideoType.CLIP});

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        assertTrue(videoTypes.stream().allMatch("clip"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Search a List of Videos Filter by Video Type (Stream)")
    @Order(65)
    public void searchVideosFilterByVideoTypeStreamTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setVideoTypes(new String[]{VideoType.STREAM});

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        assertTrue(videoTypes.stream().allMatch("stream"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Search a List of Videos Filter by Languages")
    @Order(66)
    public void searchVideosFilterByLanguagesTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setVideoTypes(new String[]{VideoType.CLIP});
        postQueryParameters.setLanguage(new String[]{Language.ES, Language.ID});

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        assertTrue(videos.size() > 0);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Search a List of Videos Filter by Topic")
    @Order(67)
    public void searchVideosFilterByTopicTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setTopics(new String[]{"singing"});

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        List<String> topics = new ArrayList<>();

        for(Video video : videos) {
            topics.add(video.getTopicId());
        }

        assertTrue(topics.stream().allMatch("singing"::equals));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Search a List of Videos Filter by Topic")
    @Order(68)
    public void searchVideosFilterByChannelIdsTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setChannelIds(new String[]{
                "UC5CwaMl1eIgY8h02uZw7u8A", // Suisei
                "UCMwGHR0BTZuLsmjY_NT5Pwg" // Ina
        });

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        assertNotNull(videos);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Search a List of Videos Filter by Organizations")
    @Order(69)
    public void searchVideosFilterByOrganizationsTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setOrganizations(new String[]{
                "Hololive",
                "Nijisanji"
        });

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        assertNotNull(videos);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Search a List of Videos with Offset")
    @Order(70)
    public void searchVideosOffsetTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setOffset(10);

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        assertNotNull(videos);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Search a List of Videos with Limit")
    @Order(71)
    public void searchVideosLimitTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setLimit(10);

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        assertEquals(videos.size(), 10);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Search a List of Videos All Parameters")
    @Order(72)
    public void searchVideosAllParametersTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setSort(SortOrder.OLDEST);
        postQueryParameters.setVideoTypes(new String[]{VideoType.STREAM, VideoType.CLIP});
        postQueryParameters.setLanguage(new String[]{Language.EN, Language.JA});
        postQueryParameters.setTopics(new String[]{"singing"});
        postQueryParameters.setChannelIds(new String[]{"UC5CwaMl1eIgY8h02uZw7u8A"});
        postQueryParameters.setOrganizations(new String[]{Organizations.HOLOLIVE});
        postQueryParameters.setOffset(5);
        postQueryParameters.setLimit(10);

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        assertNotNull(videos);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    /*
    *
    * Tests for post-search-commentSearch (https://holodex.net/api/v2/search/commentSearch)
    * This endpoint doesn't seem to be working correctly
    *
    * */
    @Test
    @DisplayName("Search Comments in Videos")
    @Order(73)
    public void searchCommentsVideosTest() throws UnirestException, JsonProcessingException {
        String word = "lemon";
        postQueryParameters.setComment(word);

        List<Video> videos = holodexClient.searchCommentsVideos(postQueryParameters);

        List<List<Comment>> comments = new ArrayList<>();

        for(Video video : videos) {
            comments.add(video.getComments());
        }

        int counter = 0;
        int totalComments = 0;

        for (List<Comment> commentList : comments) {

            totalComments += commentList.size();

            for (Comment comment : commentList) {
                String lowerCaseMessage = comment.getMessage().toLowerCase();
                comment.setMessage(lowerCaseMessage);

                if(lowerCaseMessage.contains(word))
                    counter++;
            }
        }

        assertEquals(totalComments, counter);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(comments));
    }

    @Test
    @DisplayName("Search Comments in Videos Null Comment")
    @Order(74)
    public void searchCommentsVideosNullCommentTest() {
        assertThrows(RuntimeException.class, () -> {
            holodexClient.searchCommentsVideos(postQueryParameters);
        });
    }

}
