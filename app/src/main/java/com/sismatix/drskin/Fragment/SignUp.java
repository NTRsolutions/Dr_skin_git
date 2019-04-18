package com.sismatix.drskin.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.ApiClient;
import com.sismatix.drskin.Retrofit.ApiInterface;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUp extends Fragment implements View.OnClickListener {

    Toolbar toolbar_Signup;
    LinearLayout lv_singupmain;
    Button btn_signup,btn_alredyaccount;
    EditText signup_fullname, signup_email, etPassword;
   // TextInputLayout etPasswordLayout;
    public SignUp() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);


        AllocateMemory(v);
       // setupUI(lv_singupmain);
        ((Bottom_navigation) getActivity()).setSupportActionBar(toolbar_Signup);
        ((Bottom_navigation) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_36dp);
        btn_signup.setOnClickListener(this);
        btn_alredyaccount.setOnClickListener(this);
        return v;
    }
    private void AllocateMemory(View v) {
        toolbar_Signup = (Toolbar) v.findViewById(R.id.toolbar_Signup);
        lv_singupmain = (LinearLayout) v.findViewById(R.id.lv_singupmain);
        btn_signup=(Button)v.findViewById(R.id.btn_signup);
        btn_alredyaccount=(Button)v.findViewById(R.id.btn_alredyaccount);
        signup_fullname=(EditText)v.findViewById(R.id.sign_fullname);
        signup_email=(EditText)v.findViewById(R.id.sign_email);
        etPassword=(EditText)v.findViewById(R.id.etPassword);
       // etPasswordLayout=(TextInputLayout) v.findViewById(R.id.etPasswordLayout);


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
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_signup){
            validateSignupData();

        }else if (view == btn_alredyaccount){
            pushFragment(new SignIn(),"signin");
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
    private void validateSignupData() {

        final String signup_fullnamee = signup_fullname.getText().toString();
        final String signup_emailid = signup_email.getText().toString();
        final String signup_passwordd = etPassword.getText().toString();
        if (signup_fullname.getText().toString().equals("") && signup_fullname.getText().length() == 0) {
            /*signup_input_layout_fullname.setError("Please enter your Fullname");*/
            Toast.makeText(getContext(), "Please enter your Fullname", Toast.LENGTH_SHORT).show();
        } else if (signup_email.getText().length() == 0) {
            /*signup_input_layout_email.setError("Please enter your Email id");*/
            Toast.makeText(getContext(), "Please enter your Email id", Toast.LENGTH_SHORT).show();
        }/*else if (signup_emailid.matches(pattern)!=true) {
            signup_input_layout_email.setError("Please enter valid Email id");
        } else if (check = m.matches() != true) {
            signup_input_layout_email.setError("Please enter valid Email id");
        }*/ else if (isValidEmailAddress(signup_email.getText().toString()) == false) {
            /*signup_input_layout_email.setError("Please enter valid email");*/
            Toast.makeText(getContext(), "Please enter valid Email id", Toast.LENGTH_SHORT).show();
        } else if (etPassword.getText().length() == 0) {
            /*signup_input_layout_password.setError("Please enter your Password");*/
            Toast.makeText(getContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
        } else if (etPassword.getText().toString().length() <= 5) {
            /*signup_input_layout_password.setError("Password must be at least 6 characters long");*/
            Toast.makeText(getContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
        } else {
            signUpUser(signup_fullnamee, signup_emailid, signup_passwordd);
        }
    }
    private void signUpUser(String signup_fullnamee, String signup_emailid, String signup_passwordd) {

        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> signup = apii.signup(signup_fullnamee, signup_emailid, signup_passwordd);

        signup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status", "" + status);
                    String meassg = jsonObject.getString("message");
                    Log.e("message", "" + meassg);
                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                        /*Login_preference.setcustomer_id(getActivity(),jsonObject.getString("customer_id"));
                        Login_preference.setemail(getActivity(),jsonObject.getString("email"));
                        Login_preference.setfullname(getActivity(),jsonObject.getString("fullname"));*/
                        SignIn nextFrag = new SignIn();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in,
                                        0, 0, R.anim.fade_out)
                                .replace(R.id.rootLayout, nextFrag, "login")
                                .addToBackStack(null)
                                .commit();
                    } else if (status.equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
