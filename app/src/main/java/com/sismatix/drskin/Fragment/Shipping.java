package com.sismatix.drskin.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Adapter.Cart_Delivery_Adapter;
import com.sismatix.drskin.Model.Cart_Delivery_Model;
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

import static com.sismatix.drskin.Adapter.Cart_Delivery_Adapter.MyViewHolder.rad_text;
import static com.sismatix.drskin.Adapter.Cart_Delivery_Adapter.shippingmethod;

/**
 * A simple {@link Fragment} subclass.
 */
public class Shipping extends Fragment implements View.OnClickListener {

    LinearLayout lv_shipping_next, lv_addbutton, lv_addnewaddress, lv_main, lv_Addnew, lv_save,lv_shipping_main;

    LinearLayout lv_address, lv_edit_address, tv_remov_coupn;
    TextView confirm_name, confirm_address, confirm_state, confirm_city, confirm_phone, tv_apply, tv_subtotal, tv_discount, gren_total;
    EditText Shipping_fullname, shipping_state, shipping_streetname, shipping_Appartment, shipping_PhoneNumber, shipping_postcode, et_voucher;
    Spinner edit_spinner_country_Name;

    String countryCodeAndroid = "91", country_namee, country_short_name;
    String addid, cusid, shipMethod, tot_cart, subtot,discount,coupon_code;
    CountryCodePicker ccp;

    String fistname,apartment,city,street,postcode;
    public static ArrayList<String> country_name_code = new ArrayList<String>();
    public static ArrayList<String> country_name = new ArrayList<String>();

    RecyclerView recyclerview_item_delivery;
    Cart_Delivery_Adapter cart_delivery_adapter;
    private List<Cart_Delivery_Model> cart_delivery_models = new ArrayList<Cart_Delivery_Model>();

