package com.lukas.srkandroid.activities.login;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lukas.srkandroid.R;
import com.lukas.srkandroid.activities.addCatch.AddCatch;
import com.lukas.srkandroid.entities.User;

import org.json.JSONException;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityController controller;
    private Timer connectionChecher;

    private EditText nicknameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private RadioButton connectionIndicator;
    private CheckBox rememberLoginCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new LoginActivityController(this);
        connectionChecher = new Timer();
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        nicknameEditText = findViewById(R.id.nickname);
        passwordEditText = findViewById(R.id.password);

        nicknameEditText.setText("test");
        passwordEditText.setText("123456");

        loginButton = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.login_progress);
        progressBar.setVisibility(View.INVISIBLE);
        connectionIndicator = findViewById(R.id.conn_indicator);
        rememberLoginCheckBox = findViewById(R.id.remember_login);

        initializeConnectionChecker();
        fillFormWithRememberedValues();
        loginButton.setOnClickListener(e -> login());
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    public void handleSuccessfulLogin(User user) {
        progressBar.setVisibility(View.INVISIBLE);
        if (rememberLoginCheckBox.isChecked()) {
            controller.setRememberedNickname(nicknameEditText.getText().toString());
            controller.setRememberedPassword(passwordEditText.getText().toString());
            controller.setRememberLogin(true);
        }
        else {
            controller.clearRememberedLogin();
        }
        Toast.makeText(this, "Boli ste úspešne prihlásený.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, AddCatch.class);
        intent.putExtra("user", (Serializable) user);
        startActivity(intent);
    }

    public void handleUnSuccessfulLogin() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void login() {
        if (controller.checkNetworkConnection() == false) {
            Toast.makeText(this, "Žiadne internetové pripojenie.", Toast.LENGTH_LONG).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        try {
            controller.login(nicknameEditText.getText().toString(), passwordEditText.getText().toString());
        } catch (JSONException e) {
            handleUnSuccessfulLogin();
            e.printStackTrace();
        }
    }

    private void initializeConnectionChecker() {
        connectionChecher.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        connectionIndicator.setChecked(controller.checkNetworkConnection());
                    }
                }, 0, 1000
        );
    }

    private void fillFormWithRememberedValues() {
        if (controller.deviceContainsRememberedLogin()){
            nicknameEditText.setText(controller.getRememberedNickname());
            passwordEditText.setText(controller.getRememberedPassword());
            rememberLoginCheckBox.setChecked(controller.getRememberLogin());
        }
    }

}
