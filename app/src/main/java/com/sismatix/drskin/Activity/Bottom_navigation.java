package com.sismatix.drskin.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.drskin.Adapter.DrawerItemCustomAdapter;
import com.sismatix.drskin.Fragment.Chat;
import com.sismatix.drskin.Fragment.Home;
import com.sismatix.drskin.Fragment.MyAccount_withlogin;
import com.sismatix.drskin.Fragment.MyCart;
import com.sismatix.drskin.Fragment.MyOrders;
import com.sismatix.drskin.Fragment.SignIn;
import com.sismatix.drskin.Fragment.Wishlist;
import com.sismatix.drskin.Model.DataModel;
import com.sismatix.drskin.Preference.CustomTypefaceSpan;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.ApiClient;
import com.sismatix.drskin.Retrofit.ApiInterface;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bottom_navigation extends AppCompatActivity {

    private TextView mTextMessage;
    private ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    private View notificationBadge;
    NavigationView navigationView;
    public static DrawerLayout drawer;

    public static TextView tv_navidrawer, item_count, tv_logout, tv_bottomcount;
    Bundle b;
    boolean doubleBackToExitPressedOnce = false;
    String Screen, cartitem_count, loginflagmain;
    Call<ResponseBody> cartlistt = null;

    private String[] mNavigationDrawerItemTitles;
    private ListView mDrawerList;

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
        viewPager = (ViewPager) findViewById(R.id.view_pager_bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        selectFragment(menu.getItem(0));

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(2);
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.badge_row, menuView, false);
        tv_bottomcount = (TextView) notificationBadge.findViewById(R.id.badge);
        if (cartitem_count.equalsIgnoreCase("null") || cartitem_count.equals("")) {
            tv_bottomcount.setText("0");
        } else {
            tv_bottomcount.setText(cartitem_count);
        }
        itemView.addView(notificationBadge);

        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        DataModel[] drawerItem = new DataModel[7];

        drawerItem[0] = new DataModel("HOME");
        drawerItem[1] = new DataModel("MY ACCOUNT");
        drawerItem[2] = new DataModel("MY ORDERS");
        drawerItem[3] = new DataModel("DR. GLOOSY CUSTOMER  AGREEMENT");
        drawerItem[4] = new DataModel("CONTACT US");
        drawerItem[5] = new DataModel("BOOK APPOINTMENT WITH DR");
        drawerItem[6] = new DataModel("SUGGESTIONS");

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        b = getIntent().getExtras();
        if (b != null) {
            Screen = getIntent().getExtras().getString("screen");
            Log.e("lofin", "" + Screen);
            if (Screen.equalsIgnoreCase("Login") == true) {
                pushFragment(new SignIn(), "signin");
            }
        }
        if (loginflagmain.equalsIgnoreCase("1") || loginflagmain == "1") {
            CALL_CART_COUNT_API();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        // navigationView.setNavigationItemSelectedListener(this);

        ImageView iv_drawer_close = (ImageView) findViewById(R.id.iv_drawer_close);
        iv_drawer_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                }
            }
        });

       /* View header = navigationView.getHeaderView(0);

        ImageView iv_drawer_close = (ImageView) header.findViewById(R.id.iv_drawer_close);
        iv_drawer_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                }
            }
        });*/
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            selectFragment(item);
            return false;

        }

    };

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

   /* @Override
    public boolean onNavigationItemSelected(MenuItem item) {
// Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
// Handle the camera action
            pushFragment(new Home(), "home");

        }else if (id == R.id.nav_myaccount) {
            pushFragment(new MyAccount_withlogin(),"My Account");

        } else if (id == R.id.nav_glossy) {

        } else if (id == R.id.nav_book) {

        } else if (id == R.id.nav_contact) {

        }
        else if (id == R.id.nav_suggestion) {

        }else if (id == R.id.nav_orders) {
            if (loginflagmain.equalsIgnoreCase("1") || loginflagmain == "1") {
                pushFragment(new MyOrders(),"My orders");
            } else {
                pushFragment(new SignIn(),"Login");
            }
        }
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
   */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                // drawer.openDrawer(Gravity.RIGHT);
            }
        }
        return false;
    }

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
                pushFragment(new Home(), "home");
                //viewPager.setCurrentItem(0);
                break;
            case R.id.navigation_shop:
                pushFragment(new MyCart(), "mycart");
                //viewPager.setCurrentItem(1);
                break;
            case R.id.navigation_chat:

                /*Intent i = new Intent(this, Chat_activity.class);
                Log.e("user_99", "" + Login_preference.getcustomer_id(this));
                i.putExtra("user_id", Login_preference.getcustomer_id(this));
                startActivity(i);*/


                /*String mobnum = "96599644282";*/

                String url = null;
                try {
                    url = "https://api.whatsapp.com/send?phone=+96599644282&text=" + URLEncoder.encode("Let's Chat", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

               /* PackageManager packageManager = getApplicationContext().getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                try {
                    String url = "https://api.whatsapp.com/send?phone=+96599644282&text=" + URLEncoder.encode("Let's Chat", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        getApplicationContext().startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                //pushFragment(new Chat(),"chat");
                //viewPager.setCurrentItem(2);
                break;
            case R.id.navigation_wishlist:
                pushFragment(new Wishlist(), "wishlist");
                //viewPager.setCurrentItem(3);
                break;
            case R.id.navigation_me:
                //pushFragment(new MyAccount_withlogin(),"myaccountwithlogin");
                Log.e("drawer_208", "aaa");
                drawer.openDrawer(Gravity.RIGHT);
                Log.e("drawer_209", "bbb");
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
