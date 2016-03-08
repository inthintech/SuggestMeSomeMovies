package com.keyrelations.suggestmesomemovies;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setTypeface(font);

        final TextView searchText = (TextView) findViewById(R.id.searchMovieTextBox);


        final List<Movie> movie = new ArrayList<>();
        final AddMovieAdapter adapter = new AddMovieAdapter(this, R.layout.addmovie_list, movie);
        final ListView lv = (ListView) findViewById(R.id.listViewAddMovie);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Movie mov = (Movie) lv.getItemAtPosition(position);
                Log.d("CLICKED", String.valueOf(mov.getId()));
            }
        });

        String url = "http://api.keyrelations.in/smsm/searchmovie/" + AccessToken.getCurrentAccessToken().getToken() + "/rock";
        final RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        //define the json request
        final JsonArrayRequest jsArrRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                Movie mov = new Movie(response.getJSONObject(i).getString("id"), response.getJSONObject(i).getString("title"),response.getJSONObject(i).getString("release_year"),response.getJSONObject(i).getString("poster_path"));
                                movie.add(mov);
                                if(i==response.length()-1){
                                    //lv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    Log.d("SERVICES","UPDATED");
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR","ERROR");
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if (!searchText.getText().toString().matches("")) {
                    //Log.d("SEARCH", "ONCLICK");
                    //url = url + searchText.getText().toString();


                }
            }
        });

        queue.add(jsArrRequest);

    }
}
