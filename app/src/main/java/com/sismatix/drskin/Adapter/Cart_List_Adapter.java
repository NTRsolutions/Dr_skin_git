package com.sismatix.drskin.Adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Fragment.Home;
import com.sismatix.drskin.Fragment.MyCart;
import com.sismatix.drskin.Fragment.Product_Details;
import com.sismatix.drskin.Fragment.SignIn;
import com.sismatix.drskin.Model.Cart_Model;
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

import static com.sismatix.drskin.Fragment.MyCart.cart_adapter;

public class Cart_List_Adapter extends RecyclerView.Adapter<Cart_List_Adapter.MyViewHolder> {
    private Context context;
    private List<Cart_Model> cartList;
    int quantity;
    int current_price = 30;
    String loginflag;
    int product_total = current_price;
    Call<ResponseBody> remove_from_cart = null;
    String itemid_cart, quoteid_cart, item_qty,iswhishlisted;
    String product_id;

    public static String cart_item_grand_total;

    public Cart_List_Adapter(Context context, List<Cart_Model> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item_row, parent, false);

        loginflag=Login_preference.getLogin_flag(context);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Cart_Model cart_model = cartList.get(position);
       /* holder.tv_cart_product_title.setTypeface(Home.roboto_bold);
        holder.tv_cart_product_description.setTypeface(Home.roboto_light);
        holder.tv_product_price_total.setTypeface(Home.roboto_black);
*/
        item_qty = cart_model.getProduct_qty();
        Log.e("item_qty_81", "" + item_qty);

        Bottom_navigation.Check_String_NULL_Value(holder.tv_productname, cart_model.getProduct_name());
        Bottom_navigation.Check_String_NULL_Value(holder.tv_productprice, cart_model.getProduct_price());
        Bottom_navigation.Check_String_NULL_Value(holder.tv_product_desc, cart_model.getProduct_description());
        Bottom_navigation.Check_String_NULL_Value(holder.tv_cart_quantity_total, cart_model.getProduct_qty());
        holder.tv_productname.setTypeface(Home.opensans_bold);
        holder.iv_skuu.setTypeface(Home.opensans_bold);
        holder.tv_product_desc.setTypeface(Home.opensans_regular);
        holder.wishlisttitle.setTypeface(Home.opensans_bold);
        holder.tv_productprice.setTypeface(Home.opensans_bold);
        //  holder.tv_cart_product_title.setText(cart_model.getProduct_name());
        //holder.tv_product_price_total.setText(cart_model.getProduct_price());
        //holder.tv_cart_product_description.setText(cart_model.getProduct_description());
        //holder.tv_cart_quantity_total.setText(cart_model.getProduct_qty());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.app_logo);
        requestOptions.error(R.drawable.app_logo);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(cart_model.getProduct_image()).into(holder.iv_product);
        holder.cart_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cart_id=cart_model.getProduct_id();
                Log.e("cartid_onlick",""+cart_id);
                CALL_REMOVE_FROM_CART_API(cart_id,position);
            }
        });
