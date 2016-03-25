package com.keyrelations.suggestmesomemovies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class FindMovieAdapter extends ArrayAdapter<Movie> {

    private List<Movie> movies;
    private Context mContext;
    private FindByGenreFragment findByGenreFragment;
    private TopSuggestionsFragment topSuggestionsFragment;

    public FindMovieAdapter(Context context, int resource, List<Movie> objects, FindByGenreFragment fragment) {
        super(context, resource, objects);
        //AssetManager mngr = context.getAssets();
        movies = objects;
        this.mContext = context;
        this.findByGenreFragment=fragment;
    }

    public FindMovieAdapter(Context context, int resource, List<Movie> objects, TopSuggestionsFragment fragment) {
        super(context, resource, objects);
        //AssetManager mngr = context.getAssets();
        movies = objects;
        this.mContext = context;
        this.topSuggestionsFragment=fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.findmovie_list,parent,false);
        }

        final Movie movie = movies.get(position);

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");

        Button addButton = (Button) convertView.findViewById(R.id.buttonAddMovie);
        addButton.setTypeface(font);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*if (mContext instanceof FindMovieActivity) {
                    ((FindMovieActivity) mContext).addMovie(movie.getId());
                }*/
                if(findByGenreFragment!=null) {
                    findByGenreFragment.addMovie(movie.getId());
                }
                if(topSuggestionsFragment!=null){
                    topSuggestionsFragment.addMovie(movie.getId());
                }
            }
        });

        TextView text1 = (TextView) convertView.findViewById(R.id.textViewMovieName);
        text1.setText(movie.getTitle());

        TextView text2 = (TextView) convertView.findViewById(R.id.textViewMovieReleaseYear);
        text2.setText(movie.getReleaseYear());

        TextView text3 = (TextView) convertView.findViewById(R.id.textViewIMDBRating);
        text3.setText(movie.getImdbRating());

        TextView text4 = (TextView) convertView.findViewById(R.id.textViewSuggestedCnt);
        text4.setTypeface(font);
        String suggCnt = getContext().getString(R.string.icon_people)+" "+movie.getSuggestedCount();
        text4.setText(suggCnt);

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
