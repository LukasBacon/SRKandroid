package com.lukas.srkandroid.activities.addCatch;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.lukas.srkandroid.API;
import com.lukas.srkandroid.MyRequestQueue;
import com.lukas.srkandroid.activities.addCatch.fragments.FormFragment;
import com.lukas.srkandroid.activities.addCatch.fragments.MapFragment;
import com.lukas.srkandroid.entities.Catch;
import com.lukas.srkandroid.entities.Condition;
import com.lukas.srkandroid.entities.Fish;
import com.lukas.srkandroid.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Part;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class AddCatchController {

    private final String API_URL = API.URL;
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

    public void addCatch(Catch catch0, Bitmap rightHeadPhotoBmp, Bitmap leftHeadPhotoBmp, Map<ImageView,Bitmap> optionalPhotos) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        okhttp3.Request request = null;
        try {
            request = new Builder()
                    .url(API_URL + "/catches/add")
                    .post(RequestBody.create(JSON, catch0.toJSON().toString()))
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            Integer catchId = Integer.valueOf(response.body().string());
            uploadFilesToCatch(catchId, rightHeadPhotoBmp, leftHeadPhotoBmp, optionalPhotos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadFilesToCatch(Integer catchId, Bitmap rightHeadPhotoBmp, Bitmap leftHeadPhotoBmp, Map<ImageView,Bitmap> optionalPhotos) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("catchid", String.valueOf(catchId))
                .addPart(createPart("lefthead", "leftheadphotofromandroid.jpeg", leftHeadPhotoBmp))
                .addPart(createPart("righthead", "rightheadphotofromandroid.jpeg", rightHeadPhotoBmp));
        int i = 0;
        for (Map.Entry<ImageView,Bitmap> optionalPhoto : optionalPhotos.entrySet()) {
            builder.addPart(createPart("other"+i, "otherphotofromandoid"+i+".jpeg", optionalPhoto.getValue()));
            i++;
        }
        RequestBody requestBody = builder.build();

        okhttp3.Request request = new Builder()
                .url(API_URL + "/photos/catches/add-photos-to-catch")
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            boolean status = Boolean.parseBoolean(response.body().string());
            if (status == false) {
                Toast.makeText(formFragment.getActivity(), "Odchyt sa nepodarilo pridať pridaný.", Toast.LENGTH_LONG).show();
                deleteCatchWithId(catchId);
                formFragment.showButton();
            }
            else {
                Toast.makeText(formFragment.getActivity(), "Odchyt úspešne pridaný.", Toast.LENGTH_LONG).show();
                formFragment.clear();
                formFragment.showButton();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Part createPart(String partname, String filename, Bitmap bitmap) {
        File file = new File(formFragment.getActivity().getCacheDir(), filename);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bitmapdata = bos.toByteArray();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(partname, filename, requestFile);
    }

    private void deleteCatchWithId(Integer catchId) {
        okhttp3.Request request = new Builder()
                .url(API_URL + "/catches/delete/" + catchId)
                .delete()
                .build();
        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