///wishlist
        iswhishlisted=cart_model.getWishlist();
        Log.e("wishlisttttt",""+iswhishlisted);
        if (iswhishlisted.equals("yes")) {
            holder.iv_add_wish1.setVisibility(View.VISIBLE);
            holder.iv_add_wish.setVisibility(View.GONE);
        } else {
            holder.iv_add_wish.setVisibility(View.VISIBLE);
            holder.iv_add_wish1.setVisibility(View.GONE);
        }
        holder.iv_add_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add from wishlist
                if (loginflag.equalsIgnoreCase("1") || loginflag == "1") {
                    // Remove_FROM_WISHLIST_API();
                    String action = "add";
                    ADD_TO_WISHLIST_API(action,cart_model.getProduct_id(),holder);
                } else {
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        Fragment myFragment = new SignIn();
                       // myFragment.setArguments(b);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.rootLayout, myFragment).addToBackStack(null).commit();
                    //loadFragment(new EmailLogin());
                }
            }
        });
        holder.iv_add_wish1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add from wishlist
                if (loginflag.equalsIgnoreCase("1") || loginflag == "1") {
                    // Remove_FROM_WISHLIST_API();
                    String action = "remove";
                    Remove_FROM_WISHLIST_API(action,cart_model.getProduct_id(),holder);
                } else {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new SignIn();
                    // myFragment.setArguments(b);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.rootLayout, myFragment).addToBackStack(null).commit();
                    //loadFragment(new EmailLogin());
                }
            }
        });

       // Log.e("quoteid_cart", "" + Login_preference.getquote_id(context));

        holder.iv_cart_quantity_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.iv_cart_quantity_increase.setEnabled(false);

                int textqut = Integer.parseInt(holder.tv_cart_quantity_total.getText().toString());
                //quantity = textqut + 1;
                int Result = textqut + 1;
                itemid_cart = cart_model.getItemid();
                quoteid_cart = Login_preference.getquote_id(context);
                Log.e("result",""+Result);
                holder.tv_cart_quantity_total.setText(String.valueOf(Result));
                callAppUpdateCart(Result, itemid_cart, quoteid_cart, view, holder);
            }

        });

        holder.iv_cart_quantity_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.iv_cart_quantity_decrease.setEnabled(false);
                int textqut = Integer.parseInt(holder.tv_cart_quantity_total.getText().toString());
                itemid_cart = cart_model.getItemid();
                quoteid_cart = Login_preference.getquote_id(context);
                if (textqut != 0) {
                    int Result = textqut - 1;
                    if (Result == 0) {
                        Result = 1;
                        Log.e("result",""+Result);
                        holder.tv_cart_quantity_total.setText(String.valueOf(Result));
                        callAppUpdateCart(Result, itemid_cart, quoteid_cart, view, holder);
                        // product_total = product_total - current_price;
                    } else {
                        Log.e("result",""+Result);
                        holder.tv_cart_quantity_total.setText(String.valueOf(Result));
                        callAppUpdateCart(Result, itemid_cart, quoteid_cart, view, holder);
                    }
                } else {
                }
            }
        });

    }
    private void Remove_FROM_WISHLIST_API(String action, String product_id, final MyViewHolder holder) {
        Log.e("remove_proddd_id_wish", "" + product_id);
        Log.e("remove_customer_id_wish", "" + Login_preference.getcustomer_id(context));
        Log.e("action", "" + action);
        //makin g api call
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> remove_from_wishlist = api.add_to_wishlist(product_id, Login_preference.getcustomer_id(context), action);

        remove_from_wishlist.enqueue(new Callback<ResponseBody>() {
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
                    if (status.equalsIgnoreCase("Success")) {
                       holder.iv_add_wish.setVisibility(View.VISIBLE);
                       holder.iv_add_wish1.setVisibility(View.GONE);
                        Toast.makeText(context, "" + meassg, Toast.LENGTH_SHORT).show();
                    } else if (status.equalsIgnoreCase("error")) {
                        Toast.makeText(context, "" + meassg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ADD_TO_WISHLIST_API(String action, String proddd_id, final MyViewHolder holder) {

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> add_to_wishlist = api.add_to_wishlist(proddd_id, Login_preference.getcustomer_id(context), action);
        add_to_wishlist.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_wish", "" + response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    String message = null;
                    if (status.equalsIgnoreCase("success")) {
                        message = jsonObject.getString("message");
                        Log.e("message", "" + message);

                        holder.iv_add_wish1.setVisibility(View.VISIBLE);
                        holder.iv_add_wish.setVisibility(View.GONE);

                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        //Login_preference.setiteamqty(getActivity(),jsonObject.getString("items_qty"));
                        //loadFragment(new Cart());

                    } else if (status.equalsIgnoreCase("error")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("exception", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callAppUpdateCart(final int Resultt, String itemid_carttt, String quoteid_carttt, final View view, final MyViewHolder holder) {
        //holder.cart_count_pb.setVisibility(View.VISIBLE);

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> appupdate = api.appUpdatecart(quoteid_carttt, String.valueOf(Resultt), itemid_carttt);
        appupdate.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("responseeeeee", "" + response.body().toString());

                JSONObject jsonObject = null;
                try {
                  //  holder.cart_count_pb.setVisibility(View.GONE);
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_prepare_cart", "" + status);
                    Log.e("jsonshow", "" + jsonObject);
                    if (status.equalsIgnoreCase("success")) {

                        holder.iv_cart_quantity_increase.setEnabled(true);
                        holder.iv_cart_quantity_decrease.setEnabled(true);

                        Log.e("grand_total",""+jsonObject.getString("grand_total"));
                        MyCart.tv_maintotal.setText(jsonObject.getString("grand_total"));
                        MyCart.grand_total = jsonObject.getString("grand_total");
                        MyCart.subtotal = jsonObject.getString("subtotal");
                        //prepare_Cart();
                        Login_preference.setCart_item_count(context, jsonObject.getString("items_qty"));
                        if (jsonObject.getString("items_qty").equalsIgnoreCase("null") || jsonObject.getString("items_qty").equals("")) {

                            Bottom_navigation.tv_bottomcount.setText("0");
                            Bottom_navigation.item_count.setText("0");

                        } else {

                            Bottom_navigation.tv_bottomcount.setText(jsonObject.getString("items_qty"));
                            Bottom_navigation.item_count.setText(jsonObject.getString("items_qty"));

                        }


                        //holder.tv_cart_quantity_total.setText(item_qty);

                    } else if (status.equalsIgnoreCase("error")) {

                    }

                } catch (Exception e) {
                    Log.e("exception", "" + e);
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
        return cartList.size();
    }


    public void CALL_REMOVE_FROM_CART_API(String proddd_id, final int position) {
        String loginflag = Login_preference.getLogin_flag(context);
        if (loginflag.equalsIgnoreCase("1") || loginflag == "1") {
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            remove_from_cart = api.remove_from_cartlist(proddd_id, Login_preference.getemail(context));
            Log.e("proddd_idddd", "" + proddd_id);
        } else {
            String quote_id = Login_preference.getquote_id(context);
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            remove_from_cart = api.withoutlogin_remove_from_cartlist(proddd_id, quote_id);
            Log.e("proddd_idddd", "" + proddd_id);
        }
        remove_from_cart.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("position_show",""+position);
                cartList.remove(position);
                //notifyItemRemoved(position);
                cart_adapter.notifyDataSetChanged();
                Log.e("responjse_remove_cart", "" + response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_remove", "" + status);
                    if (status.equalsIgnoreCase("Success")) {
                        cart_item_grand_total = jsonObject.getString("grand_total");
                        MyCart.tv_maintotal.setText(cart_item_grand_total);
                        MyCart.grand_total = jsonObject.getString("grand_total");
                        MyCart.subtotal = jsonObject.getString("subtotal");
                        Bottom_navigation.Check_String_NULL_Value(MyCart.tv_maintotal, cart_item_grand_total);
                        MyCart.cart_items_count = jsonObject.getString("items_qty");
                        Login_preference.setCart_item_count(context, MyCart.cart_items_count);
                        String item_count=jsonObject.getString("items_qty");

                        if (item_count != null && !item_count.isEmpty() && !item_count.equals("null")){
                            Bottom_navigation.tv_bottomcount.setText(jsonObject.getString("items_qty"));
                            Bottom_navigation.item_count.setText(jsonObject.getString("items_qty"));
                            Product_Details.count = jsonObject.getString("items_qty");
                            if (Product_Details.count.equalsIgnoreCase("null") || Product_Details.count.equals("")) {
                                Log.e("count_40", "" + jsonObject.getString("items_qty"));
                                Product_Details.cart_count.setText("0");
                            } else {
                                Log.e("count_80", "" + jsonObject.getString("items_qty"));
                                Product_Details.cart_count.setText(jsonObject.getString("items_qty"));
                            }
                        }else {
                            Bottom_navigation.tv_bottomcount.setText("0");
                            Bottom_navigation.item_count.setText("0");
                            Product_Details.count = "0";
                            if (Product_Details.count.equalsIgnoreCase("null") || Product_Details.count.equals("")) {
                                Product_Details.cart_count.setText("0");
                            } else {
                                Product_Details.cart_count.setText(Product_Details.count);
                            }
                        }
                        Toast.makeText(context, "" + status, Toast.LENGTH_SHORT).show();
                        // prepare_Cart();
                    } else if (status.equalsIgnoreCase("error")) {
                        // Toast.makeText(context, ""+meassg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("exception", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProgressBar cart_count_pb;

        ImageView iv_product,iv_add_wish,iv_add_wish1,cart_close,iv_cart_quantity_increase,iv_cart_quantity_decrease;
        TextView tv_productname,tv_product_desc,tv_productprice,tv_cart_quantity_total,iv_skuu,wishlisttitle;

        public MyViewHolder(View view) {
            super(view);

            iv_product = (ImageView)view.findViewById(R.id.iv_product);
            iv_add_wish = (ImageView)view.findViewById(R.id.iv_add_wish);
            iv_add_wish1 = (ImageView)view.findViewById(R.id.iv_add_wish1);
            cart_close = (ImageView)view.findViewById(R.id.cart_close);
            iv_cart_quantity_increase = (ImageView)view.findViewById(R.id.iv_cart_quantity_increase);
            iv_cart_quantity_decrease = (ImageView)view.findViewById(R.id.iv_cart_quantity_decrease);
            cart_count_pb = (ProgressBar) view.findViewById(R.id.cart_count_pb);

            tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            tv_product_desc = (TextView) view.findViewById(R.id.tv_product_desc);
            tv_productprice = (TextView) view.findViewById(R.id.tv_productprice);
            tv_cart_quantity_total = (TextView) view.findViewById(R.id.tv_cart_quantity_total);
            iv_skuu = (TextView) view.findViewById(R.id.iv_skuu);
            wishlisttitle = (TextView) view.findViewById(R.id.wishlisttitle);
        }
    }
}

