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
     * Documentation: https://docs.holodex.net/docs/holodex/5dfaf299ea9fd-get-channel-information
     *
     * */
    @Test
    @DisplayName("Get Channel Information")
    public void getChannelInformationTest() throws UnirestException, JsonProcessingException {
        Channel channel = holodexClient.getChannelInformation("UC5CwaMl1eIgY8h02uZw7u8A");

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channel));
        assertEquals(channel.getName(), "Suisei Channel");
    }

    /*
     *
     * Tests for List Channels (https://holodex.net/api/v2/channels)
     * Documentation: https://docs.holodex.net/docs/holodex/4fd0f20623a29-list-channels
     *
     * */
    @Test
    @DisplayName("List Channels")
    public void listChannelsTest() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
        assertTrue(channels.size() > 1);
    }

    @Test
    @DisplayName("List Channels by Type (Vtuber)")
    public void listChannelsVtuberTypeTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelType(ChannelType.VTUBER);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        List<String> channelTypes = new ArrayList<>();

        for(Channel channel : channels) {
            channelTypes.add(channel.getType());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
        assertTrue(channelTypes.stream().allMatch("vtuber"::equals));
    }

    @Test
    @DisplayName("List Channels by Type (Subber)")
    public void listChannelsSubberTypeTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelType(ChannelType.SUBBER);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        List<String> channelTypes = new ArrayList<>();

        for(Channel channel : channels) {
            channelTypes.add(channel.getType());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
        assertTrue(channelTypes.stream().allMatch("subber"::equals));
    }

    @Test
    @DisplayName("List Channels by Language")
    public void listChannelsByLanguageTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setLanguages(new String[]{Language.ES});
        getQueryParameters.setChannelType(ChannelType.SUBBER);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
        assertEquals(channels.get(0).getName(), "F F Traducciones");
    }

    @Test
    @DisplayName("List Channels with a Limit")
    public void listChannelsLimitTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setLimit(5);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
        assertEquals(5, channels.size());
    }

    @Test
    @DisplayName("List Channels with an Offset")
    public void listChannelsOffsetTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setOffset(6);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
        assertTrue(channels.size() > 1);
    }

    @Test
    @DisplayName("List Channels in Ascending SortOrder")
    public void listChannelsOrderAscTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSortOrder(SortOrder.ASC);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
        assertTrue(channels.size() > 1);
    }

    @Test
    @DisplayName("List Channels in Descending SortOrder")
    public void listChannelsOrderDescTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSortOrder(SortOrder.DESC);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
        assertTrue(channels.size() > 1);
    }

    @Test
    @DisplayName("List Channels by Organization")
    public void listChannelsByOrgTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setOrganization(Organizations.HOLOLIVE);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        List<String> organizations = new ArrayList<>();

        for(Channel channel : channels) {
            organizations.add(channel.getOrg());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
        assertTrue(organizations.stream().allMatch("Hololive"::equals));
    }

    @Test
    @DisplayName("List Channels sorting them by field")
    public void listChannelsSortTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSort("subscriber_count");

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        int channelsIndexZeroSubscriberCount = Integer.parseInt(channels.get(0).getSubscriberCount());
        int channelsIndexOneSubscriberCount = Integer.parseInt(channels.get(1).getSubscriberCount());

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
        assertTrue(channelsIndexZeroSubscriberCount <= channelsIndexOneSubscriberCount);
    }

    @Test
    @DisplayName("List Channels All Parameters")
    public void listChannelsAllParametersTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setLanguages(new String[]{Language.JA});
        getQueryParameters.setLimit(5);
        getQueryParameters.setOffset(5);
        getQueryParameters.setSortOrder(SortOrder.ASC);
        getQueryParameters.setOrganization(Organizations.HOLOLIVE);
        getQueryParameters.setSort("clip_count");
        getQueryParameters.setChannelType(ChannelType.VTUBER);

        List<Channel> channels = holodexClient.listChannels(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
        assertNotNull(channels);
    }

    /*
     *
     * Tests for Get a single Video's metadata (https://holodex.net/api/v2/videos/{videoId})
     * Documentation: https://docs.holodex.net/docs/holodex/d18465c977416-get-a-single-video-s-metadata
     *
     * */
    @Test
    @DisplayName("Get Video Metadata")
    public void getVideoMetadataTest() throws UnirestException, JsonProcessingException {
        Video video = holodexClient.getVideoMetadata("IKKar5SS29E", null, null);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(video));
        assertEquals(video.getTitle(), "GHOST / 星街すいせい(official)");
    }

    @Test
    @DisplayName("Get Video Metadata with a null Video ID")
    public void getVideoMetadataNullVideoIdTest() {
        assertThrows(RuntimeException.class, () -> {
            holodexClient.getVideoMetadata(null, null, null);
        });
    }

    @Test
    @DisplayName("Get Video Metadata with Timestamp Comments")
    public void getVideoMetadataTimestampCommentsTest() throws UnirestException, JsonProcessingException {
        Video video = holodexClient.getVideoMetadata("urMWdWlzDCw", 1, null);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(video));
        assertEquals(video.getComments().get(0).getCommentKey(), "Ugzehy9JSmcAGL8VQU94AaABAg");
    }

    @Test
    @DisplayName("Get Video Metadata filter with Language")
    // Note: This endpoint doesn't seem to be working correctly (it doesn't filter any comments)
    public void getVideoMetadataFilterLanguageTest() throws UnirestException, JsonProcessingException {
        Video video = holodexClient.getVideoMetadata("Puyd1d445IM", null,
                new String[]{Language.EN, Language.ES});

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(video));
        assertTrue(video.getClips().size() > 1);
    }

    /*
    *
    * Tests for Query Live and Upcoming Videos (https://holodex.net/api/v2/live)
    * Documentation: https://docs.holodex.net/docs/holodex/b675902a04ca9-query-live-and-upcoming-videos
    *
    * */
    @Test
    @DisplayName("Get Live and Upcoming Videos with Channel ID")
    // This test will fail if the stream ends
    public void getLiveAndUpcomingVideosChannelIdTest() throws UnirestException, JsonProcessingException {
        String title = "【 睡眠導入 】聞くと眠くなるお姉さんの朗読 ラジオ 【 無人配信 】24/7 Unmanned Japanese story reading aloud 【 VTuber 河崎翆 作業用 安眠用 】";

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos("UCs86f6tbWatcKVt7emv9hfQ");

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(videos.get(0).getTitle(), title);
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Null Channel ID")
    // This test will fail if the stream ends
    public void getLiveAndUpcomingVideosNullChannelIdTest() throws UnirestException, JsonProcessingException {
        assertThrows(RuntimeException.class, () -> {
            holodexClient.getLiveAndUpcomingVideos("");
        });
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Video ID")
    // This test will fail if the stream ends
    public void getLiveAndUpcomingVideosVideoIdTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setVideoId("EheqWg4LOLA");

        String title = "【 睡眠導入 】聞くと眠くなるお姉さんの朗読 ラジオ 【 無人配信 】24/7 Unmanned Japanese story reading aloud 【 VTuber 河崎翆 作業用 安眠用 】";

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(videos.get(0).getTitle(), title);
    }
    @Test
    @DisplayName("Get Live and Upcoming Videos with Video ID")
    public void getLiveAndUpcomingVideosIncludeTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setVideoId("EheqWg4LOLA");
        getQueryParameters.setExtraInfo(new String[]{ExtraInfo.DESCRIPTION});

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertNotNull(videos.get(0).getDescription());
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Language Array")
    public void getLiveAndUpcomingVideosLanguageTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setLanguages(new String[]{Language.ID, Language.JA});

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videos.size() > 1);
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Limit")
    public void getLiveAndUpcomingVideosLimitTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setLimit(5);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(5, videos.size());
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Max Upcoming Hours")
    public void getLiveAndUpcomingVideosMaxUpcomingHoursTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setMaxUpcomingHours(50);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videos.size() > 1);
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos with Mentioned Channel ID")
    // This test is not easily reproducible due to the nature of this endpoint
    // You'll need to provide a new title every few days
    public void getLiveAndUpcomingVideosMentionedChannelIdTest() throws UnirestException, JsonProcessingException {
        String title = "【#神楽めあ理解度クイズ】神楽めあを一番理解しているのは誰だ!?【犬山たまき/神楽めあ/しぐれうい/舞元啓介/夕刻ロベル】";
        getQueryParameters.setMentionedChannelId("UCANDOlYTJT7N5jlRC3zfzVA"); // Roberu

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(videos.get(0).getTitle(), title);
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos Offset")
    public void getLiveAndUpcomingVideosOffsetTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setOffset(6);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videos.size() > 0);
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos in Ascending Order")
    public void getLiveAndUpcomingVideosAscTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSortOrder(SortOrder.ASC);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(indexZeroAvailableAtDate.isBefore(lastIndexAvailableAtDate));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos in Descending Order")
    public void getLiveAndUpcomingVideosDescTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSortOrder(SortOrder.DESC);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(indexZeroAvailableAtDate.isAfter(lastIndexAvailableAtDate));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Organization")
    // This endpoint includes other organizations that are collabing with people from the
    // specified organization, that's why I didn't use assertTrue(organizations.stream().allMatch("org"::equals));
    public void getLiveAndUpcomingByOrgTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setOrganization(Organizations.HOLOLIVE);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(videos.get(0).getChannel().getOrg(), "Hololive");
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos Sort by Field")
    public void getLiveAndUpcomingSortByFieldTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSort("live_viewers");

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        int videosIndexZeroLiveViewers = videos.get(0).getLiveViewers();
        int videosIndexOneLiveViewers = videos.get(1).getLiveViewers();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videosIndexZeroLiveViewers <= videosIndexOneLiveViewers);
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Status")
    public void getLiveAndUpcomingByStatusTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setStatus(Status.UPCOMING);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        List<String> statuses = new ArrayList<>();

        for(Video video : videos) {
            statuses.add(video.getStatus());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(statuses.stream().allMatch("upcoming"::equals));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Topic")
    public void getLiveAndUpcomingByTopicTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setTopic("singing");

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        List<String> topics = new ArrayList<>();

        for(Video video : videos) {
            topics.add(video.getTopicId());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(topics.stream().allMatch("singing"::equals));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Video Type (Clip)")
    public void getLiveAndUpcomingByVideoTypeClipTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setVideoType(VideoType.CLIP);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videoTypes.stream().allMatch("clip"::equals));
    }

    @Test
    @DisplayName("Get Live and Upcoming Videos by Video Type (Stream)")
    public void getLiveAndUpcomingByVideoTypeStreamTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setVideoType(VideoType.STREAM);

        List<Video> videos = holodexClient.getLiveAndUpcomingVideos(getQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videoTypes.stream().allMatch("stream"::equals));
    }

    /*
    *
    * Tests for Query Videos (https://holodex.net/api/v2/videos)
    * Documentation: https://docs.holodex.net/docs/holodex/ba328f7332280-query-videos
    *
    * */
    @Test
    @DisplayName("Get Videos")
    public void getVideosTest() throws UnirestException, JsonProcessingException {
        List<Video> videos = holodexClient.getVideos();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videos.size() > 0);
    }

    @Test
    @DisplayName("Get Videos by Channel ID")
    public void getVideosByChannelIdTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(videos.get(0).getChannel().getName(), "Suisei Channel");
    }

    @Test
    @DisplayName("Get Videos by Minimum Date")
    public void getVideosByMinimumDateTest() throws UnirestException, JsonProcessingException, ParseException {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse("2019-08-24T14:15:22Z");

        getQueryParameters.setFrom(offsetDateTime);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videos.get(0).getAvailableAt().isAfter(offsetDateTime));
    }

    @Test
    @DisplayName("Get Video by Video ID")
    public void getVideosByVideoIdTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setVideoId("8ZdLXELdF9Q");

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(videos.get(0).getTitle(), "『VIOLET』 - Ninomae Ina'nis");
    }

    @Test
    @DisplayName("Get Videos by Extra Info")
    public void getVideosByExtraInfoTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setExtraInfo(new String[]{ExtraInfo.DESCRIPTION});

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertNotNull(videos.get(0).getDescription());
    }

    @Test
    @DisplayName("Get Videos by Language")
    public void getVideosByLanguageTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setLanguages(new String[]{Language.JA});

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        List<String> languages = new ArrayList<>();

        for (Video video : videos) {
            languages.add(video.getLang());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(languages.stream().allMatch("ja"::equals));
    }

    @Test
    @DisplayName("Get Videos with a Limit")
    public void getVideosLimitTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setLimit(5);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(videos.size(), 5);
    }

    @Test
    @DisplayName("Get Videos By Maximum Upcoming Hours")
    public void getVideosByMaximumUpcomingHoursTest() throws UnirestException, JsonProcessingException {
        OffsetDateTime tomorrow = OffsetDateTime.now(ZoneOffset.UTC).plusDays(1);

        getQueryParameters.setMaxUpcomingHours(24);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videos.get(0).getAvailableAt().isBefore(tomorrow));
    }

    @Test
    @DisplayName("Get Videos By Mentioned Channel ID")
    // This test isn't easily reproducible due to the nature of this endpoint
    // You'll have to change the expected video ID every few days
    public void getVideosByMentionedChannelIdTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setMentionedChannelId("UC1DCedRgGHBdm81E1llLhOQ"); // Pekora

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(videos.get(0).getId(), "oRNm619BVts");
    }

    @Test
    @DisplayName("Get Videos with Offset")
    public void getVideosOffsetTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setOffset(6);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        assertEquals(videos.get(0).getId(), "9MVcIlxP0eI");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
    }

    @Test
    @DisplayName("Get Videos Sorting them in Ascending Order")
    public void getVideosAscOrderTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSortOrder(SortOrder.ASC);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(indexZeroAvailableAtDate.isBefore(lastIndexAvailableAtDate));
    }

    @Test
    @DisplayName("Get Videos Sorting them in Descending Order")
    public void getVideosDescOrderTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSortOrder(SortOrder.DESC);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(indexZeroAvailableAtDate.isAfter(lastIndexAvailableAtDate));
    }

    @Test
    @DisplayName("Get Videos by Organization")
    public void getVideosByOrganizationTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setOrganization(Organizations.INDEPENDENTS);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        List<String> organizations = new ArrayList<>();

        for(Video video : videos) {
            organizations.add(video.getChannel().getOrg());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(organizations.stream().allMatch("Independents"::equals));
    }

    @Test
    @DisplayName("Get Videos Sorting them by Field")
    public void getVideosSortByFieldTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setSort("duration");

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        int indexZeroDuration = videos.get(0).getDuration();
        int indexOneDuration = videos.get(1).getDuration();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(indexZeroDuration >= indexOneDuration);
    }

    @Test
    @DisplayName("Get Videos by Status")
    public void getVideosByStatusTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setStatus(Status.LIVE);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        List<String> statuses = new ArrayList<>();

        for(Video video : videos) {
            statuses.add(video.getStatus());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(statuses.stream().allMatch("live"::equals));
    }

    @Test
    @DisplayName("Get Videos by Maximum Date")
    public void getVideosByMaximumDateTest() throws UnirestException, JsonProcessingException {
        OffsetDateTime maximumDate = OffsetDateTime.parse("2019-08-24T14:15:22Z");

        getQueryParameters.setTo(maximumDate);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videos.get(0).getAvailableAt().isBefore(maximumDate));
    }

    @Test
    @DisplayName("Get Videos by Topic")
    public void getVideosByTopicTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setTopic("singing");

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        List<String> topics = new ArrayList<>();

        for(Video video : videos) {
            topics.add(video.getTopicId());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(topics.stream().allMatch("singing"::equals));
    }

    @Test
    @DisplayName("Get Videos by Video Type (Stream)")
    public void getVideosByVideoTypeStreamTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setVideoType(VideoType.STREAM);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videoTypes.stream().allMatch("stream"::equals));
    }

    @Test
    @DisplayName("Get Videos by Video Type (Clip)")
    public void getVideosByVideoTypeClipTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setVideoType(VideoType.CLIP);

        List<Video> videos = holodexClient.getVideos(getQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videoTypes.stream().allMatch("clip"::equals));
    }

    /*
    *
    * Tests for Query Videos Related to Channel (https://holodex.net/api/v2/channels/{channelId}/{type})
    * Documentation: https://docs.holodex.net/docs/holodex/643f06b1f7e4d-query-videos-related-to-channel
    *
    * */
    @Test
    @DisplayName("Get Videos Related to Channel (Clips)")
    public void getVideosRelatedToChannelVideoTypeClipsTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A"); // Suisei
        getQueryParameters.setVideoType(VideoType.CLIPS);

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videoTypes.stream().allMatch("clip"::equals));
    }

    @Test
    @DisplayName("Get Videos Related to Channel (Videos)")
    public void getVideosRelatedToChannelVideoTypeVideosTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A"); // Suisei
        getQueryParameters.setVideoType(VideoType.VIDEOS);

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        List<String> channelNames = new ArrayList<>();

        for(Video video : videos) {
            channelNames.add(video.getChannel().getName());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(channelNames.stream().allMatch("Suisei Channel"::equals));
    }

    @Test
    @DisplayName("Get Videos Related to Channel (Collabs)")
    public void getVideosRelatedToChannelVideoTypeCollabsTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A"); // Suisei
        getQueryParameters.setVideoType(VideoType.COLLABS);

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        List<String> channelNames = new ArrayList<>();

        for(Video video : videos) {
            channelNames.add(video.getChannel().getName());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertFalse(channelNames.stream().allMatch("Suisei Channel"::equals));
    }

    @Test
    @DisplayName("Get Videos Related to Channel Null Channel ID")
    public void getVideosRelatedToChannelNullChannelIdTest() {
        getQueryParameters.setVideoType(VideoType.COLLABS);

        assertThrows(RuntimeException.class, () -> {
            holodexClient.getVideosRelatedToChannel(getQueryParameters);
        });
    }

    @Test
    @DisplayName("Get Videos Related to Channel Null Video Type")
    public void getVideosRelatedToChannelNullVideoTypeTest() {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");

        assertThrows(RuntimeException.class, () -> {
            holodexClient.getVideosRelatedToChannel(getQueryParameters);
        });
    }

    @Test
    @DisplayName("Get Videos Related to Channel with Include parameter")
    public void getVideosRelatedToChannelInludeParameterTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");
        getQueryParameters.setVideoType(VideoType.CLIPS);
        getQueryParameters.setExtraInfo(new String[]{ExtraInfo.DESCRIPTION});

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertNotNull(videos.get(0).getDescription());
    }

    @Test
    @DisplayName("Get Videos Related to Channel Filter Clips by Language")
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

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(videos.size(), total);
    }

    @Test
    @DisplayName("Get Videos Related to Channel Filter Collabs by Language")
    public void getVideosRelatedToChannelFilterCollabsByLanguageTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");
        getQueryParameters.setVideoType(VideoType.COLLABS);
        getQueryParameters.setLanguages(new String[]{Language.JA, Language.ES});

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videos.size() > 0);
    }

    @Test
    @DisplayName("Get Videos Related to Channel Filter Collabs by Language")
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
    public void getVideosRelatedToChannelLimitTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");
        getQueryParameters.setVideoType(VideoType.VIDEOS);
        getQueryParameters.setLimit(5);

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(videos.size(), 5);
    }

    @Test
    @DisplayName("Get Videos Related to Channel with Offset")
    public void getVideosRelatedToChannelOffsetTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelId("UC5CwaMl1eIgY8h02uZw7u8A");
        getQueryParameters.setVideoType(VideoType.VIDEOS);
        getQueryParameters.setOffset(5);

        List<Video> videos = holodexClient.getVideosRelatedToChannel(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videos.size() > 0);
    }

    /*
    *
    * Tests for Quickly Access Live / Upcoming for a set of Channels (https://holodex.net/api/v2/users/live)
    * Documentation: https://docs.holodex.net/docs/holodex/f1e355dc4cb79-quickly-access-live-upcoming-for-a-set-of-channels
    *
    * */
    @Test
    @DisplayName("Get Live / Upcoming Videos for a set of Channels")
    public void getLiveOrUpcomingVideosForSetOfChannelsTest() throws UnirestException, JsonProcessingException {
        getQueryParameters.setChannelIds(new String[]{
                "UC5CwaMl1eIgY8h02uZw7u8A", // Suisei
                "UC1DCedRgGHBdm81E1llLhOQ", // Pekora
                "UCO_aKKYxn4tvrqPjcTzZ6EQ", // Fauna
                "UCMwGHR0BTZuLsmjY_NT5Pwg" // Ina
        });

        List<Video> videos = holodexClient.getLiveOrUpcomingVideosForSetOfChannels(getQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertNotNull(videos);
    }

    @Test
    @DisplayName("Get Live / Upcoming Videos for a set of Channels")
    public void getLiveOrUpcomingVideosForSetOfChannelsNullChannelsTest() {
        assertThrows(RuntimeException.class, () -> {
            holodexClient.getLiveOrUpcomingVideosForSetOfChannels(getQueryParameters);
        });
    }


    /*
    *
    * Tests for post-search-videoSearch (https://holodex.net/api/v2/search/videoSearch)
    * Documentation: https://docs.holodex.net/docs/holodex/7ef9a63c3d44a-create-a-search-video-search
    *
    * */
    @Test
    @DisplayName("Search a List of Videos")
    public void searchVideosTest() throws UnirestException, JsonProcessingException {
        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertNotNull(videos);
    }

    @Test
    @DisplayName("Search a List of Videos Sort by Newest")
    public void searchVideosSortByNewestTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setSort(SortOrder.NEWEST);

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(indexZeroAvailableAtDate.isAfter(lastIndexAvailableAtDate));
    }

    @Test
    @DisplayName("Search a List of Videos Sort by Oldest")
    public void searchVideosSortByOldestTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setSort(SortOrder.OLDEST);

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(indexZeroAvailableAtDate.isBefore(lastIndexAvailableAtDate));
    }

    @Test
    @DisplayName("Search a List of Videos Filter by Video Type (Clip)")
    public void searchVideosFilterByVideoTypeClipTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setVideoTypes(new String[]{VideoType.CLIP});

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videoTypes.stream().allMatch("clip"::equals));
    }

    @Test
    @DisplayName("Search a List of Videos Filter by Video Type (Stream)")
    public void searchVideosFilterByVideoTypeStreamTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setVideoTypes(new String[]{VideoType.STREAM});

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videoTypes.stream().allMatch("stream"::equals));
    }

    @Test
    @DisplayName("Search a List of Videos Filter by Languages")
    public void searchVideosFilterByLanguagesTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setVideoTypes(new String[]{VideoType.CLIP});
        postQueryParameters.setLanguage(new String[]{Language.ES, Language.ID});

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videos.size() > 0);
    }

    @Test
    @DisplayName("Search a List of Videos Filter by Topic")
    public void searchVideosFilterByTopicTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setTopics(new String[]{"singing"});

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        List<String> topics = new ArrayList<>();

        for(Video video : videos) {
            topics.add(video.getTopicId());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(topics.stream().allMatch("singing"::equals));
    }

    @Test
    @DisplayName("Search a List of Videos Filter by Topic")
    public void searchVideosFilterByChannelIdsTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setChannelIds(new String[]{
                "UC5CwaMl1eIgY8h02uZw7u8A", // Suisei
                "UCMwGHR0BTZuLsmjY_NT5Pwg" // Ina
        });

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertNotNull(videos);
    }

    @Test
    @DisplayName("Search a List of Videos Filter by Organizations")
    public void searchVideosFilterByOrganizationsTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setOrganizations(new String[]{
                "Hololive",
                "Nijisanji"
        });

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertNotNull(videos);
    }

    @Test
    @DisplayName("Search a List of Videos with Offset")
    public void searchVideosOffsetTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setOffset(10);

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertNotNull(videos);
    }

    @Test
    @DisplayName("Search a List of Videos with Limit")
    public void searchVideosLimitTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setLimit(10);

        List<Video> videos = holodexClient.searchVideos(postQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(videos.size(), 10);
    }

    @Test
    @DisplayName("Search a List of Videos All Parameters")
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

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertNotNull(videos);
    }

    /*
    *
    * Tests for post-search-commentSearch (https://holodex.net/api/v2/search/commentSearch)
    * Documentation: https://docs.holodex.net/docs/holodex/1485e15cbe9e2-create-a-search-comment-search
    *
    * */
    @Test
    @DisplayName("Search Comments in Videos")
    public void searchCommentsVideosTest() throws UnirestException, JsonProcessingException {
        String word = "suisei";
        postQueryParameters.setComment(new String[]{word});

        List<Video> videos = holodexClient.searchCommentsVideos(postQueryParameters);

        List<List<Comment>> comments = new ArrayList<>();

        for(Video video : videos) {
            comments.add(video.getComments());
        }

        int totalCommentsWithWord = 0;
        int totalComments = 0;

        for (List<Comment> commentList : comments) {

            totalComments += commentList.size();

            for (Comment comment : commentList) {
                String lowerCaseMessage = comment.getMessage().toLowerCase();

                if(lowerCaseMessage.contains(word))
                    totalCommentsWithWord++;
            }
        }

        System.out.println("Total comments: " + totalComments);
        System.out.println("Number of comments with the word '" + word + "' in them: " + totalCommentsWithWord);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(totalComments, totalCommentsWithWord);
    }

    @Test
    @DisplayName("Search Comments in Videos Null Comment")
    public void searchCommentsVideosNullCommentTest() {
        assertThrows(RuntimeException.class, () -> {
            holodexClient.searchCommentsVideos(postQueryParameters);
        });
    }

    @Test
    @DisplayName("Search Comments in Videos Sort by Newest")
    public void searchCommentsVideosSortByNewestTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setComment(new String[]{"suisei"});
        postQueryParameters.setSort(SortOrder.NEWEST);

        List<Video> videos = holodexClient.searchCommentsVideos(postQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(indexZeroAvailableAtDate.isAfter(lastIndexAvailableAtDate));
    }

    @Test
    @DisplayName("Search Comments in Videos Sort by Oldest")
    public void searchCommentsVideosSortByOldestTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setComment(new String[]{"suisei"});
        postQueryParameters.setSort(SortOrder.OLDEST);

        List<Video> videos = holodexClient.searchCommentsVideos(postQueryParameters);

        int lastIndex = videos.size() - 1;

        OffsetDateTime indexZeroAvailableAtDate = videos.get(0).getAvailableAt();
        OffsetDateTime lastIndexAvailableAtDate = videos.get(lastIndex).getAvailableAt();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(indexZeroAvailableAtDate.isBefore(lastIndexAvailableAtDate));
    }

    @Test
    @DisplayName("Search Comments in Videos Filter by Video Type (Stream)")
    public void searchCommentsVideosFilterByVideoTypeStreamTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setComment(new String[]{"suisei"});
        postQueryParameters.setVideoTypes(new String[]{VideoType.STREAM});

        List<Video> videos = holodexClient.searchCommentsVideos(postQueryParameters);

        List<String> videoTypes = new ArrayList<>();

        for(Video video : videos) {
            videoTypes.add(video.getType());
        }

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videoTypes.stream().allMatch("stream"::equals));
    }

    @Test
    @DisplayName("Search Comments in Videos Filter by Languages")
    public void searchCommentsVideosFilterByLanguagesTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setComment(new String[]{"suisei"});
        postQueryParameters.setLanguage(new String[]{Language.ES, Language.ID});

        List<Video> videos = holodexClient.searchCommentsVideos(postQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertTrue(videos.size() > 0);
    }

    @Test
    @DisplayName("Search Comments in Videos Filter by Topic")
    // The results don't include topic_id, that's why I don't test whether all the results have
    // the topic_id "singing"
    public void searchCommentsVideosFilterByTopicTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setComment(new String[]{"suisei"});
        postQueryParameters.setTopics(new String[]{"singing"});

        List<Video> videos = holodexClient.searchCommentsVideos(postQueryParameters);

        assertTrue(videos.size() > 0);
    }

    @Test
    @DisplayName("Search Comments in Filter by Topic")
    public void searchCommentsVideosFilterByChannelIdsTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setComment(new String[]{"suisei"});
        postQueryParameters.setChannelIds(new String[]{
                "UC5CwaMl1eIgY8h02uZw7u8A", // Suisei
                "UCMwGHR0BTZuLsmjY_NT5Pwg" // Ina
        });

        List<Video> videos = holodexClient.searchCommentsVideos(postQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertNotNull(videos);
    }

    @Test
    @DisplayName("Search Comments in Videos Filter by Organizations")
    public void searchCommentsVideosFilterByOrganizationsTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setComment(new String[]{"suisei"});
        postQueryParameters.setOrganizations(new String[]{
                "Hololive",
                "Nijisanji"
        });

        List<Video> videos = holodexClient.searchCommentsVideos(postQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertNotNull(videos);
    }

    @Test
    @DisplayName("Search Comments in Videos with Offset")
    public void searchCommentsVideosOffsetTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setComment(new String[]{"suisei"});
        postQueryParameters.setOffset(10);

        List<Video> videos = holodexClient.searchCommentsVideos(postQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertNotNull(videos);
    }

    @Test
    @DisplayName("Search Comments in Videos with Limit")
    public void searchCommentsVideosLimitTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setComment(new String[]{"suisei"});
        postQueryParameters.setLimit(10);

        List<Video> videos = holodexClient.searchCommentsVideos(postQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertEquals(videos.size(), 10);
    }

    @Test
    @DisplayName("Search Comments in Videos All Parameters")
    public void searchCommentsVideosAllParametersTest() throws UnirestException, JsonProcessingException {
        postQueryParameters.setComment(new String[]{"suisei"});
        postQueryParameters.setSort(SortOrder.OLDEST);
        postQueryParameters.setVideoTypes(new String[]{VideoType.STREAM});
        postQueryParameters.setLanguage(new String[]{Language.EN, Language.JA});
        postQueryParameters.setTopics(new String[]{"singing"});
        postQueryParameters.setChannelIds(new String[]{"UC5CwaMl1eIgY8h02uZw7u8A"});
        postQueryParameters.setOrganizations(new String[]{Organizations.HOLOLIVE});
        postQueryParameters.setOffset(5);
        postQueryParameters.setLimit(10);

        List<Video> videos = holodexClient.searchCommentsVideos(postQueryParameters);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(videos));
        assertNotNull(videos);
    }

}
