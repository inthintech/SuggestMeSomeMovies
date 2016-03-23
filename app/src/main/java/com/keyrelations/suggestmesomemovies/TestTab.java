package com.keyrelations.suggestmesomemovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by srikanthanbalagan on 23/03/16.
 */
public class TestTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("Test 123");
        return rootView;
    }
}
