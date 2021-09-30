package com.aihul.jupiter.external;

import com.aihul.jupiter.entity.Game;
import com.aihul.jupiter.entity.Item;
import com.aihul.jupiter.entity.ItemType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class TwitchClient {
    private static final String TOKEN = "Bearer ";
    private static final String CLIENT_ID = "";
    private static final String TOP_GAME_URL = "https://api.twitch.tv/helix/games/top?first=%s";
    private static final String GAME_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/games?name=%s";
    private static final int DEFAULT_GAME_LIMIT = 20;

    private static final String STREAM_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/streams?game_id=%s&first=%s";
    private static final String VIDEO_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/videos?game_id=%s&first=%s";
    private static final String CLIP_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/clips?game_id=%s&first=%s";
    private static final String TWITCH_BASE_URL = "https://www.twitch.tv/";
    private static final int DEFAULT_SEARCH_LIMIT = 20;

    /**
     * A private helper function to concatenate url and parameters(gameName or limit)
     *
     * @param url      An original url
     * @param gameName GameName if exits
     * @param limit    the Number of top games to display
     * @return A complete url for searching
     */
    private String buildGameURL(String url, String gameName, int limit) {
        // TOP_GAME_URL
        if (gameName.equals("")) {
            return String.format(url, limit);
        } else {
            try {
                gameName = URLEncoder.encode(gameName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return String.format(url, gameName);
        }
    }

    /**
     * search twitch games or gameNames by url
     *
     * @param url The complete url for search
     * @return The string representation of {@link org.json.JSONArray} if it fetches data correctly
     * @throws TwitchException it appears if there is something wrong in acquiring twitch data
     */
    private String searchTwitch(String url) throws TwitchException {
        // build a client first
        CloseableHttpClient client = HttpClients.createDefault();

        ResponseHandler<String> responseHandler = httpResponse -> {
            // check the status
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            if (responseCode < 200 || responseCode >= 300) {
                System.out.println("Response status: " + httpResponse.getStatusLine().getReasonPhrase());
                throw new TwitchException("Fail to fetch data from Twitch API");
            }
            // check the entity
            HttpEntity entity = httpResponse.getEntity();
            if (entity == null) {
                throw new TwitchException("Failed to get result from Twitch API");
            }
            // covert response to jsonObject
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));
            return jsonObject.getJSONArray("data").toString();
        };

        try {
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", TOKEN);
            request.setHeader("Client-Id", CLIENT_ID);
            return client.execute(request, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
            throw new TwitchException("Failed to connect twitch website");
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * convert data sting to the List of Game
     *
     * @param data the string of representation of {@link org.json.JSONArray}
     * @return A list of {@link Game object}
     * @throws TwitchException
     */
    private List<Game> getGameList(String data) throws TwitchException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Arrays.asList(mapper.readValue(data, Game[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new TwitchException("Failed to parse game data from Twitch API");
        }
    }

    public List<Game> topGames(int limit) throws TwitchException {
        // check the validity of limit
        if (limit <= 0) {
            limit = DEFAULT_GAME_LIMIT;
        }
        return getGameList(searchTwitch(buildGameURL(TOP_GAME_URL, "", limit)));
    }

    public Game searchGame(String gameName) throws TwitchException {
        List<Game> list = getGameList(searchTwitch(buildGameURL(GAME_SEARCH_URL_TEMPLATE, gameName, 0)));
        return list.get(0) != null ? list.get(0) : null;
    }

    //---------------------------- for stream, clip and video type information---------------------------------------
    private String buildSearchURL(String url, String gameId, int limit) {
        try {
            gameId = URLEncoder.encode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format(url, gameId, limit);
    }

    private List<Item> getItemList(String data) throws TwitchException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Arrays.asList(mapper.readValue(data, Item[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new TwitchException("Failed to parse item data from Twitch API");
        }
    }

    public List<Item> searchByType(String gameId, ItemType itemType) {
        List<Item> items = Collections.EMPTY_LIST;
        switch (itemType) {
            case STREAM:
                items = getItemList(searchTwitch(buildSearchURL(STREAM_SEARCH_URL_TEMPLATE, gameId, DEFAULT_SEARCH_LIMIT)));
                break;
            case CLIP:
                items = getItemList(searchTwitch(buildSearchURL(CLIP_SEARCH_URL_TEMPLATE, gameId, DEFAULT_SEARCH_LIMIT)));
                break;
            case VIDEO:
                items = getItemList(searchTwitch(buildSearchURL(VIDEO_SEARCH_URL_TEMPLATE, gameId, DEFAULT_SEARCH_LIMIT)));
                break;
        }
        for (Item item : items) {
            if (itemType.equals(ItemType.STREAM)) {
                item.setUrl(TWITCH_BASE_URL + item.getBroadcasterName());
            }
            item.setType(itemType);
            item.setGameId(gameId);
        }
        return items;
    }

    public Map<String, List<Item>> searchItems(String gameId) throws TwitchException {
        Map<String, List<Item>> itemMap = new HashMap<>();
        for (ItemType type : ItemType.values()) {
            itemMap.put(type.toString(), searchByType(gameId, type));
        }
        return itemMap;
    }
}
