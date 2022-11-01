package com.lijukay.quotesAltDesign.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lijukay.quotesAltDesign.Adapter.ECAdapter;
import com.lijukay.quotesAltDesign.Items.ECItem;
import com.lijukay.quotesAltDesign.R;
import com.lijukay.quotesAltDesign.Others.RecyclerViewInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    private RecyclerView recyclerView;
    private ECAdapter adapter;
    private ArrayList<ECItem> items;
    private RequestQueue requestQueue;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String language;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences2 = getSharedPreferences("Theme", 0);
        String theme = sharedPreferences2.getString("Theme", "red");
        if (theme.equals("red")){
            setTheme(R.style.AppTheme);
        } else if (theme.equals("purple")){
            setTheme(R.style.AppThemePurple);
        }

        SharedPreferences sharedPreferences3 = getSharedPreferences("Language", 0);
        language = sharedPreferences3.getString("Language", Locale.getDefault().getLanguage());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("NightMode", 0);
        boolean isNightMode = sharedPreferences.getBoolean("Night", false);
        if (isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        Toolbar toolbar = findViewById(R.id.tl);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(v -> onBackPressed());

        //RecyclerView "setup"
        recyclerView = findViewById(R.id.editorsChoiceRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Creating new ArrayList for mECItem
        items = new ArrayList<>();

        //SwipeRefreshLayout "setup"
        swipeRefreshLayout = findViewById(R.id.swipeEC);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Toast.makeText(MainActivity.this, getString(R.string.refreshing), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                items.clear();
                adapter.notifyDataSetChanged();
                getLanguageEC();
            }, 2000);
        });
        requestQueue = Volley.newRequestQueue(this);
        getLanguageEC();
    }

    private void getLanguageEC() {
        if (language.equals("en")) {
            parseJSON();
        } else if (language.equals("de")) {
            parseJSONGER();
        } else {
            parseJSON();
        }
    }

    private void parseJSONGER() {
        String urlGER = "https://lijukay.github.io/Quotes-M3/quotesGER.json";

        JsonObjectRequest requestGER = new JsonObjectRequest(Request.Method.GET, urlGER, null,
                responseGER -> {
                    try {
                        JSONArray jsonArrayGER = responseGER.getJSONArray("EditorsChoice");

                        for (int g = 0; g < jsonArrayGER.length(); g++) {
                            JSONObject ecGER = jsonArrayGER.getJSONObject(g);

                            String quoteECGER = ecGER.getString("quote");
                            String authorECGER = ecGER.getString("author");

                            items.add(new ECItem(authorECGER, quoteECGER));
                        }

                        adapter = new ECAdapter(MainActivity.this, items, this);
                        recyclerView.setAdapter(adapter);
                        Log.e("intent", "Hat geklappt...");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error", "hat nicht geklappt...");
                    }
                }, error -> {
            error.printStackTrace();
            Log.e("error", "Hat nicht geklappt 2");
        });
        requestQueue.add(requestGER);
    }

    private void parseJSON() {
        String url = "https://lijukay.github.io/Quotes-M3/quotesEN.json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("EditorsChoice");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ec = jsonArray.getJSONObject(i);

                            String quoteEC = ec.getString("quote");
                            String authorEC = ec.getString("author");

                            items.add(new ECItem(authorEC, quoteEC));
                        }

                        adapter = new ECAdapter(MainActivity.this, items, this);
                        recyclerView.setAdapter(adapter);
                        Log.e("intent", "Hat geklappt...");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error", "hat nicht geklappt...");
                    }
                }, error -> {
            error.printStackTrace();
            Log.e("error", "Hat nicht geklappt 2");
        });
        requestQueue.add(request);
    }


    //Menu setup
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about) {
            AboutApp();
            return true;
        } else if (item.getItemId() == R.id.samsungdesign) {
            SamsungDesign();
            return true;
        } else if (item.getItemId() == R.id.people) {
            People();
            return true;
        } else if (item.getItemId() == R.id.all) {
            All();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    //Menu Intents(?)
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
        Intent intentA = new Intent(this, SettingsActivity.class);
        startActivity(intentA);
    }

    @Override
    public void onItemClick(int position) {
        String langu = Locale.getDefault().getLanguage();

        if (langu.equals("de")) {
            String urlP = "https://lijukay.github.io/Quotes-M3/quotesGER.json";
            JsonObjectRequest requestP = new JsonObjectRequest(Request.Method.GET, urlP, null,
                    responseP -> {
                        try {
                            JSONArray jsonArrayP = responseP.getJSONArray("EditorsChoice");

                            JSONObject ec = jsonArrayP.getJSONObject(position);

                            String quoteE = ec.getString("quote");
                            String authorP = ec.getString("author");

                            showDialogs(authorP, quoteE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error", "Something is not right with the file! Contact the developer!");
                        }
                    }, errorAll -> {
                errorAll.printStackTrace();
                Log.e("error", "File is not reachable, check your Internet connection. If you are connected to the internet, contact the developer!");
            });
            requestQueue.add(requestP);
        } else {
            String urlP = "https://lijukay.github.io/Quotes-M3/quotesEN.json";


            JsonObjectRequest requestP = new JsonObjectRequest(Request.Method.GET, urlP, null,
                    responseP -> {
                        try {
                            JSONArray jsonArrayP = responseP.getJSONArray("EditorsChoice");

                            JSONObject ec = jsonArrayP.getJSONObject(position);

                            String quoteE = ec.getString("quote");
                            String authorP = ec.getString("author");

                            showDialogs(authorP, quoteE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error", "Something is not right with the file! Contact the developer!");
                        }
                    }, Throwable::printStackTrace);
            requestQueue.add(requestP);
        }
    }


    private void showDialogs(String author, String quote) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_quotes);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        TextView authorT = dialog.findViewById(R.id.authort);
        authorT.setText(author);
        TextView quoteT = dialog.findViewById(R.id.quotet);
        CardView copy = dialog.findViewById(R.id.copyText);
        CardView share = dialog.findViewById(R.id.shareText);
        copy.setOnClickListener(view -> copyText(quote + "\n\n~ " + author));
        share.setOnClickListener(view -> {
            Intent shareText = new Intent();
            shareText.setAction(Intent.ACTION_SEND);
            shareText.putExtra(Intent.EXTRA_TEXT, quote + "\n\n~" + author);
            shareText.setType("text/plain");
            Intent sendText = Intent.createChooser(shareText, null);
            startActivity(sendText);

        });
        quoteT.setText(quote);
        quoteT.setMaxLines(3);


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void copyText(String quote) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Quotes", quote);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, getString(R.string.copiedMessage), Toast.LENGTH_SHORT).show();
    }
}
