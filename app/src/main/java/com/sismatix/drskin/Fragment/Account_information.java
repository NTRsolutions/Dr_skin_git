package com.sismatix.drskin.Fragment;


import android.os.Bundle;
import android.renderscript.Allocation;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.Preference.MyAddress_Preference;
import com.sismatix.drskin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Account_information extends Fragment {
    Toolbar toolbar_accountinfo;
        TextView tv_username,tv_fullname,tv_email,tv_address;

    public Account_information() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_account_information, container, false);
        Allocationmemeory(v);
        setHasOptionsMenu(true);
        ((Bottom_navigation) getActivity()).setSupportActionBar(toolbar_accountinfo);
        ((Bottom_navigation) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_36dp);

        tv_username.setText(Login_preference.getfullname(getActivity()));
        tv_fullname.setText(Login_preference.getfullname(getActivity()));
        tv_email.setText(Login_preference.getemail(getActivity()));
        tv_address.setText(MyAddress_Preference.getAppartment(getActivity()) + "" + MyAddress_Preference.getStreetAddress(getActivity()) +
                MyAddress_Preference.getCity(getActivity()) + "" + MyAddress_Preference.getCountryId(getActivity()) + "" + MyAddress_Preference.getZipcode(getActivity()) + "" +
                MyAddress_Preference.getPhoneNumber(getActivity()));

        return v;
    }

    private void Allocationmemeory(View v) {
        toolbar_accountinfo=(Toolbar)v.findViewById(R.id.toolbar_accountinfo);
        tv_username=(TextView) v.findViewById(R.id.tv_username);
        tv_fullname=(TextView) v.findViewById(R.id.tv_fullname);
        tv_email=(TextView) v.findViewById(R.id.tv_email);
        tv_address=(TextView) v.findViewById(R.id.tv_address);
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
