package com.yadren.worldcinema;

import static android.media.CamcorderProfile.get;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.Normalizer2;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yadren.worldcinema.adapter.CategoryAdapter;
import com.yadren.worldcinema.common.API;

import org.json.JSONException;
import org.json.JSONObject;

public class MainScreenActivity extends AppCompatActivity {
    ImageView coverImage;
    Button watchCoverButton;
    RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        requestQueue = Volley.newRequestQueue(this);

        coverImage = findViewById(R.id.coverImage);
        watchCoverButton = findViewById(R.id.watchCoverButton);

        coverImage.setVisibility(View.INVISIBLE);
        watchCoverButton.setVisibility(View.INVISIBLE);

        try {
            setForegroundImage();
            setCategoryAdapter("new");
            setCategoryAdapter("forMe");
            setCategoryAdapter("inTrend");
        } catch (AuthFailureError e) {
            e.printStackTrace();
        }

    }

    public void setCoverImageByName(String name) {
        String url = API.hostnameURL + "/up/images/" + name;

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                coverImage.setImageBitmap(response);
                coverImage.setVisibility(View.VISIBLE);
                watchCoverButton.setVisibility(View.VISIBLE);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(imageRequest);
    }

    public void setForegroundImage() throws AuthFailureError {
        String url = API.hostnameURL + "/movies/cover";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                response -> {
                    try {
                        String foregroundImageName = response.getString("foregroundImage");
                        int movieId = response.getInt("movieId");

                        watchCoverButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MainScreenActivity.this, MovieActivity.class);
                                intent.putExtra("movieId", movieId);
                                startActivity(intent);
                            }
                        });

                        setCoverImageByName(foregroundImageName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        requestQueue.add(request);
    }

    public void setCategoryAdapter(String category) {
        int itemLayoutId, imageLayoutId;
        RecyclerView categoryRecycler;

        switch (category) {
            case "inTrend":
                itemLayoutId = R.layout.in_trend_item;
                imageLayoutId = R.id.inTrendImage;
                categoryRecycler = findViewById(R.id.inTrendRecycler);
                break;
            case "new":
                itemLayoutId = R.layout.new_item;
                imageLayoutId = R.id.newImage;
                categoryRecycler = findViewById(R.id.newRecycler);
                break;
            case "forMe":
                itemLayoutId = R.layout.forme_item;
                imageLayoutId = R.id.forMeImage;
                categoryRecycler = findViewById(R.id.forMeRecycler);
                break;
            default:
                System.out.println("Invalid category name");
                return;
        }

        String url = API.hostnameURL + "/movies?filter=" + category;
        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                            LinearLayoutManager.HORIZONTAL,
                            false);

                    CategoryAdapter adapter = new CategoryAdapter(this,
                                response,
                            itemLayoutId,
                            imageLayoutId
                    );

                    categoryRecycler.setLayoutManager(layoutManager);
                    categoryRecycler.setAdapter(adapter);

                },
                error -> {
                    error.printStackTrace();
                }
        );
        requestQueue.add(request);
    }

    @SuppressLint("NonConstantResourceId")
    public void tabButtonClick(View view) {
        ImageView mainButton, podborkaButton,
                starButton, userButton;
        mainButton = findViewById(R.id.mainButton);
        podborkaButton = findViewById(R.id.podborkaButton);
        starButton = findViewById(R.id.starButton);
        userButton = findViewById(R.id.userButton);
        ImageView[] buttons = {mainButton, podborkaButton, starButton, userButton};

        for (ImageView button: buttons) {
            button.setColorFilter(ContextCompat.getColor(this, R.color.unactiveTab));
        }

        switch (view.getId()) {
            case R.id.mainButton:
                mainButton.setColorFilter(ContextCompat.getColor(this, R.color.mainColor));
                break;
            case R.id.podborkaButton:
                podborkaButton.setColorFilter(ContextCompat.getColor(this, R.color.mainColor));
                break;
            case R.id.starButton:
                starButton.setColorFilter(ContextCompat.getColor(this, R.color.mainColor));
                break;
            case R.id.userButton:
                userButton.setColorFilter(ContextCompat.getColor(this, R.color.mainColor));
                break;
        }
    }
}
