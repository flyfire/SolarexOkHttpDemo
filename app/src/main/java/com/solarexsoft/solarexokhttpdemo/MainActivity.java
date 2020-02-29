package com.solarexsoft.solarexokhttpdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.solarexsoft.solarexokhttp.Call;
import com.solarexsoft.solarexokhttp.Callback;
import com.solarexsoft.solarexokhttp.Request;
import com.solarexsoft.solarexokhttp.RequestBody;
import com.solarexsoft.solarexokhttp.Response;
import com.solarexsoft.solarexokhttp.SolarexHttpClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    SolarexHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_get).setOnClickListener(this);
        findViewById(R.id.btn_post).setOnClickListener(this);
        client = new SolarexHttpClient.Builder().build();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_get) {
            Request request = new Request.Builder().url("https://www.wanandroid.com/banner/json").build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    Log.d(TAG, "get code: " + response.code());
                    Log.d(TAG, "get headers: " + response.headers());
                    Log.d(TAG, "get body: " + response.body());
                }
            });
        } else if (id == R.id.btn_post) {
            RequestBody requestBody = new RequestBody()
                    .add("username", "Solarex")
                    .add("password", "123456");
            Request request = new Request.Builder().url("https://www.wanandroid.com/user/login")
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    Log.d(TAG, "post code: " + response.code());
                    Log.d(TAG, "post headers: " + response.headers());
                    Log.d(TAG, "post body: " + response.body());
                }
            });
        }
    }
}
