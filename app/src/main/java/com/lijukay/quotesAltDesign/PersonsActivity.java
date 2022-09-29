package com.lijukay.quotesAltDesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
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

public class PersonsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerViewP;
    private PersonsAdapter mPAdapter;
    private ArrayList<PersonsItem> mPItem;
    private RequestQueue mRequestQueueP;
    private PersonsAdapter.RecyclerViewClickListener listener;
    private SwipeRefreshLayout swipeRefreshLayoutP;

    @SuppressLint("NotifyDataSetChanged")
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
        swipeRefreshLayoutP = findViewById(R.id.swipePersons);
        swipeRefreshLayoutP.setOnRefreshListener(() -> {
            Toast.makeText(PersonsActivity.this, getString(R.string.refreshing), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                swipeRefreshLayoutP.setRefreshing(false);
                mPItem.clear();
                mPAdapter.notifyDataSetChanged();
                parseJSONP();
            }, 2000);
        });

        mRequestQueueP = Volley.newRequestQueue(this);
        parseJSONP();


    }


    private void parseJSONP() {
        String urlP = "https://lijukay.github.io/Quotes-M3/quotesEN.json";


        JsonObjectRequest requestP = new JsonObjectRequest(Request.Method.GET, urlP, null,
                responseP -> {
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        mRequestQueueP.add(requestP);
    }

    private void setOnClickListener() {
        listener = (position) -> {
            String urlP = "https://lijukay.github.io/Quotes-M3/quotesEN.json";


            JsonObjectRequest requestP = new JsonObjectRequest(Request.Method.GET, urlP, null,
                    responseP -> {
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
                        }
                    }, Throwable::printStackTrace);
            mRequestQueueP.add(requestP);

        };
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
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