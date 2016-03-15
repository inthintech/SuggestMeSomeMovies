package com.keyrelations.suggestmesomemovies;

import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.AccessToken;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

public class MovieInfoActivity extends AppCompatActivity {

    RequestQueue queue;
    String url;
    JsonArrayRequest jsArrRequest;
    ProgressBar spinner;
    Uri backdropImgLink;
    Uri posterImgLink;
    SimpleDraweeView backdropImg;
    SimpleDraweeView posterImg;
    Typeface font;
    TextView genreIcon;

    TextView textMsg;

    TextView movieName;
    TextView releaseYear;
    TextView movieGenre;
    TextView imdbRating;
    TextView moviePlot;
    TextView director;
    TextView actor;

    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        mainLayout = (RelativeLayout) findViewById(R.id.movieInfoMainLayout);

        font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        genreIcon = (TextView) findViewById(R.id.textViewMovieGenreIcon);
        genreIcon.setTypeface(font);

        textMsg = (TextView) findViewById(R.id.textViewMessage);


        backdropImgLink = Uri.parse("http://image.tmdb.org/t/p/w500/");
        posterImgLink = Uri.parse("http://image.tmdb.org/t/p/w342/");
        movieName = (TextView) findViewById(R.id.textViewMovieName);
        movieName.setText("");
        releaseYear = (TextView) findViewById(R.id.textViewMovieReleaseYear);
        releaseYear.setText("");
        movieGenre = (TextView) findViewById(R.id.textViewMovieGenreContent);
        movieGenre.setText("");
        imdbRating = (TextView) findViewById(R.id.textViewIMDBRating);
        imdbRating.setText("");
        moviePlot = (TextView) findViewById(R.id.textViewPlotContent);
        moviePlot.setText("");
        director = (TextView) findViewById(R.id.textViewDirectorContent);
        director.setText("");
        actor = (TextView) findViewById(R.id.textViewActorContent);
        actor.setText("");

        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        //change the spinner color
        spinner.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("movieId") != null){
                url = "http://api.keyrelations.in/smsm/getmovieinfo/"+String.valueOf(AccessToken.getCurrentAccessToken().getToken())+"/"+extras.getString("movieId");
            }
        }


        //define the json request
        jsArrRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try{
                            backdropImgLink = Uri.parse("http://image.tmdb.org/t/p/w500/"+response.getJSONObject(0).getString("backdrop_path"));
                            posterImgLink = Uri.parse("http://image.tmdb.org/t/p/w342/"+response.getJSONObject(0).getString("poster_path"));
                            movieName.setText(response.getJSONObject(0).getString("title"));
                            releaseYear.setText(response.getJSONObject(0).getString("ryear"));
                            movieGenre.setText(response.getJSONObject(0).getString("genre"));
                            imdbRating.setText(response.getJSONObject(0).getString("imdb_rating"));
                            moviePlot.setText(response.getJSONObject(0).getString("plot"));
                            director.setText(response.getJSONObject(0).getString("director"));
                            actor.setText(response.getJSONObject(0).getString("actor"));

                            backdropImg = (SimpleDraweeView) findViewById(R.id.imageViewMovieBackDrop);
                            backdropImg.getHierarchy().setProgressBarImage(new ImageLoadProgressBar());
                            backdropImg.setImageURI(backdropImgLink);

                            posterImg = (SimpleDraweeView) findViewById(R.id.imageViewMoviePoster);
                            posterImg.getHierarchy().setProgressBarImage(new ImageLoadProgressBar());
                            posterImg.setImageURI(posterImgLink);

                            mainLayout.setVisibility(View.VISIBLE);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                        spinner.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textMsg.setText("No data available for this movie");
                error.printStackTrace();
                spinner.setVisibility(View.GONE);
            }
        });

        jsArrRequest.setShouldCache(false);
        jsArrRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        spinner.setVisibility(View.VISIBLE);
        queue.add(jsArrRequest);
    }
}
