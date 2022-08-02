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

import java.util.ArrayList;

public class Person extends AppCompatActivity {
    private ArrayList<PersonRV> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Toolbar toolbarP = findViewById(R.id.tlPersons);
        setSupportActionBar(toolbarP);

        Persons();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menuP) {
        getMenuInflater().inflate(R.menu.menu_p, menuP);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem itemP) {
        if(itemP.getItemId() == R.id.aboutP){
            AboutAppP();
            return true;
        } else if(itemP.getItemId() == R.id.samsungdesignP){
            SamsungDesignP();
            return true;
        } else if(itemP.getItemId() == R.id.ec){
            ECsP();
            return true;
        } else if(itemP.getItemId() == R.id.allP){
            AllP();
            return true;
        } else {
            return super.onOptionsItemSelected(itemP);
        }
    }

    private void AllP() {
        Intent intentALLP = new Intent(this, AllQuotes.class);
        startActivity(intentALLP);
    }


    private void ECsP() {
        Intent intentECP = new Intent(this, MainActivity.class);
        startActivity(intentECP);
    }

    private void SamsungDesignP() {
        //On click: Samsung Design
        Uri uriSP = Uri.parse("https://github.com/Lijukay/Quotes");
        Intent intentSP = new Intent(Intent.ACTION_VIEW, uriSP);
        startActivity(intentSP);
    }


    private void AboutAppP() {
        //On click About
        Intent intentAP = new Intent(this, About.class);
        startActivity(intentAP);
        Log.e("intent","about");
    }
    private void Persons() {
        RecyclerView recyclerViewP = findViewById(R.id.personsRV);
        recyclerViewP.setLayoutManager(new LinearLayoutManager(this));
        persons = new ArrayList<>();
        PersonAdapter personAdapter = new PersonAdapter(this, persons);
        recyclerViewP.setAdapter(personAdapter);

        CreateDataForCardsP();
    }

    private void CreateDataForCardsP() {
        persons.add(new PersonRV("Lijukay"));
        persons.add(new PersonRV("Keine Ahnung, test?"));

        /*try {
            String jsonDataString = readJSONDataFromFile();
            JSONArray jsonArray = new JSONArray(jsonDataString);

            for (int i = 0; i < jsonArray.length(); ++i){
                JSONObject itemObj = jsonArray.getJSONObject(i);
                String quote = itemObj.getString("quote");
                String author = itemObj.getString("author");

                EC ec = new EC(quote, author);
                ecs.add(ec);
            }

        }catch (JSONException | IOException e){

            Log.d(TAG, "addItemsFromJSON", e);
        }*/
    }
    /*private String readJSONDataFromFile() throws IOException{
        InputStream inputStream = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            String jsonString = null;
            inputStream = getResources().openRawResource(R.raw.editorschoice);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((jsonString = bufferedReader.readLine()) != null){
                stringBuilder.append(jsonString);
            }
        } finally {

            if(inputStream != null){
                inputStream.close();
            }

        }

        return new String(stringBuilder);
    }*/
}