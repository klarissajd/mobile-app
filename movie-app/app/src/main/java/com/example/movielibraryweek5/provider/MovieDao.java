package com.example.movielibraryweek5.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    //get all movies
    @Query("select * from movies")
    LiveData<List<Movie>> getAllMovies();

    //insert movies in
    @Insert
    void addMovie(Movie movie);

    //deleting movies
    @Query("delete FROM movies")
    void deleteAllMovies();

}
