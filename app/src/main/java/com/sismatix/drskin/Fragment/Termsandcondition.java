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
public class Termsandcondition extends Fragment {
    Toolbar toolbar_termsconditions;
    TextView tv_termscondition;


    public Termsandcondition() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_termsandcondition, container, false);
        Allocationmemory(v);
        setHasOptionsMenu(true);
        ((Bottom_navigation) getActivity()).setSupportActionBar(toolbar_termsconditions);
        ((Bottom_navigation) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_36dp);
        tv_termscondition.setText(Html.fromHtml(getString(R.string.about_text)));
        return v;
    }
    private void Allocationmemory(View v) {
        toolbar_termsconditions=(Toolbar)v.findViewById(R.id.toolbar_termsconditions);
        tv_termscondition=(TextView) v.findViewById(R.id.tv_termscondition);

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
