package com.sid.androsid.ecodrive.BottomNavigation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sid.androsid.ecodrive.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EcoFragment extends Fragment {


    public EcoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eco, container, false);
    }

}
