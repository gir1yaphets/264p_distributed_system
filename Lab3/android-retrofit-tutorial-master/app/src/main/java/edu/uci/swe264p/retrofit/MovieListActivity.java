package edu.uci.swe264p.retrofit;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static edu.uci.swe264p.retrofit.MainActivity.API_KEY;
import static edu.uci.swe264p.retrofit.MainActivity.BASE_URL;


public class MovieListActivity extends AppCompatActivity {

    private static Retrofit retrofit = null;
    private static final String TAG = MovieListActivity.class.getName();
    private RecyclerView recyclerView;
    private MovieListAdapter adapter;
    private List<Movie> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        initView();
        fetchTopRatedMovies();
    }

    private void initView() {
        recyclerView = findViewById(R.id.rvMovieList);
        adapter = new MovieListAdapter(this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void fetchTopRatedMovies() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        MovieApiService movieApiService = retrofit.create(MovieApiService.class);
        Call<TopRatedResponse> call = movieApiService.getTopRatedMovie(API_KEY);
        call.enqueue(new Callback<TopRatedResponse>() {
            @Override
            public void onResponse(Call<TopRatedResponse> call, Response<TopRatedResponse> response) {
                TopRatedResponse ratedResponse = response.body();
                data = ratedResponse.getResults();
                adapter.setData(data);
            }
            @Override
            public void onFailure(Call<TopRatedResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }
}
