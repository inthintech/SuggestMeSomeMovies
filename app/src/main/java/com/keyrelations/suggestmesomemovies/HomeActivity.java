package com.keyrelations.suggestmesomemovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.common.logging.LoggingDelegate;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    //new request queue

    //RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

    Uri uri;
    SimpleDraweeView draweeView;
    TextView userName;

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
    TextView textMsg;
    EditText filterText;


    private String[] mFilterTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    ArrayAdapter<String> sAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void navigateToAddMovieActivity() {
        Intent intent = new Intent(this, AddMovieActivity.class);
        startActivity(intent);
    }

    public void navigateToLoginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void navigateToMovieInfoActivity(String movieId) {
        Intent intent = new Intent(this, MovieInfoActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }

    public void navigateToFindMovieActivity(String searchId,String searchName) {
        Intent intent = new Intent(this, FindMovieActivity.class);
        String genreSearchId = "0";
        switch (searchId) {
            case "0":
                genreSearchId = "1";
                break;
            case "1":
                genreSearchId = "2";
                break;
            case "2":
                genreSearchId = "3";
                break;
            case "3":
                genreSearchId = "4";
                break;
            case "4":
                genreSearchId = "5";
                break;
            case "5":
                genreSearchId = "6";
                break;
            case "6":
                genreSearchId = "7";
                break;
            case "7":
                genreSearchId = "8";
                break;
            case "8":
                genreSearchId = "9";
                break;
            case "9":
                genreSearchId = "11";
                break;
            case "10":
                genreSearchId = "12";
                break;
            case "11":
                genreSearchId = "13";
                break;
            case "12":
                genreSearchId = "14";
                break;
            case "13":
                genreSearchId = "15";
                break;
            case "14":
                genreSearchId = "16";
                break;
            case "15":
                genreSearchId = "17";
                break;
            case "16":
                genreSearchId = "18";
                break;
            case "17":
                genreSearchId = "19";
                break;
            case "18":
                genreSearchId = "20";
                break;
            case "19":
                genreSearchId = "99";
                break;
        }
        intent.putExtra("searchType", genreSearchId);
        intent.putExtra("searchName", searchName);
        startActivity(intent);
    }

    public void refreshActivityData() {
        queue.add(jsArrRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // getSupportActionBar().setIcon(R.drawable.imdb);
        textMsg = (TextView) findViewById(R.id.textViewMessage);


        userName = (TextView) findViewById(R.id.navMenuUserName);

        draweeView = (SimpleDraweeView) findViewById(R.id.fbprofilepic);



        if(Profile.getCurrentProfile()!=null) {
            uri = Profile.getCurrentProfile().getProfilePictureUri(200, 200);
            userName.setText(Profile.getCurrentProfile().getName());
            draweeView.setImageURI(uri);
        }
        else {

            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code
                            try {
                                //Log.d("GRAPHAPI",object.getJSONObject("picture").getJSONObject("data").getString("url"));
                                uri = Uri.parse(object.getJSONObject("picture").getJSONObject("data").getString("url"));
                                draweeView.setImageURI(uri);
                                userName.setText(object.getString("name"));
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "name,picture");
            request.setParameters(parameters);
            request.executeAsync();

        }


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                navigateToAddMovieActivity();
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshActivityData();
            }
        });

        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        //change the spinner color
        spinner.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);


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

        movie = new ArrayList<>();
        adapter = new MyLibraryAdapter(this, R.layout.mylibrary_list, movie);
        lv = (ListView) findViewById(R.id.listViewMyLibrary);
        lv.setAdapter(adapter);


        filterText = (EditText) findViewById(R.id.filterMovieTextBox);

        filterText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(movie.size()>0) {
                    adapter.getFilter().filter(s.toString());
                }
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Movie mov = (Movie) lv.getItemAtPosition(position);
                Movie mov = (Movie) lv.getAdapter().getItem(position);
                ////Log.d("CLICKED", String.valueOf(mov.getId()));
                navigateToMovieInfoActivity(String.valueOf(mov.getId()));
            }
        });


        queue = VolleySingleton.getInstance(this).getRequestQueue();


        //define the json request
        jsArrRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()==0){
                            textMsg.setText("Your movie library is empty.");
                        }
                        else {
                            getSupportActionBar().setTitle("My Library ("+response.length()+")");
                            filterText.setText("");
                        }
                        try {
                            movie.clear();
                            for (int i = 0; i < response.length(); i++) {
                                Movie mov = new Movie(response.getJSONObject(i).getString("id"), response.getJSONObject(i).getString("title"), response.getJSONObject(i).getString("release_year"), response.getJSONObject(i).getString("poster_path"), response.getJSONObject(i).getString("is_suggested"), response.getJSONObject(i).getString("suggested_cnt"), response.getJSONObject(i).getString("imdb_rating"));
                                movie.add(mov);
                                if (i == response.length() - 1) {
                                    //lv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    //Log.d("SERVICES", "UPDATED");
                                }
                            }

                        } catch (JSONException e) {
                            //e.printStackTrace();
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        spinner.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                spinner.setVisibility(View.GONE);
                textMsg.setText("Unable to retrieve data from server!");
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

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.itemLogout:
                LoginManager.getInstance().logOut();
                finish();
                navigateToLoginActivity();
                return true;
            case R.id.itemAbout:
                Toast.makeText(this, getResources().getString(R.string.menu_about_content), Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            ////Log.d("LEFTMENU",String.valueOf(position));
            navigateToFindMovieActivity(String.valueOf(position),String.valueOf(parent.getItemAtPosition(position)));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void editMovieSuggestion(final String movieId, final String suggId) {
        spinner.setVisibility(View.VISIBLE);
        //Log.d("DEBUGLOG", "Started to add movie");
        url = "http://api.keyrelations.in/smsm/modusersuggestion/" + AccessToken.getCurrentAccessToken().getToken() + "/" + movieId + "/" + suggId;
        //Log.d("DEBUGLOG", url);
        JsonArrayRequest jrArrRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("DEBUGLOG", "Response is valid");
                        if (suggId.equals("1")) {

                            Toast.makeText(getBaseContext(), "Movie suggested to users", Toast.LENGTH_SHORT).show();
                            //Log.d("DEBUGLOG", "Movie suggested");
                        } else {

                            Toast.makeText(getBaseContext(), "Movie unsuggested to users", Toast.LENGTH_SHORT).show();
                            //Log.d("DEBUGLOG", "Movie unsuggested");
                        }
                        spinner.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ////Log.d("ERROR", "ERROR");
                //textMsg.setText("Error! Please try again.");
                spinner.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), "Error! Please try again", Toast.LENGTH_SHORT).show();
                //Log.d("DEBUGLOG", "API call failed");
            }
        });
        jrArrRequest.setShouldCache(false);
        queue.add(jrArrRequest);
    }

}
