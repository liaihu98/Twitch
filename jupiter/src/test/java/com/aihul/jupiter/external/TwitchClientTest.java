package com.aihul.jupiter.external;

import org.junit.jupiter.api.Test;

public class TwitchClientTest {
    private static final String TOKEN = "Bearer 0s1t0wli1dwz06ttlg9ad1oz2r89ah";
    private static final String CLIENT_ID = "0vxhjy1ht1topewqy0wob4t110bhsv";
    private static final String TOP_GAME_URL = "https://api.twitch.tv/helix/games/top?first=%s";
    private static final String GAME_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/games?name=%s";
    private static final int DEFAULT_GAME_LIMIT = 20;

    @Test
    public void testBuildGameUrl() {
        String url = new String("url");
        String gameName = new String("lol");
        int limit = 20;

        TwitchClient twitchClient = new TwitchClient();
    }
}
