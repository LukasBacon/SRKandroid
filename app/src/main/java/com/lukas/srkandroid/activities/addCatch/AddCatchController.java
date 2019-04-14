package com.lukas.srkandroid.activities.addCatch;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.lukas.srkandroid.MyRequestQueue;
import com.lukas.srkandroid.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddCatchController {

    final String API_URL = "http://rybarskyklub.sk/rest";

    AddCatchFormView view;

    public AddCatchController(AddCatchFormView view) {
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
        MyRequestQueue.getInstance(view.getActivity().getApplicationContext()).addToRequestQueue(request);
    }
}
