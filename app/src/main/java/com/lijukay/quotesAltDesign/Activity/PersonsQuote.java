package com.lijukay.quotesAltDesign.Activity;

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
import android.content.Context;
import android.content.Intent;
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
import com.lijukay.quotesAltDesign.Adapter.PQAdapter;
import com.lijukay.quotesAltDesign.Items.PQItem;
import com.lijukay.quotesAltDesign.R;
import com.lijukay.quotesAltDesign.Others.RecyclerViewInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class PersonsQuote extends AppCompatActivity implements RecyclerViewInterface {
    private RecyclerView mRecyclerViewPQ;
    private PQAdapter mPQAdapter;
    private ArrayList<PQItem> mPQItem;
    private RequestQueue mRequestQueuePQ;
    private String pQuotes, authorP;
    private SwipeRefreshLayout swipeRefreshLayoutPQ;




    @SuppressLint({"NotifyDataSetChanged", "InflateParams"})
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons_quote);
        Toolbar toolbarPQ = findViewById(R.id.tlPQ);
        setSupportActionBar(toolbarPQ);
        Intent intent = getIntent();

        authorP = intent.getStringExtra("authorP");

        Objects.requireNonNull(getSupportActionBar()).setTitle(authorP);
        toolbarPQ.setTitleTextAppearance(this, androidx.appcompat.R.style.TextAppearance_AppCompat_Title);

        mRecyclerViewPQ = findViewById(R.id.PersonsRV);
        mRecyclerViewPQ.setHasFixedSize(true);
        mRecyclerViewPQ.setLayoutManager(new LinearLayoutManager(this));

        mPQItem = new ArrayList<>();

        swipeRefreshLayoutPQ = findViewById(R.id.swipePQ);
        swipeRefreshLayoutPQ.setOnRefreshListener(() -> {
            Toast.makeText(PersonsQuote.this, getString(R.string.refreshing), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                swipeRefreshLayoutPQ.setRefreshing(false);
                try {
                    trimCache(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mPQItem.clear();
                mPQAdapter.notifyDataSetChanged();
                getLanguagePQ();
            }, 2000);
        });

        mRequestQueuePQ = Volley.newRequestQueue(this);
        getLanguagePQ();
    }

    private void getLanguagePQ() {
        String langPQ = Locale.getDefault().getLanguage();
        if (langPQ.equals("en")) {
            parseJSONPQ();
        } else if (langPQ.equals("de")) {
            parseJSONPQGER();
        } else {
            parseJSONPQ();
        }
    }

    private void parseJSONPQGER() {
        String urlPQ = "https://lijukay.github.io/Quotes-M3/quotesGER.json";


        JsonObjectRequest requestPQGER = new JsonObjectRequest(Request.Method.GET, urlPQ, null,
                responsePQGER -> {
                    try {
                        pQuotes = authorP;
                        JSONArray jsonArrayPQGER = responsePQGER.getJSONArray(pQuotes);

                        for (int pqG = 0; pqG < jsonArrayPQGER.length(); pqG++) {
                            JSONObject pq = jsonArrayPQGER.getJSONObject(pqG);

                            String quotePQGER = pq.getString("quotePQ");
                            String authorPQGER = pq.getString("authorPQ");

                            mPQItem.add(new PQItem(authorPQGER, quotePQGER));
                        }

                        mPQAdapter = new PQAdapter(PersonsQuote.this, mPQItem, this);
                        mRecyclerViewPQ.setAdapter(mPQAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        mRequestQueuePQ.add(requestPQGER);
    }

    private void parseJSONPQ() {
        String urlPQ = "https://lijukay.github.io/Quotes-M3/quotesEN.json";


        JsonObjectRequest requestPQ = new JsonObjectRequest(Request.Method.GET, urlPQ, null,
                responsePQ -> {
                    try {
                        pQuotes = authorP;
                        JSONArray jsonArrayPQ = responsePQ.getJSONArray(pQuotes);

                        for (int a = 0; a < jsonArrayPQ.length(); a++) {
                            JSONObject pq = jsonArrayPQ.getJSONObject(a);

                            String quotePQ = pq.getString("quotePQ");
                            String authorPQ = pq.getString("authorPQ");

                            mPQItem.add(new PQItem(authorPQ, quotePQ));
                        }

                        mPQAdapter = new PQAdapter(PersonsQuote.this, mPQItem, this);
                        mRecyclerViewPQ.setAdapter(mPQAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        mRequestQueuePQ.add(requestPQ);
    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menuPQ) {
        getMenuInflater().inflate(R.menu.menu_aq, menuPQ);
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

    @Override
    public void onItemClick(int position) {
        String langu = Locale.getDefault().getLanguage();

        if (langu.equals("de")) {
            String urlP = "https://lijukay.github.io/Quotes-M3/quotesGER.json";
            JsonObjectRequest requestP = new JsonObjectRequest(Request.Method.GET, urlP, null,
                    responseP -> {
                        try {
                            JSONArray jsonArrayP = responseP.getJSONArray(authorP);
                            Log.e("author", authorP);

                            JSONObject ec = jsonArrayP.getJSONObject(position);

                            String quoteE = ec.getString("quotePQ");
                            String authorP = ec.getString("authorPQ");

                            showDialogs(authorP, quoteE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace);
            mRequestQueuePQ.add(requestP);
        } else {
            String urlP = "https://lijukay.github.io/Quotes-M3/quotesEN.json";


            JsonObjectRequest requestP = new JsonObjectRequest(Request.Method.GET, urlP, null,
                    responseP -> {
                        try {
                            JSONArray jsonArrayP = responseP.getJSONArray(authorP);

                            JSONObject ec = jsonArrayP.getJSONObject(position);

                            String quoteE = ec.getString("quotePQ");
                            String authorP = ec.getString("authorPQ");

                            showDialogs(authorP, quoteE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace);
            mRequestQueuePQ.add(requestP);
        }
    }

    private void showDialogs(String author, String quote) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.dialog_quotes);

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