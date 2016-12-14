package com.linagas.android.gastest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hamze sarsour local on 12/1/2016.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{








    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    private String username;
    private String password;

    private String shared_username= "";
    private String shared_password = "";

    private Intent main,login;

    Config config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        config = new Config();

        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);

        buttonLogin = (Button) findViewById(R.id.btnLogin);



        buttonLogin.setOnClickListener(this);


        load_saved_preferences();

        if (shared_username != null && shared_password != null)
        {
            login = new Intent(LoginActivity.this,MapsActivity.class);
            startActivity(login);
            finish();
        }
        else
        {

        }


    }


    private void userLogin() {
        username = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("User verified")){

                            openMap();

                        }else{

                            Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(config.KEY_USERNAME,username);
                map.put(config.KEY_PASSWORD,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void openMap(){


        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_user_username), editTextUsername.getText().toString());
        editor.putString(getString(R.string.saved_user_password), editTextPassword.getText().toString());

        editor.commit();

        main = new Intent(LoginActivity.this , MapsActivity.class);
        startActivity(main);
        finish();
    }

    @Override
    public void onClick(View v) {
        userLogin();
    }


    public void load_saved_preferences(){

        SharedPreferences pref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        shared_username = pref.getString(getString(R.string.saved_user_username), null);
        shared_password = pref.getString(getString(R.string.saved_user_password), null);


    }
}
