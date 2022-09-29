package com.lijukay.quotesAltDesign;
//Start of Imports
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
//End of Imports

public class About extends AppCompatActivity {
    //------Start of defining global variables------//
    private String changelogchange, versionName, apkUrl, status;
    private RequestQueue mRequestQueueU;
    private SwipeRefreshLayout swipeRefreshLayoutAb;
    private int versionC, bsdsizeHalf, versionA;
    private final int PERMISSION_REQUEST_CODE_WRITE_EXTERNAL = 100;
    private RelativeLayout update;
    private TextView changesTexts, dialogTitle;
    private Button updateButtonCV;
    private AlertDialog dialog;
    private View alertCustomDialog;
    public static final String BroadcastStringForAction = "checkInternet";
    private IntentFilter mIntentFilter;
    //------End of defining global variables------//

    //------OnCreate------//
    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //------Set LayoutInflater for alertCustomDialog------//
        alertCustomDialog = LayoutInflater.from(About.this).inflate(R.layout.update_dialog, null);
        //------Find Views by their ID with a method------//
        FindViewById();
        //------Set a Volley RequestQueue------//
        mRequestQueueU = Volley.newRequestQueue(this);
        //------Parse the JSON------//
        parseJSONVersion();
        //------Set the SwipeRefreshLayout-Listener------//
        swipeRefreshLayoutAb.setOnRefreshListener(() -> {
            //------Showing a ToastMessage to inform the user that the Activity is updated------//
            Toast.makeText(About.this, getString(R.string.refreshing), Toast.LENGTH_SHORT).show();
            //------Create a handler------//
            new Handler().postDelayed(() -> {
                swipeRefreshLayoutAb.setRefreshing(false);
                //------Parse JSON again every swipe------//
                parseJSONVersion();
            }, 2000);
        });

