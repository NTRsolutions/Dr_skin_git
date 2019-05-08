package com.sismatix.drskin.Fragment;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sismatix.drskin.Activity.Chat_messge;
import com.sismatix.drskin.Activity.CirclePagerIndicatorDecoration;
import com.sismatix.drskin.Adapter.Product_recycler_adapter;
import com.sismatix.drskin.Adapter.SlidingVideoAdapterMain;
import com.sismatix.drskin.Model.Cuntrylist_model;
import com.sismatix.drskin.Model.Product_Grid_Model;
import com.sismatix.drskin.Model.sliderimage_model;
import com.sismatix.drskin.Model.slidervideo_model;
import com.sismatix.drskin.Preference.CheckNetwork;
import com.sismatix.drskin.Preference.Login_preference;
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
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class Shop extends Fragment implements View.OnClickListener {

    RecyclerView recycler_product;
    private List<Product_Grid_Model> product_model = new ArrayList<Product_Grid_Model>();
    private Product_recycler_adapter product_adapter;

    RecyclerView recycler_shop_videos;
    private List<slidervideo_model> slidervideo_models = new ArrayList<slidervideo_model>();
    SlidingVideoAdapterMain slidingVideoAdapterMain;

    ImageView iv_search,iv_chat;
    LinearLayout lv_productnotavelable;
    String vlaue,name;
    TextView tv_title;
    public static Button cart_count;
    ProgressBar progressBar;
    LinearLayout lv_productnotfound,l_cartshow;
    RelativeLayout rl_image;

    public Shop() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shop, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            vlaue = bundle.getString("vlaue");
            name = bundle.getString("name");
            Log.e("category_id_thcf", "" + vlaue);
            /// product_array =bundle.getString("products_array");
            Log.e("products_arrayyyy", "" + name);
        }
        Allocatememory(v);
        tv_title.setTypeface(Home.opensans_bold);
       Log.e("cart_total",""+Login_preference.getCart_item_count(getActivity()));
       String cart_countt=Login_preference.getCart_item_count(getActivity());
       if (cart_countt.equalsIgnoreCase("")|| cart_countt == "null"){
          cart_count.setText("0");
       }else {
           cart_count.setText(Login_preference.getCart_item_count(getActivity()));
       }
        product_adapter = new Product_recycler_adapter(getActivity(), product_model);
        recycler_product.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_product.setItemAnimator(new DefaultItemAnimator());
        recycler_product.setAdapter(product_adapter);
        iv_search.setOnClickListener(this);
        l_cartshow.setOnClickListener(this);
        iv_chat.setOnClickListener(this);
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            callgetVideoApi(vlaue);
            CALL_PRODUCT_API(vlaue);
        } else {
            Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    private void callgetVideoApi(String vlaue) {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> addcategory = api.addcategoryprod(vlaue);
        addcategory.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_video", "" + response.body().toString());
                progressBar.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_video", "" + status);
                    String title = jsonObject.getString("title");
                    // collapsingToolbar.setTitle(title);

                    String categoryimage = jsonObject.getString("categoryimage");
                    Log.e("categoryimage_video", "" + categoryimage);

                    String shopvideo = jsonObject.getString("video");
                    Log.e("shopvideo",""+shopvideo);

                    String count_video = jsonObject.getString("count");
                    Log.e("count_vids",""+count_video);

                    if (status.equalsIgnoreCase("Success")) {

                        if (shopvideo.equalsIgnoreCase("")||shopvideo.equalsIgnoreCase(null)||shopvideo.equalsIgnoreCase("null")){

                            rl_image.setVisibility(View.VISIBLE);
                            recycler_shop_videos.setVisibility(View.GONE);

                            rl_image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getContext(), "No video Available", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else {

                            rl_image.setVisibility(View.GONE);
                            recycler_shop_videos.setVisibility(View.VISIBLE);

                            slidervideo_model schedule = new slidervideo_model(jsonObject.getString("video"));
                            slidervideo_models.add(schedule);

                        }

                    } else if (status.equalsIgnoreCase("error")) {
                        // Toast.makeText(getContext(), ""+meassg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }finally {
                    slidingVideoAdapterMain.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Allocatememory(View v) {
        recycler_product = (RecyclerView) v.findViewById(R.id.recycler_product);
        iv_search = (ImageView) v.findViewById(R.id.iv_search);
        iv_chat = (ImageView) v.findViewById(R.id.iv_chat);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBar);
        cart_count=(Button) v.findViewById(R.id.cart_count);
        l_cartshow=(LinearLayout) v.findViewById(R.id.l_cartshow);
        tv_title=(TextView) v.findViewById(R.id.tv_title);
        lv_productnotavelable=(LinearLayout) v.findViewById(R.id.lv_productnotavelable);

        rl_image = (RelativeLayout)v.findViewById(R.id.rl_image);
        recycler_shop_videos = (RecyclerView) v.findViewById(R.id.recycler_shop_videos);

        slidingVideoAdapterMain = new SlidingVideoAdapterMain(getActivity(), slidervideo_models);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_shop_videos.setLayoutManager(mLayoutManager1);
        recycler_shop_videos.setAdapter(slidingVideoAdapterMain);
        Log.e("sizee", "" + slidervideo_models.size());
    }

    private void CALL_PRODUCT_API(String cat_id) {
        Log.e("cartididid",""+cat_id);
       progressBar.setVisibility(View.VISIBLE);
        product_model.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> addcategory = api.addcategoryprod(cat_id);
        addcategory.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                progressBar.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status", "" + status);
                    String title = jsonObject.getString("title");
                   // collapsingToolbar.setTitle(title);
                    String categoryimage = jsonObject.getString("categoryimage");
                    Log.e("categoryimage", "" + categoryimage);


                    if (status.equalsIgnoreCase("Success")) {
                        String products = jsonObject.getString("products");
                        Log.e("prod_list_cart", "" + products);
                        if (products.equalsIgnoreCase("[]") || products.equalsIgnoreCase("")) {
                            lv_productnotavelable.setVisibility(View.VISIBLE);
                        } else {
                            lv_productnotavelable.setVisibility(View.GONE);
                            JSONArray jsonArray = new JSONArray(products);
                            //Log.e("arrprod", "" + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject vac_object = jsonArray.getJSONObject(i);
                                    Log.e("prod_name", "" + vac_object.getString("product_name"));
                                    product_model.add(new Product_Grid_Model(vac_object.getString("product_image"),
                                            vac_object.getString("product_price"), vac_object.getString("product_name"),
                                            vac_object.getString("type"), vac_object.getString("product_id"), "product_specialprice"));

                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                    product_adapter.notifyItemChanged(i);
                                }

                            }
                        }

                    } else if (status.equalsIgnoreCase("error")) {
                        // Toast.makeText(getContext(), ""+meassg, Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cart:
                Toast.makeText(getActivity(), "cart Icon Click", Toast.LENGTH_SHORT).show();
                /*Fragment myFragment = new Cart();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.rootLayout, myFragment).addToBackStack(null).commit();
             */
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == iv_search){
            loadFragment(new Search());
        }else if (view == l_cartshow){
            loadFragment(new MyCart());
        }else if (view == iv_chat){
            loadFragment(new Live_withdr());
        }
    }

    public void loadFragment(Fragment fragment) {
        Log.e("clickone", "");
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in,
                0, 0, R.anim.fade_out);
        transaction.replace(R.id.rootLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}
