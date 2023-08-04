package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView tempDisplay;
    TextView precipitationDisplay;
    TextView humidityDisplay;
    TextView windDisplay;
    RequestQueue requestQueue;
    EditText locationText;
    String newLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempDisplay = findViewById(R.id.temp_id);
        precipitationDisplay = findViewById(R.id.precipitation_id);
        humidityDisplay = findViewById(R.id.humidity_id);
        windDisplay = findViewById(R.id.wind_id);
        locationText = findViewById(R.id.location_id);
        Button buttonWeather = findViewById(R.id.button);

    }

    public void onWeatherClick(View view) {
        makeRequest();
    }

    public void makeRequest() {
        String weatherLocation = locationText.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.weatherapi.com/v1/current.json?key=cde75afa40ce4179b2781925223003&q=" + weatherLocation;

        //Toast.makeText(this, url, Toast.LENGTH_LONG).show();
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String currentTemp = response.getJSONObject("current").getString("temp_c");
                    ((TextView) findViewById(R.id.temp_id)).setText(currentTemp);
                    String currentPrecipitation = response.getJSONObject("current").getString("precip_mm");
                    ((TextView) findViewById(R.id.precipitation_id)).setText(currentPrecipitation + "%");
                    String currentHumidity = response.getJSONObject("current").getString("humidity");
                    ((TextView) findViewById(R.id.humidity_id)).setText(currentHumidity + "%");
                    String currentWind = response.getJSONObject("current").getString("wind_kph");
                    ((TextView) findViewById(R.id.wind_id)).setText(currentWind);
                    //Toast.makeText(this, url, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("weather", error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
}