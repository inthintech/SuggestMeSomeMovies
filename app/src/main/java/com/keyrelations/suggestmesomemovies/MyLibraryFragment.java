package com.keyrelations.suggestmesomemovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MyLibraryFragment extends Fragment {

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

    public void refreshActivityData() {
        queue.add(jsArrRequest);
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

                            Toast.makeText(getContext(), "Movie suggested to users", Toast.LENGTH_SHORT).show();
                            //Log.d("DEBUGLOG", "Movie suggested");
                        } else {

                            Toast.makeText(getContext(), "Movie unsuggested to users", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Error! Please try again", Toast.LENGTH_SHORT).show();
                //Log.d("DEBUGLOG", "API call failed");
            }
        });
        jrArrRequest.setShouldCache(false);
        queue.add(jrArrRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mylibrary_layout, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshActivityData();
            }
        });

        spinner = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        //change the spinner color
        spinner.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        movie = new ArrayList<>();
        adapter = new MyLibraryAdapter(getContext(), R.layout.mylibrary_list, movie);
        lv = (ListView) rootView.findViewById(R.id.listViewMyLibrary);
        lv.setAdapter(adapter);

        textMsg = (TextView) rootView.findViewById(R.id.textViewMessage);

        filterText = (EditText) rootView.findViewById(R.id.filterMovieTextBox);

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

        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();


        //define the json request
        jsArrRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()==0){
                            movie.clear();
                            textMsg.setText(getResources().getString(R.string.empty_library));
                        }
                        else {
                            //getSupportActionBar().setTitle("My Library ("+response.length()+")");
                            textMsg.setText("");
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
                textMsg.setText(getResources().getString(R.string.server_error));
            }
        });

        jsArrRequest.setShouldCache(false);
        queue.add(jsArrRequest);
        spinner.setVisibility(View.VISIBLE);


        




        return rootView;
    }

}
