package com.sismatix.drskin.Fragment;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Adapter.Cuntrylist_Adapter;
import com.sismatix.drskin.Adapter.SlideingImageAdapter;
import com.sismatix.drskin.Model.sliderimage_model;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.ApiClient;
import com.sismatix.drskin.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.sismatix.drskin.Fragment.Wishlist.activity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Product_Details extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private List<sliderimage_model> sliderimage_models = new ArrayList<sliderimage_model>();
    Timer timer;
    final long DELAY_MS = 800, PERIOD_MS = 4000;

    int currentPage = 0;
    StringRequest stringRequest;
    private String URL_HOMEPAGE;

    Toolbar toolbar_product_detail;

    ViewPager mPager;
    CircleIndicator indicator;
    ImageView iv_wishlist, iv_itemdetail_cart, iv_back, iv_cart_quantity_increase, iv_cart_quantity_decrease, iv_search;
    LinearLayout lv_iteamdetails_click, l_cartview, lv_call, lv_email;

    TextView tv_product_name, tv_product_price, tv_short_description, tv_long_descriptionn, tv_main_title, tv_descriptiontitle,
            tv_id_addtocart, tv_sku,tv_title_contt,tv_phone,tv_email,tv_addtocart;
    ImageView iv_item_desc, iv_show_more;

    String proddd_id, loginflag, iswhishlisted, prod_name;
    ProgressBar progressBar_item;
    MenuItem fillwish, wish;
    Call<ResponseBody> addtocart = null;
    TextView tv_product_title, tv_cart_quantity_total;
    public static Button cart_count;
    public static String count;

    public Product_Details() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product__details, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            proddd_id = bundle.getString("prod_id");
            prod_name = bundle.getString("prod_name");
            Log.e("category_id_thcf", "" + proddd_id);
            /// product_array =bundle.getString("products_array");
            Log.e("products_arrayyyy", "" + prod_name);
        }
        AllocateMemory(v);
        tv_product_title.setText(prod_name);
        String cart_countt = Login_preference.getCart_item_count(getActivity());
        if (cart_countt.equalsIgnoreCase("") || cart_countt == "null") {
            cart_count.setText("0");
        } else {
            cart_count.setText(Login_preference.getCart_item_count(getActivity()));
        }
        setHasOptionsMenu(true);
        ((Bottom_navigation) getActivity()).setSupportActionBar(toolbar_product_detail);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((Bottom_navigation) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_36dp);

        call_item_detail_api(proddd_id);
        lv_iteamdetails_click.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        l_cartview.setOnClickListener(this);
        lv_call.setOnClickListener(this);
        lv_email.setOnClickListener(this);

        mPager.addOnPageChangeListener(this);
        startAutoScrollViewPager();

        iv_cart_quantity_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int textqut = Integer.parseInt(tv_cart_quantity_total.getText().toString());
                //quantity = textqut + 1;
                int Result = textqut + 1;
                totle(Result);
            }
        });

        iv_cart_quantity_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int textqut = Integer.parseInt(tv_cart_quantity_total.getText().toString());
                if (textqut != 0) {
                    int Result = textqut - 1;
                    if (Result == 0) {
                        Result = 1;
                        Log.e("result", "" + Result);
                        totle(Result);
                    } else {
                        Log.e("result", "" + Result);
                        totle(Result);
                    }
                } else {
                }
            }
        });

        return v;
    }

    private void totle(int result) {
        Log.e("ressssssut", "" + result);
        String res = String.valueOf(result);
        tv_cart_quantity_total.setText(String.valueOf(result));
    }

    private void AllocateMemory(View v) {
        toolbar_product_detail = (Toolbar) v.findViewById(R.id.toolbar_product_detail);
        mPager = (ViewPager) v.findViewById(R.id.pager);
        indicator = (CircleIndicator) v.findViewById(R.id.indicator);
        progressBar_item = (ProgressBar) v.findViewById(R.id.progressBar_item);
        indicator = (CircleIndicator) v.findViewById(R.id.indicator);
        lv_iteamdetails_click = (LinearLayout) v.findViewById(R.id.lv_iteamdetails_click);
        l_cartview = (LinearLayout) v.findViewById(R.id.l_cartview);
        lv_call = (LinearLayout) v.findViewById(R.id.lv_call);
        lv_email = (LinearLayout) v.findViewById(R.id.lv_email);

        tv_product_name = (TextView) v.findViewById(R.id.tv_product_namee);
        tv_product_name.setTypeface(Home.opensans_bold);
        tv_product_price = (TextView) v.findViewById(R.id.tv_product_pricee);
        tv_product_price.setTypeface(Home.opensans_regular);
        tv_short_description = (TextView) v.findViewById(R.id.tv_short_descriptionn);
        tv_long_descriptionn = (TextView) v.findViewById(R.id.tv_long_descriptionn);
        tv_long_descriptionn.setTypeface(Home.opensans_regular);
        tv_short_description.setTypeface(Home.opensans_regular);
        tv_product_title = (TextView) v.findViewById(R.id.tv_product_title);
        tv_sku = (TextView) v.findViewById(R.id.tv_sku);
        tv_title_contt = (TextView) v.findViewById(R.id.tv_product_cont);
        tv_phone = (TextView) v.findViewById(R.id.tv_phone);
        tv_email = (TextView) v.findViewById(R.id.tv_email);
        tv_addtocart = (TextView) v.findViewById(R.id.tv_addtocart);
        tv_title_contt.setTypeface(Home.opensans_bold);
        tv_phone.setTypeface(Home.opensans_regular);
        tv_email.setTypeface(Home.opensans_regular);
        tv_sku.setTypeface(Home.opensans_regular);

        tv_descriptiontitle = (TextView) v.findViewById(R.id.tv_descriptiontitle);
        tv_descriptiontitle.setTypeface(Home.opensans_bold);
        tv_cart_quantity_total = (TextView) v.findViewById(R.id.tv_cart_quantity_total);
        cart_count = (Button) v.findViewById(R.id.cart_count);

        iv_item_desc = (ImageView) v.findViewById(R.id.iv_item_desc);
        iv_show_more = (ImageView) v.findViewById(R.id.iv_show_more);
        iv_cart_quantity_increase = (ImageView) v.findViewById(R.id.iv_cart_quantity_increase);
        iv_cart_quantity_decrease = (ImageView) v.findViewById(R.id.iv_cart_quantity_decrease);
        iv_search = (ImageView) v.findViewById(R.id.iv_search);
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

    private void call_item_detail_api(String proddd_id) {
        Log.e("proddd_id", "" + proddd_id);
        Log.e("customer_id", "" + Login_preference.getcustomer_id(getActivity()));
        progressBar_item.setVisibility(View.VISIBLE);
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Log.e("ciddd_item", "" + Login_preference.getcustomer_id(getActivity()));
        Call<ResponseBody> addcategory = api.appprodview(proddd_id, Login_preference.getcustomer_id(getActivity()));
        sliderimage_models.clear();
        addcategory.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e("response", "" + response);
                progressBar_item.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    Log.e("dataaa", "" + jsonObject);
                    String typeee = jsonObject.getString("type");
                    Log.e("type_item_details", "" + typeee);

                    String main_title = jsonObject.getString("product_name");
                    toolbar_product_detail.setTitle(main_title);
                    tv_product_name.setText(main_title);

                    String proname = jsonObject.getString("product_sku");

                    tv_sku.setText("SKU :" + proname);
                    // Navigation_drawer_activity.Check_String_NULL_Value(tv_product_name, proname);

                    String proprice = jsonObject.getString("product_price");
                    tv_product_price.setText(proprice);
                    //Navigation_drawer_activity.Check_String_NULL_Value(tv_product_price, proprice);

                    final String shortdesc = jsonObject.getString("short_description");

                    if (shortdesc != null && !shortdesc.isEmpty() && !shortdesc.equals("null")) {
                        tv_short_description.setText(Html.fromHtml(shortdesc));
                    } else {
                        tv_short_description.setText("");
                    }
                    final String desc = jsonObject.getString("description");

                    if (desc != null && !desc.isEmpty() && !desc.equals("null")) {
                        tv_long_descriptionn.setText(Html.fromHtml(desc));
                    } else {
                        tv_long_descriptionn.setText("");
                    }
                    iv_item_desc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iv_show_more.setVisibility(View.VISIBLE);
                            iv_item_desc.setVisibility(View.GONE);
                            tv_long_descriptionn.setVisibility(View.GONE);
                            tv_short_description.setVisibility(View.VISIBLE);

                        }
                    });

                    iv_show_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iv_show_more.setVisibility(View.GONE);
                            iv_item_desc.setVisibility(View.VISIBLE);
                            tv_long_descriptionn.setVisibility(View.VISIBLE);
                            tv_short_description.setVisibility(View.GONE);
                        }
                    });

                    JSONArray jsonArray = jsonObject.getJSONArray("mediaGallary");

                    Log.e("jsonarrraay", "" + jsonArray);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String value = jsonArray.getString(i);
                        sliderimage_models.add(new sliderimage_model(jsonArray.getString(i)));
                        Log.e("json", i + "=" + value);
                    }
                    mPager.setAdapter(new SlideingImageAdapter(getActivity(), sliderimage_models));
                   // indicator.setViewPager(mPager);
                    if(jsonArray.length()>1)
                    {
                        indicator.setViewPager(mPager);
                    }
                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startAutoScrollViewPager() {

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == sliderimage_models.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    @Override
    public void onClick(View view) {
        if (view == lv_iteamdetails_click) {
            AddTocart();
        } else if (view == iv_search) {
            pushFragment(new Search(), "search");
        } else if (view == l_cartview) {
            pushFragment(new MyCart(), "cart");
        } else if (view == lv_call) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + 923145689));
            getActivity().startActivity(i);
        } else if (view == lv_email) {
            Intent it = new Intent(Intent.ACTION_SEND);
            it.putExtra(Intent.EXTRA_EMAIL, new String[]{"sales@doctorskin.net"});
            it.putExtra(Intent.EXTRA_SUBJECT,"");
            it.putExtra(Intent.EXTRA_TEXT,"");
            it.setType("message/rfc822");
            startActivity(Intent.createChooser(it,"Choose Mail App"));
            /*
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sales@doctorskin.net"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            getActivity().startActivity(Intent.createChooser(intent, ""));*/
        }
    }

    private void AddTocart() {

        String loginflag = Login_preference.getLogin_flag(getActivity());
        Log.e("customeriddd", "" + Login_preference.getcustomer_id(getActivity()));
        if (loginflag.equalsIgnoreCase("1") || loginflag == "1") {
            Log.e("Qyt_000", "" + tv_cart_quantity_total.getText().toString());
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            addtocart = api.addtocart(proddd_id, Login_preference.getcustomer_id(getActivity()), tv_cart_quantity_total.getText().toString());
            Log.e("proddd_idddd", "" + proddd_id);

        } else {
            String quote_id = Login_preference.getquote_id(getActivity());
            Log.e("logout_431", "" + quote_id);

            if (quote_id.equalsIgnoreCase("") || quote_id == "null") {
                Log.e("without_quote_login", "");
                Log.e("Qyt_111", "" + tv_cart_quantity_total.getText().toString());
                ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
                addtocart = api.withoutlogin_quote_addtocart(proddd_id, tv_cart_quantity_total.getText().toString());
                Log.e("proddd_id__437", "" + proddd_id);

            } else {
                Log.e("without_login_withquote", "");
                ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
                Log.e("with_pass_quote_id_442", "" + Login_preference.getquote_id(getActivity()));
                Log.e("Qyt_442", "" + tv_cart_quantity_total.getText().toString());
                addtocart = api.withoutlogin_addtocart(proddd_id, Login_preference.getquote_id(getActivity()), tv_cart_quantity_total.getText().toString());
            }
        }

        addtocart.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e("", "" + response.body().toString());
                Log.e("response",""+response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    String meassg = jsonObject.getString("message");
                    Log.e("message", "" + meassg);
                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                        Log.e("quote_iddddd", "" + jsonObject.getString("quote_id"));
                        Login_preference.setquote_id(getActivity(), jsonObject.getString("quote_id"));
                        Login_preference.setiteamqty(getActivity(), jsonObject.getString("items_qty"));
                        Login_preference.setCart_item_count(getActivity(), jsonObject.getString("items_qty"));
                        cart_count.setText(Login_preference.getCart_item_count(getActivity()));
                        String item_count = jsonObject.getString("items_qty");
                        Log.e("item_count",""+item_count);
                        if (item_count != null && !item_count.isEmpty() && !item_count.equals("null")) {
                            Bottom_navigation.tv_bottomcount.setText(jsonObject.getString("items_qty"));
                            Bottom_navigation.item_count.setText(jsonObject.getString("items_qty"));
                            count = jsonObject.getString("items_qty");
                            if (count.equalsIgnoreCase("null") || count.equals("")) {
                                Log.e("count_40", "" + jsonObject.getString("items_qty"));
                                cart_count.setText("0");
                            } else {
                                Log.e("count_80", "" + jsonObject.getString("items_qty"));
                                cart_count.setText(jsonObject.getString("items_qty"));
                            }
                        } else {
                            Bottom_navigation.tv_bottomcount.setText("0");
                            Bottom_navigation.item_count.setText("0");
                            count = "0";
                            if (count.equalsIgnoreCase("null") || count.equals("")) {
                                cart_count.setText("0");
                            } else {
                                cart_count.setText(count);
                            }
                        }

                    } else if (status.equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("exception", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle item selection
        switch (item.getItemId()) {

            case android.R.id.home:
// this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
// if this doesn't work as desired, another possibility is to call `finish()` here.
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
