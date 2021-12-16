package com.codehub.movieinfoapp.ui.fragments;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codehub.movieinfoapp.BuildConfig;
import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.adapters.FavouritesAdapter;
import com.codehub.movieinfoapp.common.AbstractFragment;
import com.codehub.movieinfoapp.models.Movie;
import com.codehub.movieinfoapp.models.MoviesCategory;
import com.codehub.movieinfoapp.rest_api.search_activity.json.JsonResponse;
import com.codehub.movieinfoapp.rest_api.search_activity.json.JsonResultsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static android.service.controls.ControlsProviderService.TAG;

public class FavouritesFragment extends AbstractFragment {
    private static FavouritesFragment instance = null;
    private RecyclerView favourites_recyclerView;
    public FavouritesAdapter favouritesAdapter;
    public ArrayList<Movie> favouriteMovies;
    private FirebaseFirestore fireStoreDb;
    private String baseUrl;
    int count = 0;
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_favourites;
    }
    public static FavouritesFragment getInstance() {
        return instance;
    }

    @Override
    public void startOperations(View view) {
        instance = this;
        fireStoreDb = FirebaseFirestore.getInstance();
        favouriteMovies = new ArrayList<>();
        favourites_recyclerView = getActivity().findViewById(R.id.favourites_recycler_view);
        favouritesAdapter = new FavouritesAdapter(getContext(), favouriteMovies);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        favourites_recyclerView.setLayoutManager(manager);
        favourites_recyclerView.setAdapter(favouritesAdapter);
        getFavouritesFromDB();
    }

    @Override
    public void stopOperations() {

    }

    public void getFavouritesFromDB() {
        try {
            count = 0;
            favouriteMovies = new ArrayList<>();

            //Initialize request que
            RequestQueue queue = Volley.newRequestQueue(getContext());
            // Add a new document with a generated ID
            DocumentReference docRef = fireStoreDb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if (document.get("favouriteMovies") != null) {
                                List<Long> favourites = (List<Long>) document.get("favouriteMovies");
                                if (!favourites.isEmpty()) {
                                    for (long i : favourites) {
                                        baseUrl = "https://api.themoviedb.org/3/movie/" + i + "?api_key=" + BuildConfig.MDB_API_KEY + "&language=en-US";
                                        StringRequest nameRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response != null) {
                                                    try {
                                                        //convert json response to objects/classes
                                                        JsonResultsResponse jsonResultsResponse = new Gson().fromJson(response, JsonResultsResponse.class);
                                                        System.out.println("BBLYAT" + jsonResultsResponse);
                                                        //parse data to movie object
                                                        parseMovieResponse(jsonResultsResponse);
                                                        if (count == favourites.size() - 1) {
                                                            Collections.reverse(favouriteMovies);
                                                            favouritesAdapter.refreshUi(favouriteMovies);
                                                        }
                                                        count++;
                                                    } catch (Exception e) {

                                                        e.printStackTrace();
                                                    }


                                                }

                                            }

                                        }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {


                                            }
                                        }

                                        );
                                        //Add request
                                        queue.add(nameRequest);

                                    }
                                } else {
                                    favouritesAdapter.refreshUi(favouriteMovies);
                                }

                            }
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void parseMovieResponse(JsonResultsResponse response) {

            try {
//                JSONObject object = jsonArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setMovieName(response.getTitle());
                movie.setMovieThumbnailUrl(response.getPoster_path());
                movie.setId(response.getId());
                movie.setMovieDescription(response.getOverview());
                movie.setMovieRating(response.getVote_average());
                favouriteMovies.add(movie);

            } catch (Exception e) {
                e.printStackTrace();
            }


//        favouritesAdapter.filterList(movies,false,null);

    }

}