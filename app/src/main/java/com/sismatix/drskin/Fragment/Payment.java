package com.sismatix.drskin.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.drskin.Adapter.Payment_Method_Adapter;
import com.sismatix.drskin.Model.Payment_Method_Model;
import com.sismatix.drskin.Preference.CheckNetwork;
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

import static com.sismatix.drskin.Adapter.Cart_Delivery_Adapter.shippingmethod;
import static com.sismatix.drskin.Adapter.Payment_Method_Adapter.paymentcode_ada;

/**
 * A simple {@link Fragment} subclass.
 */
public class Payment extends Fragment {

    LinearLayout lv_nextt, lv_left, lv_riht;
    TextView gren_total;
    RecyclerView payment_method_recyclerview;
    Payment_Method_Adapter payment_method_adapter;
    private List<Payment_Method_Model> payment_method_models = new ArrayList<Payment_Method_Model>();
    String shipping_method, payCode,subtotal,discount,grand_tot,coupon_code;

    public Payment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment, container, false);

        Checkout.iv_shipping_done.setVisibility(View.VISIBLE);
        Checkout.iv_payment_done.setVisibility(View.INVISIBLE);
        Checkout.iv_confirmation_done.setVisibility(View.INVISIBLE);
        Checkout.lv_payment_selected.setVisibility(View.VISIBLE);
        Checkout.lv_shipping_selected.setVisibility(View.INVISIBLE);
        Checkout.lv_confirmation_selected.setVisibility(View.INVISIBLE);

        AllocateMemory(v);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            shipping_method = bundle.getString("shippingmethod");
            Log.e("payment_shipmethod", "" + shipping_method);
            subtotal= bundle.getString("subtotal");
            discount= bundle.getString("discount");
            grand_tot= bundle.getString("grand_tot_cart");
            coupon_code = bundle.getString("coupon_code");

            payCode = bundle.getString("paymentcodee");
            Log.e("payment_paycode", "" + payCode);

        }
        gren_total.setText(grand_tot);
        lv_nextt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePaymentData();
            }
        });

        lv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        Bundle bundle1 = new Bundle();
                        bundle1.putString("shippingmethod", "" + shippingmethod);
                        bundle1.putString("grand_tot_cart", "" + grand_tot);
                        bundle1.putString("subtotal", "" + subtotal);
                        bundle1.putString("discount", "" + discount);
                        bundle1.putString("coupon_code", "" + coupon_code);
                        Fragment myFragment = new Shipping();
                        myFragment.setArguments(bundle1);
                        getActivity().getSupportFragmentManager().beginTransaction() .setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).replace(R.id.frameLayout_checkout, myFragment).addToBackStack(null).commit();

                    }
                }, 1000);
            }
        });

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_PAYMENT_API();
        } else {
            Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    private void validatePaymentData() {
        if (shippingmethod == null || shippingmethod == "" || shippingmethod == "null") {
            Toast.makeText(getActivity(), "Please Select Shipping Method", Toast.LENGTH_SHORT).show();
        } else if (paymentcode_ada == null) {
            Toast.makeText(getActivity(), "Please Select Payment Code", Toast.LENGTH_SHORT).show();
        } else {
            loadfrag();
        }
    }

    private void loadfrag() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putString("shippingmethodd", "" + shippingmethod);
                bundle.putString("paymentcodee", "" + paymentcode_ada);
                bundle.putString("grand_tot_cart", "" + grand_tot);
                bundle.putString("subtotal", "" + subtotal);
                bundle.putString("discount", "" + discount);
                bundle.putString("coupon_code", "" + coupon_code);
                Fragment myFragment = new Confirm_Order();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).replace(R.id.frameLayout_checkout, myFragment).addToBackStack(null).commit();
            }
        }, 1000);

    }

    private void CALL_PAYMENT_API() {

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> categorylist = api.getPaymentMethods();
        payment_method_models.clear();
        categorylist.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_payment", "" + response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_payment", "" + status);
                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("payment_method");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            try {
                                JSONObject vac_object = jsonArray.getJSONObject(i);
                                payment_method_models.add(new Payment_Method_Model(vac_object.getString("label"),
                                        vac_object.getString("value")));

                            } catch (Exception e) {
                                Log.e("Exception_payment", "" + e);
                            } finally {
                                payment_method_adapter.notifyItemChanged(i);
                            }

                        }

                    } else if (status.equalsIgnoreCase("error")) {
                    }

                } catch (Exception e) {
                    Log.e("Exc", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        /*for (int i=0;i<6;i++) {
            payment_method_models.add(new Payment_Method_Model("", ""));
        }
        payment_method_adapter.notifyDataSetChanged();*/
    }

    private void AllocateMemory(View v) {
        payment_method_recyclerview = (RecyclerView) v.findViewById(R.id.payment_method_recyclerview);
        gren_total = (TextView) v.findViewById(R.id.gren_total);
        //   lv_payment_next = (LinearLayout)v.findViewById(R.id.lv_payment_next);
        lv_left = (LinearLayout) v.findViewById(R.id.lv_left);
        lv_nextt = (LinearLayout) v.findViewById(R.id.lv_nextt);
        payment_method_adapter = new Payment_Method_Adapter(getActivity(), payment_method_models);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(false);
        payment_method_recyclerview.setLayoutManager(layoutManager);
        payment_method_recyclerview.setAdapter(payment_method_adapter);
    }

    private void loadFragment(Fragment fragment) {
        Log.e("clickone", "");
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in,
                0, 0, R.anim.fade_out);
        transaction.replace(R.id.frameLayout_checkout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
