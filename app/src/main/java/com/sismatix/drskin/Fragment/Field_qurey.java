package com.sismatix.drskin.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Activity.Chat_messge;
import com.sismatix.drskin.Activity.Splash;
import com.sismatix.drskin.Model.My_order_model;
import com.sismatix.drskin.Preference.CheckNetwork;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.ApiClient;
import com.sismatix.drskin.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Field_qurey extends Fragment {


    public Field_qurey() {
        // Required empty public constructor
    }
    EditText sign_fullname,sign_email,edt_article_detail;
    LinearLayout lv_Checkout,lv_ask_doctor;
    TextView tv_title,tv_submittitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_field_qurey, container, false);

        sign_fullname=(EditText)v.findViewById(R.id.sign_fullname);
        sign_email=(EditText)v.findViewById(R.id.sign_email);
        edt_article_detail=(EditText)v.findViewById(R.id.edt_article_detail);
        lv_Checkout=(LinearLayout) v.findViewById(R.id.lv_Checkout);
        lv_ask_doctor=(LinearLayout) v.findViewById(R.id.lv_ask_doctor);
        tv_title=(TextView) v.findViewById(R.id.tv_title);
        tv_submittitle=(TextView) v.findViewById(R.id.tv_submittitle);
        sign_fullname.setTypeface(Home.opensans_regular);
        sign_email.setTypeface(Home.opensans_regular);
        edt_article_detail.setTypeface(Home.opensans_regular);
        tv_title.setTypeface(Home.opensans_bold);
        tv_submittitle.setTypeface(Home.opensans_bold);
        lv_ask_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Live_withdr());
            }
        });
        lv_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sign_fullname.getText().length() == 0) {
                    /*signup_input_layout_email.setError("Please enter your Email id");*/
                    Toast.makeText(getContext(), "Please enter your Fullname", Toast.LENGTH_SHORT).show();
                } else if (sign_email.getText().length() == 0) {
                    /*signup_input_layout_password.setError("Please enter your Password");*/
                    Toast.makeText(getContext(), "Please enter your Email id", Toast.LENGTH_SHORT).show();
                } else if (edt_article_detail.getText().length() == 0) {
                    /*signup_input_layout_password.setError("Please enter your Password");*/
                    Toast.makeText(getContext(), "Please enter your Question", Toast.LENGTH_SHORT).show();
                }else {
                    if (CheckNetwork.isNetworkAvailable(getActivity())) {
                        String name=sign_fullname.getText().toString();
                        String email=sign_email.getText().toString();
                        String queriy=edt_article_detail.getText().toString();
                        Submit_queriy(name,email,queriy);
                    } else {
                        Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return v;
    }
    private void loadFragment(Fragment fragment) {
        Log.e("clickone", "");
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.rootLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void Submit_queriy(String name,String email,String que) {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> appContact = api.AppContact(name,email,que);
        appContact.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_mo", "" + response.body().toString());
              //  progressBar.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_mo", "" + status);
                    if (status.equalsIgnoreCase("Success")) {
                        sign_fullname.setText("");
                        sign_email.setText("");
                        edt_article_detail.setText("");

                        String message = jsonObject.getString("message");
                        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
                    } else if (status.equalsIgnoreCase("error")) {
                    }

                } catch (Exception e) {

                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
              //  progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
