package org.example.dto;

public class Move {

    String hop;
    int speed = 1;
    String bot_id;

    public Move(String hop, int speed, String bot_id) {
        this.hop = hop;
        this.speed = speed;
        this.bot_id = bot_id;
    }

    @Override
    public String toString() {
        return "{" + "\"hop\":\"" + hop + '\"' + ", \"speed\":" + speed + ", \"bot_id\":\"" + bot_id + '\"' + "}";
    }
}
