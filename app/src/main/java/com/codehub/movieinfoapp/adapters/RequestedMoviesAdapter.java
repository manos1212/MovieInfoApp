package com.codehub.movieinfoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RequestedMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<Movie> requestedMovies;
    public ArrayList<Movie> requestedCategoryMovies;
    private Context context;
    String categoryName;
    private LayoutInflater layoutInflater;

    //Url Prefix for used as thumbnail url prefix
    private static final String base_url = "https://image.tmdb.org/t/p/w500";

    public RequestedMoviesAdapter(Context context,ArrayList<Movie> requestedMovies,ArrayList<Movie> requestedCategoryMovies,String categoryName) {

        this.requestedMovies = requestedMovies;
        this.categoryName = categoryName;
        this.requestedCategoryMovies = requestedCategoryMovies;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 1;
        else return 2;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("VIEWTYPEEEEEEEEE"+viewType);
            if(viewType==1){
                    View view = layoutInflater.inflate(R.layout.holder_single_movie_category, parent, false);
                    return new CategoryMovieViewHolder(view);
            }else{
                View view = layoutInflater.inflate(R.layout.holder_search_single_movie, parent, false);
                return new MovieViewHolder(view);

            }




    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder && requestedMovies!=null && requestedMovies.size()>0) {
            Movie movie = requestedMovies.get(position-1);//use -1 because 1st item ins vertical recycler view is the horizontal rv
            ((MovieViewHolder)holder).movieName_textView.setText(movie.movieName);
            if(movie.movieThumbnailUrl!=null) {
                //parse url inside xml image_thumbnail
                Picasso.get().load(base_url + movie.movieThumbnailUrl).into(((MovieViewHolder)holder).movieThumbnail);
//                Picasso.get().load(movie.movieThumbnailUrl).into(holder.movieThumbnail);
            }else{
                ((MovieViewHolder)holder).movieThumbnail.setImageResource(R.drawable.image_place_holder);
//                Picasso.get().load(R.drawable.image_place_holder).into(holder.movieThumbnail);
            }

//            String name = getDataVertical().get(position-1).mName;
//            Log.d("###", "Setting name: " + name);
//            ((MyViewHolder) holder).getmDataTextView().setText(name);
        } else if (holder instanceof CategoryMovieViewHolder) {
            String text;
            if(requestedCategoryMovies!=null){
                if(requestedMovies==null || requestedMovies.size()==0){
                    ((CategoryMovieViewHolder)holder).movies_tag_word.setVisibility(View.GONE);
                }else{
                    ((CategoryMovieViewHolder)holder).movies_tag_word.setVisibility(View.VISIBLE);
                }
                text  = "Category " + '"'+categoryName+'"';
                ((CategoryMovieViewHolder)holder).categoryName.setVisibility(View.VISIBLE);
                ((CategoryMovieViewHolder)holder).recyclerView.setVisibility(View.VISIBLE);
                ((CategoryMovieViewHolder)holder).category_hz_list.setPadding(10,10,10,10);

            }else{
                text = "";
                ((CategoryMovieViewHolder)holder).categoryName.setVisibility(View.GONE);
                ((CategoryMovieViewHolder)holder).recyclerView.setVisibility(View.GONE);
                ((CategoryMovieViewHolder)holder).category_hz_list.setPadding(0,0,0,0);
            }

            ((CategoryMovieViewHolder)holder).categoryName.setText(text);

            ((CategoryMovieViewHolder)holder).recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//            ((CategoryMovieViewHolder)holder).recyclerView.setHasFixedSize(true);
            ((CategoryMovieViewHolder)holder).recyclerView.setAdapter(new MovieAdapter(context, requestedCategoryMovies,true));
//            ((CategoryMovieViewHolder)holder).categoryName.setText(categories.get(position).categoryName);
        }

    }

    @Override
    public int getItemCount() {
      if(requestedMovies!=null && requestedMovies.size()>0){
          System.out.println("YAAAAA"+requestedMovies.size());
            return requestedMovies.size()+1;
        }else{
          if(requestedCategoryMovies!=null && requestedCategoryMovies.size()>0){
              return 1;
          }else{
              return 0;
          }

        }
    }



    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView movieThumbnail;
        public TextView movieName_textView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieName_textView = (TextView) itemView.findViewById(R.id.search_movieName_textView);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.search_movie_thumbnail);
        }
    }

    private static class CategoryMovieViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView categoryName;
        TextView movies_tag_word;
        ConstraintLayout category_hz_list;

        public CategoryMovieViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.movies_recycler_view);
            categoryName = (TextView) itemView.findViewById(R.id.category_name_textView);
            category_hz_list = (ConstraintLayout) itemView.findViewById(R.id.category_horizontal_list_layout);
            movies_tag_word = (TextView) itemView.findViewById(R.id.movieList_results);
            categoryName.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            category_hz_list.setPadding(0,0,0,0);
        }
    }
    public  void filterList(ArrayList<Movie> filteredList,boolean isCategoryMovie,String categoryName){
        if(isCategoryMovie){
            requestedCategoryMovies = filteredList;
            this.categoryName = categoryName;
        }else{
            requestedMovies = filteredList;
        }

        notifyDataSetChanged();
    }
}