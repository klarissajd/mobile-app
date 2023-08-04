package com.example.movieroomcp_b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView tV;
    Button btn_add;
    Button btn_remove_some;
    Button btn_get_no;

    public static final String COLUMN_TITLE = "movieTitle";
    public static final String COLUMN_YEAR = "movieYear";
    public static final String COLUMN_COUNTRY = "movieCountry";
    public static final String COLUMN_GENRE = "movieGenre";
    public static final String COLUMN_COST = "movieCost";
    public static final String COLUMN_KEYWORD = "movieKeyword";

    public static final String AUTHORITY = "content://fit2081.app.KLARISSA/movies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tV = findViewById(R.id.number_count);
    }

    public void onGetNumberMovieButton(View view) {
        Uri uri = Uri.parse(AUTHORITY);
        Cursor result = getContentResolver().query(uri, null, null, null);
        tV.setText(result.getCount() + "");
    }

    public void onDeleteCertainMovieButton(View view) {
        Uri uri = Uri.parse(AUTHORITY);
        String [] args = {"1500"};
        String selection = "movieCost < ?";
        getContentResolver().delete(uri, selection, args);
    }

    public void onAddRandomMovieButton(View view) {
        Uri uri = Uri.parse(AUTHORITY);
        ContentValues values = new ContentValues();
        Random random = new Random();

        String[] cTitle = {"Minion", "Encanto", "The Ring", "Friends", "Avengers: The End Game", "Flipped", "High School Musical", "The Notebook", "Conjuring", "Anne of Green Gables"};
        int[] cYear = {2022, 1950, 2006, 2000, 1997, 1942, 2011, 1989, 1974, 1950};
        String[] cCountry = {"USA", "UK", "Australia", "China", "South Korea", "Taiwan", "Mexico", "Russia", "Indonesia", "India"};
        String[] cGenre = {"Horror", "Fantasy", "Science Fiction", "Romance", "Cartoon", "Thriller", "Action", "Musical", "History", "Mystery"};
        float[] cCost = {1550, 1500, 2100, 1000, 950, 1700, 890, 1230, 2700, 2350};
        String[] cKeyword = {"slice of life", "suitable for children", "not suitable for children", "a remake", "adaptations from novel", "take from real-life experience", "trigger warning for blood", "created by disney", "contemporary", "jump scare warning"};

        int rTitle = random.nextInt(10);
        int rYear = random.nextInt(10);
        int rCountry = random.nextInt(10);
        int rGenre = random.nextInt(10);
        int rCost = random.nextInt(10);
        int rKeyword = random.nextInt(10);

        values.put(COLUMN_TITLE, cTitle[rTitle]);
        values.put(COLUMN_YEAR, cYear[rYear]);
        values.put(COLUMN_COUNTRY, cCountry[rCountry]);
        values.put(COLUMN_GENRE, cGenre[rGenre]);
        values.put(COLUMN_COST, cCost[rCost]);
        values.put(COLUMN_KEYWORD, cKeyword[rKeyword]);
        getContentResolver().insert(uri, values);

    }
}