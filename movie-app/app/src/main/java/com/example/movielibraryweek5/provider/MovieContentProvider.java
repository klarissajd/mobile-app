package com.example.movielibraryweek5.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.Nullable;

public class MovieContentProvider extends ContentProvider {
    public static final String CONTENT_AUTHORITY = "fit2081.app.KLARISSA";
    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int MULTIPLE_ROW_MOVIES = 1;
    private static final int SINGLE_ROW_MOVIES = 2;

    MovieDatabase db;
    private static final UriMatcher sUriMatcher = createUriMatcher();

    public MovieContentProvider() {}

    private static UriMatcher createUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, "movies", MULTIPLE_ROW_MOVIES);
        uriMatcher.addURI(authority, "movies" + "/#", SINGLE_ROW_MOVIES);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        db = MovieDatabase.getDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Movie.TABLE_NAME);
        String query = builder.buildQuery(projection, selection, null, null, sortOrder, null);

        final Cursor cursor = db
                .getOpenHelper()
                .getReadableDatabase()
                .query(query, selectionArgs);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId = db
                .getOpenHelper()
                .getWritableDatabase()
                .insert(Movie.TABLE_NAME, 0, values);

        return ContentUris.withAppendedId(CONTENT_URI, rowId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletionCount;

        deletionCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .delete(Movie.TABLE_NAME, selection, selectionArgs);

        return deletionCount;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updateCount;

        updateCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .update(Movie.TABLE_NAME, 0, values, selection, selectionArgs);

        return updateCount;
    }
}