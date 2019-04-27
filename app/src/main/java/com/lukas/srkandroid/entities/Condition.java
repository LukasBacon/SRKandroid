package com.lukas.srkandroid.entities;

import com.lukas.srkandroid.entities.interfaces.LoadableFromJSON;
import com.lukas.srkandroid.entities.interfaces.SelectBoxItem;
import com.lukas.srkandroid.entities.interfaces.WritableToJSON;

import org.json.JSONException;
import org.json.JSONObject;

public class Condition implements SelectBoxItem, LoadableFromJSON, WritableToJSON {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public String toSelectBoxLabel() {
        return name;
    }

    @Override
    public Condition loadFromJsonObject(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getInt("id");
        name = jsonObject.getString("name");
        return this;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        return jsonObject;
    }
}
