import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import constants.ChannelType;
import constants.Languages;
import constants.Organizations;
import constants.SortOrder;
import io.github.cdimascio.dotenv.Dotenv;
import model.Channel;
import model.Video;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HolodexClientTest {

    private static HolodexClient holodexClient;

    private static final Dotenv dotenv = Dotenv.load();

    private final static String HOLODEX_API_KEY = dotenv.get("HOLODEX_API_KEY");

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setup() {
        holodexClient = new HolodexClient(HOLODEX_API_KEY);
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
        List<Channel> channels = holodexClient.listChannels(null, null, null,
                null, null, null, null);
        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels by Type (Vtuber)")
    @Order(3)
    public void listChannelsVtuberTypeTest() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, null,
                null, null, null, ChannelType.VTUBER);
        assertEquals(channels.get(0).getType(), "vtuber");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels by Type (Subber)")
    @Order(4)
    public void listChannelsSubberTypeTest() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, null,
                null, null, null, ChannelType.SUBBER);
        assertEquals(channels.get(0).getType(), "subber");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels by Language")
    @Order(5)
    public void listChannelsByLanguage() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(Languages.ES, null, null,
                null, null, null, ChannelType.SUBBER);
        assertEquals(channels.get(0).getName(), "F F Traducciones");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels with a Limit")
    @Order(6)
    public void listChannelsLimit() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, 5, null,
                null, null, null, null);
        assertEquals(5, channels.size());
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels with an Offset")
    @Order(7)
    public void listChannelsOffset() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, 6,
                null, null, null, null);
        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels in Ascending SortOrder")
    @Order(8)
    public void listChannelsOrderAsc() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, null,
                SortOrder.ASC, null, null, null);
        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels in Descending SortOrder")
    @Order(9)
    public void listChannelsOrderDesc() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, null,
                SortOrder.DESC, null, null, null);
        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels by Organization")
    @Order(10)
    public void listChannelsByOrg() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, null,
                null, Organizations.HOLOLIVE, null, null);
        assertEquals(channels.get(0).getOrg(), "Hololive");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    @DisplayName("List Channels sorting them by field")
    @Order(11)
    public void listChannelsSort() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, null,
                null, null, "subscriber_count", null);

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
    public void getVideoMetadata() throws UnirestException, JsonProcessingException {
        Video video = holodexClient.getVideoMetadata("IKKar5SS29E", null, null);
        assertEquals(video.getTitle(), "GHOST / 星街すいせい(official)");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(video));
    }

    @Test
    @DisplayName("Get Video Metadata with a null Video ID")
    @Order(13)
    public void getVideoMetadataNullVideoId() {
        assertThrows(RuntimeException.class, () -> {
            holodexClient.getVideoMetadata(null, null, null);
        });
    }

    @Test
    @DisplayName("Get Video Metadata with Timestamp Comments")
    @Order(14)
    public void getVideoMetadataTimestampComments() throws UnirestException, JsonProcessingException {
        Video video = holodexClient.getVideoMetadata("urMWdWlzDCw", 1, null);
        assertEquals(video.getComments().get(0).getCommentKey(), "Ugzehy9JSmcAGL8VQU94AaABAg");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(video));
    }

    @Test
    @DisplayName("Get Video Metadata filter with Language")
    @Order(14)
    // Note: This endpoint doesn't seem to be working correctly (it doesn't filter any comments)
    public void getVideoMetadataFilterLanguage() throws UnirestException, JsonProcessingException {
        Video video = holodexClient.getVideoMetadata("Puyd1d445IM", null,
                new Languages[]{Languages.EN, Languages.ES});
        assertTrue(video.getClips().size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(video));
    }

}
