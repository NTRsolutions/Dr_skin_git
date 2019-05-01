package com.sismatix.drskin.Fragment;


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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sismatix.drskin.Adapter.Cuntrylist_Adapter;
//import com.sismatix.drskin.Adapter.SlidingImage_Adapter;
import com.sismatix.drskin.Model.Cuntrylist_model;
import com.sismatix.drskin.Model.sliderimage_model;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements ViewPager.OnPageChangeListener {

    RecyclerView recycler_cuntrylist;
    Cuntrylist_Adapter cuntrylist_adapter;
    private List<Cuntrylist_model> cuntrylist_models = new ArrayList<Cuntrylist_model>();
    private List<sliderimage_model> sliderimage_models = new ArrayList<sliderimage_model>();
    Timer timer;
    final long DELAY_MS = 800, PERIOD_MS = 4000;

    int currentPage = 0;
    private ViewPager mPager;
    StringRequest stringRequest;
    private String URL_HOMEPAGE;
    private CircleIndicator indicator;
    ProgressBar progressBar_home;

    public Home() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        recycler_cuntrylist = (RecyclerView) v.findViewById(R.id.recycler_cuntrylist);
        mPager = (ViewPager) v.findViewById(R.id.pager);
        indicator = (CircleIndicator) v.findViewById(R.id.indicator);
        progressBar_home = (ProgressBar) v.findViewById(R.id.progressBar_home);
        cuntrylist_adapter = new Cuntrylist_Adapter(getActivity(), cuntrylist_models);
        recycler_cuntrylist.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_cuntrylist.setItemAnimator(new DefaultItemAnimator());
/*LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
layoutManager.setReverseLayout(false);
recycler_cuntrylist.setLayoutManager(layoutManager);*/
        recycler_cuntrylist.setAdapter(cuntrylist_adapter);
        URL_HOMEPAGE = "http://travel.demoproject.info/api/get_homepage_details.php";
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_PRODUCT_CATEGORY_API();
        } else {
            Toast.makeText(getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        callHomePageApi();

        mPager.addOnPageChangeListener(this);
        startAutoScrollViewPager();

        return v;
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
        sliderimage_models.clear();
//progressBar.setVisibility(View.VISIBLE);

        stringRequest = new StringRequest(Request.Method.POST, URL_HOMEPAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("Response_homePage", "" + response);
                        Log.e("URL_HOMEPAGE", "" + URL_HOMEPAGE);
                        try {

// progressBar.setVisibility(View.GONE);

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            Log.e("status_home", "" + status);
                            String message = jsonObject.getString("message");

                            if (status.equals("Success") == true) {
                                Log.e("message", "" + message);

                                JSONObject data_obj = jsonObject.getJSONObject("data");
                                Log.e("data_obj", "" + data_obj);

                                JSONArray banners_arr = data_obj.getJSONArray("banners");
                                Log.e("banners", "" + banners_arr);
                                if (banners_arr != null && banners_arr.isNull(0) != true) {
                                    for (int i = 0; i < banners_arr.length(); i++) {
                                        try {
                                            JSONObject object = banners_arr.getJSONObject(i);
                                            //sliderimage_models.add(new sliderimage_model(object.getString("location_id"), object.getString("location_image")));
                                            mPager.setAdapter(new SlidingImage_Adapter(getActivity(), sliderimage_models));
                                        } catch (Exception e) {
                                            Log.e("Exception", "" + e);
                                        } finally {
                                        }
                                    }
                                    if (banners_arr.length() > 1) {
                                        indicator.setViewPager(mPager);
                                    }
                                } else {
//Toast.makeText(getActivity(), "array_null", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {

                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
// Toast.makeText(getActivity(), "not get Response"+error, Toast.LENGTH_SHORT).show();
                        Log.e("error", "" + error);
                    }

                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
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