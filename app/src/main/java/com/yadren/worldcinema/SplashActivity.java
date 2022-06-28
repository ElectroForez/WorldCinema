package com.yadren.worldcinema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yadren.worldcinema.common.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String dataFilePath = getCacheDir().toString() + "/data.txt";
                File dataFile = new File(dataFilePath);

                if (!dataFile.exists()) {
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

                try {
                    FileReader fileReader = new FileReader(dataFile);
                    Scanner scanner = new Scanner(fileReader);

                    String login = null;
                    String password = null;
                    if (scanner.hasNext()) {
                        login = scanner.nextLine().trim();
                        password = scanner.nextLine().trim();
                    }

                    JSONObject json = new JSONObject();
                    json.put("email", login);
                    json.put("password", password);

                    String url = API.hostnameURL + "/auth/login";

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                API.token = response.getString("token");
                                Intent intent = new Intent(SplashActivity.this, MainScreenActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, error -> {
                        error.printStackTrace();

                        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    });
                    requestQueue.add(request);

                } catch (FileNotFoundException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 2000);
    }
}