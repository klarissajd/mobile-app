package com.example.movielibraryweek5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.movielibraryweek5.provider.Movie;
import com.example.movielibraryweek5.provider.MovieViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    EditText titleText;
    EditText yearText;
    EditText countryText;
    EditText genreText;
    EditText costText;
    EditText keywordsText;

    final String Log_Tag = "myLifeCycle";
    SharedPreferences sp;
    final String KEY_TITLE = "Key_Title";
    final String KEY_YEAR = "Key_Year";
    final String KEY_COUNTRY = "Key_Country";
    final String KEY_GENRE = "Key_Genre";
    final String KEY_COST = "Key_Cost";
    final String KEY_KEYWORDS = "Key_Keywords";
    final String SP_FILE = "SP_File";

    //LAB 5
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    Toolbar toolbar;

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private ListView myListView;

    //LAB 6
    ArrayList<Movie> movieData = new ArrayList<>();

    //LAB 7
    private MovieViewModel mMovieViewModel;

    //LAB 8
    FirebaseDatabase db;
    DatabaseReference ref;
    ArrayAdapter<Movie> movieArrayAdapter;

    //LAB 10
    View gesturesArea;
    final String TAG = "WEEK_10_LAB";
    int xStart, yStart, xEnd, yEnd;
    String swipeMessage = "";

    //LAB 11
    final String WEEK11TAG = "WEEK_11_LAB";
    private GestureDetectorCompat mDetector;
    //private ScaleGestureDetector mScaleDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Previous Weeks
        Log.d(Log_Tag, "This is onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);

        titleText = findViewById(R.id.title_id);
        yearText = findViewById(R.id.year_id);
        countryText = findViewById(R.id.countryTextView);
        genreText = findViewById(R.id.genreTextView);
        costText = findViewById(R.id.cost_id);
        keywordsText = findViewById(R.id.keywordTextView);

        sp = getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);

        //to store and put all the input to the text place

        String title = sp.getString(KEY_TITLE, "");
        titleText.setText(title);

        int year = sp.getInt(KEY_YEAR, 0);
        yearText.setText("" + year);

        String country = sp.getString(KEY_COUNTRY, "");
        countryText.setText(country);

        String genre = sp.getString(KEY_GENRE, "");
        genreText.setText(genre);

        float cost = sp.getFloat(KEY_COST, 0f);
        costText.setText("" + cost);

        String keywords = sp.getString(KEY_KEYWORDS, "");
        keywordsText.setText(keywords);

        //Week 5
        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //LAB 7
        mMovieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        //LAB 8
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("/movie");

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp = getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                //change the input to string then store it to the key

                String mTitle = titleText.getText().toString();
                editor.putString(KEY_TITLE, mTitle);

                int mYear = Integer.parseInt(yearText.getText().toString());
                editor.putInt(KEY_YEAR, mYear);

                String mCountry = countryText.getText().toString();
                editor.putString(KEY_COUNTRY, mCountry);

                String mGenre = genreText.getText().toString();
                editor.putString(KEY_GENRE, mGenre);

                float mCost = Float.parseFloat(costText.getText().toString());
                editor.putFloat(KEY_COST, mCost);

                String mKeywords = keywordsText.getText().toString();
                editor.putString(KEY_KEYWORDS, mKeywords);

                Movie movie = new Movie(mTitle, mYear, mCountry, mGenre, mCost, mKeywords);
                movieData.add(movie);
                mMovieViewModel.insert(movie);
                ref.push().setValue(movie);

                editor.apply();

                listItems.add(mTitle + " | " + mYear);
                adapter.notifyDataSetChanged();
            }
        });

        myListView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        myListView.setAdapter(adapter);

        //LAB 10
        /*

        gesturesArea = findViewById(R.id.gestureView);

        gesturesArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int eventType = motionEvent.getActionMasked();
                switch (eventType) {
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch: Up");
                        Log.d(TAG, String.format("(%d,%d) --- (%d,%d)", xStart, yStart, xEnd, yEnd));
                        xEnd = (int) motionEvent.getX();
                        yEnd = (int) motionEvent.getY();
                        if ((xEnd - xStart >= 100) && Math.abs(yEnd - yStart) < 50) {
                            sp = getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();

                            //change the input to string then store it to the key

                            String mTitle = titleText.getText().toString();
                            editor.putString(KEY_TITLE, mTitle);

                            int mYear = Integer.parseInt(yearText.getText().toString());
                            editor.putInt(KEY_YEAR, mYear);

                            String mCountry = countryText.getText().toString();
                            editor.putString(KEY_COUNTRY, mCountry);

                            String mGenre = genreText.getText().toString();
                            editor.putString(KEY_GENRE, mGenre);

                            float mCost = Float.parseFloat(costText.getText().toString());
                            editor.putFloat(KEY_COST, mCost);

                            String mKeywords = keywordsText.getText().toString();
                            editor.putString(KEY_KEYWORDS, mKeywords);

                            Movie movie = new Movie(mTitle, mYear, mCountry, mGenre, mCost, mKeywords);
                            movieData.add(movie);
                            mMovieViewModel.insert(movie);
                            ref.push().setValue(movie);

                            editor.apply();

                            listItems.add(mTitle + " | " + mYear);
                            adapter.notifyDataSetChanged();

                            swipeMessage = "Swipe Horizontally";
                            Toast.makeText(getBaseContext(), swipeMessage, Toast.LENGTH_LONG).show();
                        }
                        else if ((Math.abs(xEnd - xStart) < 50) && Math.abs(yEnd - yStart) >= 100) {
                            String title = "";
                            titleText.setText(title);

                            yearText.setText("");

                            String country = "";
                            countryText.setText(country);

                            String genre = "";
                            genreText.setText(genre);

                            costText.setText("");

                            String keywords = "";
                            keywordsText.setText(keywords);

                            swipeMessage = "Swipe Vertically";
                            Toast.makeText(getBaseContext(), swipeMessage, Toast.LENGTH_LONG).show();

                        } else if ((xStart - xEnd >= 100) && Math.abs(yEnd - yStart) < 50) {
                            Intent i = new Intent(MainActivity.this, CardViewActivity.class);
                            i.putExtra("movie_key", movieData);
                            startActivity(i);
                        }
                        return true;
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "onTouch: Down");
                        xStart = (int) motionEvent.getX();
                        yStart = (int) motionEvent.getY();
                        return true;
                    default:
                        return false;
                }
            }
        });

         */

        //LAB 11
        MyGestureListener myGestureListener = new MyGestureListener();
        mDetector = new GestureDetectorCompat(this, myGestureListener);
        mDetector.setOnDoubleTapListener(myGestureListener);

        gesturesArea = findViewById(R.id.gestureView);
        gesturesArea.setOnTouchListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear_fields) {
            //clear it by inputting empty string to the text box

            String title = "";
            titleText.setText(title);

            yearText.setText("");

            String country = "";
            countryText.setText(country);

            String genre = "";
            genreText.setText(genre);

            costText.setText("");

            String keywords = "";
            keywordsText.setText(keywords);
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d(WEEK11TAG, "onSingleTapConfirmed");
            float costAdded = Float.parseFloat(costText.getText().toString()) + 150;
            costText.setText(costAdded + "");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(WEEK11TAG, "onDoubleTap");
            //String title = sp.getString(KEY_TITLE, "");
            titleText.setText("Harry Potter");

            //int year = sp.getInt(KEY_YEAR, 0);
            yearText.setText("2000");

            //String country = sp.getString(KEY_COUNTRY, "");
            countryText.setText("UK");

            String genre = sp.getString(KEY_GENRE, "");
            genreText.setText("Fantasy");

            float cost = sp.getFloat(KEY_COST, 0f);
            costText.setText("250");

            String keywords = sp.getString(KEY_KEYWORDS, "");
            keywordsText.setText("Series, JK Rowling, Young Adult");

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            String message = "onScroll (" + distanceX + ", " + distanceY + ")";
            Log.d(WEEK11TAG, message);

            int count = Integer.parseInt(yearText.getText().toString()) + Math.round(distanceX);
            yearText.setText(count + "");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(WEEK11TAG, "onFling");
            moveTaskToBack(true);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(WEEK11TAG, "onLongPress");

            String title = "";
            titleText.setText(title);

            yearText.setText("");

            String country = "";
            countryText.setText(country);

            String genre = "";
            genreText.setText(genre);

            costText.setText("");

            String keywords = "";
            keywordsText.setText(keywords);
        }
    }


    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.item_id_1) {
                //create a shared preferences object

                sp = getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                //change the input to string then store it to the key

                String mTitle = titleText.getText().toString();
                editor.putString(KEY_TITLE, mTitle);

                int mYear = Integer.parseInt(yearText.getText().toString());
                editor.putInt(KEY_YEAR, mYear);

                String mCountry = countryText.getText().toString();
                editor.putString(KEY_COUNTRY, mCountry);

                String mGenre = genreText.getText().toString();
                editor.putString(KEY_GENRE, mGenre);

                float mCost = Float.parseFloat(costText.getText().toString());
                editor.putFloat(KEY_COST, mCost);

                String mKeywords = keywordsText.getText().toString();
                editor.putString(KEY_KEYWORDS, mKeywords);

                Movie movie = new Movie(mTitle, mYear, mCountry, mGenre, mCost, mKeywords);
                movieData.add(movie);
                mMovieViewModel.insert(movie);
                ref.push().setValue(movie);

                editor.apply();

                listItems.add(mTitle + " | " + mYear);
                adapter.notifyDataSetChanged();

            } else if (id == R.id.item_id_2) {
                if (listItems.size() > 0) {
                    listItems.remove(listItems.size() - 1);
                    adapter.notifyDataSetChanged();
                }
            } else if (id == R.id.item_id_3) {
                listItems.clear();
                adapter.notifyDataSetChanged();
            } else if (id == R.id.item_id_4) {
                //to be added
                Intent i = new Intent(MainActivity.this, CardViewActivity.class);
                i.putExtra("movie_key", movieData);
                startActivity(i);
            } else if (id == R.id.item_id_5) {
                mMovieViewModel.deleteAll();
                ref.setValue(null);
            } else if (id == R.id.item_id_6) {
                Intent i = new Intent(MainActivity.this, ListMovieDatabaseActivity.class);
                startActivity(i);
            }
            //close the drawer
            drawerlayout.closeDrawers();
            //tell the OS
            return true;
        }
    }

    public void onAddBtn(View view) {
        Log.d(Log_Tag, "This is onAdd");

        //create a shared preferences object

        sp = getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //change the input to string then store it to the key

        String mTitle = titleText.getText().toString();
        editor.putString(KEY_TITLE, mTitle);

        int mYear = Integer.parseInt(yearText.getText().toString());
        editor.putInt(KEY_YEAR, mYear);

        String mCountry = countryText.getText().toString();
        editor.putString(KEY_COUNTRY, mCountry);

        String mGenre = genreText.getText().toString();
        editor.putString(KEY_GENRE, mGenre);

        float mCost = Float.parseFloat(costText.getText().toString());
        editor.putFloat(KEY_COST, mCost);

        String mKeywords = keywordsText.getText().toString();
        editor.putString(KEY_KEYWORDS, mKeywords);

        Movie movie = new Movie(mTitle, mYear, mCountry, mGenre, mCost, mKeywords);
        movieData.add(movie);
        mMovieViewModel.insert(movie);
        ref.push().setValue(movie);

        editor.apply();

        listItems.add(mTitle + " | " + mYear);
        adapter.notifyDataSetChanged();

        //message for toast

        String message = "Movie - " + titleText.getText().toString() + " - has been added";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        //LAB 6
        //use a constructor
    }

    public void onClearBtn(View view) {
        Log.d(Log_Tag, "This is onClear");

        //clear it by inputting empty string to the text box

        String title = "";
        titleText.setText(title);

        yearText.setText("");

        String country = "";
        countryText.setText(country);

        String genre = "";
        genreText.setText(genre);

        costText.setText("");

        String keywords = "";
        keywordsText.setText(keywords);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(Log_Tag, "This is onSaveInstanceState");

        //store in the bundle first

        String lowerGenre = genreText.getText().toString().toLowerCase();
        outState.putString(KEY_GENRE, lowerGenre);

    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(Log_Tag, "This is onRestoreInstanceState");

        //take from the bundle (no need to check for null since this is called only when bundle is not null)

        String storeGenre = savedInstanceState.getString(KEY_GENRE);
        genreText.setText(storeGenre);

        String mTitle = titleText.getText().toString().toUpperCase();
        titleText.setText(mTitle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Log_Tag, "This is onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Log_Tag, "This is onStart");

        //LAB 8

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //movieData.add(snapshot.getValue(Movie.class));
                //movieArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Log_Tag, "This is onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Log_Tag, "This is onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Log_Tag, "This is onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(Log_Tag, "This is onRestart");
    }

    //LAB 10




}

