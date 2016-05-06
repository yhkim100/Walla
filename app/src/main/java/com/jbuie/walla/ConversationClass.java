package com.jbuie.walla;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.Firebase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jonbuie on 4/9/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationClass implements Serializable {

    public ConversationClass(){
    }

    private String user1;
    private String user2;
    public String uniqueID;


    public ConversationClass(String user1, String user2, String uniqueID) {
        String useRef;
        this.user1 = user1;
        this.user2 = user2;
        //this.chatRef = chatRef+"testing";
        this.uniqueID = uniqueID;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {return user2;}

    public String getUniqueID() {return uniqueID;}

    // Right now just using the time becuase you can't have an email address as a URL name
    // example@example.com? Come on SON! do better

    private String generateRefId(){
        return getTime();
    }

    private String getTime(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }



}
