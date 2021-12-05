package com.codehub.movieinfoapp.rest_api.search_activity;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codehub.movieinfoapp.BuildConfig;
import com.codehub.movieinfoapp.adapters.RequestedCategoryMoviesAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AbstractActivity {
    EditText searchText;
    ImageView back_btn;
    RecyclerView search_movies_rv;
    RecyclerView search_movie_category_rv;
    ArrayList<Movie> movies;
    ArrayList<Movie> categoryMovie;
    RequestedMoviesAdapter movieAdapter;
    RequestedCategoryMoviesAdapter categoryMoviesAdapter;
    Map<String,Integer> api_movie_categories;
    String category_type;
    TextView movie_list_title;
    TextView category_title;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    public void startOperations() {
        api_movie_categories = setCategories();
        searchText = findViewById(R.id.textField_Search);
        back_btn = findViewById(R.id.back_btn);
        movie_list_title  = findViewById(R.id.search_movie_list_title);
        category_title = findViewById(R.id.search_category_title);
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
            final Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
            Runnable workRunnable;



            @Override
            public void afterTextChanged(Editable editable) {
                handler.removeCallbacks(workRunnable);
                workRunnable = () -> applyChecks(editable.toString());
                handler.postDelayed(workRunnable, 300 /*delay*/);

            }
        });


        search_movies_rv = findViewById(R.id.searchForMovies_recycler_view);
        search_movie_category_rv = findViewById(R.id.searchForMovieCategory_recycler_view);
