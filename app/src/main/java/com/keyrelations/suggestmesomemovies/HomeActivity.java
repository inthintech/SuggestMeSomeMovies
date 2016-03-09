package com.keyrelations.suggestmesomemovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    //new request queue
    //RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

    Toolbar toolbar;
    FloatingActionButton fab;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<Movie> movie;
    MyLibraryAdapter adapter;
    ListView lv;
    String url = "http://api.keyrelations.in/smsm/getuserlibrary/" + AccessToken.getCurrentAccessToken().getToken();
    RequestQueue queue;
    JsonArrayRequest jsArrRequest;
    ProgressBar spinner;

    private String[] mFilterTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    ArrayAdapter<String> sAdapter;


    public void navigateToAddMovieActivity(){
        Intent intent = new Intent(this, AddMovieActivity.class);
        startActivity(intent);
    }

    public void refreshActivityData(){
        queue.add(jsArrRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // getSupportActionBar().setIcon(R.drawable.imdb);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                navigateToAddMovieActivity();
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshActivityData();
            }
        });

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        //change the spinner color
        spinner.getIndeterminateDrawable().setColorFilter(0xFF4F0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        mFilterTitles = getResources().getStringArray(R.array.navmenu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, mFilterTitles);

        // Set the adapter for the list view
        mDrawerList.setAdapter(sAdapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        Uri uri = Profile.getCurrentProfile().getProfilePictureUri(200, 200);
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.fbprofilepic);
        draweeView.setImageURI(uri);
        //Log.d("FACEBOOK",Profile.getCurrentProfile().getProfilePictureUri(200,200).toString());

        TextView userName = (TextView) findViewById(R.id.navMenuUserName);
        userName.setText(Profile.getCurrentProfile().getName());

        movie = new ArrayList<>();
        adapter = new MyLibraryAdapter(this, R.layout.mylibrary_list, movie);
        lv = (ListView) findViewById(R.id.listViewMyLibrary);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Movie mov = (Movie) lv.getItemAtPosition(position);
                Log.d("CLICKED", String.valueOf(mov.getId()));
            }
        });



        queue = VolleySingleton.getInstance(this).getRequestQueue();


        //define the json request
        jsArrRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            movie.clear();
                            for (int i = 0; i < response.length(); i++) {
                                Movie mov = new Movie(response.getJSONObject(i).getString("id"), response.getJSONObject(i).getString("title"),response.getJSONObject(i).getString("release_year"),response.getJSONObject(i).getString("poster_path"),response.getJSONObject(i).getString("is_suggested"),response.getJSONObject(i).getString("suggested_cnt"),response.getJSONObject(i).getString("imdb_rating"));
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
                    mSwipeRefreshLayout.setRefreshing(false);spinner.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);spinner.setVisibility(View.GONE);
            }
        });

        jsArrRequest.setShouldCache(false);
        queue.add(jsArrRequest);
        spinner.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }*/


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //selectItem(position);
            Log.d("LEFTMENU",String.valueOf(position));
        }
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
