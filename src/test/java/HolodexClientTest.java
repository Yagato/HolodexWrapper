import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import constants.ChannelType;
import constants.Languages;
import constants.Order;
import constants.Organizations;
import io.github.cdimascio.dotenv.Dotenv;
import model.Channel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    public void listChannelsTest() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, null,  null,null, null,null);
        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    public void listChannelsVtuberTypeTest() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, null, null, null,null, ChannelType.VTUBER);
        assertEquals(channels.get(0).getType(), "vtuber");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    public void listChannelsSubberTypeTest() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, null, null, null,null, ChannelType.SUBBER);
        assertEquals(channels.get(0).getType(), "subber");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    public void listChannelsByLanguage() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(Languages.ES, null, null,null, null, null, ChannelType.SUBBER);
        assertEquals(channels.get(0).getName(), "F F Traducciones");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    public void listChannelsLimit() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, 5, null, null, null, null,null);
        assertEquals(5, channels.size());
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    public void listChannelsOffset() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, 6, null, null, null, null);
        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    public void listChannelsOrderAsc() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, null, Order.ASC, null, null, null);
        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    public void listChannelsOrderDesc() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, null, Order.DESC, null, null, null);
        assertTrue(channels.size() > 1);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    public void listChannelsByOrg() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, null, null, Organizations.HOLOLIVE, null, null);
        assertEquals(channels.get(0).getOrg(), "Hololive");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

    @Test
    public void listChannelsSort() throws UnirestException, JsonProcessingException {
        List<Channel> channels = holodexClient.listChannels(null, null, null, null, null, "subscriber_count", null);

        int channelsIndexZeroSubscriberCount = Integer.parseInt(channels.get(0).getSubscriberCount());
        int channelsIndexOneSubscriberCount = Integer.parseInt(channels.get(1).getSubscriberCount());

        assertTrue(channelsIndexZeroSubscriberCount <= channelsIndexOneSubscriberCount);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(channels));
    }

}
