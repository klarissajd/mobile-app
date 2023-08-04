package com.example.movielibraryweek5.provider;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.util.SneakyThrow;

import java.io.Serializable;
import java.net.PortUnreachableException;

import static com.example.movielibraryweek5.provider.Movie.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Movie implements Serializable {
    public static final String TABLE_NAME = "movies";
    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = COLUMN_ID)
    private int id;

    public static final String COLUMN_TITLE = "movieTitle";
    @ColumnInfo(name = COLUMN_TITLE)
    private String title;

    public static final String COLUMN_YEAR =  "movieYear";
    @ColumnInfo(name = COLUMN_YEAR)
    private int year;

    public static final String COLUMN_COUNTRY = "movieCountry";
    @ColumnInfo(name = COLUMN_COUNTRY)
    private String country;

    public static final String COLUMN_GENRE = "movieGenre";
    @ColumnInfo(name = COLUMN_GENRE)
    private String genre;

    public static final String COLUMN_COST = "movieCost";
    @ColumnInfo(name = COLUMN_COST)
    private float cost;

    public static final String COLUMN_KEYWORD = "movieKeyword";
    @ColumnInfo(name = COLUMN_KEYWORD)
    private String keyword;

    public Movie() {}

    public Movie(String title, int year, String country, String genre, float cost, String keyword) {
        this.title = title;
        this.year = year;
        this.country = country;
        this.genre = genre;
        this.cost = cost;
        this.keyword = keyword;
    }

    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @NonNull
    public static Movie fromContentValues(@Nullable ContentValues values) {
        final Movie movie = new Movie();
        if (values != null && values.containsKey(COLUMN_ID)) {
            movie.id = values.getAsInteger(COLUMN_ID);
        }
        if (values != null && values.containsKey(COLUMN_TITLE)) {
            movie.title = values.getAsString(COLUMN_TITLE);
        }
        if (values != null && values.containsKey(COLUMN_YEAR)) {
            movie.year = values.getAsInteger(COLUMN_YEAR);
        }
        if (values != null && values.containsKey(COLUMN_COUNTRY)) {
            movie.country = values.getAsString(COLUMN_COUNTRY);
        }
        if (values != null && values.containsKey(COLUMN_GENRE)) {
            movie.genre = values.getAsString(COLUMN_GENRE);
        }
        if (values != null && values.containsKey(COLUMN_COST)) {
            movie.cost = values.getAsFloat(COLUMN_COST);
        }
        if (values != null && values.containsKey(COLUMN_KEYWORD)) {
            movie.keyword = values.getAsString(COLUMN_KEYWORD);
        }
        return movie;
    }

}

