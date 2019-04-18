package com.sismatix.drskin.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAddress extends Fragment {

    Toolbar toolbar_addaddress;

    public AddAddress() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_address, container, false);

        AllocateMemory(v);
        setHasOptionsMenu(true);
        ((Bottom_navigation) getActivity()).setSupportActionBar(toolbar_addaddress);
        ((Bottom_navigation) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_36dp);

        return v;
    }

    private void AllocateMemory(View v) {
        toolbar_addaddress = (Toolbar) v.findViewById(R.id.toolbar_addaddress);
    }

}
