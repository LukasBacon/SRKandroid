package com.lukas.srkandroid.entities.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

public interface WritableToJSON {

    JSONObject toJSON() throws JSONException;

}
