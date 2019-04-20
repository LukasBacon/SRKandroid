package com.lukas.srkandroid.activities.addCatch;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.lukas.srkandroid.MyRequestQueue;
import com.lukas.srkandroid.activities.addCatch.fragments.FormFragment;
import com.lukas.srkandroid.activities.addCatch.fragments.MapFragment;
import com.lukas.srkandroid.entities.Condition;
import com.lukas.srkandroid.entities.Fish;
import com.lukas.srkandroid.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddCatchController {

    private final String API_URL = "http://rybarskyklub.sk/rest";
    private final String LOCATION_INFO_URL = "http://ip-api.com/json";

    private FormFragment formFragment;
    private MapFragment mapFragment;

    public AddCatchController(FormFragment formFragment) {
        this.formFragment = formFragment;
    }

    public AddCatchController(MapFragment mapFragment) { this.mapFragment = mapFragment; }

    public void fetchUsers() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                API_URL + "/users/all",
                null,
                (JSONArray usersJsonArray) -> {
                    List<User> users = new ArrayList<>();
                    for (int i = 0; i < usersJsonArray.length(); i++) {
                        JSONObject userJson = null;
                        try {
                            userJson = usersJsonArray.getJSONObject(i);
                            users.add(new User().loadFromJsonObject(userJson));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    formFragment.setUsers(users);
                }, error -> {
            formFragment.handleFetchDataError();
                }
        );
        MyRequestQueue.getInstance(formFragment.getActivity()).addToRequestQueue(request);
    }

    public void fetchFishes() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                API_URL + "/fishes/all",
                null,
                (JSONArray fishesJsonArray) -> {
                    List<Fish> fishes = new ArrayList<>();
                    for (int i = 0; i < fishesJsonArray.length(); i++) {
                        JSONObject fishJson = null;
                        try {
                            fishJson = fishesJsonArray.getJSONObject(i);
                            fishes.add(new Fish().loadFromJsonObject(fishJson));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    formFragment.setFishes(fishes);
                }, error -> {
            formFragment.handleFetchDataError();
                }
        );
        MyRequestQueue.getInstance(formFragment.getActivity()).addToRequestQueue(request);
    }


    public void fetchConditions() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                API_URL + "/fishes/conditions",
                null,
                (JSONArray conditionsJsonArray) -> {
                    List<Condition> conditions = new ArrayList<>();
                    for (int i = 0; i < conditionsJsonArray.length(); i++) {
                        JSONObject conditionJson = null;
                        try {
                            conditionJson = conditionsJsonArray.getJSONObject(i);
                            conditions.add(new Condition().loadFromJsonObject(conditionJson));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    formFragment.setConditions(conditions);
                }, error -> {
            formFragment.handleFetchDataError();
                }
        );
        MyRequestQueue.getInstance(formFragment.getActivity()).addToRequestQueue(request);
    }

    public void getLocationInfo() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                LOCATION_INFO_URL,
                null,
                (JSONObject locationJson) -> {
                    LatLng position = null;
                    try {
                        position = new LatLng(locationJson.getDouble("lat"),
                                              locationJson.getDouble("lon"));
                        mapFragment.setMarkerOnPosition(position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    error.printStackTrace();
                }
        );
        MyRequestQueue.getInstance(mapFragment.getActivity()).addToRequestQueue(request);
    }


}
