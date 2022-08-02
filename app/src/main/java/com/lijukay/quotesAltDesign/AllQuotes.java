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

public class AllQuotes extends AppCompatActivity {
    private ArrayList<AllRV> aqs;
    /*private static final String TAG = "AllQuotes";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quotes);
        Toolbar toolbarAQ = findViewById(R.id.tlall);
        setSupportActionBar(toolbarAQ);

        AQs();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menuAQ) {
        getMenuInflater().inflate(R.menu.menu_aq, menuAQ);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem itemAQ) {
        if(itemAQ.getItemId() == R.id.aboutA){
            AboutApp();
            return true;
        } else if(itemAQ.getItemId() == R.id.samsungdesignA){
            SamsungDesign();
            return true;
        } else if(itemAQ.getItemId() == R.id.PersonsA){
            People();
            return true;
        } else if(itemAQ.getItemId() == R.id.ecA){
            ECsA();
            return true;
        } else {
            return super.onOptionsItemSelected(itemAQ);
        }
    }

    private void ECsA() {
        Intent intentECsA = new Intent(this, MainActivity.class);
        startActivity(intentECsA);
    }

    private void People() {
        Intent intentPA = new Intent(this, Person.class);
        startActivity(intentPA);
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


    private void AQs() {
        RecyclerView recyclerViewAQ = findViewById(R.id.allQuotesRV);
        recyclerViewAQ.setLayoutManager(new LinearLayoutManager(this));
        aqs = new ArrayList<>();
        AllAdapter allAdapter = new AllAdapter(this, aqs);
        recyclerViewAQ.setAdapter(allAdapter);

        CreateDataForCardsAQ();
    }

    private void CreateDataForCardsAQ() {
        aqs.add(new AllRV("Das Leben geht weiter, ob mit dir oder ohne dich. Du siehst, niemand nimmt Rücksicht auf dich, also tu es auch nicht", "Lijukay"));
        aqs.add(new AllRV("Es ist sicher nicht einfach, das Leben, wie man es hat, zu leben. Dennoch tun es einige stolz. Ihr Geheimnis?: Sie achten nicht auf die anderen. Das macht sie nicht neidisch", "Lijukay"));

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