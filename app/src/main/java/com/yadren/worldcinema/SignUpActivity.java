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

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
    }


    public void signUpClick(View view){
        String firstName = ((TextView) findViewById(R.id.nameUp)).getText().toString();
        String lastName = ((TextView) findViewById(R.id.surnameUp)).getText().toString();
        String email = ((TextView) findViewById(R.id.emailUp)).getText().toString();
        String password = ((TextView) findViewById(R.id.passwordUp)).getText().toString();
        String repeatPassword = ((TextView) findViewById(R.id.repeatPasswordUp)).getText().toString();

        String[] fields = {firstName, lastName, email, password, repeatPassword};

        for (String fieldText: fields) {
            if (fieldText.equals("")) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show();
                return;
            }
        }

        if (password.equals(repeatPassword)) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_LONG).show();
            return;
        }

        if (Pattern.matches("[a-zA-Z0-9]@[a-zA-Z0-9][.][a-z]{1,3}", password)) {
            Toast.makeText(this, "Неверный формат почты", Toast.LENGTH_LONG).show();
            return;
        }


        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
            json.put("firstName", firstName);
            json.put("lastName", lastName);
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