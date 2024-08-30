package com.main.comicapp.adapters.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.models.Genre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GenreAdminAdapter extends RecyclerView.Adapter<GenreAdminAdapter.GenreViewHolder> {

    private List<Genre> genreList;
    private List<Genre> filteredGenreList;
    private OnGenreClickListener genreClickListener;
    private OnManageClickListener manageClickListener;
    private NoResultsCallback noResultsCallback;

    public interface OnGenreClickListener {
        void onGenreClick(Genre genre);
    }

    public interface OnManageClickListener {
        void onManageClick(Genre genre);
    }

    public interface NoResultsCallback {
        void onUpdateNoResultsVisibility(int itemCount);
    }

    public GenreAdminAdapter(OnGenreClickListener genreClickListener, OnManageClickListener manageClickListener, NoResultsCallback noResultsCallback) {
        this.genreClickListener = genreClickListener;
        this.manageClickListener = manageClickListener;
        this.noResultsCallback = noResultsCallback;
        this.genreList = new ArrayList<>();
        this.filteredGenreList = new ArrayList<>();
    }

    public void setGenres(List<Genre> genres) {
        this.genreList = genres;
        this.filteredGenreList = new ArrayList<>(genres);
        notifyDataSetChanged();
        if (noResultsCallback != null) {
            noResultsCallback.onUpdateNoResultsVisibility(getItemCount());
        }
    }

    public void filterGenres(String query) {
        filteredGenreList.clear();
        if (query.isEmpty()) {
            filteredGenreList.addAll(genreList);
        } else {
            String lowerCaseQuery = query.toLowerCase(Locale.ROOT);
            for (Genre genre : genreList) {
                if (genre.getName().toLowerCase(Locale.ROOT).contains(lowerCaseQuery)) {
                    filteredGenreList.add(genre);
                }
            }
        }

        Collections.sort(filteredGenreList, (g1, g2) -> g1.getName().compareToIgnoreCase(g2.getName()));

        notifyDataSetChanged();
        if (noResultsCallback != null) {
            noResultsCallback.onUpdateNoResultsVisibility(getItemCount());
        }
    }


    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_genre, parent, false);
        return new GenreViewHolder(itemView, genreClickListener, manageClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = filteredGenreList.get(position);
        holder.bind(genre);
    }

    @Override
    public int getItemCount() {
        return filteredGenreList != null ? filteredGenreList.size() : 0;
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView genreName;
        ImageView genreIcon;
        Button btnManage;
        Genre currentGenre;

        GenreViewHolder(View itemView, OnGenreClickListener genreClickListener, OnManageClickListener manageClickListener) {
            super(itemView);
            genreName = itemView.findViewById(R.id.tv_genre_name);
            genreIcon = itemView.findViewById(R.id.iv_genre_icon);
            btnManage = itemView.findViewById(R.id.btn_genre_status);

            itemView.setOnClickListener(v -> {
                if (genreClickListener != null && currentGenre != null) {
                    genreClickListener.onGenreClick(currentGenre);
                }
            });

            btnManage.setOnClickListener(v -> {
                if (manageClickListener != null && currentGenre != null) {
                    manageClickListener.onManageClick(currentGenre);
                }
            });
        }

        public void bind(Genre genre) {
            currentGenre = genre;
            genreName.setText(genre.getName());
        }
    }
}
