package com.example.movielibraryweek5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.movielibraryweek5.provider.Movie;
import com.example.movielibraryweek5.provider.MovieViewModel;

import java.util.ArrayList;

//LAB 6
public class CardViewActivity extends AppCompatActivity {

    ArrayList<Movie> movieData = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MovieCardAdapter mcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        recyclerView = findViewById(R.id.my_recycler_view);

        layoutManager = new LinearLayoutManager(this);  //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.
        recyclerView.setLayoutManager(layoutManager);   // Also StaggeredGridLayoutManager and GridLayoutManager or a custom Layout manager

        movieData = (ArrayList<Movie>) getIntent().getSerializableExtra("movie_key");

        mcAdapter = new MovieCardAdapter();
        mcAdapter.setMovie(movieData);
        recyclerView.setAdapter(mcAdapter);
    }

}