package com.codehub.movieinfoapp.rest_api.search_activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codehub.movieinfoapp.adapters.RequestedMoviesAdapter;
import com.codehub.movieinfoapp.common.AbstractActivity;
import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.models.Movie;
import com.codehub.movieinfoapp.rest_api.search_activity.json.JsonResponse;
import com.codehub.movieinfoapp.rest_api.search_activity.json.JsonResultsResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AbstractActivity {
    EditText searchText;
    ImageView back_btn;
    RecyclerView search_movies_rv;
    ArrayList<Movie> movies;
    RequestedMoviesAdapter movieAdapter;


    @Override
    public int getLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    public void startOperations() {
        searchText = findViewById(R.id.textField_Search);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()){
                    prepareData(editable.toString());
                }

            }
        });


        search_movies_rv = findViewById(R.id.searchForMovies_recycler_view);
//        prepareData(searchText.getText().toString());
        movieAdapter = new RequestedMoviesAdapter(SearchActivity.this,movies);
        LinearLayoutManager manager = new LinearLayoutManager(SearchActivity.this);
        search_movies_rv.setLayoutManager(manager);
        search_movies_rv.setAdapter(movieAdapter);

    }

    @Override
    public void stopOperations() {

    }

    private void prepareData(String query) {

        String[] movieCategories = {"popular","latest","now_playing","top_rated"};
        String apiUrl = "https://api.themoviedb.org/3/search/movie?api_key=25d7f408d51921f8095ecc68a7f9812c&language=en-US&page=1&include_adult=false&query="+query;
        ProgressBar progressIndicator = findViewById(R.id.search_progress_indicator);
        //show progress indicator
        progressIndicator.setVisibility(View.VISIBLE);
        //Initialize request que
        RequestQueue queue = Volley.newRequestQueue(this);
        //Initialize String request
        StringRequest request = new StringRequest(Request.Method.GET,apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    //hide progress indicator
                    progressIndicator.setVisibility(View.GONE);
                    try {
                        //convert json response to objects/classes
                        JsonResponse jsonResponse = new Gson().fromJson(response, JsonResponse.class);
                        System.out.println(jsonResponse);
                        //parse data to movie object
                        parseResponse(jsonResponse);
//                        JSONArray jsonArray = new JSONArray(response);
//                        parseArray(jsonArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }

        );
        //Add request
        queue.add(request);

    }

    private void parseResponse(JsonResponse response) {
        ArrayList<Movie> movies = new ArrayList<>();
        List<JsonResultsResponse> results = response.getResults();
        for(int i=0;i<results.size();i++){
            try {
//                JSONObject object = jsonArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setMovieName(results.get(i).getTitle());
                movie.setMovieThumbnailUrl(results.get(i).getPoster_path());
                movies.add(movie);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //Add the requested/filtered Movies to recyclerView through the MovieAdapter
        movieAdapter.filterList(movies);
    }
}