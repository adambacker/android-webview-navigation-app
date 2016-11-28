package com.develop.jamesd.keepitosher_dev;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity{

    EditText etUsername;
    EditText etPassword;
    TextView tvForgot;
    String userName="";
    String password="";
    String androidid = "";
    Button loginBtn,registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        TelephonyManager TM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String imeiNo = TM.getDeviceId();
        androidid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        Toast.makeText(getApplicationContext(), androidid, Toast.LENGTH_LONG).show();


        String url = Constants.backendURL + Constants.loginURL;

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

//        etUsername.setText("keepitkosher");
//        etPassword.setText("keepitkosher");

        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        tvForgot = (TextView) findViewById(R.id.tvForgotPass);


        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (userName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Error! Name empty.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Error! Password empty.", Toast.LENGTH_LONG).show();
                    return;
                }


                ////Post loging data to backend

                String url = Constants.backendURL;

                JSONObject json = new JSONObject();
                JSONObject type = new JSONObject();
                JSONObject requestInfo = new JSONObject();
                try {
                    type.put("type", "userLogin");
                    requestInfo.put("username", userName);
                    requestInfo.put("password", password);
                    requestInfo.put("device_token", androidid);
                    requestInfo.put("version_kikapp", "1.0");
                    json.put("request", type);
                    json.put("requestinfo", requestInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject response_json = response.getJSONObject("response");
                            String code = response_json.getString("code");
                            String message = response_json.getString("message");
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            if (!code.equals("200")) {
                                return;
                            }
                            finish();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("response", response.toString());
                            startActivity(intent);
//                            Intent intent = new Intent(LoginActivity.this, CustomActivity.class);
//                            intent.putExtra("response", response.toString());
//                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                BackendApplication.getsInstance().getRequestQueue().add(jsonObjectRequest);

//                final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            Toast.makeText(getApplicationContext(), "res " + response, Toast.LENGTH_LONG).show();
//                            String code = jsonObject.getString("code");
//                            if (!code.equals("200")) {
//                                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
//                                String message = jsonObject1.getString("message");
//                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
//                                return;
//                            }
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            intent.putExtra("response", response);
//                            startActivity(intent);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        NetworkResponse response = error.networkResponse;
//                        Toast.makeText(getApplicationContext(), "error : " + response.statusCode, Toast.LENGTH_LONG).show();
//                    }
//                }) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String,String> params = new HashMap<String, String>();
//                        params.put(Constants.username, userName);
//                        params.put(Constants.password, password);
//
//                        //  params.put(KEY_EMAIL, email);
//                        return params;
//                        //  return super.getParams();
//                    }
//
//                };
//                BackendApplication.getsInstance().getRequestQueue().add(request);

            }
        });
    }
}
