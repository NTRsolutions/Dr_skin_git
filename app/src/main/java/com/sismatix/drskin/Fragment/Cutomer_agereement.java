package com.sismatix.drskin.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cutomer_agereement extends Fragment {

    TextView tv_agereement;
    Toolbar toolbar_agereement;
    public Cutomer_agereement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_cutomer_agereement, container, false);
        tv_agereement=(TextView)v.findViewById(R.id.tv_agereement);
        toolbar_agereement=(Toolbar)v.findViewById(R.id.toolbar_agereement);
        tv_agereement.setTypeface(Home.opensans_regular);
        tv_agereement.setText(Html.fromHtml(getString(R.string.agrement)));

        setHasOptionsMenu(true);
        ((Bottom_navigation) getActivity()).setSupportActionBar(toolbar_agereement);
        ((Bottom_navigation) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);

        return v;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
