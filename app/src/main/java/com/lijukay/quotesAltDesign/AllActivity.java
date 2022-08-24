package com.lijukay.quotesAltDesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;

public class AllActivity extends AppCompatActivity {
    private RecyclerView mRecyclerViewAll;
    private AllAdapter mAllAdapter;
    private ArrayList<AllItem> mAllItem;
    private RequestQueue mRequestQueueAll;
    private SwipeRefreshLayout swipeRefreshLayoutAll;

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
        swipeRefreshLayoutAll = findViewById(R.id.swipeAll);
        swipeRefreshLayoutAll.setOnRefreshListener(() -> {
            Toast.makeText(AllActivity.this, "Refreshing... please wait", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                swipeRefreshLayoutAll.setRefreshing(false);
                mAllItem.clear();
                mAllAdapter.notifyDataSetChanged();
                getLanguageAll();
            }, 2000);
        });
        mRequestQueueAll = Volley.newRequestQueue(this);
        getLanguageAll();
    }

    private void getLanguageAll() {
        String langAll = Locale.getDefault().getLanguage();
        if(langAll.equals("en")){
            parseJSONAll();
        }else if(langAll.equals("de")){
            parseJSONGERAll();
        }else{
            parseJSONAll();
        }
    }

    private void parseJSONGERAll() {
        String urlGERAll = "https://lijukay.github.io/Quotes-M3/quotesGER.json";

        JsonObjectRequest requestAllGER = new JsonObjectRequest(Request.Method.GET, urlGERAll, null,
                responseAllGER -> {
                    try {
                        JSONArray jsonArrayAll = responseAllGER.getJSONArray("AllQuotes");

                        for(int ga = 0; ga < jsonArrayAll.length(); ga++){
                            JSONObject agq = jsonArrayAll.getJSONObject(ga);

                            String quoteAllGER = agq.getString("quoteAll");
                            String authorAllGER = agq.getString("authorAll");

                            mAllItem.add(new AllItem(authorAllGER, quoteAllGER));
                        }

                        mAllAdapter = new AllAdapter(AllActivity.this, mAllItem);
                        mRecyclerViewAll.setAdapter(mAllAdapter);
                        Log.e("intent", "Hat geklapptAll...");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error", "hat nicht geklapptall...");
                    }
                }, errorAll -> {
            errorAll.printStackTrace();
            Log.e("error", "Hat nicht geklappt all");
        });
        mRequestQueueAll.add(requestAllGER);
    }

    private void parseJSONAll() {
        String urlAll = "https://lijukay.github.io/Quotes-M3/quotesEN.json";

        JsonObjectRequest requestAll = new JsonObjectRequest(Request.Method.GET, urlAll, null,
                responseAll -> {
                    try {
                        JSONArray jsonArrayAll = responseAll.getJSONArray("AllQuotes");

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
                }, errorAll -> {
                    errorAll.printStackTrace();
                    Log.e("error", "Hat nicht geklappt 2all");
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