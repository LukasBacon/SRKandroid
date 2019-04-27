package com.lukas.srkandroid.entities;

import com.lukas.srkandroid.entities.interfaces.WritableToJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Coords implements Serializable, WritableToJSON {

    private Integer id;
    private Double lat;
    private Double lng;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Coords{" +
                "id=" + id +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("lat", lat);
        jsonObject.put("lng", lng);
        return jsonObject;
    }
}
