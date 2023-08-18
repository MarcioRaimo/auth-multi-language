package com.example.javajavafx;

import org.json.JSONObject;

import java.text.MessageFormat;

public class User {
    public String email;
    public String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static User fromJson(JSONObject obj) {
        return new User(obj.getString("email"), obj.getString("password"));
    }

    public static String toJson(User self) {
        return String.format("{\"email\": \"%s\", \"password\": \"%s\"}", self.email, self.password);
    }
}
