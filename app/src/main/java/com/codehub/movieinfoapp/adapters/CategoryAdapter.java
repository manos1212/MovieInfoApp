package com.codehub.movieinfoapp.adapters;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.codehub.movieinfoapp.models.Movie;
import com.codehub.movieinfoapp.models.MoviesCategory;
import com.codehub.movieinfoapp.rest_api.search_activity.json.JsonResponse;
import com.codehub.movieinfoapp.ui.fragments.FavouritesFragment;
import com.codehub.movieinfoapp.ui.fragments.HomeFragment;
import com.google.gson.Gson;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    public ArrayList<MoviesCategory> categories;
    private Context context;
    private LayoutInflater layoutInflater;
//    ArrayList<MovieAdapter> movieAdapters = new ArrayList<>();
    MovieAdapter movieAdapter;
    Integer[] urlPages = {2,2,2,2};

    public CategoryAdapter(ArrayList<MoviesCategory> categories, Context context) {
        this.categories = categories;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.holder_single_movie_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {

        movieAdapter =  new MovieAdapter(context, categories.get(position).movies);
//        movieAdapters.add(movieAdapter);//do this in order to be able to handle horizontal pagination
        LinearLayoutManager layoutManager =new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
//        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setAdapter(movieAdapter);

        holder.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pos = layoutManager.findLastCompletelyVisibleItemPosition();
                int numItems = recyclerView.getAdapter().getItemCount();
//                int visibleItemCount = manager.getChildCount();
//                int totalItemCount = manager.getItemCount();
//                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
//                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!holder.isLoading) {
                    if (pos>=numItems-1) {
//                        holder.circular_indicator.setVisibility(View.VISIBLE); //Enable to show indicator on horizontal scroll
                        HomeFragment.getInstance().paginateResults(categories.get(position).categoryUrl, urlPages[position],position,holder);
                        urlPages[position]+=1;
                        System.out.println("PPOSITION" + urlPages[position]);

                    }
                }
            }
        });
        holder.categoryName.setText(categories.get(position).categoryName);
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView categoryName;
        TextView movies_word_tag;
        ProgressBar circular_indicator;
        public boolean isLoading;


        public CategoryViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.movies_recycler_view);
            categoryName = (TextView) itemView.findViewById(R.id.category_name_textView);
            movies_word_tag = (TextView) itemView.findViewById(R.id.movieList_results);
            movies_word_tag.setVisibility(View.GONE);
            isLoading=false;
            circular_indicator = itemView.findViewById(R.id.pagination_progress_indicator);

        }
    }
    //called after shimmer on start of HomeFragment
    public  void updateUi(ArrayList<MoviesCategory> categoriesList){
        categories=categoriesList;
        notifyDataSetChanged();

    }

    //called on horizontal pagination
    public  void updateMoviesRV(ArrayList<Movie> addedMovies,int position,CategoryViewHolder holder){
        ArrayList<Movie> movies = categories.get(position).getMovies();
        movies.addAll(addedMovies);
        categories.get(position).setMovies(movies);
//        movieAdapters.get(position).notifyDataSetChanged();
//        movieAdapter.notifyDataSetChanged();
//        holder.circular_indicator.setVisibility(View.GONE);// enable if want to use indicator on horizontal scroll
        holder.recyclerView.getAdapter().notifyDataSetChanged();
        holder.isLoading=false;


        System.out.println("CAT POST" + position);
    }



}