package com.lukas.srkandroid.activities.addCatch;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.lukas.srkandroid.MyRequestQueue;
import com.lukas.srkandroid.activities.addCatch.fragments.FormFragment;
import com.lukas.srkandroid.entities.Condition;
import com.lukas.srkandroid.entities.Fish;
import com.lukas.srkandroid.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddCatchController {

    private static final String API_URL = "http://rybarskyklub.sk/rest";

    FormFragment view;

    public AddCatchController(FormFragment view) {
        this.view = view;
    }

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
                    view.setUsers(users);
                }, error -> {
                    view.handleFetchDataError();
                }
        );
        MyRequestQueue.getInstance(view.getActivity()).addToRequestQueue(request);
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
                    view.setFishes(fishes);
                }, error -> {
                    view.handleFetchDataError();
                }
        );
        MyRequestQueue.getInstance(view.getActivity()).addToRequestQueue(request);
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
                    view.setConditions(conditions);
                }, error -> {
                    view.handleFetchDataError();
                }
        );
        MyRequestQueue.getInstance(view.getActivity()).addToRequestQueue(request);
    }


}
