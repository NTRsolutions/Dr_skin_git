package com.sismatix.drskin.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Adapter.My_orderlist_Adapter;
import com.sismatix.drskin.Model.My_order_model;
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
public class MyOrders extends Fragment {

    RecyclerView recycler_orderlist;
    private static List<My_order_model> my_order_models = new ArrayList<My_order_model>();
    private static My_orderlist_Adapter my_orderlist_adapter;
    ProgressBar progressBar;
    Toolbar toolbar_myorders;

    public MyOrders() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_orders, container, false);

        AllocateMemory(v);

        setHasOptionsMenu(true);
        ((Bottom_navigation) getActivity()).setSupportActionBar(toolbar_myorders);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
        // snapHelper.attachToRecyclerView(recycler_wishlist);

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_Orderlist_API();
        } else {
            Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    private void CALL_Orderlist_API() {
        my_order_models.clear();
        String cusid = Login_preference.getcustomer_id(getContext());
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> wishlist = api.AppOrderList(cusid);
        Log.e("cust_iddd", "" + Login_preference.getcustomer_id(getActivity()));
        wishlist.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_mo", "" + response.body().toString());

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_mo", "" + status);
                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("order");
                        Log.e("order_arr", "" + jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject order = jsonArray.getJSONObject(i);
                                Log.e("name_myorders", "" + order.getString("name"));
                                my_order_models.add(new My_order_model("" + order.getString("increment_id"),
                                        "" + order.getString("order_id"),
                                        "" + order.getString("status"),
                                        "" + order.getString("created_at"),
                                        "" + order.getString("name"),
                                        "" + order.getString("grand_total"),
                                        ""+order.getString("Paymentmethod")));
                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                my_orderlist_adapter.notifyItemChanged(i);
                                my_orderlist_adapter.notifyDataSetChanged();
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
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AllocateMemory(View v) {
        recycler_orderlist = (RecyclerView) v.findViewById(R.id.recycler_orderlist);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        toolbar_myorders = (Toolbar) v.findViewById(R.id.toolbar_myorders);

        my_orderlist_adapter = new My_orderlist_Adapter(getActivity(), my_order_models);
        recycler_orderlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycler_orderlist.setAdapter(my_orderlist_adapter);

    }


}
