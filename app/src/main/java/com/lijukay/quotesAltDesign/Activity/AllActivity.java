package com.lijukay.quotesAltDesign.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lijukay.quotesAltDesign.Adapter.AllAdapter;
import com.lijukay.quotesAltDesign.Items.AllItem;
import com.lijukay.quotesAltDesign.R;
import com.lijukay.quotesAltDesign.Others.RecyclerViewInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class AllActivity extends AppCompatActivity implements RecyclerViewInterface {
    private RecyclerView recyclerView;
    private AllAdapter adapter;
    private ArrayList<AllItem> items;
    private RequestQueue requestQueue;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AlertDialog dialogQuotes;
    CardView shareCardView, copyCardView;
    TextView authorTextView, quoteTextView;
    String language;

    @SuppressLint({"NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences2 = getSharedPreferences("Theme", 0);
        String colorTheme = sharedPreferences2.getString("Theme", "red");
        if (colorTheme.equals("red")){
            setTheme(R.style.AppTheme);
        } else if (colorTheme.equals("purple")){
            setTheme(R.style.AppThemePurple);
        }

        SharedPreferences sharedPreferencesLanguage = getSharedPreferences("Language", 0);
        language = sharedPreferencesLanguage.getString("Language", Locale.getDefault().getLanguage());
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        SharedPreferences sharedPreferencesNightMode = getSharedPreferences("NightMode", 0);
        boolean isNightMode = sharedPreferencesNightMode.getBoolean("Night", false);
        if (isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quotes);

        Toolbar toolbar = findViewById(R.id.tlall);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(v -> onBackPressed());

        View alertCustomDialog = LayoutInflater.from(AllActivity.this).inflate(R.layout.dialog_quotes, null);

        recyclerView = findViewById(R.id.allQuotesRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        items = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.swipeAll);
        swipeRefreshLayout.setOnRefreshListener(() -> {

            Toast.makeText(AllActivity.this, getString(R.string.refreshing), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                items.clear();
                adapter.notifyDataSetChanged();
                getLanguage();
            }, 2000);
        });
        requestQueue = Volley.newRequestQueue(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DisplayMetrics displayMetricsHalf = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetricsHalf);
        int halfDisplaySize = displayMetrics.heightPixels / 2;


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllActivity.this);
        alertDialog.setView(alertCustomDialog);
        authorTextView = alertCustomDialog.findViewById(R.id.authort);
        quoteTextView = alertCustomDialog.findViewById(R.id.quotet);
        copyCardView = alertCustomDialog.findViewById(R.id.copyText);
        shareCardView = alertCustomDialog.findViewById(R.id.shareText);
        dialogQuotes = alertDialog.create();
        dialogQuotes.getWindow().setGravity(Gravity.BOTTOM);
        dialogQuotes.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogQuotes.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, halfDisplaySize);


        getLanguage();

    }

    private void getLanguage() {
        if (language.equals("en")) {
            parseJSON();
        } else if (language.equals("de")) {
            parseJSONGER();
        } else {
            parseJSON();
        }
    }

    private void parseJSONGER() {
        String url = "https://lijukay.github.io/Quotes-M3/quotesGER.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                jsonObject -> {
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("AllQuotes");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String quote = object.getString("quoteAll");
                            String author = object.getString("authorAll");

                            items.add(new AllItem(author, quote));
                        }

                        adapter = new AllAdapter(AllActivity.this, items, this);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
    }
    private void parseJSON() {
        String url = "https://lijukay.github.io/Quotes-M3/quotesEN.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                jsonObject -> {
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("AllQuotes");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String quote = object.getString("quoteAll");
                            String author = object.getString("authorAll");

                            items.add(new AllItem(author, quote));
                        }

                        adapter = new AllAdapter(AllActivity.this, items, this);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aq, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.aboutA) {
            Settings();
            return true;
        } else if (item.getItemId() == R.id.samsungdesignA) {
            SamsungDesign();
            return true;
        } else if (item.getItemId() == R.id.personsA) {
            People();
            return true;
        } else if (item.getItemId() == R.id.ecA) {
            ECA();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void ECA() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void People() {
        Intent intent = new Intent(this, PersonsActivity.class);
        startActivity(intent);
    }

    private void SamsungDesign() {
        Uri uri = Uri.parse("https://github.com/Lijukay/Quotes");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void Settings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        String language = Locale.getDefault().getLanguage();

        if (language.equals("de")) {
            String url = "https://lijukay.github.io/Quotes-M3/quotesGER.json";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    jsonObject -> {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("AllQuotes");

                            JSONObject object = jsonArray.getJSONObject(position);

                            String quote = object.getString("quoteAll");
                            String author = object.getString("authorAll");

                            showDialogs(author, quote);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace);
            requestQueue.add(jsonObjectRequest);
        } else {
            String url = "https://lijukay.github.io/Quotes-M3/quotesEN.json";


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    jsonObject -> {
                        try {
                            JSONArray jsonArrayP = jsonObject.getJSONArray("AllQuotes");

                            JSONObject object = jsonArrayP.getJSONObject(position);

                            String quoteE = object.getString("quoteAll");
                            String authorP = object.getString("authorAll");

                            showDialogs(authorP, quoteE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace);
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void showDialogs(String author, String quote) {

        quoteTextView.setText(quote);
        authorTextView.setText(author);

        copyCardView.setOnClickListener(view -> copyText(quote + "\n\n~ " + author));
        shareCardView.setOnClickListener(view -> {
            Intent shareText = new Intent();
            shareText.setAction(Intent.ACTION_SEND);
            shareText.putExtra(Intent.EXTRA_TEXT, quote + "\n\n~" + author);
            shareText.setType("text/plain");
            Intent sendText = Intent.createChooser(shareText, null);
            startActivity(sendText);
        });
        quoteTextView.setMaxLines(3);

        dialogQuotes.show();
        dialogQuotes.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }



    private void copyText(String quote) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Quotes", quote);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, getString(R.string.copiedMessage), Toast.LENGTH_SHORT).show();
    }


}