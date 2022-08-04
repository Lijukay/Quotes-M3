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


public class AllActivity extends AppCompatActivity {
    private RecyclerView mRecyclerViewAll;
    private AllAdapter mAllAdapter;
    private ArrayList<AllItem> mAllItem;
    private RequestQueue mRequestQueueAll;
    private String allquotes = "AllQuotes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quotes);
        Toolbar toolbarAll = findViewById(R.id.tlall);
        setSupportActionBar(toolbarAll);


        mRecyclerViewAll = findViewById(R.id.allQuotesRV);
        mRecyclerViewAll.setHasFixedSize(true);
        mRecyclerViewAll.setLayoutManager(new LinearLayoutManager(this));

        mAllItem = new ArrayList<>();

        mRequestQueueAll = Volley.newRequestQueue(this);
        parseJSONAll();



    }

    private void parseJSONAll() {
        String urlAll = "https://lijukay.github.io/quotesaltdesign/editorschoice.json";


        JsonObjectRequest requestAll = new JsonObjectRequest(Request.Method.GET, urlAll, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseAll) {
                        try {
                            JSONArray jsonArrayAll = responseAll.getJSONArray(allquotes);

                            for(int a = 0; a < jsonArrayAll.length(); a++){
                                JSONObject ec = jsonArrayAll.getJSONObject(a);

                                String quoteAll = ec.getString("quoteAll");
                                String authorAll = ec.getString("authorAll");

                                mAllItem.add(new AllItem(authorAll, quoteAll));
                            }

                            mAllAdapter = new AllAdapter(AllActivity.this, mAllItem);
                            mRecyclerViewAll.setAdapter(mAllAdapter);
                            Log.e("intent", "Hat geklapptAll...");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error", "hat nicht geklapptall...");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError errorAll) {
                errorAll.printStackTrace();
                Log.e("error", "Hat nicht geklappt 2all");
            }
        });
        mRequestQueueAll.add(requestAll);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aq, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.aboutA){
            AboutApp();
            return true;
        } else if(item.getItemId() == R.id.samsungdesignA){
            SamsungDesign();
            return true;
        } else if(item.getItemId() == R.id.personsA){
            People();
            return true;
        } else if(item.getItemId() == R.id.ecA){
            ECA();
            return true;
        }  else if(item.getItemId() == R.id.refreshA){
            mAllItem.clear();
            parseJSONAll();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void ECA() {
        Intent intentEM = new Intent(this, MainActivity.class);
        startActivity(intentEM);
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