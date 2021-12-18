package com.codehub.movieinfoapp.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.models.Movie;
import com.codehub.movieinfoapp.rest_api.search_activity.SearchActivity;
import com.codehub.movieinfoapp.ui.fragments.HomeFragment;
import com.codehub.movieinfoapp.ui.fragments.MovieInfoFragment;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RequestedMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<Movie> requestedMovies;
    public ArrayList<Movie> requestedCategoryMovies;
    public Context context;
    private String categoryName;
    public boolean isLoading;
    private LayoutInflater layoutInflater;
    Parcelable categoryListState; // if there were many horizontal lists we should use a list of Parcelables instead.
    private String baseURL = "https://image.tmdb.org/t/p/w500";

    //Url Prefix for used as thumbnail url prefix
    private static final String base_url = "https://image.tmdb.org/t/p/w500";

    public RequestedMoviesAdapter(Context context,ArrayList<Movie> requestedMovies,ArrayList<Movie> requestedCategoryMovies,String categoryName,boolean isLoading) {
        this.requestedMovies = requestedMovies;
        this.categoryName = categoryName;
        this.isLoading = isLoading;
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
            ((MovieViewHolder)holder).movieName_textView.setText(movie.getMovieName());
            if(movie.getMovieThumbnailUrl()!=null) {
                //parse url inside xml image_thumbnail
                Picasso.get().load(base_url + movie.getMovieThumbnailUrl()).into(((MovieViewHolder)holder).movieThumbnail);
            }else{
                ((MovieViewHolder)holder).movieThumbnail.setImageResource(R.drawable.image_place_holder);
            }



            ((MovieViewHolder)holder).movieCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    System.out.println("position clicked: " + holder.getAdapterPosition());

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

                    transaction.addToBackStack("MovieInfoFragment");
                    transaction.replace(R.id.fragmentContainer, MovieInfoFragment.newInstance(
                            movie.getId(),
                            baseURL + movie.getMovieThumbnailUrl(),
                            movie.getMovieRating(),
                            false,
                            movie.getMovieName(),
                            movie.getMovieDescription()
                    ), "MovieInfoFragment").commit();

                    System.out.println("stack count" + activity.getSupportFragmentManager().getBackStackEntryCount());

                    FrameLayout fragmentContainer = activity.findViewById(R.id.fragmentContainer);
                    fragmentContainer.setTranslationY(1800);
                    fragmentContainer.animate().translationY(0).alpha(1).setDuration(250).setStartDelay(100).start();
                }
            });

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



            if (categoryListState!=null) {
//                ((CategoryMovieViewHolder) holder).layoutManager.scrollToPositionWithOffset(lastSeenFirstPosition, 0);
                ((CategoryMovieViewHolder) holder).layoutManager.onRestoreInstanceState(categoryListState);
            }
            ((CategoryMovieViewHolder)holder).categoryName.setText(text);

            ((CategoryMovieViewHolder)holder).recyclerView.setLayoutManager(((CategoryMovieViewHolder) holder).layoutManager);
//            ((CategoryMovieViewHolder)holder).recyclerView.setHasFixedSize(true);
            ((CategoryMovieViewHolder)holder).recyclerView.setAdapter(new MovieAdapter(context, requestedCategoryMovies));


            ((CategoryMovieViewHolder)holder).recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int pos = ((CategoryMovieViewHolder) holder).layoutManager.findLastCompletelyVisibleItemPosition();
                    int numItems = recyclerView.getAdapter().getItemCount();
                if (!isLoading) {
                    if (pos>=numItems-1) {
//                        holder.circular_indicator.setVisibility(View.VISIBLE); //Enable to show indicator on horizontal scroll
                        SearchActivity.getInstance().paginateCategoryResults((CategoryMovieViewHolder) holder);

                    }
                }
                }
            });

//            ((CategoryMovieViewHolder)holder).categoryName.setText(categories.get(position).categoryName);
        }

    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if(holder instanceof CategoryMovieViewHolder){
            categoryListState = ((CategoryMovieViewHolder) holder).layoutManager.onSaveInstanceState();
            super.onViewRecycled(holder);
        }

        // Store position

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
        public MaterialCardView movieCardView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieName_textView = itemView.findViewById(R.id.search_movieName_textView);
            movieThumbnail = itemView.findViewById(R.id.search_movie_thumbnail);
            movieCardView =  itemView.findViewById(R.id.movie_cardView);
        }
    }

    public static class CategoryMovieViewHolder extends RecyclerView.ViewHolder {
        private LinearLayoutManager layoutManager;
        RecyclerView recyclerView;
        TextView categoryName;
        TextView movies_tag_word;
        ConstraintLayout category_hz_list;

        public CategoryMovieViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.movies_recycler_view);
            categoryName =  itemView.findViewById(R.id.category_name_textView);
            category_hz_list = itemView.findViewById(R.id.category_horizontal_list_layout);
            movies_tag_word = itemView.findViewById(R.id.movieList_results);
            categoryName.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            layoutManager = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);
            category_hz_list.setPadding(0,0,0,0);
        }



    }


    public  void filterList(ArrayList<Movie> filteredList, boolean isCategoryMovie, String categoryName, boolean pagination){

        if(!pagination) {
            if (isCategoryMovie) {
                requestedCategoryMovies = filteredList;
                this.categoryName = categoryName;
            } else {
                requestedMovies = filteredList;
            }
            notifyDataSetChanged();
        }else{

                requestedMovies.addAll(filteredList);
//            notifyDataSetChanged();
                notifyItemRangeChanged(1,filteredList.size());

        }


    }
    public void updatePagUi(CategoryMovieViewHolder holder,ArrayList<Movie> filteredList){
        requestedCategoryMovies.addAll(filteredList);
        System.out.println(filteredList.size()+"ldldldlddld");
        holder.recyclerView.getAdapter().notifyDataSetChanged();
        isLoading=false;

    }

}
