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
import java.util.List;

public class HomeFragment extends AbstractFragment {
    private RecyclerView category_recyclerView;
    private CategoryAdapter categoryAdapter;
    private ArrayList<MoviesCategory> categories;
    ShimmerFrameLayout shimmerFrameLayout;
    private Handler handler;
    private Runnable workRunnable;
    int count;
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    public void startOperations(View view) {
        count=0;
        categories=new ArrayList<>();
        handler = new Handler(Looper.getMainLooper() /*UI thread*/);
        TextView toolBarTitle = getActivity().findViewById(R.id.toolbar_title);

//        AppCompatActivity activity = (MainActivity) getActivity();
//        activity.getSupportActionBar().hide();
        shimmerFrameLayout = view.findViewById(R.id.home_shimmer);

        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        prepareData();
        category_recyclerView = view.findViewById(R.id.category_recyclerView);
        categoryAdapter = new CategoryAdapter(categories, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        category_recyclerView.setLayoutManager(manager);
        category_recyclerView.setAdapter(categoryAdapter);

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
        categories=new ArrayList<>();
//        ArrayList<MoviesCategory> categories = new ArrayList<>();


        String popularApiUrl = "https://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.MDB_API_KEY + "&language=en-US&page=1";
        String topRatedApiUrl = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + BuildConfig.MDB_API_KEY + "&language=en-US&page=1";
        String nowPlayingApiUrl = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + BuildConfig.MDB_API_KEY + "&language=en-US&page=1";
        String upcomingApiUrl = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + BuildConfig.MDB_API_KEY + "&language=en-US&page=1";
        String[] categoryNames = {"Trending now","All Time Most Popular","Top Rated","Coming Soon"};
        String[] categoriesUrlList = {nowPlayingApiUrl, popularApiUrl,topRatedApiUrl,upcomingApiUrl};
        //Initialize request que
        RequestQueue queue = Volley.newRequestQueue(getContext());
        //Initialize String request for movieNames

        for(String categoryUrl:categoriesUrlList){
            StringRequest nameRequest = new StringRequest(Request.Method.GET, categoryUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        try {
                            //convert json response to objects/classes
                            JsonResponse jsonResponse = new Gson().fromJson(response, JsonResponse.class);
                            //hide progress indicator
//                        progressIndicator.setVisibility(View.GONE);
                            System.out.println(jsonResponse);
                            //parse data to movie object
                            MoviesCategory category = parseCategoryResponse(jsonResponse,categoryNames[count]);
                            categories.add(category);
                            count++;
//                        JSONArray jsonArray = new JSONArray(response);
//                        parseArray(jsonArray);
                        } catch (Exception e) {
                            handler.removeCallbacks(workRunnable);
                            workRunnable = () -> stopShimmerOnError();
                            handler.postDelayed(workRunnable, 2000 /*delay*/);

                            e.printStackTrace();
                        }
                        if(categoryUrl.equals(categoriesUrlList[categoriesUrlList.length-1])){
                            handler.removeCallbacks(workRunnable);
                            workRunnable = () -> stopShimmerDelay();
                            handler.postDelayed(workRunnable, 1200 /*delay*/);

                        }


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
//            System.out.println("CATEGORIES" + categories.size());
        }


//        return categories;
    }
    private void stopShimmerDelay(){
        if(shimmerFrameLayout.isShimmerStarted()){
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            categoryAdapter.updateUi(categories);
        }
    }

    private void stopShimmerOnError(){
        if(shimmerFrameLayout.isShimmerStarted()){
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Unable to fetch data", Toast.LENGTH_LONG).show();
        }
    }
        //check to add name category name here
    private MoviesCategory parseCategoryResponse(JsonResponse response, String categoryName) {
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
        return  moviesCategory;


    }
}