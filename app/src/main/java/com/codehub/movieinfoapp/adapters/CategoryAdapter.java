package com.codehub.movieinfoapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.models.MoviesCategory;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public ArrayList<MoviesCategory> categories;
    private Context context;
    private LayoutInflater layoutInflater;

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
        holder.recyclerView.setAdapter(new MovieAdapter(context, categories.get(position).movies,false));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setHasFixedSize(true);

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

        public CategoryViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.movies_recycler_view);
            categoryName = (TextView) itemView.findViewById(R.id.category_name_textView);
            movies_word_tag = (TextView) itemView.findViewById(R.id.movieList_results);
            movies_word_tag.setVisibility(View.GONE);
        }
    }
}