    public Shipping() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_shipping, container, false);

        AllocateMemory(v);

        setupUI(lv_shipping_main);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            shipMethod = bundle.getString("shippingmethod");
            discount =bundle.getString("discount");
            tot_cart = bundle.getString("grand_tot_cart");
            subtot = bundle.getString("subtotal");
            coupon_code = bundle.getString("coupon_code");

        }
        Log.e("discount",""+discount);
        if (discount != null && !discount.isEmpty() && !discount.equals("KWD0.00")){
            tv_discount.setText(discount);
            tv_apply.setVisibility(View.GONE);
            tv_remov_coupn.setVisibility(View.VISIBLE);
            et_voucher.setText(coupon_code);
            et_voucher.setEnabled(false);
        }
        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            CALL_SHIPPING_ADDRESS_API();
            CALL_CART_DELIVERY();
        } else {
            Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        tv_subtotal.setText(subtot);
        gren_total.setText(tot_cart);

        Log.e("sop", "" + shippingmethod);

        Checkout.lv_payment_selected.setVisibility(View.INVISIBLE);
        Checkout.iv_shipping_done.setVisibility(View.INVISIBLE);
        Checkout.iv_payment_done.setVisibility(View.INVISIBLE);
        Checkout.iv_confirmation_done.setVisibility(View.INVISIBLE);
        Checkout.lv_shipping_selected.setVisibility(View.VISIBLE);
        Checkout.lv_payment_selected.setVisibility(View.INVISIBLE);
        Checkout.lv_confirmation_selected.setVisibility(View.INVISIBLE);

        lv_addnewaddress.setOnClickListener(this);
        lv_save.setOnClickListener(this);
        lv_edit_address.setOnClickListener(this);
        tv_apply.setOnClickListener(this);
        tv_remov_coupn.setOnClickListener(this);

        lv_shipping_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateShippingData();
            }
        });

        ccp.showFlag(false);
        //ccp.showNameCode(false);
        ccp.showFullName(true);
        ccp.showNameCode(false);
        ccp.setShowPhoneCode(false);
        ccp.setCcpDialogShowPhoneCode(false);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCodeAndroid = ccp.getSelectedCountryCode();
                country_namee = ccp.getSelectedCountryName();
                country_short_name = ccp.getSelectedCountryNameCode();
                Log.e("Country_Code", "" + countryCodeAndroid);
                Log.e("country_name", "" + country_name);
                Log.e("cshort_name", "" + country_short_name);
                ccp.showFlag(false);
            }
        });

        ccp.setCountryForNameCode(MyAddress_Preference.getCountryId(getActivity()));

        addid = MyAddress_Preference.getAddressId(getActivity());
        Log.e("addressid", "" + addid);

        cusid = Login_preference.getcustomer_id(getActivity());
        Log.e("Customeridid", "" + addid);

        /*if (shippingmethod == null) {
            Toast.makeText(getActivity(), "Please Select Shipping Method", Toast.LENGTH_SHORT).show();
        }*/

        return v;
    }

    private void validateShippingData() {
        if (shippingmethod == null || shippingmethod == "" || shippingmethod == "null") {
            Toast.makeText(getActivity(), "Please Select Shipping Method", Toast.LENGTH_SHORT).show();
        }else  if (fistname != null && !fistname.isEmpty() && !fistname.equals("null")|| apartment != null && !apartment.isEmpty() && !apartment.equals("null")){
            loadfrag();
        } else {
            Toast.makeText(getActivity(), "Please Fill the address", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadfrag() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putString("shippingmethod", "" + shippingmethod);
                bundle.putString("grand_tot_cart", "" + gren_total.getText().toString());
                bundle.putString("subtotal", "" + tv_subtotal.getText().toString());
                bundle.putString("discount", "" + tv_discount.getText().toString());
                bundle.putString("coupon_code", "" + et_voucher.getText().toString());
                Log.e("gren_total", "" + gren_total.getText().toString());
                Log.e("sub_total", "" + tv_subtotal.getText().toString());
                Log.e("discount", "" + tv_discount.getText().toString());
                Fragment myFragment = new Payment();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).replace(R.id.frameLayout_checkout, myFragment).addToBackStack(null).commit();
            }
        }, 1000);

    }

    private void setValuesToeTextview() {

        String ph = MyAddress_Preference.getPhoneNumber(getActivity());
        String ct = MyAddress_Preference.getCity(getActivity());

        Log.e("phhhhhh", "" + ph);
        Log.e("ctttttt", "" + ct);

        confirm_name.setText(MyAddress_Preference.getFirstname(getActivity()));

        confirm_address.setText(MyAddress_Preference.getAppartment(getActivity()) + "," + "" + MyAddress_Preference.getStreetAddress(getActivity()) + "," + "" +
                MyAddress_Preference.getCity(getActivity()) + "," + "" + MyAddress_Preference.getCountryId(getActivity()) + "," + "" + MyAddress_Preference.getZipcode(getActivity()) + "," + "" +
                MyAddress_Preference.getPhoneNumber(getActivity()));

    }

    private void setValuesToeEditview() {

        Shipping_fullname.setText(MyAddress_Preference.getFirstname(getActivity()));
        Log.e("apppppp", "" + MyAddress_Preference.getAppartment(getActivity()));
        shipping_Appartment.setText(MyAddress_Preference.getAppartment(getActivity()));
        shipping_PhoneNumber.setText(MyAddress_Preference.getPhoneNumber(getActivity()));
        shipping_postcode.setText(MyAddress_Preference.getZipcode(getActivity()));
        shipping_streetname.setText(MyAddress_Preference.getStreetAddress(getActivity()));
        shipping_state.setText(MyAddress_Preference.getCity(getActivity()));
        int selected_spinner_pos = country_name_code.indexOf(MyAddress_Preference.getCountryId(getActivity()));
        edit_spinner_country_Name.setSelection(selected_spinner_pos, true);

    }

    private void AllocateMemory(View v) {

        lv_address = (LinearLayout) v.findViewById(R.id.lv_address);
        lv_shipping_main = (LinearLayout) v.findViewById(R.id.lv_shipping_main);
        recyclerview_item_delivery = (RecyclerView) v.findViewById(R.id.recyclerview_item_delivery);
        confirm_name = (TextView) v.findViewById(R.id.confirm_name);
        confirm_address = (TextView) v.findViewById(R.id.confirm_address);
        tv_apply = (TextView) v.findViewById(R.id.tv_apply);
        tv_subtotal = (TextView) v.findViewById(R.id.tv_subtotal);
        tv_discount = (TextView) v.findViewById(R.id.tv_discount);
        gren_total = (TextView) v.findViewById(R.id.gren_total);

        Shipping_fullname = (EditText) v.findViewById(R.id.Shipping_fullname);
        shipping_state = (EditText) v.findViewById(R.id.shipping_state);
        shipping_streetname = (EditText) v.findViewById(R.id.shipping_streetname);
        shipping_Appartment = (EditText) v.findViewById(R.id.shipping_Appartment);
        shipping_PhoneNumber = (EditText) v.findViewById(R.id.shipping_PhoneNumber);
        shipping_postcode = (EditText) v.findViewById(R.id.shipping_postcode);
        et_voucher = (EditText) v.findViewById(R.id.et_voucher);

        edit_spinner_country_Name = (Spinner) v.findViewById(R.id.edit_spinner_country_Name);

        lv_save = (LinearLayout) v.findViewById(R.id.lv_save);
        tv_remov_coupn = (LinearLayout) v.findViewById(R.id.tv_remov_coupn);

        lv_edit_address = (LinearLayout) v.findViewById(R.id.lv_edit_address);
        lv_shipping_next = (LinearLayout) v.findViewById(R.id.lv_shipping_next);
        lv_addbutton = (LinearLayout) v.findViewById(R.id.lv_addbutton);
        lv_addnewaddress = (LinearLayout) v.findViewById(R.id.lv_addnewaddress);
        lv_main = (LinearLayout) v.findViewById(R.id.lv_main);
        lv_Addnew = (LinearLayout) v.findViewById(R.id.lv_Addnew);
        ccp = (CountryCodePicker) v.findViewById(R.id.ccp_myaccount);

        cart_delivery_adapter = new Cart_Delivery_Adapter(getActivity(), cart_delivery_models);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setReverseLayout(false);
        layoutManager.findLastVisibleItemPosition();
        recyclerview_item_delivery.setLayoutManager(layoutManager);
        recyclerview_item_delivery.setAdapter(cart_delivery_adapter);
        recyclerview_item_delivery.setHasFixedSize(true);
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

    @Override
    public void onClick(View view) {
        if (view == lv_addnewaddress) {
            lv_Addnew.setVisibility(View.VISIBLE);
            lv_main.setVisibility(View.GONE);

        } else if (view == lv_save) {
            if (addid.equalsIgnoreCase("") || addid.equalsIgnoreCase("null")) {
                validateAdd();
            } else {
                callUpdateAddApi();
            }
        } else if (view == lv_edit_address) {
            Toast.makeText(getContext(), "editttt", Toast.LENGTH_SHORT).show();
            lv_Addnew.setVisibility(View.VISIBLE);
            lv_main.setVisibility(View.GONE);
        } else if (view == tv_apply) {
            if (et_voucher.getText().length() == 0) {
                Toast.makeText(getActivity(), "Please enter Voucher code", Toast.LENGTH_SHORT).show();
            } else {
                appycode_api();
            }
        } else if (view == tv_remov_coupn) {
            removcode_api();
        }
    }

    private void removcode_api() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> removecuopn = api.removcoupon(Login_preference.getquote_id(getActivity()));
        removecuopn.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_shipadd", "" + status);
                    if (status.equalsIgnoreCase("Success")) {
                        tv_apply.setVisibility(View.VISIBLE);
                        tv_remov_coupn.setVisibility(View.GONE);
                        et_voucher.setEnabled(true);
                        Log.e("discount", "" + jsonObject.getString("discount_amount"));
                        tv_discount.setText(jsonObject.getString("discount_amount"));
                        tv_subtotal.setText(jsonObject.getString("subtotal"));
                        gren_total.setText(jsonObject.getString("grand_total"));

                    } else if (status.equalsIgnoreCase("error")) {
                        tv_apply.setVisibility(View.GONE);
                        tv_remov_coupn.setVisibility(View.VISIBLE);
                        et_voucher.setEnabled(false);
                    }

                } catch (Exception e) {
                    et_voucher.setEnabled(false);
                    tv_apply.setVisibility(View.GONE);
                    tv_remov_coupn.setVisibility(View.VISIBLE);
                    Log.e("Exception_sss", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                tv_apply.setVisibility(View.VISIBLE);
                tv_remov_coupn.setVisibility(View.GONE);
                et_voucher.setEnabled(false);
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void appycode_api() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> shipping_addr = api.addcoupon(Login_preference.getquote_id(getActivity()), et_voucher.getText().toString());
        shipping_addr.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_shipadd", "" + status);
                    if (status.equalsIgnoreCase("Success")) {
                        tv_apply.setVisibility(View.GONE);
                        tv_remov_coupn.setVisibility(View.VISIBLE);
                        et_voucher.setText("");
                        et_voucher.setEnabled(false);
                        Log.e("discount", "" + jsonObject.getString("discount_amount"));
                        tv_discount.setText(jsonObject.getString("discount_amount"));
                        tv_subtotal.setText(jsonObject.getString("subtotal"));
                        gren_total.setText(jsonObject.getString("grand_total"));
                    } else if (status.equalsIgnoreCase("error")) {
                        tv_apply.setVisibility(View.VISIBLE);
                        tv_remov_coupn.setVisibility(View.GONE);
                        et_voucher.setEnabled(true);
                    }
                } catch (Exception e) {
                    tv_apply.setVisibility(View.VISIBLE);
                    tv_remov_coupn.setVisibility(View.GONE);
                    et_voucher.setEnabled(true);
                    Log.e("Exception_sss", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                tv_apply.setVisibility(View.VISIBLE);
                tv_remov_coupn.setVisibility(View.GONE);
                et_voucher.setEnabled(true);
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateAdd() {
        String telephone = shipping_PhoneNumber.getText().toString().toString();
        if (Shipping_fullname.getText().toString().length() == 0) {
            Shipping_fullname.setError("Please enter your Name");
        } else if (shipping_state.getText().toString().length() == 0) {
            shipping_state.setError("Please enter your State");
        }else if (shipping_streetname.getText().toString().length() == 0) {
            shipping_streetname.setError("Please enter your Street Name");
        }else if (shipping_Appartment.getText().toString().length() == 0) {
            shipping_Appartment.setError("Please enter your Appartment");
        } else if (shipping_postcode.getText().toString().length() == 0) {
            shipping_postcode.setError("Please enter your Postcode");
        } else if (telephone.length() == 0) {//et_shippingphonenumber.getText().length() == 0
            shipping_PhoneNumber.setError("Please enter your Mobile no.");
        } else if (telephone.length() < 9 || telephone.length() > 13) {//et_shippingphonenumber.getText().length() == 0
            shipping_PhoneNumber.setError("Please enter valid Mobile no.");
        }   else {
            callCreateAddApi();
        }
    }

    private void callCreateAddApi() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> create_addr = api.AppCreateAddress(Login_preference.getcustomer_id(getActivity()),
                Shipping_fullname.getText().toString(),
                country_short_name,
                shipping_postcode.getText().toString(),
                shipping_state.getText().toString(),
                shipping_streetname.getText().toString(),
                shipping_Appartment.getText().toString(),
                shipping_PhoneNumber.getText().toString());

        create_addr.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("jsonObject_241", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());

                    Log.e("jsonObject_242", "" + jsonObject);

                    String status = jsonObject.getString("status");
                    Log.e("statusupdateadd", "" + status);
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        CALL_SHIPPING_ADDRESS_API();

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        lv_Addnew.setVisibility(View.GONE);
                        lv_main.setVisibility(View.VISIBLE);

                    } else if (status.equalsIgnoreCase("error")) {

                    }

                } catch (Exception e) {
                    Log.e("Exception_shi", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callUpdateAddApi() {
        /*String addidd = MyAddress_Preference.getAddressId(getActivity());
        Log.e("addddiddd_203", "" + addidd);*/
        String cusid = Login_preference.getcustomer_id(getActivity());
        Log.e("cusiddd_204", "" + cusid);
        Log.e("up_fname", "" + Shipping_fullname.getText().toString());
        Log.e("up_pno", "" + shipping_PhoneNumber.getText().toString());
        Log.e("up_st", "" + shipping_state.getText().toString());
        Log.e("up_post", "" + shipping_postcode.getText().toString());
        Log.e("up_cname", "" + country_short_name);
        Log.e("up_street", "" + shipping_streetname.getText().toString());
        Log.e("up_apa", "" + shipping_Appartment.getText().toString());

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> create_addr = api.AppUpdateAddress(MyAddress_Preference.getAddressId(getActivity()),
                Login_preference.getcustomer_id(getActivity()),
                Shipping_fullname.getText().toString(),
                country_short_name,
                shipping_postcode.getText().toString(),
                shipping_state.getText().toString(),
                shipping_streetname.getText().toString(),
                shipping_Appartment.getText().toString(),
                shipping_PhoneNumber.getText().toString());
        Log.e("up_fname_aftercall", "" + Shipping_fullname.getText().toString());
        Log.e("country_short_name", "" + country_short_name);
        Log.e("shipping_state", "" + shipping_state.getText().toString());
        Log.e("shipping_PhoneNumber", "" + shipping_PhoneNumber.getText().toString());
        Log.e("shipping_streetname", "" + shipping_streetname.getText().toString());
        Log.e("shipping_Appartment", "" + shipping_Appartment.getText().toString());

        create_addr.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("jsonObject_241", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());

                    Log.e("jsonObject_242", "" + jsonObject);

                    String status = jsonObject.getString("status");
                    Log.e("statusupdateadd", "" + status);
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("Success")) {

                        /*String add_id = jsonObject.getString("customer_address_id");
                        Log.e("add_id_shipping_create", "" + add_id);
                        MyAddress_Preference.setAddressId(getActivity(), add_id);*/

                        CALL_SHIPPING_ADDRESS_API();

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();


                    } else if (status.equalsIgnoreCase("error")) {

                    }

                } catch (Exception e) {
                    Log.e("Exception_shi", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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
                            fistname=add_obj.getString("firstname");
                            apartment=add_obj.getString("apartment");
                            city=add_obj.getString("city");
                            street=add_obj.getString("street");
                            postcode=add_obj.getString("postcode");
                            String phone=add_obj.getString("phone");
                            /*MyAddress_Preference.setAddressId(getActivity(), add_id);*/

                            if (fistname != null && !fistname.isEmpty() && !fistname.equals("null")|| apartment != null && !apartment.isEmpty() && !apartment.equals("null")){
                                lv_addbutton.setVisibility(View.GONE);
                                lv_address.setVisibility(View.VISIBLE);
                                lv_main.setVisibility(View.VISIBLE);
                                MyAddress_Preference.setFirstname(getActivity(), add_obj.getString("firstname"));
                                MyAddress_Preference.setCountryId(getActivity(), add_obj.getString("country_id"));
                                MyAddress_Preference.setStreetAddress(getActivity(), add_obj.getString("street"));
                                MyAddress_Preference.setCity(getActivity(), add_obj.getString("city"));
                                MyAddress_Preference.setZipcode(getActivity(), add_obj.getString("postcode"));
                                MyAddress_Preference.setPhoneNumber(getActivity(), add_obj.getString("phone"));
                                MyAddress_Preference.setAppartment(getActivity(), add_obj.getString("apartment"));

                                confirm_name.setText(add_obj.getString("firstname"));
                                confirm_address.setText(add_obj.getString("apartment") + "\n" + add_obj.getString("street") + "\n" +
                                        add_obj.getString("city") + "," + add_obj.getString("postcode") + "\n" + add_obj.getString("phone"));

                                confirm_name.setText(add_obj.getString("firstname"));
                                //confirm_address.setText(streetadd_confirm + " " + zipcode_confirm + " " + city_confirm + " " + countryid_confirm);
                                //selected_spinner_pos = country_name_code.indexOf(addr_object.getString("country_id"));
                                ccp.setCountryForNameCode(add_obj.getString("country_id"));
                                setValuesToeTextview();
                                setValuesToeEditview();
                               // setValuesToeEditview();
                            }else {
                                lv_addbutton.setVisibility(View.VISIBLE);
                                lv_address.setVisibility(View.GONE);
                                lv_main.setVisibility(View.GONE);
                            }
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
    private void CALL_CART_DELIVERY() {
        cart_delivery_models.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> shippingmethods = api.getShippingMethods();
        shippingmethods.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_shipping_methods", "" + response.body().toString());

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        cart_delivery_models.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("shipping_method");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject obj = jsonArray.getJSONObject(j);
                            JSONArray val_array = obj.getJSONArray("value");
                            for (int k = 0; k < val_array.length(); k++) {
                                try {
                                    JSONObject val_object = val_array.getJSONObject(k);
                                    cart_delivery_models.add(new Cart_Delivery_Model(val_object.getString("code"),
                                            val_object.getString("method"), val_object.getString("title"),
                                            val_object.getString("price")));
                                } catch (Exception e) {
                                    Log.e("Exception_ss", "" + e);
                                } finally {
                                    cart_delivery_adapter.notifyDataSetChanged();
                                }

                            }

                        }

                    } else if (status.equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(), "" + msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("Exception_ssss", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

// check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
