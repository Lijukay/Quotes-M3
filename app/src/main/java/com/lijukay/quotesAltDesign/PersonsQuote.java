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
import com.google.android.material.appbar.CollapsingToolbarLayout;
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


public class PersonsQuote extends AppCompatActivity {
    private RecyclerView mRecyclerViewPQ;
    private PQAdapter mPQAdapter;
    private ArrayList<PQItem> mPQItem;
    private RequestQueue mRequestQueuePQ;
    private String pQuotes;
    private String authorP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons_quote);
        Toolbar toolbarPQ = findViewById(R.id.tlPQ);
        setSupportActionBar(toolbarPQ);



        Intent intent = getIntent();

        authorP = intent.getStringExtra("authorP");

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.ctlPQ);
        collapsingToolbarLayout.setTitle(authorP);


        mRecyclerViewPQ = findViewById(R.id.PersonsRV);
        mRecyclerViewPQ.setHasFixedSize(true);
        mRecyclerViewPQ.setLayoutManager(new LinearLayoutManager(this));

        mPQItem = new ArrayList<>();

        mRequestQueuePQ = Volley.newRequestQueue(this);
        parseJSONPQ();
    }

    private void parseJSONPQ() {
        String urlPQ = "https://lijukay.github.io/quotesaltdesign/editorschoice.json";


        JsonObjectRequest requestPQ = new JsonObjectRequest(Request.Method.GET, urlPQ, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responsePQ) {
                        try {
                            pQuotes = authorP;
                            JSONArray jsonArrayPQ = responsePQ.getJSONArray(pQuotes);

                            for(int a = 0; a < jsonArrayPQ.length(); a++){
                                JSONObject pq = jsonArrayPQ.getJSONObject(a);

                                String quotePQ = pq.getString("quotePQ");
                                String authorPQ = pq.getString("authorPQ");

                                mPQItem.add(new PQItem(authorPQ, quotePQ));
                            }

                            mPQAdapter = new PQAdapter(PersonsQuote.this, mPQItem);
                            mRecyclerViewPQ.setAdapter(mPQAdapter);
                            Log.e("intent", "Hat geklapptAll...");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error", "hat nicht geklapptall...");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError errorPQ) {
                errorPQ.printStackTrace();
                Log.e("error", "Hat nicht geklappt 2all");
            }
        });
        mRequestQueuePQ.add(requestPQ);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menuPQ) {
        getMenuInflater().inflate(R.menu.menu_aq, menuPQ);
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
            mPQItem.clear();
            parseJSONPQ();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void ECA() {
        Intent intentAM = new Intent(this, MainActivity.class);
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