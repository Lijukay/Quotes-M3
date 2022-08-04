package com.lijukay.quotesAltDesign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        String versionName = BuildConfig.VERSION_NAME;

        TextView textView = findViewById(R.id.versionCode);
        textView.setText(versionName);
    }
}