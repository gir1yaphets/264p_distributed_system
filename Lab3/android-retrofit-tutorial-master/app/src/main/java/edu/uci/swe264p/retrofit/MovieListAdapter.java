package edu.uci.swe264p.retrofit;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieListAdapter extends RecyclerView.Adapter<MovieItemViewHolder> {
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private Context context;
    private List<Movie> data;

    public MovieListAdapter(Context context, List<Movie> data) {
        this.context = context;
        this.data = data;
    }

    public List<Movie> getData() {
        return data;
    }

    public void setData(List<Movie> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(Movie item) {
        data.add(item);
        notifyDataSetChanged();
    }

    public void removeData(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row, parent, false);
        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItemViewHolder holder, int position) {
        Movie movie = data.get(position);

        holder.setText(R.id.tvTitle, movie.getTitle());
        holder.setText(R.id.tvReleaseDate, movie.getReleaseDate());
        holder.setText(R.id.tvVote, movie.getVoteAverage() + "");
        holder.setText(R.id.tvOverview, movie.getOverview());

        Picasso.get().load(IMAGE_BASE_URL+ movie.getPosterPath())
                .into((ImageView) holder.getViewById(R.id.ivMovie));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
