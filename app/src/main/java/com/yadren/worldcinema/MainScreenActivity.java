package com.yadren.worldcinema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
        setForegroundImage();

    }

    public void setCoverImageByName(String name) {
        String url = API.hostnameURL + "/up/images/" + name;

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Toast.makeText(MainScreenActivity.this, "Установлено", Toast.LENGTH_SHORT);
                coverImage.setImageBitmap(response);
                coverImage.setVisibility(View.VISIBLE);
                watchCoverButton.setVisibility(View.VISIBLE);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainScreenActivity.this, "Бебра", Toast.LENGTH_SHORT);
                error.printStackTrace();
            }
        });
        requestQueue.add(imageRequest);
    }

    public void setForegroundImage() {
        String url = API.hostnameURL + "/movies/cover";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                response -> {
                    try {
                        System.out.println(url);
                        String foregroundImageName = response.getString("foregroundImage");
                        setCoverImageByName(foregroundImageName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Чёт не получилось", Toast.LENGTH_SHORT);
                });
        requestQueue.add(request);
    }
}
