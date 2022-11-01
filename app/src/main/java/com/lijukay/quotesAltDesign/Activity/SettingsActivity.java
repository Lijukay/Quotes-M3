package com.lijukay.quotesAltDesign.Activity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lijukay.quotesAltDesign.BuildConfig;
import com.lijukay.quotesAltDesign.Others.InternetService;
import com.lijukay.quotesAltDesign.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

import rikka.material.preference.MaterialSwitchPreference;

public class SettingsActivity extends AppCompatActivity {

    //------Static vars because I need them outside of the Settings Fragment------//
    public static final String BroadcastStringForAction = "checkInternet";
    private IntentFilter mIntentFilter;
    SharedPreferences sharedPreferencesNightMode, sharedPreferencesColorTheme, sharedPreferencesLanguage;
    static SharedPreferences.Editor editorNightMode, editorColorTheme, editorLanguages;
    static String updateStatus;
    static boolean internet;
    static Intent starterIntent;

    //------Precreated Method for I don't know------//
    public SettingsActivity() {
    }

    //------onCreate gets called when the Activity starts------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //------Checking, if DarkTheme is active or not BEFORE the view is created in order to prevent ugly transitions on...------//
        //------...restart of the method when changing the mode------//
        //------Getting the SharedPreference with the name "NightMode" to get its value (true or false)------//
        //------sharedPreferences Mode is 0 (aka. MODE_PRIVATE)------/
        sharedPreferencesNightMode = getSharedPreferences("NightMode", 0);
        //------creating an editor for sharedPreferences because we need to change its value in this activity------//
        editorNightMode = sharedPreferencesNightMode.edit();
        //------Getting the UI mode to see if systems NightMode is active or not------//
        UiModeManager uiModeManager = (UiModeManager) this.getSystemService(Context.UI_MODE_SERVICE);
        int mode = uiModeManager.getNightMode();
        //------set boolean isNightMode to the value of the boolean of sharedPreferences------//
        boolean isNightMode = sharedPreferencesNightMode.getBoolean("Night", false);
        //------Check if the NightMode is activated in our app------//
        if (isNightMode){
            //------It is activated so the theme of this app is set to NightMode------//
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (mode == UiModeManager.MODE_NIGHT_YES) {
            //------It is not active so the theme of this app is set to LightMode------//
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        //------Getting sharedPreferences2 BEFORE creating any views so that the color theme is applied without ugly transitions------//
        sharedPreferencesColorTheme = getSharedPreferences("Theme", 0);
        //------Creating an editor for sharedPreferences2 because in this activity we need to change its value------//
        editorColorTheme = sharedPreferencesColorTheme.edit();
        //------Getting the String-Value of the sharedPreferences2 to activate the color theme it refers to------//
        String theme = sharedPreferencesColorTheme.getString("Theme", "red");
        if (theme.equals("red")){
            //------String-Value of sharedPreferences2 is red so the default AppTheme is going to be used------//
            setTheme(R.style.AppTheme);
        } else if (theme.equals("purple")){
            //------String-Value of sharedPreferences2 is purple so AppThemePurple is going to be used------//
            setTheme(R.style.AppThemePurple);
        }
        //------Finding sharedPreferences3 by its name Language------//
        sharedPreferencesLanguage = getSharedPreferences("Language", 0);
        //------Creating an editor for sharedPreferences3 because we need to change its value------//
        editorLanguages = sharedPreferencesLanguage.edit();
        //------Creating an Intent in this activity to call it later. It is used instead of recreate(); because I know how to add transitions...------//
        //------...to Intents but not how to add transitions to recreate();------//
        starterIntent = getIntent();
        super.onCreate(savedInstanceState);
        //------The view this app refers to is settings_activity. It can be found on res < layout------//
        setContentView(R.layout.settings_activity);
        //------Precreated things i don't really understand. I don't touch it without any tutorials------//
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        //------According to Android Studios Explanation:...------//
        //------...Support library version of android.app.Activity.getActionBar. ...------//
        //------...Retrieve a reference to this activity's ActionBar. ...------//
        //------...Returns:...------//
        //------...The Activity's ActionBar, or null if it does not have one.------//
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //------If it has an ActionBar, something I don't understand happens------//
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //------Finding Activity's toolbar by its ID (tls)------//
        Toolbar toolbar = findViewById(R.id.tlS);
        //------This Toolbar acts like an ActionBar for this activity window------//
        setSupportActionBar(toolbar);
        //-------When the toolbar is clicked, it acts like the Back Key on Android------//
        toolbar.setOnClickListener(v -> onBackPressed());


        //------Creating an IntentFilter. We need it to check if the Internet connection still exists to prevent app crashes on specific events------//
        mIntentFilter = new IntentFilter();
        //------Action of this IntentFilter: Checking the internet------//
        mIntentFilter.addAction(BroadcastStringForAction);
        //------Referring to the class where the service is written down and starting the service------//
        Intent serviceIntent = new Intent(this, InternetService.class);
        startService(serviceIntent);
        //------Checking if the Application is online------//
        internet = isOnline(getApplicationContext());
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private RequestQueue mRequestQueueU;
        private int versionC;
        private String apkUrl;
        private int versionA;
        private String versionName;
        private String changelogMessage;


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            //------Refer to where the Preferences are saved------//
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            //------Creating two AlertDialogs. One for language and one for color theme------//
            AlertDialog colorDialog;
            AlertDialog languageDialog;
            //------Creating a requestQueue. It was necessary but I am not sure what it does------//
            mRequestQueueU = Volley.newRequestQueue(requireActivity());
            //------Starts the parseJSONVersion-Method------//
            parseJSONVersion();
            //------Setting the view of the AlertDialogs to the layouts they should use------//
            View alertCustomDialog = LayoutInflater.from(requireContext()).inflate(R.layout.bsdcolor, null);
            View alertCustomDialogL = LayoutInflater.from(requireContext()).inflate(R.layout.dialoglang, null);
            //------Getting the half of the size of the users display------//
            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            DisplayMetrics displayMetricsHalf = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetricsHalf);
            int bsdsizeHalf = displayMetrics.heightPixels / 2;

            //------Build the alertDialogs: Gravity at the bottom, Animations, Sizes------//
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
            alertDialog.setView(alertCustomDialog);
            colorDialog = alertDialog.create();
            colorDialog.getWindow().setGravity(Gravity.BOTTOM);
            colorDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            colorDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, bsdsizeHalf);

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(requireContext());
            alertDialog2.setView(alertCustomDialogL);
            languageDialog = alertDialog2.create();
            languageDialog.getWindow().setGravity(Gravity.BOTTOM);
            languageDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            languageDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            //------Finding preferences is different from finding view although both are views...------//
            //------...instead of using findByViewId with the view's ID, you are using findPreference with a key you gave the view in the xml-file------//
            MaterialSwitchPreference darkModePreference = findPreference("nightDay");
            Preference updatePreference = findPreference("updateCheck");
            Preference sharePreference = findPreference("share");
            Preference feedbackPreference = findPreference("feedback");
            Preference bugReportPreference = findPreference("reportBug");
            Preference featureSuggestionPreference = findPreference("feature");
            Preference permissionsPreference = findPreference("permission");
            Preference policyPreference = findPreference("policy");
            Preference licensePreference = findPreference("license");
            Preference colorPreference = findPreference("color");
            Preference languagePreference = findPreference("language");
            //------Finding the sharedPreference once more because we need to set the text of the switch based on DarkMode being active or not------//
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("NightMode", 0);
            boolean isNightMode = sharedPreferences.getBoolean("Night", false);
            assert darkModePreference != null;
            if (isNightMode){
                //------Is active so text is Night-Mode on------//
                darkModePreference.setSummary(R.string.non);
            } else {
                //------Is not active so text is Night-Mode off------//
                darkModePreference.setSummary(R.string.noff);
            }
            //------Getting sharedPreferences2 to display the current active theme------//
            SharedPreferences sharedPreferences2 = requireActivity().getSharedPreferences("Theme", 0);
            String theme = sharedPreferences2.getString("Theme", "red");
            if (theme.equals("red")){
                assert colorPreference != null;
                colorPreference.setSummary(R.string.colorred);
            } else if (theme.equals("purple")){
                assert colorPreference != null;
                colorPreference.setSummary(R.string.colorpurple);
            }
            //------Getting sharedPreferencesLanguage to display current language------//
            SharedPreferences sharedPreferencesLanguage = requireActivity().getSharedPreferences("Language", 0);
            String lang = sharedPreferencesLanguage.getString("Language", Locale.getDefault().getLanguage());
            if (lang.equals("de")){
                //------lang is de so the summary says: Aktuelle Sprache ist: Deutsch------//
                assert languagePreference != null;
                languagePreference.setSummary(R.string.lande);
            } else if (lang.equals("en")){
                //------lang is en so the summary says: Current language is: English------//
                assert languagePreference != null;
                languagePreference.setSummary(R.string.lande);
            } else {
                //------lang is something else so it is not officially supported. Because of this the language is set to English------//
                assert languagePreference != null;
                languagePreference.setSummary(R.string.notsupported);
            }
            //------Set what happens when the preference is clicked------//
            languagePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    showLanguageDialog();
                    return false;
                }

                private void showLanguageDialog() {
                    languageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    languageDialog.show();

                    CardView deutschCardView = languageDialog.findViewById(R.id.deutsch);
                    CardView englishCardView = languageDialog.findViewById(R.id.english);

                    assert deutschCardView != null;
                    deutschCardView.setOnClickListener(v -> {
                        requireActivity().startActivity(new Intent(requireActivity(), AllActivity.class));
                        requireActivity().overridePendingTransition(rikka.core.R.anim.fade_in, rikka.core.R.anim.fade_out);
                        requireActivity().finishAffinity();
                        editorLanguages.putString("Language", "de");
                        editorLanguages.apply();
                    });

                    assert englishCardView != null;
                    englishCardView.setOnClickListener(v -> {
                        requireActivity().startActivity(new Intent(requireActivity(), AllActivity.class));
                        requireActivity().overridePendingTransition(rikka.core.R.anim.fade_in, rikka.core.R.anim.fade_out);
                        requireActivity().finishAffinity();
                        editorLanguages.putString("Language", "en");
                        editorLanguages.apply();
                    });
                }
            });


            assert colorPreference != null;
            colorPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    showBSD();
                    return false;
                }

                private void showBSD() {
                    colorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    colorDialog.show();
                    CardView redCardView = colorDialog.findViewById(R.id.themeRed);
                    CardView purpleCardView = colorDialog.findViewById(R.id.themePurple);
                    assert redCardView != null;
                    redCardView.setOnClickListener(v -> {
                        requireActivity().startActivity(new Intent(requireActivity(), AllActivity.class));
                        requireActivity().overridePendingTransition(rikka.core.R.anim.fade_in, rikka.core.R.anim.fade_out);
                        requireActivity().finishAffinity();
                        editorColorTheme.putString("Theme", "red");
                        editorColorTheme.apply();

                    });
                    assert purpleCardView != null;
                    purpleCardView.setOnClickListener(v -> {
                        requireActivity().startActivity(new Intent(requireActivity(), AllActivity.class));
                        requireActivity().overridePendingTransition(rikka.core.R.anim.fade_in, rikka.core.R.anim.fade_out);
                        requireActivity().finishAffinity();
                        editorColorTheme.putString("Theme", "purple");
                        editorColorTheme.apply();
                    });
                }
            });

            darkModePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                editorNightMode.putBoolean("Night", !darkModePreference.isChecked());
                editorNightMode.apply();
                requireActivity().finish();
                requireActivity().startActivity(starterIntent);
                requireActivity().overridePendingTransition(rikka.core.R.anim.fade_in, rikka.core.R.anim.fade_out);
                return true;
            });

            assert bugReportPreference != null;
            bugReportPreference.setOnPreferenceClickListener(preference -> {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle(R.string.bugtit)
                        .setMessage(R.string.bugmes)
                        .setNeutralButton(R.string.neuBut, null)
                        .setPositiveButton(R.string.posBut, (dialog, which) -> {
                            Uri telegram = Uri.parse("https://t.me/Lijukay");
                            startActivity(new Intent(Intent.ACTION_VIEW, telegram));
                        })
                        .setNegativeButton(R.string.negBut, (dialog, which) -> startActivity(composeEmail("lico.keins@gmail.com", getString(R.string.bugSubject), getString(R.string.bugMessage))))
                        .show();
                return false;
            });

            assert featureSuggestionPreference != null;
            featureSuggestionPreference.setOnPreferenceClickListener(preference -> {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle(R.string.feasugTit)
                        .setMessage(R.string.feasugMes)
                        .setNeutralButton(R.string.neuBut, null)
                        .setPositiveButton(R.string.posBut, (dialog, which) -> {
                            Uri telegram = Uri.parse("https://t.me/Lijukay");
                            startActivity(new Intent(Intent.ACTION_VIEW, telegram));
                        })
                        .setNegativeButton(R.string.negBut, (dialog, which) -> startActivity(composeEmail("lico.keins@gmail.com", getString(R.string.suggestionSubject), getString(R.string.suggestionMessage))))
                        .show();
                return false;
            });

            assert permissionsPreference != null;
            permissionsPreference.setOnPreferenceClickListener(preference -> {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle(R.string.perTit)
                        .setMessage(getString(R.string.permissions))
                        .setCancelable(true)
                        .show();

                return false;
            });



            assert updatePreference != null;
            updatePreference.setOnPreferenceClickListener((preference -> {
                if(!internet){
                    Toast.makeText(requireContext(), R.string.cfuna, Toast.LENGTH_SHORT).show();
                } else if (updateStatus.equals("No update")){
                    Toast.makeText(requireContext(), R.string.nua, Toast.LENGTH_SHORT).show();
                } else {
                    new MaterialAlertDialogBuilder(requireActivity())
                            .setTitle("Update")
                            .setMessage(changelogMessage)
                            .setNegativeButton(R.string.neuBut, (dialog, which) -> dialog.dismiss())
                            .setPositiveButton(R.string.posButUp, (dialog, which) -> InstallUpdate(requireActivity(), apkUrl, versionName))
                            .show();
                }
                return false;
            }));

            assert policyPreference != null;
            policyPreference.setOnPreferenceClickListener(preference -> {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle(R.string.privacy_policy)
                        .setMessage(getString(R.string.pp))
                        .setCancelable(true)
                        .show();
                return false;
            });

            assert licensePreference != null;
            licensePreference.setOnPreferenceClickListener(preference -> {
                Spanned licenseM = HtmlCompat.fromHtml(getString(R.string.licensemsg), 0);
                AlertDialog m = new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle(R.string.License)
                        .setMessage(licenseM)
                        .setCancelable(true)
                        .show();
                ((TextView) Objects.requireNonNull(m.findViewById(android.R.id.message))).setMovementMethod(LinkMovementMethod.getInstance());
                return false;
            });

            assert sharePreference != null;
            sharePreference.setOnPreferenceClickListener(preference -> {
                Intent shareText = new Intent();
                shareText.setAction(Intent.ACTION_SEND);
                shareText.putExtra(Intent.EXTRA_TEXT, "https://github.com/Lijukay/Quotes-M3");
                shareText.setType("text/plain");
                Intent sendText = Intent.createChooser(shareText, null);
                startActivity(sendText);
                return false;
            });

            assert feedbackPreference != null;
            feedbackPreference.setOnPreferenceClickListener(preference -> {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle(R.string.feedback)
                        .setMessage(R.string.feedmes)
                        .setNeutralButton(R.string.neuBut, null)
                        .setPositiveButton(R.string.posBut, (dialog, which) -> {
                            Uri telegram = Uri.parse("https://t.me/Lijukay");
                            startActivity(new Intent(Intent.ACTION_VIEW, telegram));
                        })
                        .setNegativeButton(R.string.negBut, (dialog, which) -> startActivity(composeEmail("lico.keins@gmail.com", getString(R.string.feedbackSubject), getString(R.string.feedbackMessage))))
                        .show();
                return false;
            });
        }

        private void parseJSONVersion() {
            String urlU = "https://lijukay.github.io/PrUp/prUp.json";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlU, null,
                    jsonObject -> {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("Quotes-M3");

                            for (int a = 0; a < jsonArray.length(); a++) {

                                JSONObject v = jsonArray.getJSONObject(a);

                                versionC = BuildConfig.VERSION_CODE;
                                versionA = v.getInt("versionsCode");
                                apkUrl = v.getString("apkUrl");
                                changelogMessage = v.getString("changelog");
                                versionName = v.getString("versionsName");
                            }

                            if (versionA > versionC) {
                                updateStatus = "Update available";
                            } else {
                                updateStatus = "No update";
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace);

            mRequestQueueU.add(jsonObjectRequest);
        }


        @SuppressWarnings("deprecation")
        @SuppressLint({"MissingSuperCall"})
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            int PERMISSION_REQUEST_CODE_WRITE_EXTERNAL = 100;
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
                    .setNeutralButton(R.string.neuButGP, (dialog, which) -> {
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
                internet = intent.getStringExtra("online_status").equals("true");
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
