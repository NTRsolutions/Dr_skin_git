package com.sismatix.drskin.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sismatix.drskin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Checkout extends Fragment implements View.OnClickListener {

    LinearLayout lv_shipping, lv_confirmation, lv_payment;
    public static LinearLayout lv_shipping_selected, lv_payment_selected, lv_confirmation_selected;
    public static ImageView iv_confirmation_done, iv_payment_done, iv_shipping_done, iv_close_checkout;
    public static TextView tv_shipping, tv_payment, tv_confirmation, checkout_total,tv_checkout_title;
    String loginflag, tot_cart,subtot,coupon_code,discount_amount;
    View v;

    public Checkout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_checkout, container, false);

        AllocateMemory(v);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            tot_cart = bundle.getString("grand_tot_cart");
            subtot = bundle.getString("subtotal");
            coupon_code = bundle.getString("coupon_code");
            discount_amount = bundle.getString("discount_amount");
            Log.e("checkout_tot", "" + tot_cart);
            Log.e("checkout_subtot", "" + subtot);

        }
        Bundle b = new Bundle();
        b.putString("grand_tot_cart", tot_cart);
        b.putString("subtotal", subtot);
        b.putString("coupon_code", coupon_code);
        b.putString("discount", discount_amount);
        loadFragment(b,v);
        checkout_total.setText(tot_cart);
        lv_shipping.setOnClickListener(this);
        lv_confirmation.setOnClickListener(this);
        lv_payment.setOnClickListener(this);
        iv_close_checkout.setOnClickListener(this);

        lv_payment_selected.setVisibility(View.INVISIBLE);
        lv_confirmation_selected.setVisibility(View.INVISIBLE);
        lv_shipping_selected.setVisibility(View.VISIBLE);

        tv_checkout_title.setTypeface(Home.opensans_bold);
        tv_shipping.setTypeface(Home.opensans_bold);
        tv_confirmation.setTypeface(Home.opensans_bold);
        tv_payment.setTypeface(Home.opensans_bold);


        /*if (loginflag.equalsIgnoreCase("1") || loginflag == "1") {
            loadFragment(new Shipping_fragment());
        } else {
            loadFragment(new SignIn());
        }*/

        return v;
    }

    private void AllocateMemory(View v) {
        lv_shipping = (LinearLayout) v.findViewById(R.id.lv_shipping);
        lv_confirmation = (LinearLayout) v.findViewById(R.id.lv_confirmation);
        lv_payment = (LinearLayout) v.findViewById(R.id.lv_payment);
        lv_shipping_selected = (LinearLayout) v.findViewById(R.id.lv_shipping_selected);
        lv_payment_selected = (LinearLayout) v.findViewById(R.id.lv_payment_selected);
        lv_confirmation_selected = (LinearLayout) v.findViewById(R.id.lv_confirmation_selected);

        iv_confirmation_done = (ImageView) v.findViewById(R.id.iv_confirmation_done);
        iv_payment_done = (ImageView) v.findViewById(R.id.iv_payment_done);
        iv_shipping_done = (ImageView) v.findViewById(R.id.iv_shipping_done);
        iv_close_checkout = (ImageView) v.findViewById(R.id.iv_close_checkout);
        tv_shipping = (TextView) v.findViewById(R.id.tv_shipping);
        tv_payment = (TextView) v.findViewById(R.id.tv_payment);
        tv_confirmation = (TextView) v.findViewById(R.id.tv_confirmation);
        tv_checkout_title = (TextView) v.findViewById(R.id.tv_checkout_title);
        checkout_total = (TextView) v.findViewById(R.id.checkout_total);
    }

    private void loadFragment(Bundle b,View v) {

        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Fragment myFragment = new Shipping();
        myFragment.setArguments(b);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_checkout, myFragment).addToBackStack(null).commit();
       /* Log.e("clickone", "");
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout_checkout, fragment);
        transaction.addToBackStack(null);

        transaction.commit();*/
    }

    private void loadFragmentmain(Fragment fragment) {
        Log.e("clickone", "");
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction .setCustomAnimations(R.anim.fade_in,
                0, 0, R.anim.fade_out);
        transaction.replace(R.id.rootLayout, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        if (v == iv_close_checkout) {
            loadFragmentmain(new Home());
        }

    }
}
