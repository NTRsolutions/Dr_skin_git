package com.sismatix.drskin.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Fragment.Home;
import com.sismatix.drskin.Fragment.MyCart;
import com.sismatix.drskin.Fragment.Wishlist;
import com.sismatix.drskin.Model.Wishlist_Model;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.ApiClient;
import com.sismatix.drskin.Retrofit.ApiInterface;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Wishlist_Adapter extends RecyclerView.Adapter<Wishlist_Adapter.MyViewHolder> {
    private Context context;
    private List<Wishlist_Model> model;
    final AppCompatActivity activity = (AppCompatActivity) context;
    String action = "remove", proddd_id;
    Fragment myFragment;
    Call<ResponseBody> addtocart = null;

    public Wishlist_Adapter(Context context, List<Wishlist_Model> wishlist_model) {
        this.context = context;
        this.model = wishlist_model;
    }

    @Override
    public Wishlist_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wishlist_row, parent, false);

        return new Wishlist_Adapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final Wishlist_Adapter.MyViewHolder holder, final int position) {
        final Wishlist_Model wishlist_model = model.get(position);


        proddd_id = wishlist_model.getProduct_id();
        //holder.tv_wishlist_haircare.setText(wishlist_model.getProduct_category());
        //Bottom_navigation.Check_String_NULL_Value(holder.tv_wishlist_haircare,wishlist_model.getProduct_category());
        holder.tv_wishlist_product_name.setText(wishlist_model.getProduct_name());
        holder.tv_wishlist_product_name.setTypeface(Home.opensans_bold);
        holder.tv_titlests.setTypeface(Home.opensans_bold);
        holder.tv_status_show.setTypeface(Home.opensans_regular);
        Bottom_navigation.Check_String_NULL_Value(holder.tv_wishlist_product_name,wishlist_model.getProduct_name());
        holder.tv_wishlist_product_price.setText(wishlist_model.getProduct_price());
        holder.tv_wishlist_product_price.setTypeface(Home.opensans_regular);
        holder.tv_wishlist_order_now.setTypeface(Home.opensans_bold);
        Bottom_navigation.Check_String_NULL_Value(holder.tv_wishlist_product_price,wishlist_model.getProduct_price());
        Glide.with(context).load(wishlist_model.getProduct_image()).into(holder.iv_wishlist_image);

        final String wishliist_product_id = wishlist_model.getProduct_id();
        final String wishlist_customer_id = Login_preference.getcustomer_id(context);
        holder.tv_wishlist_order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtoCart(view);
            }
        });
        holder.iv_wishlist_image_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                remove_wishlist(action, wishliist_product_id, wishlist_customer_id);

            }
        });

        holder.lv_wishlist_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                      //  Toast.makeText(context, wishlist_model.getProduct_id(), Toast.LENGTH_SHORT).show();
                    }
                }, 1000);

            }
        });
    }
    private void addtoCart(final View v) {
        String removewishlist="1";
        String loginflag = Login_preference.getLogin_flag(context);
        Log.e("ada_cid", "" + Login_preference.getcustomer_id(context));
        if (loginflag.equalsIgnoreCase("1") || loginflag == "1") {
            Log.e("ada_withlogin", "");
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            addtocart = api.addtocart_wish(proddd_id, Login_preference.getcustomer_id(context),removewishlist);
            Log.e("pass_apidata", "" + removewishlist);

        } else {
            String quote_id = Login_preference.getquote_id(context);
            if (quote_id.equalsIgnoreCase("") || quote_id == "null") {
                Log.e("ada_without_quote", "");
                ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
                addtocart = api.withoutlg_quote_addtocart_wish(proddd_id,removewishlist);
                Log.e("proddd_id_witoutquote", "" + proddd_id);

            }/* else {
                Log.e("without_login_withquote", "");
                ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
                Log.e("ada_withquote", "" + Login_preference.getquote_id(context));
                addtocart = api.withoutlogin_addtocart_wishlist(proddd_id, Login_preference.getquote_id(context),"1");
            }*/
        }

        addtocart.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("", "" + response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    String meassg = jsonObject.getString("message");
                    Log.e("ada_message", "" + meassg);
                    if (status.equalsIgnoreCase("Success")) {
                        Toast.makeText(context, "" + meassg, Toast.LENGTH_SHORT).show();
                        Login_preference.setquote_id(context, jsonObject.getString("quote_id"));
                        Login_preference.setiteamqty(context, jsonObject.getString("items_qty"));
                        Login_preference.setCart_item_count(context,jsonObject.getString("items_count"));
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Fragment myFragment = new MyCart();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.rootLayout, myFragment).addToBackStack(null).commit();

                    } else if (status.equalsIgnoreCase("error")) {
                        //  Toast.makeText(context, "" + meassg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("ada_exception", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void remove_wishlist(String action, String wishliist_product_id, String wishlist_customer_id) {

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> removeWishlist = api.Wishlistactions(action, wishliist_product_id, wishlist_customer_id);
        removeWishlist.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
// Log.e("remove_wishlist_res", "" + response.body().toString());

//progressBar_item.setVisibility(View.GONE);
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_wishlist_remove", "" + status);

                    if (status.equalsIgnoreCase("Success")) {
                        /* Cart.prepare_Cart();*/
                        String msg = jsonObject.getString("message");
                        // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                        Wishlist.CALL_WISHLIST_API();

                    } else {
                        Toast.makeText(context, "there are some problem(s).", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                } finally {
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return model.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_wishlist_product_price, tv_wishlist_product_name, tv_wishlist_haircare, tv_wishlist_order_now,tv_titlests,tv_status_show;
        LinearLayout lv_wishlist_click;
        ImageView iv_wishlist_image, iv_wishlist_image_remove;

        public MyViewHolder(View view) {
            super(view);
            tv_wishlist_product_price = (TextView) view.findViewById(R.id.tv_wishlist_product_price);
            tv_wishlist_product_name = (TextView) view.findViewById(R.id.tv_wishlist_product_name);
            //tv_wishlist_haircare = (TextView) view.findViewById(R.id.tv_wishlist_haircare);
            tv_wishlist_order_now = (TextView) view.findViewById(R.id.tv_wishlist_order_now);
            tv_status_show = (TextView) view.findViewById(R.id.tv_status_show);
            tv_titlests = (TextView) view.findViewById(R.id.tv_titlests);
            lv_wishlist_click = (LinearLayout) view.findViewById(R.id.lv_wishlist_click);
            iv_wishlist_image = (ImageView) view.findViewById(R.id.iv_wishlist_image);
            iv_wishlist_image_remove = (ImageView) view.findViewById(R.id.iv_wishlist_image_remove);
        }
    }
}


