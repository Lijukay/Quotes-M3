package com.lijukay.quotesAltDesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class About extends AppCompatActivity {
    //global variables:
    int versionC;
    int versionA;
    String changelog;
    String versionName;
    String apkUrl;
    private RequestQueue mRequestQueueU;
    private SwipeRefreshLayout swipeRefreshLayoutAb;
    private final int PERMISSION_REQUEST_CODE_WRITE_EXTERNAL = 100;

    //Override on Create:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set Layout of this activity
        setContentView(R.layout.activity_about);
        //Find the toolbar
        Toolbar toolbar = findViewById(R.id.tlabout);
        //Set a Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);
        //Set a string for the version-Name
        String versionName = BuildConfig.VERSION_NAME;
        //Find the TextView by Id
        TextView textView = findViewById(R.id.versionCode);
        //Set the text of the TextView to the String with the variable versionName
        textView.setText(versionName);
        //Set a OnClickListener for the Telegram logo so people can reach me :3
        findViewById(R.id.telegram).setOnClickListener(view -> {
            //Set the url that leads to my Telegram Chat
            Uri uriT = Uri.parse("https://t.me/Lijukay");
            //create an Intent to lead to my Telegram Chat
            Intent intentT = new Intent(Intent.ACTION_VIEW, uriT);
            //start the intent
            startActivity(intentT);
        });
        //Set a OnClickListener for my GitHub-Profile
        findViewById(R.id.GitHub).setOnClickListener(view -> {
            //Set the url to my GitHub-Profile
            Uri uriG = Uri.parse("https://github.com/Lijukay");
            //Set the intent to lead to my GitHub-Page
            Intent intentG = new Intent(Intent.ACTION_VIEW,uriG);
            //Start the intent
            startActivity(intentG);
        });
        //Set the RefreshLayout by using the global variable that defines the RefreshLayout
        swipeRefreshLayoutAb = findViewById(R.id.swipeAbout);
        //Set a OnRefreshListener
        swipeRefreshLayoutAb.setOnRefreshListener(() -> {
            //Make a ToastMessage to inform users that the page is refreshing
            Toast.makeText(About.this, "Refreshing... please wait", Toast.LENGTH_SHORT).show();
            //Create a Handler
            new Handler().postDelayed(() -> {
                //Notify the widget that refresh state has changed.
                swipeRefreshLayoutAb.setRefreshing(false);
                //call the method parseJSONVersion each time the RefreshLayout is triggered
                parseJSONVersion();
            }, 2000);
        });
        //Creates a default instance of the worker pool and calls RequestQueue.start() on it.
        mRequestQueueU = Volley.newRequestQueue(this);
        //Call the parseJSONVersion-Method on create
        parseJSONVersion();
    }



    private void parseJSONVersion() {
        //Set the url where the Json-File is saved
        String urlU = "https://lijukay.github.io/PrUp/prUp.json";
        //A request for retrieving a JSONObject response body at a given URL, allowing for an optional JSONObject to be passed in as part of the request body.
        JsonObjectRequest requestU = new JsonObjectRequest(Request.Method.GET, urlU, null,
                responseU -> {
                    try {
                        JSONArray jsonArrayAll = responseU.getJSONArray("Quotes-M3");

                        for(int a = 0; a < jsonArrayAll.length(); a++){
                            JSONObject v = jsonArrayAll.getJSONObject(a);
                            versionC = BuildConfig.VERSION_CODE;
                            versionA = v.getInt("versionsCode");
                            apkUrl = v.getString("apkUrl");
                            changelog = v.getString("changelog");
                            versionName = v.getString("versionsName");
                        }
                        if (versionA > versionC){
                            Button update = findViewById(R.id.updateB);
                            TextView updateA = findViewById(R.id.updateT);

                            update.setVisibility(View.VISIBLE);
                            updateA.setVisibility(View.VISIBLE);



                            update.setOnClickListener(view -> {
                                if(ContextCompat.checkSelfPermission(About.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions(About.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE_WRITE_EXTERNAL);
                                } else {
                                    InstallUpdate(this, apkUrl, versionName);
                                }
                            });
                        }

                        Log.e("intent", "Refreshing completed...");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error", "JSON Exeption. Write me to tell me about that...");
                    }
                }, errorAll -> {
            errorAll.printStackTrace();
            Log.e("error", "Something went wrong... Contact me");
        });
        mRequestQueueU.add(requestU);
    }
    //Code by Yanndroid
    public static void InstallUpdate(Context context, String url, String versionName) {
        Log.e("path", context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + context.getString(R.string.app_name) + "." + versionName + ".apk");
        Log.e("url", url);
        String destination = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + context.getString(R.string.app_name) + "." + versionName + ".apk";
        Uri fileUri = Uri.parse("file://" + destination);
        Log.e("fileUri", fileUri.toString());

        File file = new File(destination);
        if (file.exists()) file.delete();
        Log.e("file", file.toString());
        Log.e("destination", destination);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setMimeType("application/vnd.android.package-archive");
        request.setTitle(context.getString(R.string.app_name) + " Update");
        request.setDescription(versionName);
        request.setDestinationUri(fileUri);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {

                Uri apkFileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(destination));
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                install.setDataAndType(apkFileUri, "application/vnd.android.package-archive");
                context.startActivity(install);

                context.unregisterReceiver(this);
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        downloadManager.enqueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menuab, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.ecAb){
            ECAb();
            return true;
        } else if(item.getItemId() == R.id.samsungdesignAb){
            SamsungDesign();
            return true;
        } else if(item.getItemId() == R.id.peopleAb){
            People();
            return true;
        } else if(item.getItemId() == R.id.allAb){
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
    private void People() {
        Intent intentP = new Intent(this, PersonsActivity.class);
        startActivity(intentP);
    }
    private void SamsungDesign() {
        Uri uriS = Uri.parse("https://github.com/Lijukay/Quotes-OneUI");
        Intent intentS = new Intent(Intent.ACTION_VIEW, uriS);
        startActivity(intentS);
    }
    private void ECAb() {
        Intent intentA = new Intent(this, MainActivity.class);
        startActivity(intentA);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_WRITE_EXTERNAL && (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
            showPermissionDialog();
        }
        if (requestCode == PERMISSION_REQUEST_CODE_WRITE_EXTERNAL && (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            InstallUpdate(this, apkUrl, versionName);
        }
    }

    private void showPermissionDialog() {
    }

}