//        prepareData(searchText.getText().toString());

        categoryMoviesAdapter = new RequestedCategoryMoviesAdapter(SearchActivity.this,categoryMovie);
        movieAdapter = new RequestedMoviesAdapter(SearchActivity.this,movies);
        LinearLayoutManager manager = new LinearLayoutManager(SearchActivity.this);
        LinearLayoutManager manager2 = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.HORIZONTAL, false);
        search_movies_rv.setLayoutManager(manager);
        search_movie_category_rv.setLayoutManager(manager2);
        search_movie_category_rv.setAdapter(categoryMoviesAdapter);
        search_movies_rv.setAdapter(movieAdapter);

    }

    @Override
    public void stopOperations() {

    }

    private void applyChecks(String str) {
        if(!str.isEmpty()){
            prepareData(str);
        }else{
            movie_list_title.setText("");
            category_title.setText("");
            movieAdapter.filterList(new ArrayList<>());
            categoryMoviesAdapter.filterList(new ArrayList<>());
        }
    }

    private void prepareData(String query) {
//        if(!query.isEmpty()) {
            String apiUrlCategory = null;
            category_type = "";

            for (Map.Entry<String, Integer> entry : api_movie_categories.entrySet()) {
                if (query.toLowerCase().contains(entry.getKey().toLowerCase())) {
                    apiUrlCategory = " https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.MDB_API_KEY + "&with_genres=" + entry.getValue();
                    category_type = entry.getKey();
                    break;
                }
            }


//        String[] movieCategories = {"popular","latest","now_playing","top_rated"};

            String apiUrlName = "https://api.themoviedb.org/3/search/movie?api_key=" + BuildConfig.MDB_API_KEY + "&language=en-US&page=1&include_adult=false&query=" + query;


            ProgressBar progressIndicator = findViewById(R.id.search_progress_indicator);
            //show progress indicator
            progressIndicator.setVisibility(View.VISIBLE);
            //Initialize request que
            RequestQueue queue = Volley.newRequestQueue(this);
            //Initialize String request for movieNames
            StringRequest nameRequest = new StringRequest(Request.Method.GET, apiUrlName, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        try {
                            //convert json response to objects/classes
                            JsonResponse jsonResponse = new Gson().fromJson(response, JsonResponse.class);
                            //hide progress indicator
                            progressIndicator.setVisibility(View.GONE);
                            System.out.println(jsonResponse);
                            //parse data to movie object
                            boolean hasMovies =  parseMovieResponse(jsonResponse);
                            System.out.println("HAs movjdhxhvjv" + hasMovies);
                            String text;
                            if(hasMovies){
                                text = "Showing results for  " + '"' + query + '"';


                            }else{

                                text = "No results for  " + '"' + query + '"';

                            }
                            movie_list_title.setText(text);

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
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }

            );
            //Add request
            queue.add(nameRequest);
            //Initialize String request for movieCategory
            if (apiUrlCategory != null) {
                StringRequest categoryRequest = new StringRequest(Request.Method.GET, apiUrlCategory, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {

                            try {
                                //convert json response to objects/classes
                                JsonResponse jsonResponse = new Gson().fromJson(response, JsonResponse.class);
                                System.out.println(jsonResponse);
                                //parse data to movie object
                                parseCategoryResponse(jsonResponse);
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
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                );
                //Add request
                queue.add(categoryRequest);
            } else {
                category_title.setVisibility(View.GONE);
                category_title.setText("");
                categoryMoviesAdapter.filterList(new ArrayList<>());
            }

//        }else{
//            movie_list_title.setText("");
//            category_title.setText("");
//            movieAdapter.filterList(new ArrayList<>());
//            categoryMoviesAdapter.filterList(new ArrayList<>());
//        }
    }




    private boolean parseMovieResponse(JsonResponse response) {
        ArrayList<Movie> movies = new ArrayList<>();
        List<JsonResultsResponse> results = response.getResults();
        for(int i=0;i<results.size();i++){
            try {
//                JSONObject object = jsonArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setMovieName(results.get(i).getTitle());
//                if(type.equals("movie")){
                    movie.setMovieName(results.get(i).getTitle());
//                }else{
//                    movie.setMovieName("");
//                }
                movie.setMovieThumbnailUrl(results.get(i).getPoster_path());
                movies.add(movie);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //Add the requested/filtered Movies to recyclerView through the MovieAdapter
//        TextView category_title = findViewById(R.id.search_category_title);
//        if(type.equals("movie")){
            movieAdapter.filterList(movies);
            //collapse horizontal RV in case it was shown previously
//            categoryMoviesAdapter.filterList(new ArrayList<>());
//            category_title.setVisibility(View.GONE);
//        }else{
//            category_title.setVisibility(View.VISIBLE);
//            String text = "Category  " +'"'+ category_type + '"';
//            category_title.setText(text);
//            categoryMoviesAdapter.filterList(movies);
//        }
        if(movies.size()>0){
            return true;
        }
        else{
            return false;
        }
    }


    private void parseCategoryResponse(JsonResponse response) {
        ArrayList<Movie> movies = new ArrayList<>();
        List<JsonResultsResponse> results = response.getResults();
        for(int i=0;i<results.size();i++){
            try {
//                JSONObject object = jsonArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setMovieName(results.get(i).getTitle());
//                if(type.equals("movie")){
                movie.setMovieName(results.get(i).getTitle());
//                }else{
//                    movie.setMovieName("");
//                }
                movie.setMovieThumbnailUrl(results.get(i).getPoster_path());
                movies.add(movie);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //Add the requested/filtered Movies to recyclerView through the MovieAdapter
            TextView category_title = findViewById(R.id.search_category_title);
            category_title.setVisibility(View.VISIBLE);
            String text = "Category  " +'"'+ category_type + '"';
            category_title.setText(text);
            categoryMoviesAdapter.filterList(movies);
//        }


    }

    private Map<String,Integer> setCategories(){
        Map<String,Integer> api_movie_categories = new HashMap<>();

        // enter name/url pair
        api_movie_categories.put("id", 28);
        api_movie_categories.put("Adventure", 12);
        api_movie_categories.put("Animation", 16);
        api_movie_categories.put("Comedy", 35);
        api_movie_categories.put("Crime", 80);
        api_movie_categories.put("Documentary", 99);
        api_movie_categories.put("Drama", 18);
        api_movie_categories.put("Family", 10751);
        api_movie_categories.put("Fantasy", 14);
        api_movie_categories.put("History", 36);
        api_movie_categories.put("Horror", 27);
        api_movie_categories.put("Music", 10402);
        api_movie_categories.put("Mystery", 9648);
        api_movie_categories.put("Romance", 10749);
        api_movie_categories.put("Science Fiction", 878);
        api_movie_categories.put("TV Movie", 10770);
        api_movie_categories.put("Thriller", 53);
        api_movie_categories.put("War", 10752);
        api_movie_categories.put("Western", 37);

        return api_movie_categories;
    }

}