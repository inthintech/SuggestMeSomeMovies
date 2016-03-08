package com.keyrelations.suggestmesomemovies;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public void navigateToAddMovieActivity(){
        Intent intent = new Intent(this, AddMovieActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                navigateToAddMovieActivity();
            }
        });

        // log the current user access token
        Log.d("USER_ACCESS_TOKEN",AccessToken.getCurrentAccessToken().getToken());

        String url = "http://api.keyrelations.in/smsm/getuserlibrary/"+AccessToken.getCurrentAccessToken().getToken();
        final List<Movie> movie = new ArrayList<>();
        final MyLibraryAdapter adapter = new MyLibraryAdapter(this, R.layout.mylibrary_list, movie);
        final ListView lv = (ListView) findViewById(R.id.listViewMyLibrary);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Movie mov = (Movie) lv.getItemAtPosition(position);
                Log.d("CLICKED", String.valueOf(mov.getId()));
            }
        });

        //new request queue
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        //define the json request
        final JsonArrayRequest jsArrRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                Movie mov = new Movie(response.getJSONObject(i).getString("id"), response.getJSONObject(i).getString("title"),response.getJSONObject(i).getString("release_year"),response.getJSONObject(i).getString("poster_path"),response.getJSONObject(i).getString("is_suggested"));
                                movie.add(mov);
                                if(i==response.length()-1){
                                    //lv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    Log.d("SERVICES","UPDATED");
                                }
                            }

                        } catch (JSONException e) {
                            //e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });



        queue.add(jsArrRequest);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
