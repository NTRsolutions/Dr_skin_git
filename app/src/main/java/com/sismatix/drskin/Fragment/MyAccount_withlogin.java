package com.sismatix.drskin.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.R;


public class MyAccount_withlogin extends Fragment implements View.OnClickListener {
    LinearLayout lv_signout,lv_mywwishlist,lv_accountinfo,lv_aboutus,lv_tac,lv_privacypolicy,lv_without_login,lv_withlogin,lv_orderhistory;
    TextView tv_email,tv_username;
    android.support.v7.widget.Toolbar toolbar_myaccount;
    String loginflagmain;


    public MyAccount_withlogin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_my_account_withlogin, container, false);
        Allocationmemory(v);
        setHasOptionsMenu(true);
        ((Bottom_navigation) getActivity()).setSupportActionBar(toolbar_myaccount);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
        loginflagmain=Login_preference.getLogin_flag(getActivity());
        if (loginflagmain.equalsIgnoreCase("1") || loginflagmain == "1") {
           lv_withlogin.setVisibility(View.VISIBLE);
           lv_without_login.setVisibility(View.GONE);

        } else {
            lv_withlogin.setVisibility(View.GONE);
            lv_without_login.setVisibility(View.VISIBLE);
        }


        lv_signout.setOnClickListener(this);
        lv_mywwishlist.setOnClickListener(this);
        lv_accountinfo.setOnClickListener(this);
        lv_aboutus.setOnClickListener(this);
        lv_privacypolicy.setOnClickListener(this);
        lv_tac.setOnClickListener(this);
        lv_orderhistory.setOnClickListener(this);
        lv_without_login.setOnClickListener(this);
        tv_email.setText(Login_preference.getemail(getActivity()));
        tv_username.setText(Login_preference.getfullname(getActivity()));
        return v;
    }

    @SuppressLint("NewApi")
    private void Allocationmemory(View v) {
        lv_signout=(LinearLayout)v.findViewById(R.id.lv_signout);
        lv_mywwishlist=(LinearLayout)v.findViewById(R.id.lv_mywwishlist);
        lv_accountinfo=(LinearLayout)v.findViewById(R.id.lv_accountinfo);
        lv_tac=(LinearLayout)v.findViewById(R.id.lv_tac);
        lv_without_login=(LinearLayout)v.findViewById(R.id.lv_without_login);
        lv_withlogin=(LinearLayout)v.findViewById(R.id.lv_withlogin);
        lv_privacypolicy=(LinearLayout)v.findViewById(R.id.lv_privacypolicy);
        lv_orderhistory=(LinearLayout)v.findViewById(R.id.lv_orderhistory);
        lv_aboutus=(LinearLayout)v.findViewById(R.id.lv_aboutus);
        tv_email=(TextView) v.findViewById(R.id.tv_email);
        tv_username=(TextView) v.findViewById(R.id.tv_username);
        toolbar_myaccount= (android.support.v7.widget.Toolbar) v.findViewById(R.id.toolbar_myaccount);

    }
    @Override
    public void onClick(View view) {
        if(view == lv_signout) {
            Login_preference.setLogin_flag(getActivity(), "0");
            Intent intent = new Intent(getActivity(), Bottom_navigation.class);
            startActivity(intent);
        }else if(view == lv_mywwishlist){
            pushFragment(new Wishlist(),"wishlist");
        }else if(view == lv_accountinfo){
            pushFragment(new Account_information(),"account_info");
        }else if(view == lv_aboutus){
            pushFragment(new Aboutus(),"aboutus");
        }else if(view == lv_tac){
            pushFragment(new Termsandcondition(),"Terms and Condition");
        }else if(view == lv_privacypolicy){
            pushFragment(new Privacy_policy(),"privacypolicy");
        }else if(view == lv_without_login){
            pushFragment(new SignIn(),"signin");
        }else if(view == lv_orderhistory){
            pushFragment(new MyOrders(),"myorders");
        }
    }

    private void pushFragment(Fragment fragment, String add_to_backstack) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out);
                ft.replace(R.id.rootLayout, fragment);
                ft.addToBackStack(add_to_backstack);
                ft.commit();
            }
        }
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
