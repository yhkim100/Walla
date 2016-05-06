package com.jbuie.walla;

/**
 * Created by petermurphy on 4/13/16.
 */
public class User {
    private String nickname;
    private String email;
    private String phoneNumber;
    private String karma;

    public User() {
    }

    public User (String nickname_, String email_, String phone_number_, String karma_){
        this.nickname = nickname_;
        this.phoneNumber = phone_number_;
        this.karma = karma_;
        String tmpString = email_.replaceAll("@", "AT");
        String tmpString2 = tmpString.replaceAll("\\.", "DOT");
        this.email = tmpString2;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getKarma() {
        return karma;
    }


}