        //------Getting the displayMetrics and setting half size for the BottomSheetDialog------//
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        DisplayMetrics displayMetricsHalf = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetricsHalf);
        bsdsizeHalf = displayMetrics.heightPixels / 2;

        //------Find the rest of the Views by their ID and set their OnClickListener------//
        findViewById(R.id.not).setOnClickListener(view -> showDialogC(getString(R.string.notes_of_thanks), getString(R.string.Thanks)));
        findViewById(R.id.bug).setOnClickListener(view -> composeEmail("lico.keins@gmail.com", getString(R.string.bugSubject), getString(R.string.bugMessage)));
        findViewById(R.id.feed).setOnClickListener(view -> composeEmail("lico.keins@gmail.com", getString(R.string.feedbackSubject), getString(R.string.feedbackMessage)));
        findViewById(R.id.requests).setOnClickListener(view -> composeEmail("lico.keins@gmail.com", getString(R.string.suggestionSubject), getString(R.string.suggestionMessage)));
        findViewById(R.id.privacy).setOnClickListener(view -> showDialogC(getString(R.string.privacy_policy), getString(R.string.pp)));
        findViewById(R.id.permission).setOnClickListener(view -> showDialogC(getString(R.string.app_permissions), getString(R.string.permissions)));
        findViewById(R.id.sharee).setOnClickListener(view -> {
            Intent shareText = new Intent();
            shareText.setAction(Intent.ACTION_SEND);
            shareText.putExtra(Intent.EXTRA_TEXT, "https://github.com/Lijukay/Quotes-M3");
            shareText.setType("text/plain");
            Intent sendText = Intent.createChooser(shareText, null);
            startActivity(sendText);
        });
        //findViewById(R.id.Status).setOnClickListener(view -> showAlertDialog(getString(R.string.status), status, "Okay"));


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(About.this);
        alertDialog.setView(alertCustomDialog);
        ImageButton close = alertCustomDialog.findViewById(R.id.close);
        changesTexts = alertCustomDialog.findViewById(R.id.changesText);
        updateButtonCV = alertCustomDialog.findViewById(R.id.cardUpdate);
        dialog = alertDialog.create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, bsdsizeHalf);


        close.setOnClickListener(view -> dialog.dismiss());

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadcastStringForAction);
        Intent serviceIntent = new Intent(this, InternetService.class);
        startService(serviceIntent);
        if(isOnline(getApplicationContext())){
            findViewById(R.id.update).setOnClickListener(view -> showAlertDialog(getString(R.string.update), changelogchange, getString(R.string.update)));
        } else {
            findViewById(R.id.update).setOnClickListener(view -> showDialogC(getString(R.string.noInternet), getString(R.string.noInternetMessage)));
        }

    }
    //------Find by ID------//
    private void FindViewById() {
        swipeRefreshLayoutAb = findViewById(R.id.swipeAbout);
        update = findViewById(R.id.update);
        dialogTitle = alertCustomDialog.findViewById(R.id.dialog_title);
    }
    //------Email------//
    public void composeEmail(String addresses, String subject, String messageE) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + addresses));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, messageE);
        startActivity(intent);
    }
    //------JSON-File------//
    private void parseJSONVersion() {
        String urlU = "https://lijukay.github.io/PrUp/prUp.json";

        JsonObjectRequest requestU = new JsonObjectRequest(Request.Method.GET, urlU, null,
                responseU -> {
                    try {
                        JSONArray jsonArrayAll = responseU.getJSONArray("Quotes-M3");

                        for (int a = 0; a < jsonArrayAll.length(); a++) {

                            JSONObject v = jsonArrayAll.getJSONObject(a);

                            versionC = BuildConfig.VERSION_CODE;
                            versionA = v.getInt("versionsCode");
                            apkUrl = v.getString("apkUrl");
                            changelogchange = v.getString("changelog");
                            versionName = v.getString("versionsName");
                        }

                        if (versionA > versionC) {
                            update.setOnClickListener(view -> showAlertDialog(getString(R.string.update), changelogchange, getString(R.string.update)));
                            Log.e("update", "update");
                        } else {
                            update.setOnClickListener(view -> showAlertDialog(getString(R.string.noUpdate), getString(R.string.noUpdate), null));
                            Log.e("updateno", "updateno");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);

        mRequestQueueU.add(requestU);
    }
    //------Dialogs------//
    private void showAlertDialog(String title, String changelogT, String buttonText) {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changesTexts.setText(changelogT);
        updateButtonCV.setText(buttonText);
        dialogTitle.setText(title);
        dialog.show();
        if (changelogT.equals(getString(R.string.noUpdate))) {
            updateButtonCV.setVisibility(View.GONE);
        } /*else if (title.equals(getString(R.string.status))){
            updateButtonCV.setVisibility(View.VISIBLE);
            updateButtonCV.setOnClickListener(view -> dialog.dismiss());
        }*/ else if (title.equals(getString(R.string.permissionRequired))){
            updateButtonCV.setVisibility(View.VISIBLE);
            updateButtonCV.setOnClickListener(view -> {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                dialog.dismiss();
                Toast.makeText(this, getString(R.string.permissionGoTo), Toast.LENGTH_SHORT).show();
            });
        } else if(title.equals("No internet")) {
            updateButtonCV.setVisibility(View.GONE);
        } else {
            updateButtonCV.setVisibility(View.VISIBLE);
            updateButtonCV.setOnClickListener(view -> {
                if(isOnline(getApplicationContext())){
                    if (ContextCompat.checkSelfPermission(About.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(About.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE_WRITE_EXTERNAL);
                    } else {
                        InstallUpdate(this, apkUrl, versionName);
                        dialog.dismiss();
                        Toast.makeText(this, getString(R.string.updateDownload), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                    showDialogC(getString(R.string.noInternet), getString(R.string.noInternetMessage));
                }
            });
        }
    }
    private void showDialogC(String titletext, String message) {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.bottomsheetdialog_default);

        TextView title = dialog.findViewById(R.id.titlebcs);
        title.setText(titletext);

        TextView change = dialog.findViewById(R.id.changes);
        change.setText(message);


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, bsdsizeHalf);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    private void showPermissionDialog() {
        showAlertDialog(getString(R.string.permissionRequired), getString(R.string.grantPermission), "Grant permission");
    }
    //------Installation of the update------//
    //Code by Yanndroid
    public static void InstallUpdate(Context context, String url, String versionName) {
        //------Set the destination as a string------//
        String destination = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + context.getString(R.string.app_name) + "." + versionName + ".apk";
        //------Set the file uri------//
        Uri fileUri = Uri.parse("file://" + destination);

        File file = new File(destination);

        if (file.exists()) //noinspection ResultOfMethodCallIgnored
            file.delete();

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
    //------Everything for the Menu------//
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menuab, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ecAb) {
            ECAb();
            return true;
        } else if (item.getItemId() == R.id.samsungdesignAb) {
            SamsungDesign();
            return true;
        } else if (item.getItemId() == R.id.peopleAb) {
            People();
            return true;
        } else if (item.getItemId() == R.id.allAb) {
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
    //------Code to check quick response of isOnline------//
    public final BroadcastReceiver InternetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(BroadcastStringForAction)){
                if(intent.getStringExtra("online_status").equals("true")){
                    if (versionA > versionC) {
                        update.setOnClickListener(view -> showAlertDialog(getString(R.string.update), changelogchange, getString(R.string.update)));
                    } else {
                        update.setOnClickListener(view -> showAlertDialog(getString(R.string.noUpdate), getString(R.string.noUpdate), null));
                    }
                } else {
                    findViewById(R.id.update).setOnClickListener(view -> showDialogC(getString(R.string.noInternet), getString(R.string.noInternetMessage)));
                }
            }
        }
    };
    public boolean isOnline(Context c){
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(InternetReceiver, mIntentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(InternetReceiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(InternetReceiver, mIntentFilter);
    }
    //------Permission Request check------//
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
}
