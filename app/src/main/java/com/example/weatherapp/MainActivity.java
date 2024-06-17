package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView TempTextView;
    TextView textView6;
    TextView WeatherTextView;
    TextView CityTextView;
    ImageView ImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TempTextView = findViewById(R.id.TempTextView);
        textView6 = findViewById(R.id.textView6);
        WeatherTextView = findViewById(R.id.WeatherTextView);
        CityTextView = findViewById(R.id.CityTextView);
        ImageView = findViewById(R.id.ImageView);
        textView6.setText(getCurrentDate());

        // Create a new RequestQueue instance
        RequestQueue queue = Volley.newRequestQueue(this);

        // Update the URL to include units=metric for Celsius
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Bengaluru,IN&units=metric&appid=785d77cb166a12b97e2a63eeedd77853";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("WEATHER", "Response: " + response.toString());
                        try {
                            JSONObject mainJSONObject = response.getJSONObject("main");
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject firstWeatherObject = weatherArray.getJSONObject(0);

                            String temp = Integer.toString((int) Math.round(mainJSONObject.getDouble("temp")));
                            String weatherDescription = firstWeatherObject.getString("description");
                            String city = response.getString("name");
                            TempTextView.setText(temp + "°C");
                            WeatherTextView.setText(weatherDescription);
                            CityTextView.setText(city);

                            int iconResourceId = getResources().getIdentifier("icon_" + weatherDescription.replace(" ", ""), "drawable", getPackageName());
                            if (iconResourceId != 0) {
                                ImageView.setImageResource(iconResourceId);
                            } else {
                                ImageView.setImageResource(R.drawable.icon_scatteredclouds); // Default icon in case of an unknown description
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("WEATHER", "Error: " + error.toString());
                    }
                });

        // Add the request to the RequestQueue
        queue.add(jsObjRequest);
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        return dateFormat.format(calendar.getTime());
    }
}
