package com.yadren.worldcinema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);

    }

    public void signUpClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void signInClick(View view){
        String login = ((TextView)findViewById(R.id.editTextEmailIn)).getText().toString();
        String password = ((TextView)findViewById(R.id.editTextPasswordIn)).getText().toString();
        System.out.println(login + ":" + password);
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

        String url = "http://cinema.areas.su/auth/login";

        RequestQueue requestQueue = Volley.newRequestQueue(SignInActivity.this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> startActivity(new Intent(SignInActivity.this, MainScreenActivity.class)),
                error -> Toast.makeText(this, "Неверные данные авторизации", Toast.LENGTH_LONG).show());
        requestQueue.add(request);
    }
}