package com.yadren.worldcinema.adapter;

import android.content.Context;
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
import com.yadren.worldcinema.R;
import com.yadren.worldcinema.common.API;

import java.util.HashMap;
import java.util.List;

public class FramesAdapter extends RecyclerView.Adapter<FramesAdapter.FramesHolder> {
    Context context;
    List<String> frames;
    RequestQueue requestQueue;
    HashMap<String, Bitmap> cacheImage = new HashMap<>();

    public FramesAdapter(Context context, List<String> frames) {
        this.context = context;
        this.frames = frames;
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public FramesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.frames_item, parent, false);
        return new FramesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FramesHolder holder, int position) {
        String frameName = frames.get(position);

        if (cacheImage.containsKey(frameName)) {
            holder.frameImage.setImageBitmap(cacheImage.get(frameName));
            holder.frameImage.setVisibility(View.VISIBLE);
            return;
        }

        holder.frameImage.setVisibility(View.INVISIBLE);

        String url = API.hostnameURL + "/up/images/" + frameName;
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.frameImage.setImageBitmap(response);
                holder.frameImage.setVisibility(View.VISIBLE);
                cacheImage.put(frameName, response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });
        requestQueue.add(imageRequest);

    }

    @Override
    public int getItemCount() {
        return frames.size();
    }

    public final class FramesHolder extends RecyclerView.ViewHolder {
        ImageView frameImage;

        public FramesHolder(@NonNull View itemView) {
            super(itemView);
            frameImage = itemView.findViewById(R.id.frameImage);
        }
    }
}
