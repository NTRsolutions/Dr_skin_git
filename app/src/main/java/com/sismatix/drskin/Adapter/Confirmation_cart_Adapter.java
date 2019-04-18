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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Fragment.MyCart;
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

public class Confirmation_cart_Adapter extends RecyclerView.Adapter<Confirmation_cart_Adapter.MyViewHolder> {
    private Context context;
    private List<Cart_Model> cartList;
    String loginflag;

    public Confirmation_cart_Adapter(Context context, List<Cart_Model> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public Confirmation_cart_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.confirmation_cartlist_row, parent, false);

        loginflag= Login_preference.getLogin_flag(context);
        return new Confirmation_cart_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Confirmation_cart_Adapter.MyViewHolder holder, final int position) {
        final Cart_Model cart_model = cartList.get(position);
        holder.tv_productname.setText(cart_model.getProduct_name());
        holder.tv_productprice.setText(cart_model.getProduct_price());
        holder.tv_cart_quantity_total.setText(cart_model.getProduct_qty());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.app_logo);
        requestOptions.error(R.drawable.app_logo);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(cart_model.getProduct_image()).into(holder.iv_product);
    }
    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProgressBar cart_count_pb;
        ImageView iv_product;
        TextView tv_productname,tv_productprice,tv_cart_quantity_total;

        public MyViewHolder(View view) {
            super(view);
            iv_product = (ImageView)view.findViewById(R.id.iv_product);
            cart_count_pb = (ProgressBar) view.findViewById(R.id.cart_count_pb);
            tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            tv_productprice = (TextView) view.findViewById(R.id.tv_productprice);
            tv_cart_quantity_total = (TextView) view.findViewById(R.id.tv_cart_quantity_total);
        }
    }
}

