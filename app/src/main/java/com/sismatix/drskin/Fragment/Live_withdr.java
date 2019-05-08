package com.sismatix.drskin.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.drskin.Activity.YPlayer;
import com.sismatix.drskin.Adapter.SlidingVideoAdapterMain;
import com.sismatix.drskin.Model.slidervideo_model;
import com.sismatix.drskin.Preference.CheckNetwork;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.ApiClient;
import com.sismatix.drskin.Retrofit.ApiInterface;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sismatix.drskin.Adapter.SlidingVideoAdapterMain.video_id_pass;

/**
 * A simple {@link Fragment} subclass.
 */
public class Live_withdr extends Fragment {

    TextView tv_titleupcoming,tv_time,tv_day,tv_th,tv_month_yres,tv_submittitle;
    LinearLayout lv_ask_doctor,tv_watchnow;

    RecyclerView recycler_shop_videos;
    private List<slidervideo_model> slidervideo_models = new ArrayList<slidervideo_model>();
    SlidingVideoAdapterMain slidingVideoAdapterMain;
    RelativeLayout rl_image;
    public Live_withdr() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_live_withdr, container, false);
        tv_titleupcoming=(TextView)v.findViewById(R.id.tv_titleupcoming);
        tv_time=(TextView)v.findViewById(R.id.tv_time);
        tv_day=(TextView)v.findViewById(R.id.tv_day);
        tv_submittitle=(TextView)v.findViewById(R.id.tv_submittitle);
        tv_month_yres=(TextView)v.findViewById(R.id.tv_month_yres);
        tv_th=(TextView)v.findViewById(R.id.tv_th);
        lv_ask_doctor=(LinearLayout)v.findViewById(R.id.lv_ask_doctor);
        tv_watchnow=(LinearLayout)v.findViewById(R.id.tv_watchnow);
        rl_image=(RelativeLayout) v.findViewById(R.id.rl_image);
        recycler_shop_videos=(RecyclerView) v.findViewById(R.id.recycler_live_videos);
        slidingVideoAdapterMain = new SlidingVideoAdapterMain(getActivity(), slidervideo_models);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_shop_videos.setLayoutManager(mLayoutManager1);
        recycler_shop_videos.setAdapter(slidingVideoAdapterMain);
        Log.e("sizee", "" + slidervideo_models.size());
        tv_titleupcoming.setTypeface(Home.opensans_bold);
        tv_submittitle.setTypeface(Home.opensans_bold);
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            livevideoapi();
        } else {
            Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        lv_ask_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Field_qurey());
            }
        });

        tv_watchnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("video_id_pass",""+video_id_pass);
                // Toast.makeText(context, video_id_pass, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), YPlayer.class);
                intent.putExtra("videoId", video_id_pass);
                startActivity(intent);
            }
        });

        return v;
    }

    private void livevideoapi() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> addcategory = api.AppLivevideo();
        addcategory.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_video", "" + response);
                //progressBar.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_video", "" + status);
                    if (status.equalsIgnoreCase("Success")) {

                        String shopvideo = jsonObject.getString("video_url");
                        Log.e("shopvideo",""+shopvideo);
                        String datetime =jsonObject.getString("next_live_date");
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat(datetime);
                        String strDate = sdf.format(c.getTime());
                        String[] parts = strDate.split(" "); //CREATED_DATE mins pass string date and time
                        String datee = parts[0];
                        String timee = parts[1];
                        Log.e("PART1=======", "" + datee);
                        Log.e("PART2=======", "" + timee);

                        SimpleDateFormat sdff = new SimpleDateFormat(timee);
                        String strtime = sdff.format(c.getTime());
                        String[] partss = strtime.split(":");
                        int h = Integer.parseInt(partss[0]);
                        int m = Integer.parseInt(partss[1]);
                        String s = partss[2];
                        Log.e("date_covert","houes:"+h+" mints:"+m+"  second:"+s+"");

                        String timeSet = "";
                        if (h > 12) {
                            h -= 12;
                            timeSet = "PM";
                        } else if (h == 0) {
                            h += 12;
                            timeSet = "AM";
                        } else if (h == 12){
                            timeSet = "PM";
                        }else{
                            timeSet = "AM";
                        }
                        String min = "";
                        if (m < 10)
                            min = "0" + m ;
                        else
                            min = String.valueOf(m);
                        tv_time.setTypeface(Home.opensans_bold);
                        Log.e("time_set",""+h + ":" + min + " " + timeSet );
                        tv_time.setText(h + ":" + min + " " + timeSet );


                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                        String inputDateStr=datee;
                        Date date = inputFormat.parse(inputDateStr);
                        String outputDateStr = outputFormat.format(date);
                        Log.e("showfinal_date",""+outputDateStr);


                        String[] separate = outputDateStr.split(" ");
                      //  String s =separate[0]; // this will contain "Fruit"
                        String day = separate[0];
                        String month = separate[1];
                        String yers = separate[2];
                        tv_day.setTypeface(Home.opensans_bold);
                        tv_day.setText(day);
                        tv_th.setTypeface(Home.opensans_bold);
                        tv_month_yres.setTypeface(Home.opensans_light);
                        tv_month_yres.setText("OF "+month+"\n"+yers);

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

                            slidervideo_model schedule = new slidervideo_model(jsonObject.getString("video_url"));
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

    private void loadFragment(Fragment fragment) {
        Log.e("clickone", "");
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.rootLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
