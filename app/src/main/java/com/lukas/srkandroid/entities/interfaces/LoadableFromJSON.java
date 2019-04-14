package com.lukas.srkandroid.entities.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

public interface LoadableFromJSON {

    LoadableFromJSON loadFromJsonObject(JSONObject jsonObject) throws JSONException;

}
