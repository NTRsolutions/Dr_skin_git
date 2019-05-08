package com.sismatix.drskin.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Adapter.Cart_List_Adapter;
import com.sismatix.drskin.Model.Cart_Model;
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
public class MyCart extends Fragment {

    public static List<Cart_Model> cartList = new ArrayList<Cart_Model>();
    public static Cart_List_Adapter cart_adapter;
    RecyclerView cart_recyclerview;
    public Call<ResponseBody> cartlistt = null;
    public static String grand_total;
    ImageView iv_place_order;
    public static TextView tv_maintotal;
    public static Context context = null;
    LinearLayout lv_place_order;
    ProgressBar progressBar_cart;
    TextView title_cart,tv_total,tv_checkouttt;
    public static String cart_items_count;
    public static String loginflag,subtotal,coupon_code,discount_amount;
    public static String qt, qoute_id_cart, productslist;
    public static LinearLayout lv_productnotavelable,lv_Checkout;
    ImageView iv_close;


    Dialog fullscreenDialog;
    public MyCart() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_cart, container, false);
        cart_recyclerview=(RecyclerView)v.findViewById(R.id.cart_recyclerview);
        lv_productnotavelable=(LinearLayout) v.findViewById(R.id.lv_productnotavelable);
        lv_Checkout=(LinearLayout) v.findViewById(R.id.lv_Checkout);
        tv_maintotal=(TextView) v.findViewById(R.id.tv_maintotal);
        title_cart=(TextView) v.findViewById(R.id.title_cart);
        tv_total=(TextView) v.findViewById(R.id.tv_total);
        tv_checkouttt=(TextView) v.findViewById(R.id.tv_checkouttt);
        tv_checkouttt.setTypeface(Home.opensans_bold);
        title_cart.setTypeface(Home.opensans_bold);
        tv_maintotal.setTypeface(Home.opensans_bold);
        tv_total.setTypeface(Home.opensans_bold);
        iv_close=(ImageView) v.findViewById(R.id.iv_close);
        progressBar_cart=(ProgressBar) v.findViewById(R.id.progressBar_cart);
        loginflag = Login_preference.getLogin_flag(getActivity());
        cart_adapter = new Cart_List_Adapter(getActivity(), cartList);
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        cart_recyclerview.setLayoutManager(mLayoutManager);
        cart_recyclerview.setItemAnimator(new DefaultItemAnimator());
        cart_recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        cart_recyclerview.setAdapter(cart_adapter);




        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Bottom_navigation.class);
                startActivity(intent);
            }
        });
        lv_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (loginflag.equalsIgnoreCase("1") || loginflag == "1") {

                            if (productslist.equalsIgnoreCase("[]") || productslist.equalsIgnoreCase("")) {
                                Toast.makeText(getActivity(), "Product Not found in shopping cart.", Toast.LENGTH_SHORT).show();
                            } else {
                                Bundle b = new Bundle();
                                b.putString("grand_tot_cart", grand_total);
                                b.putString("subtotal", subtotal);
                                b.putString("coupon_code", coupon_code);
                                b.putString("discount_amount", discount_amount);
                                Log.e("grand_tot_cart", "" + grand_total);
                                loadFragment(new Checkout(),b);
                                /*AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                Fragment myFragment = new Checkout();
                                myFragment.setArguments(b);
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.rootLayout, myFragment).addToBackStack(null).commit();*/
                            }
                        } else {
                            String screen_type = "cart";
                            Bundle b = new Bundle();
                            b.putString("grand_tot_cart", grand_total);
                            b.putString("subtotal", subtotal);
                            b.putString("coupon_code", coupon_code);
                            b.putString("discount_amount", discount_amount);
                            b.putString("value", screen_type);
                            // fullscreen_login_dialog();
                            loadFragment(new SignIn(), b);
                        }
                    }
                }, 1000);

                /*if (loginflag.equalsIgnoreCase("1") || loginflag == "1") {

                    Bundle b = new Bundle();
                    b.putString("grand_tot_cart", grand_total);
                    b.putString("subtotal", subtotal);
                    Log.e("grand_tot_cart", "" + grand_total);
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new Checkout();
                    myFragment.setArguments(b);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.rootLayout, myFragment).addToBackStack(null).commit();

                }else {

                }
*/
                /*loadFragment(new Checkout(),"checkout");*/
            }
        });
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            prepare_Cart();
        } else {
            Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return v;
    }
    private void loadFragment(Fragment fragment, Bundle b) {
        Log.e("clickone", "");
        fragment.setArguments(b);
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.rootLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void prepare_Cart() {
        progressBar_cart.setVisibility(View.VISIBLE);
        cartList.clear();
        cart_adapter.notifyDataSetChanged();
        String email = Login_preference.getemail(getContext());
        String loginflag = Login_preference.getLogin_flag(getContext());
        Log.e("customeriddd", "" + Login_preference.getcustomer_id(getContext()));
        Log.e("loginnnn", "" + loginflag);
        if (loginflag.equalsIgnoreCase("1") || loginflag == "1") {
            Log.e("with_login", "");
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            cartlistt = api.Cartlist(email);

        } else {
            Log.e("without_login", "");
            String quote_id = Login_preference.getquote_id(getContext());
            Log.e("quoteidd_cart", "" + quote_id);
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            cartlistt = api.getlistcart(quote_id);

        }
        cartlistt.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("responseeeeee", "" + response);

                JSONObject jsonObject = null;
                try {
                    progressBar_cart.setVisibility(View.GONE);
                    jsonObject = new JSONObject(response.body().string());
                    Log.e("jason_response",""+jsonObject);
                    String status = jsonObject.getString("status");
                    Log.e("status_prepare_cart", "" + status);
                    String messg=jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        grand_total = jsonObject.getString("grand_total");
                        subtotal = jsonObject.getString("subtotal");
                        Log.e("gtot", "" + grand_total);
                        tv_maintotal.setText(grand_total);
                        // Bottom_navigation.Convert_String_First_Letter(tv_maintotal, grand_total);

                        qoute_id_cart = jsonObject.getString("quote_id");
                        Log.e("qoute_id_cart", "" + qoute_id_cart);
                        Login_preference.setCart_item_count(getActivity(), jsonObject.getString("items_qty"));
                        Log.e("cart_items_total_cart", "" + Login_preference.getCart_item_count(getActivity()));
                        Log.e("items_count",""+jsonObject.getString("items_qty"));
                        coupon_code=jsonObject.getString("coupon_code");
                        discount_amount=jsonObject.getString("discount_amount");
                        Log.e("discount_amount",""+discount_amount);
                        /*if (item_count != null && !item_count.isEmpty() && !item_count.equals("null")){
                            Bottom_navigation.tv_bottomcount.setText(jsonObject.getString("items_count"));
                            Bottom_navigation.item_count.setText(jsonObject.getString("items_count"));
                        }else {

                            Bottom_navigation.tv_bottomcount.setText("0");
                            Bottom_navigation.item_count.setText("0");
                        }*/

                        productslist = jsonObject.getString("products");
                        Log.e("prod_list_cart", "" + productslist);
                        if (productslist.equalsIgnoreCase("[]") || productslist.equalsIgnoreCase("")) {

                            lv_productnotavelable.setVisibility(View.VISIBLE);

                        } else {
                            lv_productnotavelable.setVisibility(View.GONE);
                            JSONArray jsonArray = jsonObject.getJSONArray("products");
                            Log.e("jsonarr_cart", "" + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject vac_object = jsonArray.getJSONObject(i);
                                    Log.e("product_price", "" + vac_object.getString("product_price"));
                                    cartList.add(new Cart_Model(vac_object.getString("product_name"),
                                            vac_object.getString("product_price"),
                                            vac_object.getString("product_image"),
                                            vac_object.getString("product_sku"),
                                            vac_object.getString("product_id"),
                                            vac_object.getString("row_total"),
                                            vac_object.getString("product_qty"),
                                            vac_object.getString("itemid"),
                                            vac_object.getString("wishlist")));
                                    qt = vac_object.getString("product_qty");
                                    Log.e("qtttttttt", "" + qt);
                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                    cart_adapter.notifyItemChanged(i);
                                }
                            }
                        }
                        String item_count=jsonObject.getString("items_qty");
                        Log.e("items_qty",""+jsonObject.getString("items_qty"));
                        if (item_count != null && !item_count.isEmpty() && !item_count.equals("null")){
                            Bottom_navigation.tv_bottomcount.setText(jsonObject.getString("items_qty"));
                            Bottom_navigation.item_count.setText(jsonObject.getString("items_qty"));
                            String countt = jsonObject.getString("items_qty");
                            if (countt.equalsIgnoreCase("null") || countt.equals("")) {
                                Log.e("count_40", "" + jsonObject.getString("items_qty"));
                                Product_Details.cart_count.setText("0");
                            } else {
                                Log.e("count_80", "" + jsonObject.getString("items_qty"));
                                Product_Details.cart_count.setText(jsonObject.getString("items_qty"));
                            }
                        }else {
                            Bottom_navigation.tv_bottomcount.setText("0");
                            Bottom_navigation.item_count.setText("0");
                            Product_Details.count = "0";
                            String countt = jsonObject.getString("items_qty");
                            if (countt.equalsIgnoreCase("null") || countt.equals("")) {
                                Product_Details.cart_count.setText("0");
                            } else {
                                Product_Details.cart_count.setText(Product_Details.count);
                            }
                        }
                    } else if (status.equalsIgnoreCase("error")) {

                    }
                    if (messg.equalsIgnoreCase("No Data Found")){
                        lv_productnotavelable.setVisibility(View.VISIBLE);
                    }else {
                        lv_productnotavelable.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar_cart.setVisibility(View.GONE);
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
