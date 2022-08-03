package com.lijukay.quotesAltDesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<EC> ecs;
    /*private static final String TAG = "MainActivity";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.tl);
        setSupportActionBar(toolbar);

        ECs();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.about){
            AboutApp();
            return true;
        } else if(item.getItemId() == R.id.samsungdesign){
            SamsungDesign();
            return true;
        } else if(item.getItemId() == R.id.people){
            People();
            return true;
        } else if(item.getItemId() == R.id.all){
            All();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void All() {
        Intent intentAM = new Intent(this, AllQuotes.class);
        startActivity(intentAM);
    }

    private void People() {
        Intent intentP = new Intent(this, Person.class);
        startActivity(intentP);
    }

    private void SamsungDesign() {
        //On click: Samsung Design
        Uri uriS = Uri.parse("https://github.com/Lijukay/Quotes");
        Intent intentS = new Intent(Intent.ACTION_VIEW, uriS);
        startActivity(intentS);
    }


    private void AboutApp() {
        //On click About
        Intent intentA = new Intent(this, About.class);
        startActivity(intentA);
        Log.e("intent","about");
    }


    private void ECs() {
        RecyclerView recyclerView = findViewById(R.id.editorsChoiceRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ecs = new ArrayList<>();
        ECAdapter ecAdapter = new ECAdapter(this, ecs);
        recyclerView.setAdapter(ecAdapter);

        InputECs();
    }

    private void InputECs() {

    }


}