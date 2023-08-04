package com.example.movielibraryweek5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.movielibraryweek5.provider.Movie;
import com.example.movielibraryweek5.provider.MovieViewModel;

import java.util.ArrayList;

//LAB 7
public class ListMovieDatabaseActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MovieViewModel mMovieViewModel;
    MovieCardAdapter mcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie_database);

        recyclerView = findViewById(R.id.my_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mcAdapter = new MovieCardAdapter();
        recyclerView.setAdapter(mcAdapter);

        mMovieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        mMovieViewModel.getAllMovies().observe(this, newData -> {
            mcAdapter.setMovie(newData);
            mcAdapter.notifyDataSetChanged();
        });
    }
}