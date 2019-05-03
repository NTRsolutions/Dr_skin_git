package com.sismatix.drskin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Activity.Order_summery_activty;
import com.sismatix.drskin.Activity.Paymentscreen;
import com.sismatix.drskin.Fragment.Home;
import com.sismatix.drskin.Model.My_order_model;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.ApiClient;
import com.sismatix.drskin.Retrofit.ApiInterface;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class My_orderlist_Adapter extends RecyclerView.Adapter<My_orderlist_Adapter.MyViewHolder> {
    Context context;
    private List<My_order_model> myorderModels;
    String orderidd, custidd;

    public My_orderlist_Adapter(FragmentActivity context, List<My_order_model> myorderModels) {
        this.context = context;
        this.myorderModels = myorderModels;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_myorders, viewGroup, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final My_order_model my_order_model = myorderModels.get(position);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(my_order_model.getCreated_at());
        String strDate = sdf.format(c.getTime());
        String[] parts = strDate.split(" "); //CREATED_DATE mins pass string date and time
        String datee = parts[0];
        holder.tv_orderidtitle.setTypeface(Home.opensans_bold);
        holder.tv_order_id.setTypeface(Home.opensans_bold);
        holder.tv_created_date.setTypeface(Home.opensans_regular);
        holder.tv_created_date.setText(datee);
        holder.tv_order_id.setText(my_order_model.getIncrement_id());
        holder.tv_order_status.setTypeface(Home.opensans_bold);
        Log.e("my_orderStatus",""+my_order_model.getStatus());
        if (my_order_model.getStatus().equalsIgnoreCase("pending")){
            holder.tv_order_status.setTextColor(Color.BLACK);
        }else  if (my_order_model.getStatus().equalsIgnoreCase("Successful")){
            holder.tv_order_status.setTextColor(Color.RED);
        }
        holder.tv_order_status.setText(my_order_model.getStatus());
        holder.lv_onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("order", "" + my_order_model.getOrder_id());
                Intent intent=new Intent(context,Order_summery_activty.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.lv_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callReorderapi();
            }
        });

        orderidd = my_order_model.getOrder_id();
        Log.e("orderidddd", "" + orderidd);
        custidd = Login_preference.getcustomer_id(context);
        Log.e("custidddd", "" + custidd);
    }
    private void callReorderapi() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> reorder = api.AppReorder(custidd, orderidd);
        reorder.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("responseeeeee_wishlist", "" + response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String message = jsonObject.getString("message");
                    Log.e("message_reorder", "" + message);
                    String status = jsonObject.getString("status");
                    Log.e("status_wishlist", "" + status);
                    if (status.equalsIgnoreCase("Success")) {
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                        String count = jsonObject.getString("items_count");
                        Login_preference.setCart_item_count(context, jsonObject.getString("items_count"));
                        if (count != null && !count.isEmpty() && !count.equals("null")){
                            Bottom_navigation.tv_bottomcount.setText(jsonObject.getString("items_count"));
                            Bottom_navigation.item_count.setText(jsonObject.getString("items_count"));
                        } else {
                            Bottom_navigation.tv_bottomcount.setText("0");
                            Bottom_navigation.item_count.setText("0");
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return myorderModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_created_date, tv_order_id,tv_order_status, tv_name, tv_paymentmethod, grand_total, tv_wishlist_order_now, tv_wishlist_haircare,
                tv_orderidtitle, tv_rec, tv_pm, tv_tot_total;
        LinearLayout lv_reorder,lv_onclick;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_order_id = (TextView) view.findViewById(R.id.tv_order_id);
            tv_created_date = (TextView) view.findViewById(R.id.tv_created_date);
            tv_order_status = (TextView) view.findViewById(R.id.tv_order_status);
            tv_orderidtitle = (TextView) view.findViewById(R.id.tv_orderidtitle);
            lv_reorder = (LinearLayout) view.findViewById(R.id.lv_reorder);
            lv_onclick = (LinearLayout) view.findViewById(R.id.lv_onclick);
        }
    }
}
