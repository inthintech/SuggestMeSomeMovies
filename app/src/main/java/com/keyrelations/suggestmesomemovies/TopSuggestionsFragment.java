package com.keyrelations.suggestmesomemovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.ads.AdView;

import java.util.List;

public class TopSuggestionsFragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.findmovie_list, container, false);


        return rootView;
    }


}
