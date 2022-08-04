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


public class PersonsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerViewP;
    private PersonsAdapter mPAdapter;
    private ArrayList<PersonsItem> mPItem;
    private RequestQueue mRequestQueueP;
    private PersonsAdapter.RecyclerViewClickListener listener;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Toolbar toolbarAll = findViewById(R.id.tlPersons);
        setSupportActionBar(toolbarAll);


        mRecyclerViewP = findViewById(R.id.personsRV);
        mRecyclerViewP.setHasFixedSize(true);
        mRecyclerViewP.setLayoutManager(new LinearLayoutManager(this));

        mPItem = new ArrayList<>();

        mRequestQueueP = Volley.newRequestQueue(this);
        parseJSONP();



    }

    private void parseJSONP() {
        String urlP = "https://lijukay.github.io/quotesaltdesign/editorschoice.json";


        JsonObjectRequest requestP = new JsonObjectRequest(Request.Method.GET, urlP, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseP) {
                        try {
                            JSONArray jsonArrayP = responseP.getJSONArray("Persons");

                            for(int a = 0; a < jsonArrayP.length(); a++){
                                JSONObject ec = jsonArrayP.getJSONObject(a);

                                String authorP = ec.getString("authorP");

                                mPItem.add(new PersonsItem(authorP));
                            }

                            setOnClickListener();
                            mPAdapter = new PersonsAdapter(PersonsActivity.this, mPItem, listener);
                            mRecyclerViewP.setAdapter(mPAdapter);
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
        mRequestQueueP.add(requestP);
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            String urlP = "https://lijukay.github.io/quotesaltdesign/editorschoice.json";


            JsonObjectRequest requestP = new JsonObjectRequest(Request.Method.GET, urlP, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject responseP) {
                            try {
                                JSONArray jsonArrayP = responseP.getJSONArray("Persons");

                                    JSONObject ec = jsonArrayP.getJSONObject(position);

                                    String authorP = ec.getString("authorP");

                                    mPItem.add(new PersonsItem(authorP));

                                    Intent intent = new Intent(PersonsActivity.this, PersonsQuote.class);
                                    intent.putExtra("authorP", authorP);
                                    startActivity(intent);
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
            mRequestQueueP.add(requestP);

        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_p, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.aboutP){
            AboutApp();
            return true;
        } else if(item.getItemId() == R.id.samsungdesignP){
            SamsungDesign();
            return true;
        } else if(item.getItemId() == R.id.ecP){
            ECP();
            return true;
        } else if(item.getItemId() == R.id.allP){
            All();
            return true;
        }  else if(item.getItemId() == R.id.refreshP){
            mPItem.clear();
            parseJSONP();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void All() {
        Intent intentAM = new Intent(this, AllActivity.class);
        startActivity(intentAM);
    }

    private void ECP() {
        Intent intentP = new Intent(this, MainActivity.class);
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