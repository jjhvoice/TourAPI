package com.example.test.tourapi;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText;
    ListView listView;
    Button button;

    String URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";
    String ServiceKey = "zIb71ZTgLlUbbRO%2BnpV26YTd3YzLPrLBDf%2B9fNnMm66bMgFPPaUuGz6yIRlo5SmXzue9GWpzpZ9PWs0jZx617Q%3D%3D";
    String keyword;
    String MobileOS = "AND";
    String MobileApp = "TourAPI";
    String _type = "json";

    ImageTask task;
    ImageAdapter adapter;

    String imgURL;
    Bitmap imgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.edittext);
        listView = (ListView)findViewById(R.id.listview);
        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(this);

        adapter = new ImageAdapter();
    }

    public class ImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                imgBitmap = BitmapFactory.decodeStream(is);
                is.close();

            } catch(IOException e){
                e.printStackTrace();
            }
            return imgBitmap;
        }

        protected void onPostExecute(Bitmap imgBitmap){
            if(imgBitmap != null) {
                Toast.makeText(MainActivity.this, "이미지를 가져왔습니다", Toast.LENGTH_SHORT).show();
                adapter.addItem(imgBitmap);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "이미지를 가져오지 못했습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApiService retrofitService = retrofit.create(RetrofitApiService.class);
        keyword = editText.getText().toString();
        Call<Contributor> call = retrofitService.getRepo(ServiceKey, keyword, MobileOS, MobileApp, _type);
        call.enqueue(new Callback<Contributor>() {
            @Override
            public void onResponse(Call<Contributor> call, Response<Contributor> response) {
                if(response.isSuccessful()) {
                    task = new ImageTask();

                    Toast.makeText(MainActivity.this, "데이터를 가져왔습니다", Toast.LENGTH_SHORT).show();
                    Log.e("성공", String.valueOf(response.raw()));

                    //이미지링크가 비어있을 경우 대비
                    int i = 0;
                    while(true) {
                        imgURL = response.body().getResponse().getBody().getItems().getItem().get(i).getFirstimage();
                        i++;

                        if(imgURL != null) break;
                    }

                    task.execute(imgURL);
                }
            }

            @Override
            public void onFailure(Call<Contributor> call, Throwable t) {
                Toast.makeText(MainActivity.this, "데이터를 가져오지 못했습니다", Toast.LENGTH_SHORT).show();
                Log.e("실패", t.getMessage());
                Log.e("실패", String.valueOf(call.request()));
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == button) {
            getData();
        }
    }
}
