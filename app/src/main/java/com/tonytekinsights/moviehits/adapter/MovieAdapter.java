package com.tonytekinsights.moviehits.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonytekinsights.moviehits.R;
import com.tonytekinsights.moviehits.models.Movie;
import com.tonytekinsights.moviehits.models.MovieResults;
import com.tonytekinsights.moviehits.ui.MovieInfoActivity;
import com.tonytekinsights.moviehits.utilities.NetworkUtils;

import java.net.URL;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private MovieResults movieResults;
    private Context context;

    public void setData(MovieResults movieResults) {
        this.movieResults = movieResults;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.movieResults != null ? this.movieResults.getSize() : 0;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private ImageView movieImage;
        private URL urlImage = NetworkUtils.buildUrl(context, NetworkUtils.MOVIE_URL_IMAGE);
        private TextView movieTitle;
        private int movieId;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView)itemView.findViewById(R.id.mv_image);
            movieTitle = (TextView)itemView.findViewById(R.id.mv_title);
        }

        public void bind(int position){
            Movie movie = movieResults.getMovies().get(position);
            this.movieId = movie.getId();

            Picasso.with(context)
                    .load(urlImage + movie.getPosterPath())
                    //.placeholder(R.drawable.shape_movie_poster)
                    .into(movieImage);

            movieImage.setOnClickListener(this);
            movieTitle.setText(movie.getTitle());
        }

        public void onClick(View v) {
            Context context = v.getContext();

            if(!NetworkUtils.isOnline(context)) {
                NetworkUtils.noInternet(v);
            } else {
                Intent intent = new Intent(context, MovieInfoActivity.class);
                intent.putExtra(context.getString(R.string.movie_id), this.movieId);

                context.startActivity(intent);
            }
        }
    }
}
