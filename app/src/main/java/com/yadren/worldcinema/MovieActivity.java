package com.yadren.worldcinema;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yadren.worldcinema.adapter.FramesAdapter;
import com.yadren.worldcinema.common.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    ImageView mainMoviePicture;
    TextView movieTitle, movieDescription, ageConstraint;
    RecyclerView framesRecycler;
    int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        requestQueue = Volley.newRequestQueue(this);
        movieId = getIntent().getIntExtra("movieId", 0);

        mainMoviePicture = findViewById(R.id.mainMoviePicture);
        mainMoviePicture.setVisibility(View.INVISIBLE);

        movieTitle = findViewById(R.id.movieTitle);
        movieDescription = findViewById(R.id.movieDescription);
        ageConstraint = findViewById(R.id.ageConstraint);

        setTextFieldsVisibility(View.INVISIBLE);
        downloadPage();
    }

    public void backButtonClick(View view) {
        super.onBackPressed();
        finish();
    }

    private void setTextFieldsVisibility(int visibility) {
        movieTitle.setVisibility(visibility);
        movieDescription.setVisibility(visibility);
        ageConstraint.setVisibility(visibility);
    }

    private void setFramesAdapter(List<String> framesName) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        framesRecycler = findViewById(R.id.framesRecycler);

        FramesAdapter framesAdapter = new FramesAdapter(this, framesName);
        framesRecycler.setLayoutManager(layoutManager);
        framesRecycler.setAdapter(framesAdapter);


    }

    private void downloadPage() {
        String url = API.hostnameURL + "/movies/" + movieId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                response -> {
                    try {
                        String movieTitleValue = response.getString("name");
                        String movieDescriptionValue = response.getString("description");
                        movieTitle.setText(movieTitleValue);
                        movieDescription.setText(movieDescriptionValue);

                        String age = response.getString("age");
                        setAge(age);

                        setTextFieldsVisibility(View.VISIBLE);

                        List<String> framesName = new ArrayList<>();

                        JSONArray framesJSONArray = response.getJSONArray("images");
                        for (int i = 0; i < framesJSONArray.length(); i++) {
                            String frameName = (String) framesJSONArray.get(i);
                            framesName.add(frameName);
                        }
                        setFramesAdapter(framesName);

                        String posterName = response.getString("poster");
                        setPoster(posterName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                }
        );
        requestQueue.add(request);
    }

    private void setAge(String age) {
        ageConstraint.setText(age + "+");

        int color = getResources().getIdentifier("age" + age + "plus", "color", getPackageName());
        int colorId = ContextCompat.getColor(this, color);
        ageConstraint.setTextColor(colorId);
    }


    private void setPoster(String posterName) {
        String url = API.hostnameURL + "/up/images/" + posterName;
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                mainMoviePicture.setImageBitmap(response);
                mainMoviePicture.setVisibility(View.VISIBLE);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                error -> {
                    error.printStackTrace();
                });

        requestQueue.add(request);
    }
}