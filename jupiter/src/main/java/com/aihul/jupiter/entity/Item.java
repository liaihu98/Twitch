package com.aihul.jupiter.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item {
    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("broadcaster_name")
    @JsonAlias({ "user_name" })
    private String broadcasterName;

    @JsonProperty("url")
    private String url;

    @JsonProperty("game_id")
    private String gameId;

    @JsonProperty("item_type")
    private ItemType type;

    public String getBroadcasterName() {
        return broadcasterName;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
