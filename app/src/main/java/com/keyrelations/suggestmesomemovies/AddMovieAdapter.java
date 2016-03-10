package com.keyrelations.suggestmesomemovies;


import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class AddMovieAdapter extends ArrayAdapter<Movie> {

    private List<Movie> movies;
    private Context mContext;

    public AddMovieAdapter(Context context, int resource, List<Movie> objects) {
        super(context, resource, objects);
        //AssetManager mngr = context.getAssets();
        movies = objects;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.addmovie_list,parent,false);
        }

        final Movie movie = movies.get(position);

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");

        Button addButton = (Button) convertView.findViewById(R.id.buttonAddMovie);
        addButton.setTypeface(font);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mContext instanceof AddMovieActivity){
                    ((AddMovieActivity)mContext).addMovie(movie.getId());
                }
            }
        });

        TextView text1 = (TextView) convertView.findViewById(R.id.textViewMovieName);
        text1.setText(movie.getTitle());

        TextView text2 = (TextView) convertView.findViewById(R.id.textViewMovieReleaseYear);
        text2.setText(movie.getReleaseYear());

        Uri uri = Uri.parse("http://image.tmdb.org/t/p/w342/" + movie.getPosterPath());
        SimpleDraweeView draweeView = (SimpleDraweeView) convertView.findViewById(R.id.imageViewMoviePoster);

        if(movie.getPosterPath()!=null) {
            draweeView.getHierarchy().setProgressBarImage(new ImageLoadProgressBar());
            draweeView.setImageURI(uri);
        }
        else{
            draweeView.getHierarchy().setPlaceholderImage(R.drawable.noimage);
        }
        return convertView;
    }
}

