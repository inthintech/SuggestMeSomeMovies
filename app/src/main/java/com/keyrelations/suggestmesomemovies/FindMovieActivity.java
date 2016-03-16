package com.keyrelations.suggestmesomemovies;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.ads.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FindMovieActivity extends AppCompatActivity {

    List<Movie> movie;
    FindMovieAdapter adapter;
    ListView lv;
    String url;
    RequestQueue queue;
    JsonArrayRequest jsArrRequest;
    TextView textMsg;
    ProgressBar spinner;
    String title;
    AdView adView;
    RelativeLayout mlayout;

    public void navigateToMovieInfoActivity(String movieId) {
        Intent intent = new Intent(this, MovieInfoActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_movie);

        // Instantiate an AdView view
        adView = new AdView(this, "223264698023434_232733610409876", AdSize.BANNER_HEIGHT_50);


        adView.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                // Ad failed to load.
                // Add code to hide the ad's view

                adView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad was loaded
                // Add code to show the ad's view
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Use this function to detect when an ad was clicked.
            }

        });

        // Find the main layout of your activity
        mlayout = (RelativeLayout)findViewById(R.id.layoutFindMovie);

        // Add the ad view to your activity layout
        mlayout.addView(adView);

        adView.setId(R.id.reservedNamedIdAds);

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        p.addRule(RelativeLayout.BELOW, R.id.reservedNamedIdAds);



        //AdSettings.addTestDevice("c69b4f0755b4deb0c0bb4061f950e40a");

        // Request to load an ad
        adView.loadAd();

        textMsg = (TextView) findViewById(R.id.textViewMessage);

        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        //change the spinner color
        spinner.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        spinner.setVisibility(View.GONE);

        movie = new ArrayList<>();
        adapter = new FindMovieAdapter(this, R.layout.findmovie_list, movie);
        lv = (ListView) findViewById(R.id.listViewFindMovie);
        if(adView.getVisibility()==View.VISIBLE) {
            lv.setLayoutParams(p);
        }
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Movie mov = (Movie) lv.getItemAtPosition(position);
                ////Log.d("CLICKED", String.valueOf(mov.getId()));
                navigateToMovieInfoActivity(String.valueOf(mov.getId()));
            }
        });

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("searchType") != null){
                url = "http://api.keyrelations.in/smsm/findmovie/" + AccessToken.getCurrentAccessToken().getToken() +"/"+extras.getString("searchType");
            }
            if(extras.getString("searchName") != null){
                title = extras.getString("searchName");
                setTitle(title);
            }
        }

        //define the json request
        jsArrRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("DEBUGLOG",String.valueOf(url));
                        //Log.d("DEBUGLOG","Response is valid");
                        //Log.d("DEBUGLOG", "Response has " + String.valueOf(response.length()) + " records");
                        if(response.length()==0){
                            textMsg.setText(getResources().getString(R.string.no_data_found));
                        }
                        else{
                            textMsg.setText("");
                            setTitle(title + " (" + String.valueOf(response.length())+")");
                        }
                        try {
                            movie.clear();
                            for (int i = 0; i < response.length(); i++) {
                                Movie mov = new Movie(response.getJSONObject(i).getString("id"), response.getJSONObject(i).getString("title"),response.getJSONObject(i).getString("release_year"),response.getJSONObject(i).getString("poster_path"),response.getJSONObject(i).getString("suggested_cnt"),response.getJSONObject(i).getString("imdb_rating"));
                                movie.add(mov);
                                if(i==response.length()-1){
                                    //lv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    //Log.d("DEBUGLOG", "Data is now updated");
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Log.d("DEBUGLOG", "JSON Exception occured");
                        }
                        spinner.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spinner.setVisibility(View.GONE);
                //Log.d("DEBUGLOG", "API call failed. Unable to retrieve data");
                textMsg.setText(getResources().getString(R.string.server_error));
            }
        });

        jsArrRequest.setShouldCache(false);
        //Log.d("DEBUGLOG", "Request sent to server for movie query");
        spinner.setVisibility(View.VISIBLE);
        queue.add(jsArrRequest);


    }

    public void addMovie(final String movieId){
        spinner.setVisibility(View.VISIBLE);
        //Log.d("DEBUGLOG", "Started to add movie");
        url = "http://api.keyrelations.in/smsm/addusermovie/" + AccessToken.getCurrentAccessToken().getToken() + "/" + movieId;
        //Log.d("DEBUGLOG", url);
        JsonArrayRequest jrArrRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("DEBUGLOG", "Response is valid");
                        ////Log.d("DEBUGLOG", "Response has " + String.valueOf(response.length()) + " records");
                        try {
                            ////Log.d("DEBUGLOG", response.getJSONObject(0).getString("message"));
                            ////Log.d("DEBUGLOG", movieId);

                            if (response.getJSONObject(0).getInt("code")==1) {
                                spinner.setVisibility(View.GONE);
                                Toast.makeText(getBaseContext(), "Movie added to library", Toast.LENGTH_SHORT).show();
                                //Log.d("DEBUGLOG", "Movie added");
                            } else {
                                spinner.setVisibility(View.GONE);
                                Toast.makeText(getBaseContext(), "Movie added in library", Toast.LENGTH_SHORT).show();
                                //Log.d("DEBUGLOG", "Movie added");
                            }
                        } catch (JSONException e) {

                            spinner.setVisibility(View.GONE);
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ////Log.d("ERROR", "ERROR");
                //textMsg.setText("Error! Please try again.");
                spinner.setVisibility(View.GONE);
                //Log.d("DEBUGLOG", "Error Response");
            }
        });
        jrArrRequest.setShouldCache(false);
        queue.add(jrArrRequest);
    }
}
