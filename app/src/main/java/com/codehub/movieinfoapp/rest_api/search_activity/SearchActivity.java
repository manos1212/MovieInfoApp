package com.codehub.movieinfoapp.rest_api.search_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
//import com.codehub.movieinfoapp.adapters.RequestedCategoryMoviesAdapter;
import com.codehub.movieinfoapp.adapters.CategoryAdapter;
import com.codehub.movieinfoapp.adapters.RequestedMoviesAdapter;
import com.codehub.movieinfoapp.common.AbstractActivity;
import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.models.Movie;
import com.codehub.movieinfoapp.rest_api.search_activity.json.JsonResponse;
import com.codehub.movieinfoapp.rest_api.search_activity.json.JsonResultsResponse;
import com.codehub.movieinfoapp.ui.fragments.HomeFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AbstractActivity {
    private static SearchActivity instance = null;
    private LinearLayoutManager manager;
    Parcelable state;
    EditText searchText;
    ImageView back_btn;
    RecyclerView search_movies_rv;
    ArrayList<Movie> movies;
    ArrayList<Movie> categoryMovies;
    RequestedMoviesAdapter movieAdapter;
//    RequestedCategoryMoviesAdapter categoryMoviesAdapter;
    Map<String,Integer> api_movie_categories;
    String category_type;
    int category_num;
    TextView movie_list_title;
    private Handler handler;
    Runnable workRunnable;
    private String latestQuery = "";
    int page;
    int catPage;
    boolean isLoading;
    ProgressBar progressIndicator;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_search;
    }
    public static SearchActivity getInstance() {
        return instance;
    }

    @Override
    public void startOperations() {
        instance = this;
        progressIndicator = findViewById(R.id.search_progress_indicator);
        isLoading = false;
        handler = new Handler(Looper.getMainLooper());
        api_movie_categories = setCategories();
        searchText = findViewById(R.id.textField_Search);
        back_btn = findViewById(R.id.back_btn);
        movie_list_title = findViewById(R.id.search_movie_list_title);
        searchText.requestFocus();
//        category_title = findViewById(R.id.search_category_title);
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
                getUserInput(editable.toString());
            }
        });

        search_movies_rv = findViewById(R.id.searchForMovies_recycler_view);
        manager = new LinearLayoutManager(SearchActivity.this);
        search_movies_rv.setLayoutManager(manager);

        //handle state when user navigates away from Search Activity
        if (state == null){
            movieAdapter = new RequestedMoviesAdapter(SearchActivity.this, movies, categoryMovies, category_type,false);
            search_movies_rv.setAdapter(movieAdapter);
            }else{
            loadPreviousState();
        }

//        search_movies_rv.setItemViewCacheSize(100);
        search_movies_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = manager.getChildCount();
                int totalItemCount = manager.getItemCount();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        paginateMovieResults(latestQuery,page);
                    }
                }
            }
        });

        hideKeyboardOnScroll(search_movies_rv);
