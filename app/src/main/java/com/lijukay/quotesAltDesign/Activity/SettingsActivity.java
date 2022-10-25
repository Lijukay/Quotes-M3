package com.lijukay.quotesAltDesign.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lijukay.quotesAltDesign.BuildConfig;
import com.lijukay.quotesAltDesign.Others.InternetService;
import com.lijukay.quotesAltDesign.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Objects;

import rikka.material.preference.MaterialSwitchPreference;

public class SettingsActivity extends AppCompatActivity {

    public static final String BroadcastStringForAction = "checkInternet";
    private IntentFilter mIntentFilter;
    SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    static String updateA;

    public SettingsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Toolbar toolbar = findViewById(R.id.tlS);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(v -> onBackPressed());

        UiModeManager uiModeManager = (UiModeManager) this.getSystemService(Context.UI_MODE_SERVICE);
        int mode = uiModeManager.getNightMode();
        sharedPreferences = getSharedPreferences("NightMode", 0);
        editor = sharedPreferences.edit();
        boolean isNightMode = sharedPreferences.getBoolean("Night", false);
        if (isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (mode == UiModeManager.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadcastStringForAction);
        Intent serviceIntent = new Intent(this, InternetService.class);
        startService(serviceIntent);
        if(isOnline(getApplicationContext())){
            updateA = "Internet";
        } else {
            updateA = "No internet";
        }



    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private RequestQueue mRequestQueueU;
        private int versionC;
        private String apkUrl;
        private int versionA;
        private String versionName;
        private String changelogchange;
        private final int PERMISSION_REQUEST_CODE_WRITE_EXTERNAL = 100;






        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            AlertDialog bdialog;
            mRequestQueueU = Volley.newRequestQueue(requireActivity());
            parseJSONVersion();


            View alertCustomDialog = LayoutInflater.from(requireContext()).inflate(R.layout.bsdcolor, null);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            DisplayMetrics displayMetricsHalf = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetricsHalf);
            int bsdsizeHalf = displayMetrics.heightPixels / 2;


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
            alertDialog.setView(alertCustomDialog);
            bdialog = alertDialog.create();
            bdialog.getWindow().setGravity(Gravity.BOTTOM);
            bdialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            bdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, bsdsizeHalf);



            MaterialSwitchPreference darkMode = findPreference("nightDay");
            Preference update = findPreference("updateCheck");
            Preference share = findPreference("share");
            Preference feedback = findPreference("feedback");
            Preference bugReport = findPreference("reportBug");
            Preference feature = findPreference("feature");
            Preference permissions = findPreference("permission");
            Preference policy = findPreference("policy");
            Preference license = findPreference("license");
            Preference color = findPreference("color");


            color.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    showBSD();
                    return false;
                }

                private void showBSD() {
                    bdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    bdialog.show();


                }
            });

            assert darkMode != null;
            darkMode.setOnPreferenceChangeListener((preference, newValue) -> {
                if (!darkMode.isChecked()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("Night", true);
                    editor.apply();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("Night", false);
                    editor.apply();
                }
                return true;
            });

            assert bugReport != null;
            bugReport.setOnPreferenceClickListener(preference -> {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Report a bug")
                        .setMessage("How do you want to send a bug report? Chose between Telegram and E-Mail. Keep in mind, that every conversation WILL BE deleted after everything is done, EXCEPT you want to keep it.")
                        .setNeutralButton("Cancel", null)
                        .setPositiveButton("Telegram", (dialog, which) -> {
                            Uri telegram = Uri.parse("https://t.me/Lijukay");
                            startActivity(new Intent(Intent.ACTION_VIEW, telegram));
                        })
                        .setNegativeButton("E-Mail", (dialog, which) -> startActivity(composeEmail("lico.keins@gmail.com", getString(R.string.bugSubject), getString(R.string.bugMessage))))
                        .show();
                return false;
            });

            assert feature != null;
            feature.setOnPreferenceClickListener(preference -> {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Suggest a feature")
                        .setMessage("How do you want to send a feature suggestion? Chose between Telegram and E-Mail. Keep in mind, that every conversation WILL BE deleted after everything is done, EXCEPT you want to keep it.")
                        .setNeutralButton("Cancel", null)
                        .setPositiveButton("Telegram", (dialog, which) -> {
                            Uri telegram = Uri.parse("https://t.me/Lijukay");
                            startActivity(new Intent(Intent.ACTION_VIEW, telegram));
                        })
                        .setNegativeButton("E-Mail", (dialog, which) -> startActivity(composeEmail("lico.keins@gmail.com", getString(R.string.suggestionSubject), getString(R.string.suggestionMessage))))
                        .show();
                return false;
            });

            assert permissions != null;
            permissions.setOnPreferenceClickListener(preference -> {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("App's permissions")
                        .setMessage(getString(R.string.permissions))
                        .setCancelable(true)
                        .show();

                return false;
            });



            assert update != null;
            update.setOnPreferenceClickListener((preference -> {
                if(updateA.equals("No internet")){
                    Toast.makeText(requireContext(), "Can't check for update", Toast.LENGTH_SHORT).show();
                    Log.e("bhdh", "HNd");
                } else if (updateA.equals("No update")){
                    Log.e("no", "nu");
                    Toast.makeText(requireContext(), "No update available", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("update", "ja");
                    new MaterialAlertDialogBuilder(requireActivity())
                            .setTitle("Update")
                            .setMessage(changelogchange)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Install", (dialog, which) -> {
                                InstallUpdate(requireActivity(), apkUrl, versionName);
                            })
                            .show();
                }
                return false;
            }));

            assert policy != null;
            policy.setOnPreferenceClickListener(preference -> {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Privacy Policy")
                        .setMessage(getString(R.string.pp))
                        .setCancelable(true)
                        .show();
                return false;
            });

            assert license != null;
            license.setOnPreferenceClickListener(preference -> {
                Spanned licenseM = HtmlCompat.fromHtml(getString(R.string.licensemsg), 0);
                AlertDialog m = new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Licenses")
                        .setMessage(licenseM)
                        .setCancelable(true)
                        .show();
                ((TextView) Objects.requireNonNull(m.findViewById(android.R.id.message))).setMovementMethod(LinkMovementMethod.getInstance());
                return false;
            });

            assert share != null;
            share.setOnPreferenceClickListener(preference -> {
                Intent shareText = new Intent();
                shareText.setAction(Intent.ACTION_SEND);
                shareText.putExtra(Intent.EXTRA_TEXT, "https://github.com/Lijukay/Quotes-M3");
                shareText.setType("text/plain");
                Intent sendText = Intent.createChooser(shareText, null);
                startActivity(sendText);
                return false;
            });

            assert feedback != null;
            feedback.setOnPreferenceClickListener(preference -> {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Send feedback")
                        .setMessage("How do you want to send your feedback? Chose between Telegram and E-Mail. Keep in mind, that every conversation WILL BE deleted after everything is done, EXCEPT you want to keep it.")
                        .setNeutralButton("Cancel", null)
                        .setPositiveButton("Telegram", (dialog, which) -> {
                            Uri telegram = Uri.parse("https://t.me/Lijukay");
                            startActivity(new Intent(Intent.ACTION_VIEW, telegram));
                        })
                        .setNegativeButton("E-Mail", (dialog, which) -> startActivity(composeEmail("lico.keins@gmail.com", getString(R.string.feedbackSubject), getString(R.string.feedbackMessage))))
                        .show();
                return false;
            });
        }

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
                                updateA = "Update available";
                            } else {
                                updateA = "No update";
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace);

            mRequestQueueU.add(requestU);
        }
        @SuppressLint("MissingSuperCall")
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == PERMISSION_REQUEST_CODE_WRITE_EXTERNAL && (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                showPermissionDialog();
            }
            if (requestCode == PERMISSION_REQUEST_CODE_WRITE_EXTERNAL && (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                InstallUpdate(requireActivity(), apkUrl, versionName);
            }
        }

        private void showPermissionDialog() {
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.permissionRequired)
                    .setMessage(R.string.grantPermission)
                    .setNeutralButton("Grant permission", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        dialog.dismiss();
                        Toast.makeText(requireActivity(), getString(R.string.permissionGoTo), Toast.LENGTH_SHORT).show();
                    })
                    .setCancelable(true);

        }


    }

    public final BroadcastReceiver InternetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(BroadcastStringForAction)){
                if(intent.getStringExtra("online_status").equals("true")){

                } else {
                    updateA = "No internet";
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




    public static Intent composeEmail(String addresses, String subject, String messageE) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + addresses));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, messageE);
        return intent;
    }




}
