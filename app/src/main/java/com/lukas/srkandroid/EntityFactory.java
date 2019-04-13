package com.lukas.srkandroid;

import com.lukas.srkandroid.entities.User;

import org.json.JSONException;
import org.json.JSONObject;

public class EntityFactory {

    public static User createUserObjectFromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            user.setId(jsonObject.getInt("id"));
            user.setNickname(jsonObject.getString("nickname"));
            user.setFirstName(jsonObject.getString("firstName"));
            user.setLastName(jsonObject.getString("lastName"));
            user.setAdmin(jsonObject.getBoolean("admin"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
