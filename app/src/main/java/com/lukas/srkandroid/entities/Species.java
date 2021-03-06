package com.lukas.srkandroid.entities;

import com.lukas.srkandroid.entities.interfaces.LoadableFromJSON;
import com.lukas.srkandroid.entities.interfaces.WritableToJSON;

import org.json.JSONException;
import org.json.JSONObject;

public class Species implements LoadableFromJSON, WritableToJSON {

    private Integer id;
    private String nameSk;
    private String nameLat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameSk() {
        return nameSk;
    }

    public void setNameSk(String nameSk) {
        this.nameSk = nameSk;
    }

    public String getNameLat() {
        return nameLat;
    }

    public void setNameLat(String nameLat) {
        this.nameLat = nameLat;
    }

    @Override
    public String toString() {
        return "Species{" +
                "id=" + id +
                ", nameSk='" + nameSk + '\'' +
                ", nameLat='" + nameLat + '\'' +
                '}';
    }

    @Override
    public Species loadFromJsonObject(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getInt("id");
        nameSk = jsonObject.getString("nameSk");
        nameLat = jsonObject.getString("nameLat");
        return this;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("nameSk", nameSk);
        jsonObject.put("nameLat", nameLat);
        return jsonObject;
    }
}
