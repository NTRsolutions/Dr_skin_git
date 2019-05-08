package com.sismatix.drskin.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Preference.CheckNetwork;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.Preference.MyAddress_Preference;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.ApiClient;
import com.sismatix.drskin.Retrofit.ApiInterface;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Account_information extends Fragment implements View.OnClickListener {
    Toolbar toolbar_accountinfo;
    TextView tv_username, tv_fullname, tv_email, tv_address, tv_number,tv_email_main
            ,tv_accountinfo_title,tv_titleinfo,tv_full,tv_emailll,tv_phone,tv_myadressess,tv_kwaitaddree;
    LinearLayout lv_acc_main,lv_edit_addresssss,lv_myaccount_main;
    LinearLayout lv_edit_account_info,lv_edit_add,lv_savee;
    EditText edt_fullname, edt_ph_no;
    ImageView edit_ic, ok_ic;
    boolean flag_fullname = true;
    String fistname,apartment,city,street,postcode;
    CountryCodePicker ccp;
    String countryCodeAndroid = "91", country_namee, country_short_name,addid;
    public static ArrayList<String> country_name_code = new ArrayList<String>();
    public static ArrayList<String> country_name = new ArrayList<String>();
    EditText Shipping_fullname, shipping_state, shipping_streetname, shipping_Appartment, shipping_PhoneNumber, shipping_postcode, et_voucher;
    Spinner edit_spinner_country_Name;

    public Account_information() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account_information, container, false);
        Allocationmemeory(v);
        setupUI(lv_myaccount_main);
        setHasOptionsMenu(true);
        ((Bottom_navigation) getActivity()).setSupportActionBar(toolbar_accountinfo);
        ((Bottom_navigation) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);

        tv_username.setText(Login_preference.getfullname(getActivity()));
        tv_fullname.setText(Login_preference.getfullname(getActivity()));
        tv_email.setText(Login_preference.getemail(getActivity()));
        tv_email_main.setText(Login_preference.getemail(getActivity()));
        tv_address.setText(MyAddress_Preference.getAppartment(getActivity()) + "" + MyAddress_Preference.getStreetAddress(getActivity()) +
                MyAddress_Preference.getCity(getActivity()) + "" + MyAddress_Preference.getCountryId(getActivity()) + "" + MyAddress_Preference.getZipcode(getActivity()) + "" +
                MyAddress_Preference.getPhoneNumber(getActivity()));
        tv_number.setText(MyAddress_Preference.getPhoneNumber(getContext()));

        lv_edit_account_info.setOnClickListener(this);
        lv_edit_add.setOnClickListener(this);
        lv_savee.setOnClickListener(this);
        tv_accountinfo_title.setTypeface(Home.opensans_bold);
        tv_username.setTypeface(Home.opensans_bold);
        tv_email_main.setTypeface(Home.opensans_regular);
        tv_fullname.setTypeface(Home.opensans_regular);
        tv_email.setTypeface(Home.opensans_regular);
        tv_number.setTypeface(Home.opensans_regular);
        tv_address.setTypeface(Home.opensans_regular);
        tv_titleinfo.setTypeface(Home.opensans_bold);
        tv_full.setTypeface(Home.opensans_bold);
        tv_emailll.setTypeface(Home.opensans_bold);
        tv_phone.setTypeface(Home.opensans_bold);
        tv_myadressess.setTypeface(Home.opensans_bold);
        tv_kwaitaddree.setTypeface(Home.opensans_bold);

        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            CALL_SHIPPING_ADDRESS_API();
        } else {
            Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

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
        return v;
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
                                lv_acc_main.setVisibility(View.VISIBLE);
                                MyAddress_Preference.setFirstname(getActivity(), add_obj.getString("firstname"));
                                MyAddress_Preference.setCountryId(getActivity(), add_obj.getString("country_id"));
                                MyAddress_Preference.setStreetAddress(getActivity(), add_obj.getString("street"));
                                MyAddress_Preference.setCity(getActivity(), add_obj.getString("city"));
                                MyAddress_Preference.setZipcode(getActivity(), add_obj.getString("postcode"));
                                MyAddress_Preference.setPhoneNumber(getActivity(), add_obj.getString("phone"));
                                MyAddress_Preference.setAppartment(getActivity(), add_obj.getString("apartment"));

                                tv_address.setText(add_obj.getString("apartment") + "\n" + add_obj.getString("street") + "\n" +
                                        add_obj.getString("city") + "," + add_obj.getString("postcode") + "\n" + add_obj.getString("phone"));

                                //confirm_address.setText(streetadd_confirm + " " + zipcode_confirm + " " + city_confirm + " " + countryid_confirm);
                                //selected_spinner_pos = country_name_code.indexOf(addr_object.getString("country_id"));
                                ccp.setCountryForNameCode(add_obj.getString("country_id"));
                                setValuesToeTextview();
                                setValuesToeEditview();
                                lv_acc_main.setVisibility(View.VISIBLE);
                                lv_edit_addresssss.setVisibility(View.GONE);
                                // setValuesToeEditview();

                            }else {
                                lv_acc_main.setVisibility(View.GONE);
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

    private void setValuesToeTextview() {
        String ph = MyAddress_Preference.getPhoneNumber(getActivity());
        String ct = MyAddress_Preference.getCity(getActivity());

        Log.e("phhhhhh", "" + ph);
        Log.e("ctttttt", "" + ct);

        tv_address.setText(MyAddress_Preference.getAppartment(getActivity()) + "," + "" + MyAddress_Preference.getStreetAddress(getActivity()) + "," + "" +
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

    private void Allocationmemeory(View v) {
        toolbar_accountinfo = (Toolbar) v.findViewById(R.id.toolbar_accountinfo);
        tv_username = (TextView) v.findViewById(R.id.tv_username);
        tv_fullname = (TextView) v.findViewById(R.id.tv_fullname);
        tv_email = (TextView) v.findViewById(R.id.tv_email);
        tv_address = (TextView) v.findViewById(R.id.tv_address);
        tv_number = (TextView) v.findViewById(R.id.tv_number);
        tv_email_main = (TextView) v.findViewById(R.id.tv_email_main);
        tv_accountinfo_title = (TextView) v.findViewById(R.id.tv_accountinfo_title);
        tv_titleinfo = (TextView) v.findViewById(R.id.tv_titleinfo);
        tv_full = (TextView) v.findViewById(R.id.tv_full);
        tv_emailll = (TextView) v.findViewById(R.id.tv_emailll);
        tv_phone = (TextView) v.findViewById(R.id.tv_phone);
        tv_myadressess = (TextView) v.findViewById(R.id.tv_myadressess);
        tv_kwaitaddree = (TextView) v.findViewById(R.id.tv_kwaitaddree);
        ccp = (CountryCodePicker) v.findViewById(R.id.ccp_myaccount);

        lv_acc_main = (LinearLayout) v.findViewById(R.id.lv_acc_main);
        lv_edit_addresssss = (LinearLayout) v.findViewById(R.id.lv_edit_addresssss);
        lv_savee = (LinearLayout) v.findViewById(R.id.lv_savee);
        lv_myaccount_main = (LinearLayout) v.findViewById(R.id.lv_myaccount_main);

        lv_edit_account_info = (LinearLayout) v.findViewById(R.id.lv_edit_account_info);
        lv_edit_add = (LinearLayout) v.findViewById(R.id.lv_edit_add);

        edt_fullname = (EditText) v.findViewById(R.id.edt_fullname);
        edt_ph_no = (EditText) v.findViewById(R.id.edt_ph_no);

        edit_ic = (ImageView) v.findViewById(R.id.edit_ic);
        ok_ic = (ImageView) v.findViewById(R.id.ok_ic);

        Shipping_fullname = (EditText) v.findViewById(R.id.Shipping_fullname);
        shipping_state = (EditText) v.findViewById(R.id.shipping_state);
        shipping_streetname = (EditText) v.findViewById(R.id.shipping_streetname);
        shipping_Appartment = (EditText) v.findViewById(R.id.shipping_Appartment);
        shipping_PhoneNumber = (EditText) v.findViewById(R.id.shipping_PhoneNumber);
        shipping_postcode = (EditText) v.findViewById(R.id.shipping_postcode);
        et_voucher = (EditText) v.findViewById(R.id.et_voucher);
        edit_spinner_country_Name = (Spinner) v.findViewById(R.id.edit_spinner_country_Name);
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

    @Override
    public void onClick(View v) {

        if (v == lv_edit_account_info) {

            if (lv_edit_account_info.isClickable() == true && flag_fullname == true) {

                tv_fullname.setVisibility(View.GONE);
                tv_number.setVisibility(View.GONE);
                edit_ic.setVisibility(View.GONE);
                edt_fullname.setVisibility(View.VISIBLE);
                edt_fullname.setText(Login_preference.getfullname(getActivity()));
                edt_ph_no.setVisibility(View.VISIBLE);
                edt_ph_no.setText(MyAddress_Preference.getPhoneNumber(getContext()));
                ok_ic.setVisibility(View.VISIBLE);

                flag_fullname = false;

            } else if (lv_edit_account_info.isClickable() == true && flag_fullname == false) {
                tv_fullname.setVisibility(View.VISIBLE);
                tv_number.setVisibility(View.VISIBLE);
                edit_ic.setVisibility(View.VISIBLE);
                edt_fullname.setVisibility(View.GONE);
                edt_ph_no.setVisibility(View.GONE);
                ok_ic.setVisibility(View.GONE);

                flag_fullname = true;
            }
        }else if (v == lv_edit_add){
            lv_acc_main.setVisibility(View.GONE);
            lv_edit_addresssss.setVisibility(View.VISIBLE);
        }else if (v == lv_savee){
            validateAdd();
            /*if (addid.equalsIgnoreCase("") || addid.equalsIgnoreCase("null")) {

            }*/
        }


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
            callUpdateAddApi();
        }
    }

    private void callUpdateAddApi() {
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
}
