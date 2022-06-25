package com.yadren.worldcinema;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.yadren.worldcinema.common.API;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
    }


    public void signInClick(View view){
        String login = ((TextView)findViewById(R.id.editTextEmailIn)).getText().toString();
        String password = ((TextView)findViewById(R.id.editTextPasswordIn)).getText().toString();
        if (login.equals("") || password.equals("")) {
            Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_LONG).show();
            return;
        }


        JSONObject json = new JSONObject();
        try {
            json.put("email", login);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = API.hostnameURL + "/auth/login";

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> {
                    try {
                        requestOnResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> requestOnError(error)
        );
        requestQueue.add(request);


    }

    private void requestOnResponse(JSONObject response) throws JSONException {
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
        finishActivity(0);
    }

    private void requestOnError(VolleyError error) {
        Toast.makeText(this, "Неверные данные регистрации", Toast.LENGTH_SHORT).show();
    }


}