package com.sismatix.drskin.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Adapter.Product_recycler_adapter;
import com.sismatix.drskin.Adapter.Wishlist_Adapter;
import com.sismatix.drskin.Model.Product_Grid_Model;
import com.sismatix.drskin.Model.Wishlist_Model;
import com.sismatix.drskin.Preference.CheckNetwork;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.ApiClient;
import com.sismatix.drskin.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Wishlist extends Fragment implements View.OnClickListener {
    RecyclerView recycler_wishlist;
    public static List<Wishlist_Model> wishlist_models = new ArrayList<Wishlist_Model>();
    public static Wishlist_Adapter wishlist_adapter;
    Toolbar toolbar_mywishlist;
    static AppCompatActivity activity;
    static ProgressBar progressBar;
    static LinearLayout lv_productnotavelablewish,l_cartshow;
    public static String customerid;
    public static Button cart_count;
    TextView tv_wishlist_title;
    ImageView searhch;

    public Wishlist() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_wishlist, container, false);
        toolbar_mywishlist = (Toolbar) v.findViewById(R.id.toolbar_mywishlist);
        recycler_wishlist = (RecyclerView) v.findViewById(R.id.recycler_wishlist);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBar);
        cart_count=(Button) v.findViewById(R.id.cart_count);
        searhch=(ImageView) v.findViewById(R.id.searhch);
        tv_wishlist_title=(TextView) v.findViewById(R.id.tv_wishlist_title);
        tv_wishlist_title.setTypeface(Home.opensans_bold);
        String cart_countt=Login_preference.getCart_item_count(getActivity());
        if (cart_countt.equalsIgnoreCase("")|| cart_countt == "null"){
            cart_count.setText("0");
        }else {
            cart_count.setText(Login_preference.getCart_item_count(getActivity()));
        }
        lv_productnotavelablewish = (LinearLayout) v.findViewById(R.id.lv_productnotavelable);
        l_cartshow = (LinearLayout) v.findViewById(R.id.l_cartshow);
        customerid=Login_preference.getcustomer_id(getActivity());
        Log.e("maincustomer",""+customerid);

        setHasOptionsMenu(true);
        ((Bottom_navigation) getActivity()).setSupportActionBar(toolbar_mywishlist);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);


        wishlist_adapter = new Wishlist_Adapter(getContext(), wishlist_models);
        recycler_wishlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycler_wishlist.setAdapter(wishlist_adapter);
        // snapHelper.attachToRecyclerView(recycler_wishlist);

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_WISHLIST_API();
        } else {
            Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        searhch.setOnClickListener(this);
        l_cartshow.setOnClickListener(this);

        return v;
    }

    public static void CALL_WISHLIST_API() {
        progressBar.setVisibility(View.VISIBLE);
        wishlist_models.clear();

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> wishlist = api.GetWishlist(customerid);
        //cartList.clear();
        wishlist.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("responseeeeee_wishlist", "" + response);
                progressBar.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    Log.e("response_object",""+jsonObject);
                    String status = jsonObject.getString("status");
                    Log.e("status_wishlist", "" + status);
                    if (status.equalsIgnoreCase("success")) {
                        String data=jsonObject.getString("product");
                        Log.e("data_wishlist",""+data);

                        if (data.equalsIgnoreCase("[]") || data.equalsIgnoreCase("")) {
                            lv_productnotavelablewish.setVisibility(View.VISIBLE);
                        } else {
                            lv_productnotavelablewish.setVisibility(View.GONE);
                            JSONArray jsonArray = jsonObject.getJSONArray("product");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject wish_object = jsonArray.getJSONObject(i);
                                    Log.e("Name_wishlist", "" + wish_object.getString("name"));
                                    wishlist_models.add(new Wishlist_Model("" + wish_object.getString("image"),
                                            "" + wish_object.getString("name"),
                                            "" + wish_object.getString("price"),
                                            "" + wish_object.getString("category"),
                                            "" + wish_object.getString("product_id")));
                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                    wishlist_adapter.notifyItemChanged(i);
                                    wishlist_adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    } else if (status.equalsIgnoreCase("error")) {
                    }
                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
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
    public void onClick(View view) {
        if (view == searhch){
            pushFragment(new Search(),"search");
        }else if(view == l_cartshow){
            pushFragment(new MyCart(),"cart");
        }
    }
}
