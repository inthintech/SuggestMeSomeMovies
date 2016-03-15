package com.keyrelations.suggestmesomemovies;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    public void navigateToMovieInfoActivity(String movieId) {
        Intent intent = new Intent(this, MovieInfoActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_movie);

        textMsg = (TextView) findViewById(R.id.textViewMessage);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        //change the spinner color
        spinner.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        spinner.setVisibility(View.GONE);

        movie = new ArrayList<>();
        adapter = new FindMovieAdapter(this, R.layout.findmovie_list, movie);
        lv = (ListView) findViewById(R.id.listViewFindMovie);
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
                            textMsg.setText("No data found for this genre");
                        }
                        else{
                            textMsg.setText("");
                            setTitle(title + " ("+String.valueOf(response.length())+")");
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
                textMsg.setText("Unable to retrieve data from server!");
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
