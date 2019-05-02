package com.sismatix.drskin.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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

import com.myfatoorah.sdk.model.invoice.InvoiceItem;
import com.myfatoorah.sdk.views.MFSDK;
import com.sismatix.drskin.Activity.Paymentscreen;
import com.sismatix.drskin.Adapter.Confirmation_cart_Adapter;
import com.sismatix.drskin.Model.Cart_Model;
import com.sismatix.drskin.Preference.CheckNetwork;
import com.sismatix.drskin.Preference.Config;
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

import static com.sismatix.drskin.Fragment.MyCart.grand_total;
import static com.sismatix.drskin.Fragment.MyCart.lv_productnotavelable;
import static com.sismatix.drskin.Fragment.MyCart.productslist;
import static com.sismatix.drskin.Fragment.MyCart.qoute_id_cart;
import static com.sismatix.drskin.Fragment.MyCart.qt;

/**
 * A simple {@link Fragment} subclass.
 */
public class Confirm_Order extends Fragment  {

    TextView confirmpay_add, tv_cart_edit_confirm,grand_totall,shipping_price,tv_discount,
            tv_shipping_title,tv_conf_order,tv_shppingfee,discounttt,tv_continue_payment;
    RecyclerView recyclerview_confirmation;
    private List<Cart_Model> cartList = new ArrayList<Cart_Model>();
    private Confirmation_cart_Adapter confirmation_cart_adapter;
    public Call<ResponseBody> cartlistt = null;
    LinearLayout lv_ordernow;
    String shippingMethod, paymentCode,subtotal,discount,grand_tot,coupon_code,shippingprice;
    ImageView iv_left;
    String totel_pay,productlist_pay,discount_pay;
    Double totel_paysend;
    private ArrayList<InvoiceItem> invoiceItems = null;
    ProgressBar progressBar_confom_cart;
    public static Context context = null;
    public Confirm_Order() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_confirm_order, container, false);
        context=getActivity();
        AllocateMemory(v);
        MFSDK.INSTANCE.init(Config.BASE_URL, Config.EMAIL, Config.PASSWORD);
        // You can custom your action bar, but this is optional not required to set this line
        MFSDK.INSTANCE.setUpActionBar("MyFatoorah Payment", R.color.colorPrimary,
                R.color.title_text_color, true);

        Checkout.lv_payment_selected.setVisibility(View.INVISIBLE);
        Checkout.lv_shipping_selected.setVisibility(View.INVISIBLE);
        Checkout.lv_confirmation_selected.setVisibility(View.VISIBLE);
        Checkout.iv_payment_done.setVisibility(View.VISIBLE);
        Checkout.iv_shipping_done.setVisibility(View.VISIBLE);
        Checkout.iv_confirmation_done.setVisibility(View.INVISIBLE);

        tv_shipping_title.setTypeface(Home.opensans_bold);
        tv_discount.setTypeface(Home.opensans_bold);
        tv_conf_order.setTypeface(Home.opensans_bold);
        tv_cart_edit_confirm.setTypeface(Home.opensans_bold);
        tv_shppingfee.setTypeface(Home.opensans_bold);
        discounttt.setTypeface(Home.opensans_bold);
        shipping_price.setTypeface(Home.opensans_bold);
        tv_continue_payment.setTypeface(Home.opensans_bold);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            shippingMethod = bundle.getString("shippingmethodd");
            Log.e("conf_shipmethod", "" + shippingMethod);
            subtotal= bundle.getString("subtotal");
            discount= bundle.getString("discount");
            grand_tot= bundle.getString("grand_tot_cart");
            coupon_code= bundle.getString("coupon_code");
            paymentCode = bundle.getString("paymentcodee");
            Log.e("conf_paycode", "" + paymentCode);

        }
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_SHIPPING_ADDRESS_API();
            prepareConfirmCart();

        } else {
            Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        tv_cart_edit_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new MyCart();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).replace(R.id.rootLayout, myFragment).addToBackStack(null).commit();
            }
        });

        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        Bundle bundle1 = new Bundle();
                        bundle1.putString("shippingmethod", "" + shippingMethod);
                        bundle1.putString("paymentcodee",paymentCode);
                        bundle1.putString("grand_tot_cart", "" + grand_tot);
                        bundle1.putString("subtotal", "" + subtotal);
                        bundle1.putString("discount", "" + discount);
                        bundle1.putString("coupon_code", "" + coupon_code);
                        Fragment myFragment = new Payment();
                        myFragment.setArguments(bundle1);
                        getActivity().getSupportFragmentManager().beginTransaction() .setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).replace(R.id.frameLayout_checkout, myFragment).addToBackStack(null).commit();

                    }
                }, 1000);
            }
        });
        lv_ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();

                final String s = totel_pay.replace(",", "");
                Log.e("removestring",""+s);
                bundle1.putString("grand_tot_cart", "" + s);

                bundle1.putString("productlist", "" + productlist_pay);
                bundle1.putString("shippingMethod", "" + shippingMethod);
                bundle1.putString("shippingprice", "" + shippingprice);
                bundle1.putString("discount_pay", "" + discount_pay);
                bundle1.putString("paymentCode", "" + paymentCode);
                bundle1.putString("address", "" + confirmpay_add.getText().toString());
                Intent intent=new Intent(getActivity(),Paymentscreen.class);
                intent.putExtras(bundle1);
                startActivity(intent);
               /* if (CheckNetwork.isNetworkAvailable(getActivity())) {
                    Ordernow();
                } else {
                    Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        return v;
    }
    private void Ordernow() {
        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> confirm = apii.AppCreateOrder(Login_preference.getcustomer_id(getActivity()), Login_preference.getemail(getActivity()), Login_preference.getquote_id(getActivity()), MyAddress_Preference.getFirstname(getActivity()),
                MyAddress_Preference.getCountryId(getActivity()), MyAddress_Preference.getZipcode(getActivity()), MyAddress_Preference.getCity(getActivity()), MyAddress_Preference.getPhoneNumber(getActivity()), MyAddress_Preference.getStreetAddress(getActivity()), shippingMethod, paymentCode);

        confirm.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    Log.e("response_all",""+jsonObject);
                    String code = jsonObject.getString("code");
                    Log.e("code_confirmation", "" + code);
                    String meassg = jsonObject.getString("message");
                    Log.e("message_confirmation", "" + meassg);
                    if (code.equalsIgnoreCase("200")) {
                        Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                        String order=jsonObject.getString("order_id");
                      //  paymentapi();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("grand_tot_cart", "" + totel_pay);
                        bundle1.putString("productlist", "" + productlist_pay);
                        bundle1.putString("orederid", "" + order);
                        bundle1.putString("address", "" + confirmpay_add.getText().toString());
                        Intent intent=new Intent(getActivity(),Paymentscreen.class);
                        intent.putExtras(bundle1);
                        startActivity(intent);

                        //loadFragment(order);
                    } else if (code.equalsIgnoreCase("error")) {
                         Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Exception", "" + e);
                    Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
// Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*private void loadFragment(final String order) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putString("order", "" + order);
                Fragment myFragment = new OrderSummary();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).replace(R.id.rootLayout, myFragment).addToBackStack(null).commit();
            }
        }, 1000);
    }*/
    private void prepareConfirmCart() {
        progressBar_confom_cart.setVisibility(View.VISIBLE);
        cartList.clear();
        String email = Login_preference.getemail(getActivity());
        String countryid_confirm = MyAddress_Preference.getCountryId(getActivity());
        Log.e("emailsss",""+email);
        Log.e("countryid_confirm",""+countryid_confirm);
        String loginflag = Login_preference.getLogin_flag(getActivity());
        Log.e("customeriddd", "" + Login_preference.getcustomer_id(getActivity()));
        if (loginflag.equalsIgnoreCase("1") || loginflag == "1") {
            Log.e("with_login", "");
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            Log.e("emai_id",email);
            Log.e("review","1");
            Log.e("country_id",countryid_confirm);
            Log.e("paycode",paymentCode);
            Log.e("shipping_confirm",shippingMethod);
            cartlistt = api.Cartlist_totoal(email,"1",countryid_confirm,paymentCode,shippingMethod);
        } else {
            Log.e("without_login", "");
            String quote_id = Login_preference.getquote_id(getActivity());//359
            Log.e("quoteidd", "" + quote_id);
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            cartlistt = api.getlistcart(quote_id);
        }

        cartlistt.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("responseeeeee", "" + response);

                JSONObject jsonObject = null;
                try {

                    progressBar_confom_cart.setVisibility(View.GONE);
                    jsonObject = new JSONObject(response.body().string());
                    Log.e("jason_response", "" + jsonObject);
                    String status = jsonObject.getString("status");
                    Log.e("status_prepare_cart", "" + status);

                    if (status.equalsIgnoreCase("Success")) {
                        lv_productnotavelable.setVisibility(View.VISIBLE);

                        String currentstr = jsonObject.getString("grand_total");
                        String[] separate = currentstr.split("KWD");
                        String s =separate[0]; // this will contain "Fruit"

                        grand_totall.setText(jsonObject.getString("grand_total"));
                        totel_pay =separate[1]; // this will contain " they taste good"
                        Log.e("status_condisation", "" + separate[1]);
                        Log.e("price_pass",""+totel_pay);

                        String currentstrrr = jsonObject.getString("discount_amount");
                        String[] separateee = currentstrrr.split("KWD");
                        String sss =separateee[0]; // this will contain "Fruit"

                        tv_discount.setText(jsonObject.getString("discount_amount"));
                        discount_pay =separateee[1]; // this will contain " they taste good"
                        Log.e("discount_condisation", "" + separate[1]);
                        Log.e("price_discount",""+discount_pay);

                        shipping_price.setText(jsonObject.getString("shipping_amount"));
                        String currentString = jsonObject.getString("shipping_amount");
                        String[] separated = currentString.split("KWD");
                        String A =separated[0]; // this will contain "Fruit"
                        shippingprice= separated[1]; // this will contain " they taste good"
                        Log.e("shippingprice",""+shippingprice);
                        //tv_maintotal.setText(grand_total);
                        // Bottom_navigation.Convert_String_First_Letter(tv_maintotal, grand_total);

                        qoute_id_cart = jsonObject.getString("quote_id");
                        Log.e("qoute_id_cart", "" + qoute_id_cart);
                        Login_preference.setCart_item_count(getActivity(), jsonObject.getString("items_count"));
                        Log.e("cart_items_total_cart", "" + Login_preference.getCart_item_count(getActivity()));
                       /* if (jsonObject.getString("items_count").equalsIgnoreCase("null") || jsonObject.getString("items_count").equals("")) {

                            Bottom_navigation.tv_bottomcount.setText("0");
                            Bottom_navigation.item_count.setText("0");

                        } else {

                            Bottom_navigation.tv_bottomcount.setText(jsonObject.getString("items_count"));
                            Bottom_navigation.item_count.setText(jsonObject.getString("items_count"));

                        }*/
                        productslist = jsonObject.getString("products");
                        productlist_pay=jsonObject.getString("products");

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
                                    Log.e("Name", "" + vac_object.getString("product_name"));
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
                                    confirmation_cart_adapter.notifyItemChanged(i);
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
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                progressBar_confom_cart.setVisibility(View.GONE);
            }
        });

    }

    private void CALL_SHIPPING_ADDRESS_API() {

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> shipping_addr = api.GET_SHIPPING_ADDRESS(Login_preference.getcustomer_id(getActivity()));
        shipping_addr.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_shipadd", "" + status);
                    String add = jsonObject.getString("address");
                    Log.e("addressarr", "" + add);
                    if (status.equalsIgnoreCase("Success")) {

                        JSONObject add_obj = jsonObject.getJSONObject("address");
                        Log.e("jsArr", "" + add_obj);

                        try {

                            String add_id = add_obj.getString("customer_address_id");
                            Log.e("add_id_defa", "" + add_id);
                            MyAddress_Preference.setAddressId(getActivity(), add_id);
                            Log.e("fnamee", "" + add_obj.getString("firstname"));
                            Log.e("appartment", "" + add_obj.getString("apartment"));
                            Log.e("cityyy", "" + add_obj.getString("city"));
                            Log.e("streettt", "" + add_obj.getString("street"));
                            Log.e("postcodeee", "" + add_obj.getString("postcode"));
                            Log.e("phoneee", "" + add_obj.getString("phone"));
                            /*MyAddress_Preference.setAddressId(getActivity(), add_id);*/

                            MyAddress_Preference.setFirstname(getActivity(), add_obj.getString("firstname"));
                            MyAddress_Preference.setCountryId(getActivity(), add_obj.getString("country_id"));
                            MyAddress_Preference.setStreetAddress(getActivity(), add_obj.getString("street"));
                            MyAddress_Preference.setCity(getActivity(), add_obj.getString("city"));
                            MyAddress_Preference.setZipcode(getActivity(), add_obj.getString("postcode"));
                            MyAddress_Preference.setPhoneNumber(getActivity(), add_obj.getString("phone"));
                            MyAddress_Preference.setAppartment(getActivity(), add_obj.getString("apartment"));

                            //confirm_name.setText(add_obj.getString("firstname"));
                            confirmpay_add.setText(add_obj.getString("apartment") + "," + "" + add_obj.getString("street") + "," + "" +
                                    add_obj.getString("city") + "," + add_obj.getString("postcode") + "," + "" + add_obj.getString("phone"));

                            //confirm_name.setText(add_obj.getString("firstname"));
                            //confirm_address.setText(streetadd_confirm + " " + zipcode_confirm + " " + city_confirm + " " + countryid_confirm);
                            //selected_spinner_pos = country_name_code.indexOf(addr_object.getString("country_id"));

                        } catch (Exception e) {
                            Log.e("Exception_s", "" + e);
                        } finally {

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
        confirmpay_add = (TextView) v.findViewById(R.id.confirmpay_add);
        grand_totall = (TextView) v.findViewById(R.id.grand_totall);
        shipping_price = (TextView) v.findViewById(R.id.shipping_price);
        tv_discount = (TextView) v.findViewById(R.id.tv_discount);
        tv_shipping_title = (TextView) v.findViewById(R.id.tv_shipping_title);
        tv_conf_order = (TextView) v.findViewById(R.id.tv_conf_order);
        recyclerview_confirmation = (RecyclerView) v.findViewById(R.id.recyclerview_confirmation);
        tv_cart_edit_confirm = (TextView) v.findViewById(R.id.tv_cart_edit_confirm);
        discounttt = (TextView) v.findViewById(R.id.discounttt);
        tv_shppingfee = (TextView) v.findViewById(R.id.tv_shppingfee);
        tv_continue_payment = (TextView) v.findViewById(R.id.tv_continue_payment);
        iv_left = (ImageView) v.findViewById(R.id.iv_left);
        lv_ordernow = (LinearLayout) v.findViewById(R.id.lv_ordernow);
        progressBar_confom_cart = (ProgressBar) v.findViewById(R.id.progressBar_confom_cart);

        confirmation_cart_adapter = new Confirmation_cart_Adapter(getActivity(), cartList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerview_confirmation.setLayoutManager(mLayoutManager);
        recyclerview_confirmation.setItemAnimator(new DefaultItemAnimator());
        recyclerview_confirmation.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerview_confirmation.setAdapter(confirmation_cart_adapter);
    }
}
