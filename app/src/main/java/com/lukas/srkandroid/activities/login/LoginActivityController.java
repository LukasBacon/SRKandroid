package com.lukas.srkandroid.activities.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lukas.srkandroid.API;
import com.lukas.srkandroid.MyRequestQueue;
import com.lukas.srkandroid.entities.User;

import org.json.JSONException;
import org.json.JSONObject;

class LoginActivityController {

    final String LOGIN_RESOURCE_URL = API.URL + "/users/auth";
    LoginActivity activity;

    public LoginActivityController(LoginActivity activity) {
        this.activity = activity;
    }

    public void login(String nickname, String password) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickname", nickname);
        jsonObject.put("password", password);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                LOGIN_RESOURCE_URL,
                jsonObject,
                response -> {
                    User user = null;
                    try {
                        user = new User().loadFromJsonObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    activity.handleSuccessfulLogin(user);
                },
                error -> {
                    Log.i("login", error.getMessage());
                    activity.handleUnSuccessfulLogin();
                    if (error.getMessage().contains("org.json.JSONException")) {
                        Toast.makeText(activity.getApplicationContext(), "Nesprávne prihlasovacie údaje.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(activity.getApplicationContext(), "Nastala chyba pri prihlásení.", Toast.LENGTH_LONG).show();
                    }
                });
        MyRequestQueue.getInstance(activity.getApplicationContext()).addToRequestQueue(request);
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isConnected = true;
        }
        return isConnected;
    }

    public boolean deviceContainsRememberedLogin() {
        return !getRememberedNickname().isEmpty() && !getRememberedPassword().isEmpty();
    }

    public void clearRememberedLogin() {
        setRememberedNickname("");
        setRememberedPassword("");
        setRememberLogin(false);
    }

    public String getRememberedNickname() {
        return activity.getPreferences(Context.MODE_PRIVATE).getString("nickname", "");
    }

    public void setRememberedNickname(String nickname) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("nickname", nickname);
        editor.apply();
    }

    public String getRememberedPassword() {
        return activity.getPreferences(Context.MODE_PRIVATE).getString("password", "");
    }

    public void setRememberedPassword(String password) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("password", password);
        editor.apply();
    }

    public boolean getRememberLogin() {
        return activity.getPreferences(Context.MODE_PRIVATE).getBoolean("remember", false);
    }

    public void setRememberLogin(boolean rememberLogin) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("remember", rememberLogin);
        editor.apply();
    }
}
