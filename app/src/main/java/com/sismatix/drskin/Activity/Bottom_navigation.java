package com.sismatix.drskin.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.drskin.Fragment.Chat;
import com.sismatix.drskin.Fragment.Home;
import com.sismatix.drskin.Fragment.MyAccount_withlogin;
import com.sismatix.drskin.Fragment.MyCart;
import com.sismatix.drskin.Fragment.SignIn;
import com.sismatix.drskin.Fragment.Wishlist;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.ApiClient;
import com.sismatix.drskin.Retrofit.ApiInterface;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bottom_navigation extends AppCompatActivity {

    private TextView mTextMessage;
    private ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    private View notificationBadge;
    public static TextView tv_navidrawer, item_count, tv_logout, tv_bottomcount;
    Bundle b;
    boolean doubleBackToExitPressedOnce = false;
    String Screen,cartitem_count,loginflagmain;
    Call<ResponseBody> cartlistt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        cartitem_count = Login_preference.getCart_item_count(Bottom_navigation.this);
        loginflagmain = Login_preference.getLogin_flag(Bottom_navigation.this);
        Log.e("cart_total_items", "" + cartitem_count);
//test
        mTextMessage = (TextView) findViewById(R.id.message);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setItemIconTintList(null);
        viewPager=(ViewPager)findViewById(R.id.view_pager_bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        selectFragment(menu.getItem(0));

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(1);
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.badge_row, menuView, false);
        tv_bottomcount = (TextView) notificationBadge.findViewById(R.id.badge);
        if (cartitem_count.equalsIgnoreCase("null") || cartitem_count.equals("")) {
            tv_bottomcount.setText("0");
        } else {
            tv_bottomcount.setText(cartitem_count);
        }
        itemView.addView(notificationBadge);

        b = getIntent().getExtras();
        if(b!=null)
        {
            Screen=getIntent().getExtras().getString("screen");
            Log.e("lofin",""+Screen);
            if(Screen.equalsIgnoreCase("Login")==true) {
                pushFragment(new SignIn(),"signin");
            }
        }
        if (loginflagmain.equalsIgnoreCase("1") || loginflagmain == "1") {
            CALL_CART_COUNT_API();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            selectFragment(item);
            return false;

        }

    };

    private void CALL_CART_COUNT_API() {

        String email = Login_preference.getemail(Bottom_navigation.this);

        String loginflag = Login_preference.getLogin_flag(Bottom_navigation.this);
        Log.e("customeriddd", "" + Login_preference.getcustomer_id(Bottom_navigation.this));
        if (loginflag.equalsIgnoreCase("1") || loginflag == "1") {
            Log.e("with_login", "");
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            cartlistt = api.Cartlist(email);
        }

        cartlistt.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("responseeeeee", "" + response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_prepare_cart", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        String cart_items_count = jsonObject.getString("items_qty");
                        Login_preference.setCart_item_count(Bottom_navigation.this, cart_items_count);
                        Log.e("cart_items_total_cart", "" + cart_items_count);
                        if (jsonObject.getString("items_qty").equalsIgnoreCase("null") || jsonObject.getString("items_qty").equals("")) {
                            Bottom_navigation.tv_bottomcount.setText("0");
                            Bottom_navigation.item_count.setText("0");
                        } else {
                            Bottom_navigation.tv_bottomcount.setText(jsonObject.getString("items_qty"));
                            Bottom_navigation.item_count.setText(jsonObject.getString("items_qty"));
                        }

                    } else if (status.equalsIgnoreCase("error")) {

                    }

                } catch (Exception e) {
                    Log.e("nav_exc", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Bottom_navigation.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void selectFragment(MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.navigation_home:
                // Action to perform when Home Menu item is selected.
                pushFragment(new Home(),"home");
                //viewPager.setCurrentItem(0);
                break;
            case R.id.navigation_shop:
                pushFragment(new MyCart(),"mycart");
                //viewPager.setCurrentItem(1);
                break;
            case R.id.navigation_chat:
                pushFragment(new Chat(),"chat");
                //viewPager.setCurrentItem(2);
                break;
            case R.id.navigation_wishlist:
                pushFragment(new Wishlist(),"wishlist");
                //viewPager.setCurrentItem(3);
                break;
            case R.id.navigation_me:
                pushFragment(new MyAccount_withlogin(),"myaccountwithlogin");
        }
    }
    private void pushFragment(Fragment fragment, String add_to_backstack) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getSupportFragmentManager();
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
    public static void Check_String_NULL_Value(TextView textview, String text) {


        if (text.equalsIgnoreCase("null") == true) {
            textview.setText("");
        } else {

            textview.setText(Html.fromHtml(Convert_String_First_Letter(text)));
        }

    }
    public static String Convert_String_First_Letter(String convert_string) {
        String upperString;

        if (convert_string.length() > 0) {
            upperString = convert_string.substring(0, 1).toUpperCase() + convert_string.substring(1);
        } else {
            upperString = " ";
        }
        return upperString;
    }

    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if (count == 1) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                super.finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            String title = fragmentManager.getBackStackEntryAt(count - 2).getName();
            super.onBackPressed();
            Log.e("onBackPressetitle", "" + title);
        }
    }
}
