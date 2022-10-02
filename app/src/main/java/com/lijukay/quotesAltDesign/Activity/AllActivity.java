package com.lijukay.quotesAltDesign.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class AllActivity extends AppCompatActivity implements RecyclerViewInterface {
    private RecyclerView mRecyclerViewAll;
    private AllAdapter mAllAdapter;
    private ArrayList<AllItem> mAllItem;
    private RequestQueue mRequestQueueAll;
    private SwipeRefreshLayout swipeRefreshLayoutAll;
    private View alertCustomDialog;
    private AlertDialog dialog;
    CardView share, copy;
    TextView authorT, quoteT;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quotes);

        Toolbar toolbarAll = findViewById(R.id.tlall);
        setSupportActionBar(toolbarAll);

        alertCustomDialog = LayoutInflater.from(AllActivity.this).inflate(R.layout.dialog_quotes, null);


        mRecyclerViewAll = findViewById(R.id.allQuotesRV);
        mRecyclerViewAll.setHasFixedSize(true);
        mRecyclerViewAll.setLayoutManager(new LinearLayoutManager(this));

        mAllItem = new ArrayList<>();
        swipeRefreshLayoutAll = findViewById(R.id.swipeAll);
        swipeRefreshLayoutAll.setOnRefreshListener(() -> {
            Toast.makeText(AllActivity.this, getString(R.string.refreshing), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                swipeRefreshLayoutAll.setRefreshing(false);
                try {
                    trimCache(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mAllItem.clear();
                mAllAdapter.notifyDataSetChanged();
                getLanguageAll();
            }, 2000);
        });
        mRequestQueueAll = Volley.newRequestQueue(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DisplayMetrics displayMetricsHalf = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetricsHalf);
        int bsdsizeHalf = displayMetrics.heightPixels / 2;


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllActivity.this);
        alertDialog.setView(alertCustomDialog);
        authorT = alertCustomDialog.findViewById(R.id.authort);
        quoteT = alertCustomDialog.findViewById(R.id.quotet);
        copy = alertCustomDialog.findViewById(R.id.copyText);
        share = alertCustomDialog.findViewById(R.id.shareText);
        dialog = alertDialog.create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, bsdsizeHalf);


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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
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
                        }
                    }, Throwable::printStackTrace);
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
                        }
                    }, Throwable::printStackTrace);
            mRequestQueueAll.add(requestP);
        }
    }

    private void showDialogs(String author, String quote) {
        //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        quoteT.setText(quote);
        authorT.setText(author);
        dialog.show();

        copy.setOnClickListener(view -> copyText(quote + "\n\n~ " + author));
        share.setOnClickListener(view -> {
            Intent shareText = new Intent();
            shareText.setAction(Intent.ACTION_SEND);
            shareText.putExtra(Intent.EXTRA_TEXT, quote + "\n\n~" + author);
            shareText.setType("text/plain");
            Intent sendText = Intent.createChooser(shareText, null);
            startActivity(sendText);
        });
        quoteT.setMaxLines(3);

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }



    private void copyText(String quote) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Quotes", quote);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, getString(R.string.copiedMessage), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            trimCache(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else {
            return false;
        }
    }

}