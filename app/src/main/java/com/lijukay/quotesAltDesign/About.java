package com.lijukay.quotesAltDesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.tlabout);
        setSupportActionBar(toolbar);

        String versionName = BuildConfig.VERSION_NAME;

        TextView textView = findViewById(R.id.versionCode);
        textView.setText(versionName);

        findViewById(R.id.telegram).setOnClickListener(view -> {
            Uri uriT = Uri.parse("https://t.me/Lijukay");
            Intent intentT = new Intent(Intent.ACTION_VIEW, uriT);
            startActivity(intentT);
        });

        findViewById(R.id.GitHub).setOnClickListener(view -> {
            Uri uriG = Uri.parse("https://github.com/Lijukay");
            Intent intentG = new Intent(Intent.ACTION_VIEW,uriG);
            startActivity(intentG);

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        Uri uriS = Uri.parse("https://github.com/Lijukay/Quotes");
        Intent intentS = new Intent(Intent.ACTION_VIEW, uriS);
        startActivity(intentS);
    }
    private void ECAb() {
        Intent intentA = new Intent(this, MainActivity.class);
        startActivity(intentA);
    }

}