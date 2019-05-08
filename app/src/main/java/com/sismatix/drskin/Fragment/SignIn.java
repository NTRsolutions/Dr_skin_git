package com.sismatix.drskin.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Preference.Login_preference;
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
public class SignIn extends Fragment implements View.OnClickListener {
    Toolbar toolbar_Signin;
    Button btn_createaccount_signin,btn_signin;
    LinearLayout lv_mainsignin,lv_main;
    EditText et_email_signin;
    ProgressBar progressBar;
    Bundle bundle;
    TextInputEditText etPassword;
    TextView tv_forgotpassword_signin,tv_signin_title;
    String tot_cart,subtot,screen_type;

    public SignIn() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        AllocateMemory(v);
        setupUI(lv_mainsignin);
        bundle = this.getArguments();

        setHasOptionsMenu(true);
        ((Bottom_navigation) getActivity()).setSupportActionBar(toolbar_Signin);
        ((Bottom_navigation) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);
        btn_signin.setOnClickListener(this);
        //toolbar_Signin.setTitle(R.string.signin);

        btn_createaccount_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new SignUp();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).replace(R.id.rootLayout, myFragment).addToBackStack(null).commit();
            }
        });

        tv_signin_title.setTypeface(Home.opensans_bold);
        et_email_signin.setTypeface(Home.opensans_regular);
        etPassword.setTypeface(Home.opensans_regular);
        btn_signin.setTypeface(Home.opensans_bold);
        tv_forgotpassword_signin.setTypeface(Home.opensans_bold);
        btn_createaccount_signin.setTypeface(Home.opensans_bold);
        return v;
    }

    @Override
    public void onClick(View view) {
        final String login_email = et_email_signin.getText().toString();
        final String password = etPassword.getText().toString();

        if (et_email_signin.getText().length() == 0) {
            /*signup_input_layout_email.setError("Please enter your Email id");*/
            Toast.makeText(getContext(), "Please enter your Email id", Toast.LENGTH_SHORT).show();
        } else if (etPassword.getText().length() == 0) {
            /*signup_input_layout_password.setError("Please enter your Password");*/
            Toast.makeText(getContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
        } else {
            Loginapi(login_email,password);
        }
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
    private void Loginapi(String login_email,String password) {
        lv_main.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Log.e("email",""+login_email);
        Log.e("password",""+password);
        //makin g api call
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> login = api.login(login_email, password, Login_preference.getquote_id(getActivity()),Login_preference.getdevicetoken(getActivity()),"Android");

        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                lv_main.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Log.e("response", "" + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status", "" + status);
                    String meassg = jsonObject.getString("message");
                    Log.e("message", "" + meassg);
                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                        Login_preference.setLogin_flag(getActivity(), "1");
                        Log.e("customerrrf",""+jsonObject.getString("customer_id"));
                        Login_preference.setcustomer_id(getActivity(), jsonObject.getString("customer_id"));
                        Log.e("customer_id",""+Login_preference.getcustomer_id(getActivity()));
                        Login_preference.setemail(getActivity(), jsonObject.getString("email"));
                        Login_preference.setfullname(getActivity(), jsonObject.getString("fullname"));
                        Log.e("fullname_lofin",""+jsonObject.getString("fullname"));
                        String qid_login = jsonObject.getString("quote_id");
                        Log.e("qid_login",""+jsonObject.getString("quote_id"));
                        Login_preference.setquote_id(getContext(),jsonObject.getString("quote_id"));

                        if (bundle != null) {

                            tot_cart = bundle.getString("grand_tot_cart");
                            subtot = bundle.getString("subtotal");
                            screen_type = bundle.getString("value");
                            Log.e("checkout_tot", "" + tot_cart);
                            Log.e("checkout_subtot", "" + subtot);
                            Bundle b = new Bundle();
                            b.putString("grand_tot_cart", tot_cart);
                            b.putString("subtotal", subtot);
                            loadFragment(new Checkout(),b);
                        } else {
                            Intent intent = new Intent(getActivity(), Bottom_navigation.class);
                            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    } else if (status.equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("exception", "" +e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                lv_main.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void AllocateMemory(View v) {
        toolbar_Signin = (Toolbar) v.findViewById(R.id.toolbar_Signin);
        btn_createaccount_signin = (Button)v.findViewById(R.id.btn_createaccount_signin);
        btn_signin = (Button)v.findViewById(R.id.btn_signin);
        lv_mainsignin = (LinearLayout) v.findViewById(R.id.lv_mainsignin);
        lv_main = (LinearLayout) v.findViewById(R.id.lv_main);
        et_email_signin = (EditText) v.findViewById(R.id.signin_email);
        tv_forgotpassword_signin = (TextView) v.findViewById(R.id.tv_forgotpassword_signin);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        etPassword=(TextInputEditText)v.findViewById(R.id.etPassword);
        tv_signin_title=(TextView) v.findViewById(R.id.tv_signin_title);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
