package org.example.dto;

public class Action {


    String action;
    String what;
    String bot_id;

    public Action(String action, String what, String bot_id) {
        this.action = action;
        this.what = what;
        this.bot_id = bot_id;
    }

    @Override
    public String toString() {
        return "{" + "\"action\":\"" + action + "\", \"what\":\"" + what + "\", \"bot_id\":\"" + bot_id  + "\"}";
    }
}
