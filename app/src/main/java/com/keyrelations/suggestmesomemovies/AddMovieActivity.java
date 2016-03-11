package com.keyrelations.suggestmesomemovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AddMovieActivity extends AppCompatActivity {

    String url;
    RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
    ProgressBar spinner;

    public void navigateToMovieInfoActivity(String movieId) {
        Intent intent = new Intent(this, MovieInfoActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        //Log.d("DEBUGLOG", "Activity Started");
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        //final Button searchButton = (Button) findViewById(R.id.searchButton);
        //searchButton.setTypeface(font);

        final TextView searchText = (TextView) findViewById(R.id.searchMovieTextBox);

        final TextView textMsg = (TextView) findViewById(R.id.textViewMessage);




        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        //change the spinner color
        spinner.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        spinner.setVisibility(View.GONE);


        final List<Movie> movie = new ArrayList<>();
        final AddMovieAdapter adapter = new AddMovieAdapter(this, R.layout.addmovie_list, movie);
        final ListView lv = (ListView) findViewById(R.id.listViewAddMovie);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Movie mov = (Movie) lv.getItemAtPosition(position);
                ////Log.d("CLICKED", String.valueOf(mov.getId()));
                navigateToMovieInfoActivity(String.valueOf(mov.getId()));
            }
        });

        /*
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if (!searchText.getText().toString().matches("")) {
                    ////Log.d("SEARCH", "ONCLICK");
                    textMsg.setText("");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchButton.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    spinner.setVisibility(View.VISIBLE);

                    //Log.d("DEBUGLOG", "A new search is started");
                    //String  input = searchText.getText().toString();
                    //input.replace(" ", "+");
                    url = "http://api.keyrelations.in/smsm/searchmovie/" + AccessToken.getCurrentAccessToken().getToken() + "/" + searchText.getText().toString().replace(" ","+");
                    //Log.d("URL",url);
                    movie.clear();

                    queue.add(new JsonArrayRequest(url,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    //Log.d("DEBUGLOG","Response is valid");
                                    //Log.d("DEBUGLOG", "Response has " + String.valueOf(response.length()) + " records");
                                    if(response.length()==0){
                                        textMsg.setText("No data found");
                                    }
                                    else{
                                        textMsg.setText("");
                                    }
                                    try {
                                        for (int i = 0; i < response.length(); i++) {
                                            Movie mov = new Movie(response.getJSONObject(i).getString("id"), response.getJSONObject(i).getString("title"), response.getJSONObject(i).getString("release_year"), response.getJSONObject(i).getString("poster_path"));
                                            movie.add(mov);
                                            if (i == response.length() - 1) {
                                                //lv.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
                                                //Log.d("DEBUGLOG","Data Updated");
                                            }

                                        }

                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }
                                    spinner.setVisibility(View.GONE);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Log.d("ERROR", "ERROR");
                            spinner.setVisibility(View.GONE);
                            textMsg.setText("Unable to retrieve data from server!");
                        }
                    }));

                }
            }
        });*/


        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                    if (!searchText.getText().toString().matches("")) {
                        ////Log.d("SEARCH", "ONCLICK");
                        textMsg.setText("");
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                        spinner.setVisibility(View.VISIBLE);

                        //Log.d("DEBUGLOG", "A new search is started");
                        //String  input = searchText.getText().toString();
                        //input.replace(" ", "+");
                        url = "http://api.keyrelations.in/smsm/searchmovie/" + AccessToken.getCurrentAccessToken().getToken() + "/" + searchText.getText().toString().replace(" ","+");
                        //Log.d("URL",url);
                        movie.clear();

                        queue.add(new JsonArrayRequest(url,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        //Log.d("DEBUGLOG","Response is valid");
                                        //Log.d("DEBUGLOG", "Response has " + String.valueOf(response.length()) + " records");
                                        if(response.length()==0){
                                            textMsg.setText("No data found");
                                        }
                                        else{
                                            textMsg.setText("");
                                        }
                                        try {
                                            for (int i = 0; i < response.length(); i++) {
                                                Movie mov = new Movie(response.getJSONObject(i).getString("id"), response.getJSONObject(i).getString("title"), response.getJSONObject(i).getString("release_year"), response.getJSONObject(i).getString("poster_path"));
                                                movie.add(mov);
                                                if (i == response.length() - 1) {
                                                    //lv.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                    //Log.d("DEBUGLOG","Data Updated");
                                                }

                                            }

                                        } catch (JSONException e) {

                                            e.printStackTrace();
                                        }
                                        spinner.setVisibility(View.GONE);
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Log.d("ERROR", "ERROR");
                                spinner.setVisibility(View.GONE);
                                textMsg.setText("Unable to retrieve data from server!");
                            }
                        }));

                    }

                    handled = true;
                }
                return handled;
            }
        });




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
