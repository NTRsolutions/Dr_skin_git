package com.sismatix.drskin.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.drskin.Adapter.Confirmation_cart_Adapter;
import com.sismatix.drskin.Model.Cart_Model;
import com.sismatix.drskin.Preference.CheckNetwork;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.Preference.MyAddress_Preference;
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
public class OrderSummary extends Fragment {

    TextView tv_status,tv_orderid,tv_date,tv_pay_met,tv_add_os,tv_fname,tv_subtotal,tv_ship_fees,tv_gtotal;
    String order_id;
    RecyclerView order_summary_cart;
    private List<Cart_Model> cartList = new ArrayList<Cart_Model>();
    private Confirmation_cart_Adapter confirmation_cart_adapter;
    String qt;

    public OrderSummary() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order_summary, container, false);

        AllocateMemory(v);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            order_id = bundle.getString("order");
        }

        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            CALL_ORDER_SUMMARY_API(order_id);

        } else {
            Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return v;
    }
    private void CALL_ORDER_SUMMARY_API(final String order_id) {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> orderSummary = api.GetOrderSummary(order_id);
        orderSummary.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response",""+response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("Success")) {

                        String order = jsonObject.getString("order");
                        Log.e("order",""+order);
                        JSONObject order_obj = jsonObject.getJSONObject("order");
                        Log.e("orderObj",""+order_obj);

                        String status_order = order_obj.getString("status");
                        Log.e("status_order ",""+status_order );

                        String increment_id = order_obj.getString("increment_id");
                        Log.e("increment_id ",""+increment_id );

                        String created_at = jsonObject.getString("created_at");
                        Log.e("created_at",""+created_at);

                        String PaymentMethod = jsonObject.getString("Payment Method");
                        Log.e("Payment Method",""+PaymentMethod);

                        String subtotal = jsonObject.getString("subtotal");
                        Log.e("subtotal",""+subtotal);

                        String shipping = jsonObject.getString("shipping");
                        Log.e("shipping",""+shipping);

                        String shipping1 = jsonObject.getString("row_total");
                        Log.e("shipping",""+shipping1);

                        tv_status.setText(status_order);
                        tv_orderid.setText(increment_id);
                        tv_date.setText(created_at);
                        tv_subtotal.setText(subtotal);
                        tv_ship_fees.setText(shipping);
                        tv_gtotal.setText(shipping1);

                        String ship_add = jsonObject.getString("Shipping Address");
                        Log.e("shipppp",""+ship_add);

                        JSONObject add_obj = jsonObject.getJSONObject("Shipping Address");
                        Log.e("Shipping Address",""+add_obj);

                        String firstname = add_obj.getString("firstname");
                        Log.e("firstname",""+firstname);

                        tv_fname.setText(add_obj.getString("firstname"));

                        tv_add_os.setText(add_obj.getString("apartment") + "," + "" + add_obj.getString("street") + "," + "" +
                                add_obj.getString("city") + "," + add_obj.getString("postcode") + "," + "" + add_obj.getString("phone"));

                        Toast.makeText(getActivity(), "hmmmm", Toast.LENGTH_SHORT).show();

                        String products = jsonObject.getString("products");
                        Log.e("products_os",""+products);

                        JSONArray jsonArray = jsonObject.getJSONArray("products");
                        Log.e("js_arr_os",""+jsonArray);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {

                                JSONObject pro_object = jsonArray.getJSONObject(i);
                                Log.e("base_price_os", "" + pro_object.getString("product_price"));

                                cartList.add(new Cart_Model(pro_object.getString("product_name"),
                                        pro_object.getString("product_price"),
                                        pro_object.getString("product_image"),
                                        pro_object.getString("product_sku"),
                                        pro_object.getString("product_id"),
                                        pro_object.getString("row_total"),
                                        pro_object.getString("product_qty"),
                                        pro_object.getString("itemid"),
                                        pro_object.getString("wishlist")));

                                //qt = pro_object.getString("product_qty");
                                //Log.e("qtttttttt", "" + qt);

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                confirmation_cart_adapter.notifyItemChanged(i);
                            }
                        }

                    } else if (status.equalsIgnoreCase("error")) {

                    }

                } catch (Exception e) {
                    Log.e("Exception_sss", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void AllocateMemory(View v) {

        tv_status = (TextView) v.findViewById(R.id.tv_status);
        tv_orderid = (TextView) v.findViewById(R.id.tv_orderid);
        tv_date = (TextView) v.findViewById(R.id.tv_date);
        tv_pay_met = (TextView) v.findViewById(R.id.tv_pay_met);
        tv_fname = (TextView) v.findViewById(R.id.tv_fname);
        tv_add_os = (TextView) v.findViewById(R.id.tv_add_os);
        tv_subtotal = (TextView) v.findViewById(R.id.tv_subtotal);
        tv_ship_fees = (TextView) v.findViewById(R.id.tv_ship_fees);
        tv_gtotal = (TextView) v.findViewById(R.id.tv_gtotal);

        order_summary_cart = (RecyclerView) v.findViewById(R.id.order_summary_cart);

        confirmation_cart_adapter = new Confirmation_cart_Adapter(getActivity(), cartList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        order_summary_cart.setLayoutManager(mLayoutManager);
        order_summary_cart.setItemAnimator(new DefaultItemAnimator());
        order_summary_cart.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        order_summary_cart.setAdapter(confirmation_cart_adapter);
    }

}
