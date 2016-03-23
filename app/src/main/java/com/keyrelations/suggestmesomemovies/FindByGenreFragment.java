package com.keyrelations.suggestmesomemovies;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FindByGenreFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
    List<Movie> movie;
    FindMovieAdapter adapter;
    ListView lv;
    String url = "http://api.keyrelations.in/smsm/findmovie/" + AccessToken.getCurrentAccessToken().getToken() +"/1";
    RequestQueue queue;
    JsonArrayRequest jsArrRequest;
    TextView textMsg;
    TextView genreIcon;
    ProgressBar spinner;
    Spinner select;
    ArrayAdapter<CharSequence> selectAdapter;

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Log.d("DEBUGLOG",String.valueOf(pos));
        String genreSearchId="1";
        switch (pos) {
            case 0:
                genreSearchId = "1";
                break;
            case 1:
                genreSearchId = "2";
                break;
            case 2:
                genreSearchId = "3";
                break;
            case 3:
                genreSearchId = "4";
                break;
            case 4:
                genreSearchId = "5";
                break;
            case 5:
                genreSearchId = "6";
                break;
            case 6:
                genreSearchId = "7";
                break;
            case 7:
                genreSearchId = "8";
                break;
            case 8:
                genreSearchId = "9";
                break;
            case 9:
                genreSearchId = "11";
                break;
            case 10:
                genreSearchId = "12";
                break;
            case 11:
                genreSearchId = "13";
                break;
            case 12:
                genreSearchId = "14";
                break;
            case 13:
                genreSearchId = "15";
                break;
            case 14:
                genreSearchId = "16";
                break;
            case 15:
                genreSearchId = "17";
                break;
            case 16:
                genreSearchId = "18";
                break;
            case 17:
                genreSearchId = "19";
                break;
            case 18:
                genreSearchId = "20";
                break;

        }
        url = "http://api.keyrelations.in/smsm/findmovie/" + AccessToken.getCurrentAccessToken().getToken() +"/"+genreSearchId;
        jsArrRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("DEBUGLOG",String.valueOf(url));
                        //Log.d("DEBUGLOG","Response is valid");
                        //Log.d("DEBUGLOG", "Response has " + String.valueOf(response.length()) + " records");

                        if(response.length()==0){
                            movie.clear();
                            adapter.notifyDataSetChanged();
                            textMsg.setText(getResources().getString(R.string.no_data_found));
                        }
                        else{
                            movie.clear();
                            adapter.notifyDataSetChanged();
                            textMsg.setText("");
                            //setTitle(title + " (" + String.valueOf(response.length())+")");
                        }
                        try {

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
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spinner.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                //Log.d("DEBUGLOG", "API call failed. Unable to retrieve data");
                textMsg.setText(getResources().getString(R.string.server_error));
            }
        });

        jsArrRequest.setShouldCache(false);
        //Log.d("DEBUGLOG", "Request sent to server for movie query");
        spinner.setVisibility(View.VISIBLE);
        queue.add(jsArrRequest);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }



    public void refreshActivityData() {
        queue.add(jsArrRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.findbygenre_layout, container, false);

        textMsg = (TextView) rootView.findViewById(R.id.textViewMessage);

        genreIcon = (TextView) rootView.findViewById(R.id.textViewGenreIcon);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");
        genreIcon.setTypeface(font);

        select = (Spinner) rootView.findViewById(R.id.genre_spinner);
        select.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        selectAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.genre_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        selectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        select.setAdapter(selectAdapter);

        spinner = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        //change the spinner color
        spinner.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        spinner.setVisibility(View.GONE);


        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshActivityData();
            }
        });

        movie = new ArrayList<>();
        adapter = new FindMovieAdapter(getContext(), R.layout.findmovie_list, movie);
        lv = (ListView) rootView.findViewById(R.id.listViewFindMovie);
        lv.setAdapter(adapter);

        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();

        //define the json request
        jsArrRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("DEBUGLOG",String.valueOf(url));
                        //Log.d("DEBUGLOG","Response is valid");
                        //Log.d("DEBUGLOG", "Response has " + String.valueOf(response.length()) + " records");

                        if(response.length()==0){
                            movie.clear();
                            textMsg.setText(getResources().getString(R.string.no_data_found));
                        }
                        else{
                            movie.clear();
                            textMsg.setText("");
                            //setTitle(title + " (" + String.valueOf(response.length())+")");
                        }
                        try {

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
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spinner.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                //Log.d("DEBUGLOG", "API call failed. Unable to retrieve data");
                textMsg.setText(getResources().getString(R.string.server_error));
            }
        });

        jsArrRequest.setShouldCache(false);
        //Log.d("DEBUGLOG", "Request sent to server for movie query");
        spinner.setVisibility(View.VISIBLE);
        queue.add(jsArrRequest);

        return rootView;
    }
}
