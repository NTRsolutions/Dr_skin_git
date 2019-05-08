package com.sismatix.drskin.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
    LinearLayout lv_signout,lv_mywwishlist,lv_accountinfo,lv_aboutus,lv_tac,lv_privacypolicy,lv_without_login,lv_withlogin,lv_orderhistory, lv_call, lv_email,lv_insta;
    TextView tv_email,tv_username,tv_myaccount_title,tv_titlesinf,tv_textnorml,tv_spourt,tv_about,tv_terms,tv_priviry
    ,tv_gettuch,tv_contect,tv_phonee,tv_emailll,tv_singout,tv_prereences,tv_orderhistory,tv_my_wishlist,tv_accountinfo;
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
        ((Bottom_navigation) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);
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
        lv_call.setOnClickListener(this);
        lv_email.setOnClickListener(this);
        lv_insta.setOnClickListener(this);
        tv_email.setText(Login_preference.getemail(getActivity()));
        tv_email.setTypeface(Home.opensans_regular);
        tv_username.setText(Login_preference.getfullname(getActivity()));
        tv_username.setTypeface(Home.opensans_bold);
        tv_myaccount_title.setTypeface(Home.opensans_bold);
        tv_titlesinf.setTypeface(Home.opensans_bold);
        tv_textnorml.setTypeface(Home.opensans_regular);
        tv_spourt.setTypeface(Home.opensans_bold);
        tv_about.setTypeface(Home.opensans_regular);
        tv_terms.setTypeface(Home.opensans_regular);
        tv_priviry.setTypeface(Home.opensans_regular);
        tv_gettuch.setTypeface(Home.opensans_bold);
        tv_contect.setTypeface(Home.opensans_bold);
        tv_phonee.setTypeface(Home.opensans_regular);
        tv_emailll.setTypeface(Home.opensans_regular);
        tv_singout.setTypeface(Home.opensans_bold);
        tv_prereences.setTypeface(Home.opensans_bold);
        tv_orderhistory.setTypeface(Home.opensans_regular);
        tv_my_wishlist.setTypeface(Home.opensans_regular);
        tv_accountinfo.setTypeface(Home.opensans_regular);


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
        lv_call=(LinearLayout)v.findViewById(R.id.lv_call);
        lv_email=(LinearLayout)v.findViewById(R.id.lv_email);
        lv_insta=(LinearLayout)v.findViewById(R.id.lv_insta);
        tv_email=(TextView) v.findViewById(R.id.tv_email);
        tv_username=(TextView) v.findViewById(R.id.tv_username);
        tv_myaccount_title=(TextView) v.findViewById(R.id.tv_myaccount_title);
        tv_titlesinf=(TextView) v.findViewById(R.id.tv_titlesinf);
        tv_textnorml=(TextView) v.findViewById(R.id.tv_textnorml);
        tv_spourt=(TextView) v.findViewById(R.id.tv_spourt);
        tv_about=(TextView) v.findViewById(R.id.tv_about);
        tv_terms=(TextView) v.findViewById(R.id.tv_terms);
        tv_priviry=(TextView) v.findViewById(R.id.tv_priviry);
        tv_gettuch=(TextView) v.findViewById(R.id.tv_gettuch);
        tv_contect=(TextView) v.findViewById(R.id.tv_contect);
        tv_phonee=(TextView) v.findViewById(R.id.tv_phonee);
        tv_emailll=(TextView) v.findViewById(R.id.tv_emailll);
        tv_singout=(TextView) v.findViewById(R.id.tv_singout);
        tv_prereences=(TextView) v.findViewById(R.id.tv_prereences);
        tv_orderhistory=(TextView) v.findViewById(R.id.tv_orderhistory);
        tv_accountinfo=(TextView) v.findViewById(R.id.tv_accountinfo);
        tv_my_wishlist=(TextView) v.findViewById(R.id.tv_my_wishlist);
        toolbar_myaccount= (android.support.v7.widget.Toolbar) v.findViewById(R.id.toolbar_myaccount);

    }
    @Override
    public void onClick(View view) {
        if(view == lv_signout) {
            Login_preference.setLogin_flag(getActivity(), "0");
            Intent intent = new Intent(getActivity(), Bottom_navigation.class);
            Login_preference.prefsEditor.remove("quote_id").apply();
            Login_preference.prefsEditor.remove("item_count").apply();
            startActivity(intent);
        }else if(view == lv_mywwishlist){
            pushFragment(new Wishlist(),"wishlist");
        }else if(view == lv_accountinfo){
            loadFragment(new Account_information());
            //pushFragment(new Account_information(),"account_info");
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
        }else if (view == lv_call) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + 923145689));
            getActivity().startActivity(i);
        } else if (view == lv_insta) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dr.gloosy/"));
            startActivity(browserIntent);
        }else if (view == lv_email) {
            Intent it = new Intent(Intent.ACTION_SEND);
            it.putExtra(Intent.EXTRA_EMAIL, new String[]{"sales@doctorskin.net"});
            it.putExtra(Intent.EXTRA_SUBJECT,"");
            it.putExtra(Intent.EXTRA_TEXT,"");
            it.setType("message/rfc822");
            startActivity(Intent.createChooser(it,"Choose Mail App"));
            /*
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sales@doctorskin.net"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            getActivity().startActivity(Intent.createChooser(intent, ""));*/
        }
    }

    private void loadFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager manager = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.rootLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
