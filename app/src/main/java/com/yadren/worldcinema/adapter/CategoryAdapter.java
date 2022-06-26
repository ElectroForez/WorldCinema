package com.yadren.worldcinema.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.yadren.worldcinema.MovieActivity;
import com.yadren.worldcinema.R;
import com.yadren.worldcinema.common.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CollectionInTrendHolder> {
    Context context;
    JSONArray movies;
    RequestQueue requestQueue;

    HashMap<Integer, Bitmap> cacheImage = new HashMap<>();

    int itemLayoutId, imageLayoutId;

    public CategoryAdapter(Context context, JSONArray movies, int itemLayoutId, int imageLayoutId) {
        this.context = context;
        this.movies = movies;
        this.itemLayoutId = itemLayoutId;
        this.imageLayoutId = imageLayoutId;
        requestQueue = Volley.newRequestQueue(context);
        System.out.println("Start adapter: " + itemLayoutId + " " + imageLayoutId);
    }

    @NonNull
    @Override
    public CollectionInTrendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(itemLayoutId, parent, false);
        return new CollectionInTrendHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CollectionInTrendHolder holder, int position) {
        try {
            JSONObject movie = movies.getJSONObject(position);
            String poster = movie.getString("poster");
            int movieId = movie.getInt("movieId");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MovieActivity.class);
                    intent.putExtra("movieId", movieId);
                    context.startActivity(intent);
                }
            });

            if (cacheImage.containsKey(movieId)) {
                holder.collectionItemImage.setImageBitmap(cacheImage.get(movieId));
                holder.collectionItemImage.setVisibility(View.VISIBLE);
                return;
            }

            holder.collectionItemImage.setVisibility(View.INVISIBLE);

            String url = API.hostnameURL + "/up/images/" + poster;

            System.out.println("Downloading " + url);
            System.out.println("item " + itemLayoutId);
            System.out.println("image " + imageLayoutId);
            ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    holder.collectionItemImage.setImageBitmap(response);
                    holder.collectionItemImage.setVisibility(View.VISIBLE);
                    cacheImage.put(movieId, response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                }
            });
            requestQueue.add(imageRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return movies.length();
    }

    public final class CollectionInTrendHolder extends RecyclerView.ViewHolder {
        ImageView collectionItemImage;

        public CollectionInTrendHolder(@NonNull View itemView) {
            super(itemView);
            collectionItemImage = itemView.findViewById(imageLayoutId);
        }
    }
}
