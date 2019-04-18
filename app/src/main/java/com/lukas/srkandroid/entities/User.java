package com.lukas.srkandroid.entities;

import com.lukas.srkandroid.entities.interfaces.LoadableFromJSON;
import com.lukas.srkandroid.entities.interfaces.SelectBoxItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable, SelectBoxItem, LoadableFromJSON {

    private Integer id;
    private String nickname;
    private String firstName;
    private String lastName;
    private boolean admin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", admin=" + admin +
                '}';
    }

    @Override
    public String toSelectBoxLabel() {
        return firstName + " " + lastName;
    }

    @Override
    public User loadFromJsonObject(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getInt("id");
        nickname = jsonObject.getString("nickname");
        firstName = jsonObject.getString("firstName");
        lastName = jsonObject.getString("lastName");
        admin = jsonObject.getBoolean("admin");
        return this;
    }
}
