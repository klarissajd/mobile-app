package com.example.movielibraryweek5;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movielibraryweek5.provider.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieCardAdapter extends RecyclerView.Adapter<MovieCardAdapter.MyViewHolder> {

    private List<Movie> data;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false); //CardView inflated as RecyclerView list item
        MyViewHolder viewHolder = new MyViewHolder(v);
        Log.d("week6Lab","onCreateViewHolder");
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titleTextView.setText(data.get(position).getTitle());
        holder.yearTextView.setText(String.valueOf(data.get(position).getYear()));
        holder.countryTextView.setText(data.get(position).getCountry());
        holder.genreTextView.setText(data.get(position).getGenre());
        holder.costTextView.setText(String.valueOf(data.get(position).getCost()));
        holder.keywordTextView.setText(data.get(position).getKeyword());
        Log.d("week6App","onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        else
            return data.size();
    }

    public void setMovie(List<Movie> newData) {
        data = newData;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView yearTextView;
        public TextView countryTextView;
        public TextView genreTextView;
        public TextView costTextView;
        public TextView keywordTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            countryTextView = itemView.findViewById(R.id.countryTextView);
            genreTextView = itemView.findViewById(R.id.genreTextView);
            costTextView = itemView.findViewById(R.id.costTextView);
            keywordTextView = itemView.findViewById(R.id.keywordTextView);
        }
    }
}