//        getUserInput(latestQuery);
    }

    private void getUserInput(String query){
        handler.removeCallbacks(workRunnable);
        workRunnable = () -> applyChecks(query);
        handler.postDelayed(workRunnable, 250 /*delay*/);
        latestQuery = query;
    }

    @Override
    public void stopOperations() {
        state = manager.onSaveInstanceState();
    }

    private void applyChecks(String str) {
        if(!str.isEmpty()){
            prepareData(str);
            //scroll top
            search_movies_rv.getLayoutManager().scrollToPosition(0);
        }else{
            movie_list_title.setText("");
//            category_title.setText("");
            movieAdapter.filterList(null,false,null,false);
            movieAdapter.filterList(null,true,null,false);
//            movieAdapter.filterList(new ArrayList<>());
        }
    }
    private void loadPreviousState() {
        search_movies_rv.setAdapter(movieAdapter);
        manager.onRestoreInstanceState(state);
        searchText.clearFocus();
    }
    private void prepareData(String query) {
//        if(!query.isEmpty()) {
            String apiUrlCategory = null;
            category_type = "";
            category_num = 0;

            for (Map.Entry<String, Integer> entry : api_movie_categories.entrySet()) {
                if (query.toLowerCase().contains(entry.getKey().toLowerCase())) {
                    category_type = entry.getKey();
                    category_num = entry.getValue();
                    apiUrlCategory = "https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.MDB_API_KEY + "&with_genres=" + category_num;


                    break;
                }
            }


//        String[] movieCategories = {"popular","latest","now_playing","top_rated"};

            String apiUrlName = "https://api.themoviedb.org/3/search/movie?api_key=" + BuildConfig.MDB_API_KEY + "&language=en-US&page=1&include_adult=false&query=" + query;

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
                            boolean hasMovies =  parseMovieResponse(jsonResponse,false);
                            System.out.println("HAs movjdhxhvjv" + hasMovies);
                            String text;
                            if(hasMovies){
                                page=2;
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
                            catPage = 2;
                            try {
                                //convert json response to objects/classes
                                JsonResponse jsonResponse = new Gson().fromJson(response, JsonResponse.class);
                                System.out.println(jsonResponse);
                                //parse data to movie object
                                parseCategoryResponse(jsonResponse,false,null);
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
//                category_title.setVisibility(View.GONE);
//                category_title.setText("");
                movieAdapter.filterList(null,true,category_type,false);
            }

//        }else{
//            movie_list_title.setText("");
//            category_title.setText("");
//            movieAdapter.filterList(new ArrayList<>());
//            categoryMoviesAdapter.filterList(new ArrayList<>());
//        }
    }




    private boolean parseMovieResponse(JsonResponse response, boolean pagination) {
        ArrayList<Movie> movies = new ArrayList<>();
        List<JsonResultsResponse> results = response.getResults();
        for(int i=0;i<results.size();i++){
            try {
//                JSONObject object = jsonArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setMovieName(results.get(i).getTitle());
                movie.setMovieThumbnailUrl(results.get(i).getPoster_path());
                movie.setId(results.get(i).getId());
                movie.setMovieDescription(results.get(i).getOverview());
                movie.setMovieRating(results.get(i).getVote_average());
                movies.add(movie);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
            page+=1;
            movieAdapter.filterList(movies,false,null,pagination);
            isLoading = false;
        return movies.size() > 0;
    }


    private void parseCategoryResponse(JsonResponse response, boolean pagination, RequestedMoviesAdapter.CategoryMovieViewHolder holder) {
        ArrayList<Movie> movies = new ArrayList<>();
        List<JsonResultsResponse> results = response.getResults();
        for(int i=0;i<results.size();i++){
            try {
//                JSONObject object = jsonArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setMovieName(results.get(i).getTitle());
                movie.setId(results.get(i).getId());
                movie.setMovieThumbnailUrl(results.get(i).getPoster_path());
                movie.setMovieDescription(results.get(i).getOverview());
                movie.setMovieRating(results.get(i).getVote_average());
                movies.add(movie);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if(pagination){
            movieAdapter.updatePagUi(holder,movies);

            catPage+=1;
        }
//            movieAdapter.notifyItemInserted(0);
        else{
            movieAdapter.filterList(movies,true,category_type,false);
        }



    }

    private Map<String,Integer> setCategories(){
        Map<String,Integer> api_movie_categories = new HashMap<>();

        // enter name/url pair
        api_movie_categories.put("Action", 28);
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

    @SuppressLint("ClickableViewAccessibility")
    private void hideKeyboardOnScroll(RecyclerView recyclerView){

        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                searchText.clearFocus();
                return false;
            }
        });
    }

 public void paginateMovieResults(String query, int page){
     String apiUrlName = "https://api.themoviedb.org/3/search/movie?api_key=" + BuildConfig.MDB_API_KEY + "&language=en-US&page=" + page + "&include_adult=false&query=" + query;

     isLoading = true;

     //show progress indicator
     progressIndicator.setVisibility(View.VISIBLE);
     //Initialize request que
     RequestQueue queue = Volley.newRequestQueue(this);
     //Initialize String request for movieNames
     StringRequest nameRequest = new StringRequest(Request.Method.GET, apiUrlName, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
             if (response != null) {
                 System.out.println("THIS RESPONSe"+ response);
                 try {
                     //convert json response to objects/classes
                     JsonResponse jsonResponse = new Gson().fromJson(response, JsonResponse.class);
                     //if no more results then we stop pagination by avoiding running parseMovieResponse function below,thus not setting isloading to false inside parsemovieresponse (and by not increasing page count)
                     if (!jsonResponse.getResults().isEmpty()){
                         //hide progress indicator

                         System.out.println(jsonResponse);
                         //parse data to movie object
                         parseMovieResponse(jsonResponse,true);

//                        JSONArray jsonArray = new JSONArray(response);
//                        parseArray(jsonArray);

                     }
                     progressIndicator.setVisibility(View.GONE);
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
     queue.add(nameRequest);

    }


    public void paginateCategoryResults(RequestedMoviesAdapter.CategoryMovieViewHolder holder){
        movieAdapter.isLoading = true;
        String apiUrlName = "https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.MDB_API_KEY + "&with_genres=" + category_num + "&page="+catPage;


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
                        if(!jsonResponse.getResults().isEmpty()) {
                            //hide progress indicator
                            System.out.println(jsonResponse);
                            //parse data to movie object
                            parseCategoryResponse(jsonResponse, true, holder);
                        }

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
                Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }

        );
        queue.add(nameRequest);

    }


}