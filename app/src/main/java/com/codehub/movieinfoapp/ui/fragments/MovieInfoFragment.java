package com.codehub.movieinfoapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.common.AbstractFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.service.controls.ControlsProviderService.TAG;

public class MovieInfoFragment extends AbstractFragment{

    private int movieID;
    private FloatingActionButton movieBack;
    private ImageView movieImage;
    private TextView movieRating;
    private ImageView movieFavourite;
    private ImageView movieShare;
    private TextView movieTitle;
    private TextView movieDescription;
    private FirebaseFirestore fireStoreDb;

    private boolean favourite = false;


    public MovieInfoFragment() {

    }

    public static MovieInfoFragment newInstance(int id, String image, double rating, boolean favourite, String title, String description) {

        MovieInfoFragment fragment = new MovieInfoFragment();

        Bundle parameters = new Bundle();
        parameters.putInt("movie_id", id);
        parameters.putString("movie_image", image);
        parameters.putDouble("movie_rating", rating);
        parameters.putBoolean("movie_favourite", favourite);
        parameters.putString("movie_title", title);
        parameters.putString("movie_description", description);
        fragment.setArguments(parameters);

        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_movie_info;
    }

    @Override
    public void startOperations(View view) {

        fireStoreDb = FirebaseFirestore.getInstance();
        movieBack = view.findViewById(R.id.back_fab);
        movieImage = view.findViewById(R.id.movie_image);
        movieRating = view.findViewById(R.id.movie_rating);
        movieFavourite = view.findViewById(R.id.movie_favourite);
        movieShare = view.findViewById(R.id.movie_share);
        movieTitle = view.findViewById(R.id.movie_title);
        movieDescription = view.findViewById(R.id.movie_description);

        checkIfFavourite();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            movieID = (bundle.getInt("movie_id"));

            Picasso.get().load(bundle.getString("movie_image")).into(movieImage);
            movieRating.setText(String.valueOf(bundle.getDouble("movie_rating")));

            if(favourite) {
                movieFavourite.setImageResource(R.drawable.ic_baseline_favorite_filled_24);
            } else {
                movieFavourite.setImageResource(R.drawable.ic_baseline_favorite_no_fill_24);
            }

            movieTitle.setText((bundle.getString("movie_title")));
            movieDescription.setText((bundle.getString("movie_description")));
        }

        movieBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        movieFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrRemoveFromFavourites();
            }
        });

        movieShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"https://www.themoviedb.org/movie/" + movieID);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent,"Movie DB URL");
                startActivity(shareIntent);
            }
        });
    }

    @Override
    public void stopOperations() {

    }

    public void checkIfFavourite(){
        System.out.println("DsfsdfsdfsdfsdfdsF"+FirebaseAuth.getInstance().getUid());
        // Add a new document with a generated ID
        DocumentReference docRef = fireStoreDb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.get("favourites")!=null){
                            List<Long> favourites = (List<Long>) document.get("favourites");
                            System.out.println("!!!!!!!!!!!"+favourites);
                            for(long i:favourites){
                                if(i==movieID){
                                    movieFavourite.setImageResource(R.drawable.ic_baseline_favorite_filled_24);
                                    favourite = true;
                                    break;
                                }else{
                                    movieFavourite.setImageResource(R.drawable.ic_baseline_favorite_no_fill_24);
                                }
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
//        fireStoreDb.collection("users").document("favourite")
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, document.getId() + " => " + document.getData());
//                        document.getId()
//
//                    }
//                } else {
//                    Log.w(TAG, "Error getting documents.", task.getException());
//                }
//            }
//        });



//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });


    }


    public void addOrRemoveFromFavourites(){
        DocumentReference favouritesRef = fireStoreDb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(!favourite){
            favouritesRef.update("favourites", FieldValue.arrayUnion(movieID));
            movieFavourite.setImageResource(R.drawable.ic_baseline_favorite_filled_24);
            Toast.makeText(getActivity(),"Added to favourites",
                    Toast.LENGTH_SHORT).show();

        }else{
            favouritesRef.update("favourites", FieldValue.arrayRemove(movieID));
            movieFavourite.setImageResource(R.drawable.ic_baseline_favorite_no_fill_24);
            Toast.makeText(getActivity(),"Removed from favourites",
                    Toast.LENGTH_SHORT).show();
        }
            favourite=!favourite;

    }


}