package edu.monash.fit2081.countryinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

public class CountryDetails extends AppCompatActivity {

    private TextView name;
    private TextView capital;
    private TextView code;
    private TextView population;
    private TextView area;
    private TextView language;
    private TextView currency;
    private Button button;
    private String URL;
    private ImageView flag;

    static final String SP_FILE = "SP_File";
    final String KEY_NAME = "Key_Name";
    final String KEY_CAPITAL = "Key_Capital";
    final String KEY_CODE = "Key_Code";
    final String KEY_POPULATION = "Key_Population";
    final String KEY_AREA = "Key_Area";
    final String KEY_LANGUAGE = "Key_Language";
    final String KEY_CURRENCY = "Key_Currency";
    final String KEY_BUTTON = "Key_Button";
    final String KEY_IMAGE = "Key_Image";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_details);

        getSupportActionBar().setTitle(R.string.title_activity_country_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String selectedCountry = getIntent().getStringExtra("country");

        name = findViewById(R.id.country_name);
        capital = findViewById(R.id.capital);
        code = findViewById(R.id.country_code);
        population = findViewById(R.id.population);
        area = findViewById(R.id.area);
        language = findViewById(R.id.languages);
        currency = findViewById(R.id.currencies);
        button = (Button) findViewById(R.id.wiki_button);
        flag = findViewById(R.id.image_view);

        sp = getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);

        String spName = sp.getString(KEY_NAME, "");
        name.setText(spName);

        String spCapital = sp.getString(KEY_CAPITAL, "");
        capital.setText(spCapital);

        String spCode = sp.getString(KEY_CODE, "");
        code.setText(spCode);

        int spPopulation = sp.getInt(KEY_POPULATION, 0);
        population.setText(spPopulation + "");

        double spArea = (double) sp.getFloat(KEY_AREA, 0);
        area.setText(spArea + "");

        String spCurrency = sp.getString(KEY_CURRENCY, "");
        currency.setText(spCurrency);

        String spLanguage = sp.getString(KEY_LANGUAGE, "");
        language.setText(spLanguage);

        String spButton = sp.getString(KEY_BUTTON, "");
        button.setText(spButton);
        
        ExecutorService executor = Executors.newSingleThreadExecutor(); //use this thread to update the ui
        //Executor handler = ContextCompat.getMainExecutor(this);
        Handler uiHandler=new Handler(Looper.getMainLooper());  //which is here


        //using the thread, we are executing the task
        executor.execute(() -> {
            //Background work here
            CountryInfo countryInfo = new CountryInfo();

            try {
                // Create URL
                URL webServiceEndPoint = new URL("https://restcountries.com/v2/name/" + selectedCountry); //

                // Create connection
                HttpsURLConnection myConnection = (HttpsURLConnection) webServiceEndPoint.openConnection();

                String line;

                if (myConnection.getResponseCode() == 200) {
                    //JSON data has arrived successfully, now we need to open a stream to it and get a reader
                    InputStream responseBody = myConnection.getInputStream();       //open stram
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");        //get reader

                    //now use a JSON parser to decode data
                    JsonReader jsonReader = new JsonReader(responseBodyReader);     //translate to Json
                    jsonReader.beginArray(); //consume arrays's opening JSON brace
                    String keyName;
                    // countryInfo = new CountryInfo(); //nested class (see below) to carry Country Data around in
                    boolean countryFound = false;       //by default false
                    while (jsonReader.hasNext() && !countryFound) { //process array of objects
                        jsonReader.beginObject(); //consume object's opening JSON brace
                        while (jsonReader.hasNext()) {// process key/value pairs inside the current object
                            keyName = jsonReader.nextName();
                            if (keyName.equals("name")) {
                                countryInfo.setName(jsonReader.nextString());
                                if (countryInfo.getName().equalsIgnoreCase(selectedCountry)) {
                                    countryFound = true;
                                }
                            } else if (keyName.equals("alpha3Code")) {
                                countryInfo.setAlpha3Code(jsonReader.nextString());
                            } else if (keyName.equals("capital")) {
                                countryInfo.setCapital(jsonReader.nextString());
                            } else if (keyName.equals("population")) {
                                countryInfo.setPopulation(jsonReader.nextInt());
                            } else if (keyName.equals("area")) {
                                countryInfo.setArea(jsonReader.nextDouble());
                            } else if (keyName.equals("alpha2Code")) {
                                countryInfo.setAlpha2Code(jsonReader.nextString());
                            } else if (keyName.equals("languages")){
                                collectLanguage(jsonReader, countryInfo);
                            } else if (keyName.equals("currencies")) {
                                collectCurrency(jsonReader, countryInfo);
                            }
                            else {
                                jsonReader.skipValue();
                            }
                        }
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();

                    String request = "https://countryflagsapi.com/png/" + countryInfo.getAlpha2Code();
                    java.net.URL url = new java.net.URL(request);
                    HttpsURLConnection connection = (HttpsURLConnection) url
                            .openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();

                    Bitmap myBitmap = BitmapFactory.decodeStream(input);

                    uiHandler.post(()->{
                        sp = getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();

                        name.setText(countryInfo.getName());
                        String cName = name.getText().toString();
                        editor.putString(KEY_NAME, cName);

                        capital.setText(countryInfo.getCapital());
                        String cCapital = capital.getText().toString();
                        editor.putString(KEY_CAPITAL, cCapital);

                        code.setText(countryInfo.getAlpha3Code());
                        String cCode = code.getText().toString();
                        editor.putString(KEY_CODE, cCode);

                        population.setText(Integer.toString(countryInfo.getPopulation()));
                        int cPopulation = Integer.parseInt(population.getText().toString());
                        editor.putInt(KEY_POPULATION, cPopulation);

                        area.setText(Double.toString(countryInfo.getArea()));
                        float cArea = Float.parseFloat(area.getText().toString());
                        editor.putFloat(KEY_AREA, cArea);

                        String countryLanguage = "";
                        for (int i = 0; i < countryInfo.getLanguages().size(); i++) {
                            countryLanguage += countryInfo.getLanguages().get(i);
                            if (i < countryInfo.getLanguages().size() - 1) {
                                countryLanguage += ", ";
                            }
                        }
                        language.setText(countryLanguage);
                        String cLanguage = language.getText().toString();
                        editor.putString(KEY_LANGUAGE, cLanguage);

                        String countryCurrency = "";
                        for (int i = 0; i < countryInfo.getCurrencies().size(); i++) {
                            countryCurrency += countryInfo.getCurrencies().get(i);
                            if (i < countryInfo.getCurrencies().size() - 1) {
                                countryCurrency += ", ";
                            }
                        }
                        currency.setText(countryCurrency);
                        String cCurrency = currency.getText().toString();
                        editor.putString(KEY_CURRENCY, cCurrency);


                        //website for wiki
                        button.setText("Wiki " + countryInfo.getName());
                        String cButton = button.getText().toString();
                        editor.putString(KEY_BUTTON, cButton);

                        URL = "https://en.wikipedia.org/wiki/" + countryInfo.getName();

                        //flag image
                        flag.setImageBitmap(myBitmap);

                        //apply shared preference
                        editor.apply();
                    });



                } else {
                    Log.i("INFO", "Error:  No response");
                }

                // All your networking logic should be here
            } catch (Exception e) {
                Log.i("INFO", "Error " + e.toString());
            }

        });

    }

    //added
    public void onWikiClick(View view) {


        Intent i = new Intent(CountryDetails.this, WebWiki.class);
        i.putExtra("URLKey", URL);
        startActivity(i);
    }


    private class CountryInfo {
        private String name;
        private String alpha3Code;
        private String capital;
        private int population;
        private double area;
        private String alpha2Code;
        private List<String> languages = new ArrayList<>();
        private List<String> currencies = new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAlpha3Code() {
            return alpha3Code;
        }

        public void setAlpha3Code(String alpha3Code) {
            this.alpha3Code = alpha3Code;
        }

        public String getCapital() {
            return capital;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }

        public int getPopulation() {
            return population;
        }

        public void setPopulation(int population) {
            this.population = population;
        }

        public double getArea() {
            return area;
        }

        public void setArea(double area) {
            this.area = area;
        }

        public String getAlpha2Code() {
            return alpha2Code;
        }

        public void setAlpha2Code(String alpha2Code) {
            this.alpha2Code = alpha2Code;
        }

        public List<String> getLanguages() {
            return languages;
        }

        public void setLanguages(String language) {
            languages.add(language);
        }

        public List<String> getCurrencies() {
            return currencies;
        }

        public void setCurrencies(String currency) {
            currencies.add(currency);
        }
    }

    //added
    public void collectLanguage(JsonReader jsonReader, CountryInfo countryInfo) throws Exception {
        jsonReader.beginArray();
        String keyLanguage;
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                keyLanguage = jsonReader.nextName();
                if (keyLanguage.equals("name")) {
                    countryInfo.setLanguages(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
        }
        jsonReader.endArray();

    }


    public void collectCurrency(JsonReader jsonReader, CountryInfo countryInfo) throws Exception {
        jsonReader.beginArray();
        String keyCurrencies;
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                keyCurrencies = jsonReader.nextName();
                if (keyCurrencies.equals("code")) {
                    countryInfo.setCurrencies(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
        }
        jsonReader.endArray();

    }


}
