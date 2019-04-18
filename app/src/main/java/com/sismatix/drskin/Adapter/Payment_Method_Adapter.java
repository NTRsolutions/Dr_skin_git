package com.sismatix.drskin.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.drskin.Fragment.Home;
import com.sismatix.drskin.Model.Payment_Method_Model;
import com.sismatix.drskin.R;

import java.util.List;

public class Payment_Method_Adapter  extends RecyclerView.Adapter<Payment_Method_Adapter.MyViewHolder> {
    private Context context;
    private List<Payment_Method_Model> model;
    public static String paymentcode_ada;
    private int lastSelectedPosition = -1;

    public Payment_Method_Adapter (Context context, List<Payment_Method_Model> cartList) {
        this.context = context;
        this.model = cartList;
    }
    @Override
    public Payment_Method_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_method_row, parent, false);
        return new Payment_Method_Adapter.MyViewHolder(itemView);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final Payment_Method_Adapter.MyViewHolder holder, final int position) {
        final Payment_Method_Model payment_model = model.get(position);

        holder.rad_payment.setText(payment_model.getLabel());

        holder.rad_payment.setChecked(lastSelectedPosition == position);
        holder.rad_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = position;
                paymentcode_ada = payment_model.getValue();
                Log.e("paymentcode_111",""+paymentcode_ada);
                notifyDataSetChanged();
            }
        });
        if (lastSelectedPosition == position) {
            Log.e("selectedpo_76",""+lastSelectedPosition);
            holder.rad_payment.setChecked(true);
        } else {
            holder.rad_payment.setChecked(false);
        }
    }
    @Override
    public int getItemCount() {
        return model.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_payment_name;
        RadioButton rad_payment;
        RadioGroup radioGroup;
        LinearLayout lv_greylayout_click, lv_greenlayout;
        public MyViewHolder(View view) {
            super(view);
            rad_payment = (RadioButton)view.findViewById(R.id.rad_payment);
        }
    }
}