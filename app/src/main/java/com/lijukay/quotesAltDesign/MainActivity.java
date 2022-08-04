package com.lijukay.quotesAltDesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lijukay.quotesAltDesign.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ECAdapter mECAdapter;
    private ArrayList<ECItem> mECItem;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.tl);
        setSupportActionBar(toolbar);


        mRecyclerView = findViewById(R.id.editorsChoiceRV);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mECItem = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();


    }

    private void parseJSON() {
        String url = "https://lijukay.github.io/quotesaltdesign/editorschoice.json";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("EditorsChoice");

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject ec = jsonArray.getJSONObject(i);

                                String quoteEC = ec.getString("quote");
                                String authorEC = ec.getString("author");

                                mECItem.add(new ECItem(authorEC, quoteEC));
                            }

                            mECAdapter = new ECAdapter(MainActivity.this, mECItem);
                            mRecyclerView.setAdapter(mECAdapter);
                            Log.e("intent", "Hat geklappt...");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error", "hat nicht geklappt...");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("error", "Hat nicht geklappt 2");
            }
        });
        mRequestQueue.add(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.about){
            AboutApp();
            return true;
        } else if(item.getItemId() == R.id.samsungdesign){
            SamsungDesign();
            return true;
        } else if(item.getItemId() == R.id.people){
            People();
            return true;
        } else if(item.getItemId() == R.id.all){
            All();
            return true;
        }  else if(item.getItemId() == R.id.refresh){
            mECItem.clear();
            parseJSON();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void All() {
        Intent intentAM = new Intent(this, AllActivity.class);
        startActivity(intentAM);
    }
    private void People() {
        Intent intentP = new Intent(this, PersonsActivity.class);
        startActivity(intentP);
    }
    private void SamsungDesign() {
        Uri uriS = Uri.parse("https://github.com/Lijukay/Quotes");
        Intent intentS = new Intent(Intent.ACTION_VIEW, uriS);
        startActivity(intentS);
    }
    private void AboutApp() {
        Intent intentA = new Intent(this, About.class);
        startActivity(intentA);
    }




}