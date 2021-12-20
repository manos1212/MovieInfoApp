package com.codehub.movieinfoapp.ui.fragments;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codehub.movieinfoapp.BuildConfig;
import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.adapters.CategoryAdapter;
import com.codehub.movieinfoapp.common.AbstractFragment;
import com.codehub.movieinfoapp.models.Movie;
import com.codehub.movieinfoapp.models.MoviesCategory;
import com.codehub.movieinfoapp.rest_api.search_activity.json.JsonResponse;
import com.codehub.movieinfoapp.rest_api.search_activity.json.JsonResultsResponse;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;


public class HomeFragment extends AbstractFragment {
    LinkedHashMap<String,String> sortedCategories = new LinkedHashMap<>();
    private static HomeFragment instance = null;
    private RecyclerView category_recyclerView;
    private CategoryAdapter categoryAdapter;
    private ArrayList<MoviesCategory> categories;
    ShimmerFrameLayout shimmerFrameLayout;
    private Handler handler;
    private Runnable workRunnable;
    int count;
    public static LinkedHashMap<String,Integer> api_movie_categories;
    public ArrayList<String> genresUrlList;
    public ArrayList<String> genresNamesList;

    public static String popularApiUrl = "https://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.MDB_API_KEY + "&language=en-US&page=";
    public static String topRatedApiUrl = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + BuildConfig.MDB_API_KEY + "&language=en-US&page=";
    public static String nowPlayingApiUrl = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + BuildConfig.MDB_API_KEY + "&language=en-US&page=";
    public static String upcomingApiUrl = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + BuildConfig.MDB_API_KEY + "&language=en-US&page=";
    public static final ArrayList<String> categoryNames = new ArrayList<>(Arrays.asList("Trending now","All Time Most Popular","Top Rated","Coming Soon"));
    public static final ArrayList<String> categoriesUrlList = new ArrayList<>(Arrays.asList(nowPlayingApiUrl, popularApiUrl,topRatedApiUrl,upcomingApiUrl));
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_home;
    }
    public static HomeFragment getInstance() {
        return instance;
    }


    @Override
    public void startOperations(View view) {
        genresUrlList = new ArrayList<>();
        genresNamesList = new ArrayList<>();
        setGenresCategories();
        instance = this;
        handler = new Handler(Looper.getMainLooper() /*UI thread*/);
        TextView toolBarTitle = getActivity().findViewById(R.id.toolbar_title);
        shimmerFrameLayout = view.findViewById(R.id.home_shimmer);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        prepareData();
        category_recyclerView = view.findViewById(R.id.category_recyclerView);
        categoryAdapter = new CategoryAdapter(categories, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        category_recyclerView.setLayoutManager(manager);
        category_recyclerView.setAdapter(categoryAdapter);
//        category_recyclerView.setItemViewCacheSize(25);


        toolBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_recyclerView.getLayoutManager().scrollToPosition(0);
            }
        });
    }

    @Override
    public void stopOperations() {
        System.out.println("GGGGGGGGGGGGGGGG");
    }

    private void prepareData() {
        count=0;
        categories=new ArrayList<>();
        //Initialize request que
        RequestQueue queue = Volley.newRequestQueue(getContext());
        //Initialize String request for movieNames
        System.out.println("SORTED"+sortedCategories);
        for (LinkedHashMap.Entry<String, String> entry : sortedCategories.entrySet()) {
            StringRequest nameRequest = new StringRequest(Request.Method.GET, entry.getValue()+1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        try {
                            //convert json response to objects/classes
                            JsonResponse jsonResponse = new Gson().fromJson(response, JsonResponse.class);

                            //parse data to movie object
                            MoviesCategory category = parseCategoryResponse(jsonResponse, entry.getKey(),entry.getValue());
                            categories.add(category);

                        } catch (Exception e) {
                            handler.removeCallbacks(workRunnable);
                            workRunnable = () -> stopShimmerOnError();
                            handler.postDelayed(workRunnable, 2000 /*delay*/);

                            e.printStackTrace();
                        }
                        if(entry.equals(sortedCategories.entrySet().toArray()[sortedCategories.size() -1])){
                            handler.removeCallbacks(workRunnable);
                            workRunnable = () -> stopShimmerDelay();
                            handler.postDelayed(workRunnable, 1200 /*delay*/);
                        }
                        count++;

                    }

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    handler.removeCallbacks(workRunnable);
                    workRunnable = () -> stopShimmerOnError();
                    handler.postDelayed(workRunnable, 1000 /*delay*/);


                }
            }

            );
            //Add request
            queue.add(nameRequest);
        }

    }
    private void stopShimmerDelay(){
        if(shimmerFrameLayout.isShimmerStarted()){
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);

        }
        categoryAdapter.updateUi(categories);
    }

    private void stopShimmerOnError(){
        if(shimmerFrameLayout.isShimmerStarted()){
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Unable to fetch data", Toast.LENGTH_LONG).show();
        }
    }
        //check to add name category name here
    private MoviesCategory parseCategoryResponse(JsonResponse response, String categoryName,String categoryUrl) {
        MoviesCategory moviesCategory = new MoviesCategory();
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
        moviesCategory.setMovies(movies);
        moviesCategory.setCategoryName(categoryName);
        moviesCategory.setCategoryUrl(categoryUrl);
        return  moviesCategory;


    }
    public void paginateResults(String categoryUrl, int page, int position, CategoryAdapter.CategoryViewHolder holder){
        holder.isLoading=true;
        String apiUrlName = categoryUrl + page;


        //Initialize request que
        RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                            parseMovieResponse(jsonResponse, position, holder);
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
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }

        );
        queue.add(nameRequest);

    }

    private void parseMovieResponse(JsonResponse response, int position, CategoryAdapter.CategoryViewHolder holder) {
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
        categoryAdapter.updateMoviesRV(movies,position,holder);

    }
    private void setGenresCategories(){
        LinkedHashMap<String,Integer> apiMovieCategories = new LinkedHashMap<>();
        // enter name/url pair
        apiMovieCategories.put("Action", 28);
        apiMovieCategories.put("Adventure", 12);
        apiMovieCategories.put("Animation", 16);
        apiMovieCategories.put("Comedy", 35);
        apiMovieCategories.put("Crime", 80);
        apiMovieCategories.put("Documentary", 99);
        apiMovieCategories.put("Drama", 18);
        apiMovieCategories.put("Family", 10751);
        apiMovieCategories.put("Fantasy", 14);
        apiMovieCategories.put("History", 36);
        apiMovieCategories.put("Horror", 27);
        apiMovieCategories.put("Music", 10402);
        apiMovieCategories.put("Mystery", 9648);
        apiMovieCategories.put("Romance", 10749);
        apiMovieCategories.put("Science Fiction", 878);
        apiMovieCategories.put("TV Movie", 10770);
        apiMovieCategories.put("Thriller", 53);
        apiMovieCategories.put("War", 10752);
        apiMovieCategories.put("Western", 37);
        api_movie_categories = apiMovieCategories;
        for (LinkedHashMap.Entry<String, Integer> entry : apiMovieCategories.entrySet()) {
            genresNamesList.add(entry.getKey());
            genresUrlList.add("https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.MDB_API_KEY + "&with_genres=" + entry.getValue().toString() + "&page=");
        }
        categoriesUrlList.addAll(genresUrlList);
        categoryNames.addAll(genresNamesList);
        for(int i=0;i<categoriesUrlList.size();i++){
            sortedCategories.put(categoryNames.get(i),categoriesUrlList.get(i));
        }
//
//        System.out.println("GENRES URLS "+ categoriesUrlList);
//        System.out.println("GENRES NAMEs "+ categoryNames);
//        System.out.println("GENRES NAMEs "+ sortedCategories);

    }
}