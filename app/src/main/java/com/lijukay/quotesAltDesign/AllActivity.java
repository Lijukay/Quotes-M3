package com.lijukay.quotesAltDesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class AllActivity extends AppCompatActivity implements RecyclerViewInterface {
    private RecyclerView mRecyclerViewAll;
    private AllAdapter mAllAdapter;
    private ArrayList<AllItem> mAllItem;
    private RequestQueue mRequestQueueAll;
    private SwipeRefreshLayout swipeRefreshLayoutAll;

    @SuppressLint("NotifyDataSetChanged")
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
            Toast.makeText(AllActivity.this, getString(R.string.refreshing), Toast.LENGTH_SHORT).show();
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
        if (langAll.equals("en")) {
            parseJSONAll();
        } else if (langAll.equals("de")) {
            parseJSONGERAll();
        } else {
            parseJSONAll();
        }
    }

    private void parseJSONGERAll() {
        String urlGERAll = "https://lijukay.github.io/Quotes-M3/quotesGER.json";

        JsonObjectRequest requestAllGER = new JsonObjectRequest(Request.Method.GET, urlGERAll, null,
                responseAllGER -> {
                    try {
                        JSONArray jsonArrayAll = responseAllGER.getJSONArray("AllQuotes");

                        for (int ga = 0; ga < jsonArrayAll.length(); ga++) {
                            JSONObject agq = jsonArrayAll.getJSONObject(ga);

                            String quoteAllGER = agq.getString("quoteAll");
                            String authorAllGER = agq.getString("authorAll");

                            mAllItem.add(new AllItem(authorAllGER, quoteAllGER));
                        }

                        mAllAdapter = new AllAdapter(AllActivity.this, mAllItem, this);
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

                        for (int a = 0; a < jsonArrayAll.length(); a++) {
                            JSONObject ec = jsonArrayAll.getJSONObject(a);

                            String quoteAll = ec.getString("quoteAll");
                            String authorAll = ec.getString("authorAll");

                            mAllItem.add(new AllItem(authorAll, quoteAll));
                        }

                        mAllAdapter = new AllAdapter(AllActivity.this, mAllItem, this);
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
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aq, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.aboutA) {
            AboutApp();
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

    @Override
    public void onItemClick(int position) {
        String langu = Locale.getDefault().getLanguage();

        if (langu.equals("de")) {
            String urlP = "https://lijukay.github.io/Quotes-M3/quotesGER.json";
            JsonObjectRequest requestP = new JsonObjectRequest(Request.Method.GET, urlP, null,
                    responseP -> {
                        try {
                            JSONArray jsonArrayP = responseP.getJSONArray("AllQuotes");

                            JSONObject ec = jsonArrayP.getJSONObject(position);

                            String quoteE = ec.getString("quoteAll");
                            String authorP = ec.getString("authorAll");

                            showDialogs(authorP, quoteE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error", "Something is not right with the file! Contact the developer!");
                        }
                    }, errorAll -> {
                errorAll.printStackTrace();
                Log.e("error", "File is not reachable, check your Internet connection. If you are connected to the internet, contact the developer!");
            });
            mRequestQueueAll.add(requestP);
        } else {
            String urlP = "https://lijukay.github.io/Quotes-M3/quotesEN.json";


            JsonObjectRequest requestP = new JsonObjectRequest(Request.Method.GET, urlP, null,
                    responseP -> {
                        try {
                            JSONArray jsonArrayP = responseP.getJSONArray("AllQuotes");

                            JSONObject ec = jsonArrayP.getJSONObject(position);

                            String quoteE = ec.getString("quoteAll");
                            String authorP = ec.getString("authorAll");

                            showDialogs(authorP, quoteE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error", "Something is not right with the file! Contact the developer!");
                        }
                    }, errorAll -> {
                errorAll.printStackTrace();
                Log.e("error", "File is not reachable, check your Internet connection. If you are connected to the internet, contact the developer!");
            });
            mRequestQueueAll.add(requestP);
        }
    }

    private void showDialogs(String author, String quote) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.bottomsheetdialog_quotes);

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