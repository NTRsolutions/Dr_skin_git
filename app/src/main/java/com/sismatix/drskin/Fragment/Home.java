package com.sismatix.drskin.Fragment;


import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sismatix.drskin.Activity.Chat_messge;
import com.sismatix.drskin.Activity.CirclePagerIndicatorDecoration;
import com.sismatix.drskin.Adapter.Cuntrylist_Adapter;
//import com.sismatix.drskin.Adapter.SlidingImage_Adapter;
import com.sismatix.drskin.Adapter.SlidingVideoAdapterMain;
import com.sismatix.drskin.Model.Cuntrylist_model;
import com.sismatix.drskin.Model.sliderimage_model;
import com.sismatix.drskin.Model.slidervideo_model;
import com.sismatix.drskin.Preference.CheckNetwork;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.ApiClient;
import com.sismatix.drskin.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements ViewPager.OnPageChangeListener {

    RecyclerView recycler_cuntrylist;
    Cuntrylist_Adapter cuntrylist_adapter;
    private List<Cuntrylist_model> cuntrylist_models = new ArrayList<Cuntrylist_model>();
    private List<sliderimage_model> sliderimage_models = new ArrayList<sliderimage_model>();
    Timer timer;
    TextView title_tv;
    final long DELAY_MS = 800, PERIOD_MS = 4000;

    int currentPage = 0;
    private ViewPager mPager;
    StringRequest stringRequest;
    private String URL_HOMEPAGE;
    private CircleIndicator indicator;
    ProgressBar progressBar_home;
    LinearLayout lv_chat_doctor,lv_ask_doctor;

    public static AssetManager am;
    public static Typeface  opensans_bold, opensans_light, opensans_regular;

    RecyclerView recycler_prem_videos;
    private List<slidervideo_model> slidervideo_models = new ArrayList<slidervideo_model>();
    SlidingVideoAdapterMain slidingVideoAdapterMain;
    ScrollingPagerIndicator indicator_home;
    RelativeLayout rl_image;

    public Home() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        setFontStyle();
        rl_image = (RelativeLayout)v.findViewById(R.id.rl_image);
        indicator_home = (ScrollingPagerIndicator)v.findViewById(R.id.indicator_home);
        recycler_cuntrylist = (RecyclerView) v.findViewById(R.id.recycler_cuntrylist);
        recycler_prem_videos = (RecyclerView) v.findViewById(R.id.recycler_prem_videos);

//slideingVideoAdapter = new SlidingImage_Adapter(getContext(),slidervideo_models);

        slidingVideoAdapterMain = new SlidingVideoAdapterMain(getActivity(), slidervideo_models);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_prem_videos.setLayoutManager(mLayoutManager1);
        recycler_prem_videos.setAdapter(slidingVideoAdapterMain);
        indicator_home.attachToRecyclerView(recycler_prem_videos);
        Log.e("sizee", "" + slidervideo_models.size());

        progressBar_home = (ProgressBar) v.findViewById(R.id.progressBar_home);
        lv_chat_doctor = (LinearLayout)v.findViewById(R.id.lv_chat_doctor);
        lv_ask_doctor = (LinearLayout)v.findViewById(R.id.lv_ask_doctor);
        cuntrylist_adapter = new Cuntrylist_Adapter(getActivity(), cuntrylist_models);
        recycler_cuntrylist.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_cuntrylist.setItemAnimator(new DefaultItemAnimator());

        recycler_cuntrylist.setAdapter(cuntrylist_adapter);
       // URL_HOMEPAGE_VIDEOS = "http://doctorskin.net/customapi/AppHomeVideo.php";

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_PRODUCT_CATEGORY_API();
        } else {
            Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        callHomePageApi();

//startAutoScrollViewPager();

        lv_ask_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Field_qurey());
            }
        });

        lv_chat_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Chat_messge.class);
                startActivity(intent);
            }
        });
        return v;
    }

    private void loadFragment(Fragment fragment) {
        Log.e("clickone", "");
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.rootLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void setFontStyle() {

        //holder.txt_pack_title.setTypeface(Home.typeface);
        //holder.txt_pack_title.setText(package_model.getPackage_title());

        am = getActivity().getAssets();
        opensans_bold = Typeface.createFromAsset(am,
                String.format(Locale.getDefault(), "OpenSans-Bold.ttf"));
        opensans_light = Typeface.createFromAsset(am,
                String.format(Locale.getDefault(), "OpenSans-Light.ttf"));
        opensans_regular = Typeface.createFromAsset(am,
                String.format(Locale.getDefault(), "OpenSans-Regular.ttf"));

    }
    private void CALL_PRODUCT_CATEGORY_API() {
        progressBar_home.setVisibility(View.VISIBLE);

        cuntrylist_models.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> categorylist = api.categorylist("all");

        categorylist.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                progressBar_home.setVisibility(View.GONE);
                cuntrylist_models.clear();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_prod_cat", "" + status);
                    if (status.equalsIgnoreCase("success")) {
                        String category = jsonObject.getString("category");
                        Log.e("catttt_prod_cat", "" + category);
                        JSONArray jsonArray = jsonObject.getJSONArray("category");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            try {
                                JSONObject vac_object = jsonArray.getJSONObject(i);
                                Log.e("Name", "" + vac_object.getString("name"));
                                cuntrylist_models.add(new Cuntrylist_model(vac_object.getString("image"), vac_object.getString("name"), vac_object.getString("value")));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                cuntrylist_adapter.notifyItemChanged(i);
                            }

                        }

                    } else if (status.equalsIgnoreCase("error")) {
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                progressBar_home.setVisibility(View.GONE);
            }
        });
    }

    private void callHomePageApi() {

        slidervideo_models.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> video = api.getVideos();

        video.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                slidervideo_models.clear();
                JSONObject object = null;
                try {
//getting the whole json object from the response
                    object = new JSONObject(response.body().string());
                    Log.e("arrresponse", "" + response);

                    String status = object.getString("status");
                    Log.e("status_vids",""+status);

                    String content = object.getString("content");
                    Log.e("content",""+content);

                    JSONArray cont_arr = new JSONArray(content);
                    Log.e("cont_arr",""+cont_arr);
                    slidervideo_models.clear();
                    for (int i = 0; i < cont_arr.length(); i++) {

                        if (cont_arr.equals("")||cont_arr.equals(null)||cont_arr.equals("null")){
                            recycler_prem_videos.setVisibility(View.GONE);
                            indicator_home.setVisibility(View.GONE);
                            rl_image.setVisibility(View.VISIBLE);
                            rl_image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getContext(), "No video Available", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            recycler_prem_videos.setVisibility(View.VISIBLE);
                            indicator_home.setVisibility(View.VISIBLE);
                            rl_image.setVisibility(View.GONE);
                            try {
                                slidervideo_model schedule = new slidervideo_model(cont_arr.getString(i));
                                slidervideo_models.add(schedule);
                                Log.e("schedule",""+slidervideo_models.size());

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            }finally {
                                slidingVideoAdapterMain.notifyItemChanged(i);
                            }
                        }

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
}