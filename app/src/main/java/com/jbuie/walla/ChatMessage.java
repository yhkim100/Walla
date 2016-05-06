package com.jbuie.walla;

/**
 * Created by jonbuie on 4/2/16.
 * with Reference to https://github.com/firebase/FirebaseUI-Android/tree/master/codelabs/chat
 *
 */
public class ChatMessage {
    private String name;
    private String text;

    public ChatMessage() {
    }

    public ChatMessage(String user, String text) {
        this.name = user;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
