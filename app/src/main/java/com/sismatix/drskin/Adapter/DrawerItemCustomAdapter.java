package com.sismatix.drskin.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.drskin.Activity.Bottom_navigation;
import com.sismatix.drskin.Fragment.Cutomer_agereement;
import com.sismatix.drskin.Fragment.Home;
import com.sismatix.drskin.Fragment.MyAccount_withlogin;
import com.sismatix.drskin.Fragment.MyCart;
import com.sismatix.drskin.Fragment.MyOrders;
import com.sismatix.drskin.Fragment.SignIn;
import com.sismatix.drskin.Model.DataModel;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.R;

import org.jetbrains.annotations.TestOnly;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class DrawerItemCustomAdapter extends ArrayAdapter<DataModel> {

    Context mContext;
    int layoutResourceId;
    DataModel data[] = null;
    int selected=-1;
    String loginflagmain;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, DataModel[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        loginflagmain= Login_preference.getLogin_flag(mContext);

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        final TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);
        final RelativeLayout relative_main = (RelativeLayout) listItem.findViewById(R.id.relative_main);

        DataModel folder = data[position];


      //  imageViewIcon.setImageResource(folder.icon);
        textViewName.setTypeface(Home.opensans_bold);
        textViewName.setText(folder.getName());


        relative_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selected=position;
                notifyDataSetChanged();
                Fragment fragment = null;

                switch (position) {
                    case 0:
                        // pushFragment(new Home(),"home");
                        fragment = new Home();
                        break;
                    case 1:
                        fragment = new MyAccount_withlogin();
                       // pushFragment(new MyAccount_withlogin(),"My Account");

                        //  pushFragment(new Home(),"home");
                        break;
                    case 2:
                        if (loginflagmain.equalsIgnoreCase("1") || loginflagmain == "1") {
                            fragment = new MyOrders();
                        } else {
                            fragment = new SignIn();
                        }
                        //fragment = new TableFragment();
                        break;
                    case 3:
                        //Toast.makeText(mContext, "Under Development", Toast.LENGTH_SHORT).show();
                        fragment = new Cutomer_agereement();

                        //fragment = new TableFragment();
                        break;
                    case 4:
                        //fragment = new MyCart();
                        fragment = new Cutomer_agereement();
                        //fragment = new TableFragment();
                        break;
                    case 5:
                        //fragment = new MyCart();
                        fragment = new Cutomer_agereement();
                        //fragment = new TableFragment();
                        break;
                    case 6:
                        //fragment = new MyCart();
                        fragment = new Cutomer_agereement();
                        //fragment = new TableFragment();
                        break;

                    default:
                        break;
                }


                AppCompatActivity activity = (AppCompatActivity) view.getContext();
               // Fragment myFragment = new Home();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.rootLayout, fragment).addToBackStack(null).commit();
                Bottom_navigation.drawer.closeDrawer(GravityCompat.END);

            }
        });


        if(selected==position)
        {
            textViewName.setTextColor(mContext.getResources().getColor(R.color.white));
            relative_main.setBackgroundColor(mContext.getResources().getColor(R.color.red));

        }else {
            textViewName.setTextColor(mContext.getResources().getColor(R.color.white));
            relative_main.setBackgroundColor(mContext.getResources().getColor(R.color.black));

        }

       // relative_main.setBackgroundColor(mContext.getResources().getColor(R.color.red));
/*

        textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewName.setTextColor(mContext.getResources().getColor(R.color.red));
            }
        });

*/
        return listItem;
    }
}

