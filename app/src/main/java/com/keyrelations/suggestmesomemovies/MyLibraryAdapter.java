package com.keyrelations.suggestmesomemovies;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class MyLibraryAdapter extends ArrayAdapter<Movie> implements Filterable {

    private List<Movie> movies;
    private List<Movie> originalMovies;
    private Context mContext;
    private ItemFilter mFilter = new ItemFilter();

    public MyLibraryAdapter(Context context, int resource, List<Movie> objects) {
        super(context, resource, objects);
        //AssetManager mngr = context.getAssets();
        movies = objects;
        originalMovies = objects;
        //filteredMovies.clear();
        this.mContext = context;
    }

    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public int getCount() {
        return movies.size();
    }


    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.mylibrary_list,parent,false);
        }

        final Movie movie = movies.get(position);

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");

        final Button suggestButton = (Button) convertView.findViewById(R.id.buttonEditSuggestion);
        suggestButton.setTypeface(font);

        if(movie.getIsSuggested().equals("Y")){
            suggestButton.setTextColor(ContextCompat.getColor(mContext,R.color.colorMovieSuggested));
        }
        else{
            suggestButton.setTextColor(ContextCompat.getColor(mContext,R.color.colorMovieUnSuggested));
        }

        suggestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mContext instanceof HomeActivity) {
                    if(movie.getIsSuggested().equals("Y")){
                        movie.setIsSuggested("N");
                        //suggestButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorMovieUnSuggested));
                        ((HomeActivity) mContext).editMovieSuggestion(movie.getId(), "0");
                    }
                    else{
                        movie.setIsSuggested("Y");
                        //suggestButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorMovieSuggested));
                        ((HomeActivity) mContext).editMovieSuggestion(movie.getId(), "1");
                    }
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




    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            /*
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Movie> list = getMovies();
            final List<Movie> nlist = getFilteredMovies();
            nlist.clear();
            int count = list.size();
            //final List<Movie> nlist = filteredMovies ;
            //Log.d("DEBUGLOG","Total movies is "+String.valueOf(movies.size()));
            //Log.d("DEBUGLOG","Total unfil movies is "+String.valueOf(unFilteredMovies.size()));
            //Log.d("DEBUGLOG","Total fil movies is "+String.valueOf(filteredMovies.size()));
            String filterableString ;
            if(!constraint.toString().isEmpty()){
                Log.d("DEBUGLOG","Filter String is not empty "+filterString);
            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getTitle();
                if (filterableString.toLowerCase().contains(filterString)) {
                    //nlist.add(list.get(i));
                    //Movie mov= new Movie(list.get(i).getId(),list.get(i).getTitle(),list.get(i).getReleaseYear(),list.get(i).getPosterPath(),list.get(i).getIsSuggested(),list.get(i).getSuggestedCount());
                    nlist.add(new Movie(list.get(i).getId(),list.get(i).getTitle(),list.get(i).getReleaseYear(),list.get(i).getPosterPath(),list.get(i).getIsSuggested(),list.get(i).getSuggestedCount()));
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            }
            else{
                Log.d("DEBUGLOG","Filter String is empty ");
                results.values = getOriginalMovies();
                results.count = getOriginalMovies().size();
            }

            //Log.d("DEBUGLOG","Total movies is "+String.valueOf(movies.size()));
            //Log.d("DEBUGLOG","Total unfil movies is "+String.valueOf(unFilteredMovies.size()));
            //Log.d("DEBUGLOG","Total fil movies is "+String.valueOf(filteredMovies.size()));
            */

            if(!constraint.toString().isEmpty()){
                Log.d("DEBUGLOG","Filter String is not empty "+constraint.toString().toLowerCase());

                List<Movie> nlist = new ArrayList<>();

                for (int i = 0; i < originalMovies.size(); i++) {
                    //Log.d("DEBUGLOG","Movie Name "+movies.get(i).getTitle().toLowerCase());
                    if (originalMovies.get(i).getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        //Log.d("DEBUGLOG","Match found as "+String.valueOf(i));
                        nlist.add(new Movie(originalMovies.get(i).getId(),originalMovies.get(i).getTitle(),originalMovies.get(i).getReleaseYear(),originalMovies.get(i).getPosterPath(),originalMovies.get(i).getIsSuggested(),originalMovies.get(i).getSuggestedCount(),originalMovies.get(i).getImdbRating()));

                    }
                }
                results.values = nlist;
                results.count = nlist.size();
            }
            else{
                Log.d("DEBUGLOG","Filter String is empty ");
                results.values = originalMovies;
                results.count = originalMovies.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            movies = (List<Movie>) results.values;
            Log.d("DEBUGLOG", "List is modified with rows " + String.valueOf(movies.size()));
            //Log.d("DEBUGLOG","List is modified with rows "+String.valueOf(movies.get(1).getId())+String.valueOf(movies.get(1).getTitle())+String.valueOf(movies.get(1).getReleaseYear())+String.valueOf(movies.get(1).getPosterPath())+String.valueOf(movies.get(1).getIsSuggested())+String.valueOf(movies.get(1).getSuggestedCount())+String.valueOf(movies.get(1).getImdbRating()));
            notifyDataSetChanged();

        }

    }


}
