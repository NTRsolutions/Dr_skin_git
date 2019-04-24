package com.sismatix.drskin.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myfatoorah.sdk.MFSDKListener;
import com.myfatoorah.sdk.model.invoice.InvoiceItem;
import com.myfatoorah.sdk.model.invoice.InvoiceModel;
import com.myfatoorah.sdk.model.transaction.TransactionResponseModel;
import com.myfatoorah.sdk.utils.Country;
import com.myfatoorah.sdk.utils.CurrencyISO;
import com.myfatoorah.sdk.utils.InvoiceLanguage;
import com.myfatoorah.sdk.utils.PaymentMethod;
import com.myfatoorah.sdk.views.MFSDK;
import com.sismatix.drskin.Fragment.Confirm_Order;
import com.sismatix.drskin.Fragment.OrderSummary;
import com.sismatix.drskin.Model.Cart_Model;
import com.sismatix.drskin.Model.Cuntrylist_model;
import com.sismatix.drskin.Preference.CheckNetwork;
import com.sismatix.drskin.Preference.Config;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.Preference.MyAddress_Preference;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.ApiClient;
import com.sismatix.drskin.Retrofit.ApiInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sismatix.drskin.Adapter.Cart_Delivery_Adapter.shippingmethod;
import static com.sismatix.drskin.Adapter.Payment_Method_Adapter.paymentcode_ada;
import static com.sismatix.drskin.Fragment.MyCart.qt;

public class Paymentscreen extends AppCompatActivity implements MFSDKListener, View.OnClickListener{
    private ArrayList<InvoiceItem> invoiceItems = null;
    String grand_tot_cart,productlist,shippingMethod,address,paymentCode;
    JSONArray jsonArray_pay;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_paymentscreen);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            grand_tot_cart = bundle.getString("grand_tot_cart");
            productlist = bundle.getString("productlist");
            shippingMethod = bundle.getString("shippingMethod");
            paymentCode = bundle.getString("paymentCode");
            address = bundle.getString("address");
            try{
                invoiceItems = new ArrayList();
                InvoiceItem item = new InvoiceItem();
                JSONArray array = new JSONArray(productlist);
                Log.e("jsonarr_cart", "" + array);
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject vac_object = array.getJSONObject(i);
                        Log.e("Name", "" + vac_object.getString("product_name"));
                        Double totel_pay = Double.valueOf(vac_object.getString("product_price").substring(1));
                        item.setProductName(vac_object.getString("product_name"));
                        item.setQuantity(Integer.valueOf(vac_object.getString("product_qty")));
                        item.setUnitPrice(Double.valueOf(totel_pay));
                        invoiceItems.add(item);
                    } catch (Exception e) {
                        Log.e("Exception", "" + e);
                    } finally {
                    }
                }
            }catch (Exception e){
            }
        }
        MFSDK.INSTANCE.init(Config.BASE_URL, Config.EMAIL, Config.PASSWORD);
        // You can custom your action bar, but this is optional not required to set this line
        MFSDK.INSTANCE.setUpActionBar("MyFatoorah Payment",R.color.colorPrimary,
                R.color.toolbar_background_color, true);
            // MFSDK.INSTANCE.createInvoice(this, generateInvoiceModel(), "en", PaymentMethod.ALL);
            MFSDK.INSTANCE.createInvoice(this, generateInvoiceModel(), "en", PaymentMethod.ALL);
    }
    private InvoiceModel generateInvoiceModel() {
        InvoiceModel invoiceModel = new InvoiceModel();

        // Not required to send all the following data, the only required are (invoiceValue, customerName, CountryCodeId, DisplayCurrencyId) and the other are optional.
        invoiceModel.setInvoiceValue(Double.valueOf(grand_tot_cart)); // Note this value will be ignored if you already add products (invoiceItems) to the invoice
        invoiceModel.setCustomerName(Login_preference.getfullname(this));
        invoiceModel.setInvoiceItem(invoiceItems);
        invoiceModel.setLanguage(InvoiceLanguage.EN); // You can select any other language from the 'InvoiceLanguage' object
        invoiceModel.setDisplayCurrencyIsoAlpha(CurrencyISO.Kuwaiti_Dinar_KWD); // You can select any other displayCurrencyId from the 'Currency' object
        invoiceModel.setCountryCodeId(Country.KUWAIT); // You can select any other countryCodeId from the 'Country' object
        invoiceModel.setCustomerAddress("");
        invoiceModel.setCustomerBlock("");
        invoiceModel.setCallBackUrl("http://doctorskin.net/customapi/");
        invoiceModel.setCustomerCivilId("");
        invoiceModel.setCustomerEmail(Login_preference.getemail(this));
        invoiceModel.setCustomerHouseBuildingNo("");
        invoiceModel.setCustomerMobile(MyAddress_Preference.getPhoneNumber(this));
        invoiceModel.setCustomerReference("");
        invoiceModel.setCustomerStreet(MyAddress_Preference.getStreetAddress(this));
        invoiceModel.setExpireDate("");
        invoiceModel.setApiCustomFileds("");
        return invoiceModel;
    }
    @Override
    public void onClick(View view) {
    }
    @Override
    public void onCanceled(@NotNull String s) {
        Log.e("onCanceled",""+s);
    }
    @Override
    public void onFailed(int i, @NotNull String s) {
        Log.e("onFailed",""+s);
    }
    @Override
    public void onSuccess(@NotNull TransactionResponseModel transactionResponseModel) {

        String text = "Success\n\nResponse:\n\n" + new Gson().toJson(transactionResponseModel);
        Log.e("response",""+new Gson().toJson(transactionResponseModel));
        Log.e("payment_response",""+text);
        Log.e("AuthorizationId",""+transactionResponseModel.getAuthorizationId());
        String Authorization=transactionResponseModel.getAuthorizationId();
        Paymentsucessapi(Authorization);

        /*try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(transactionResponseModel));
            Log.e("jsonObject",""+jsonObject);

            JSONObject object=new JSONObject(String.valueOf(jsonObject));
            Log.e("","InvoiceDisplayValue"+object.getString("InvoiceDisplayValue"));
        } catch (Exception e) {
            Log.e("exception",""+e);
        }*/
    }

    private void Paymentsucessapi(String authorization) {
        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> paymentsucess = apii.AppPayFatoorahResponce(authorization);

        paymentsucess.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    Log.e("response_all",""+jsonObject);
                    String code = jsonObject.getString("code");
                    Log.e("code_confirmation", "" + code);
                    String meassg = jsonObject.getString("message");
                    Log.e("message_confirmation", "" + meassg);
                    if (code.equalsIgnoreCase("200")) {
                          if (CheckNetwork.isNetworkAvailable(Paymentscreen.this)) {
                    Ordernow();
                    } else {
                    Toast.makeText(Paymentscreen.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                    } else if (code.equalsIgnoreCase("error")) {

                     //   Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Exception", "" + e);
                  //  Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
// Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void Ordernow() {
        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> confirm = apii.AppCreateOrder(Login_preference.getcustomer_id(Paymentscreen.this), Login_preference.getemail(Paymentscreen.this), Login_preference.getquote_id(Paymentscreen.this), MyAddress_Preference.getFirstname(Paymentscreen.this),
                MyAddress_Preference.getCountryId(Paymentscreen.this), MyAddress_Preference.getZipcode(Paymentscreen.this)
                , MyAddress_Preference.getCity(Paymentscreen.this), MyAddress_Preference.getPhoneNumber(Paymentscreen.this),
                MyAddress_Preference.getStreetAddress(Paymentscreen.this), shippingMethod, paymentCode);

        confirm.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    Log.e("response_all",""+jsonObject);
                    String code = jsonObject.getString("code");
                    Log.e("code_confirmation", "" + code);
                    String meassg = jsonObject.getString("message");
                    Log.e("message_confirmation", "" + meassg);
                    if (code.equalsIgnoreCase("200")) {
                        Toast.makeText(Paymentscreen.this, "" + meassg, Toast.LENGTH_SHORT).show();
                        String order=jsonObject.getString("order_id");
                        //  paymentapi();
                        Bundle bundle = new Bundle();
                        bundle.putString("order", "" + order);
                        Fragment myFragment = new OrderSummary();
                        myFragment.setArguments(bundle);
                        Paymentscreen.this.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).replace(R.id.frameLayout_checkout, myFragment).addToBackStack(null).commit();
                        Paymentscreen.this.finish();
                      /*  Intent intent=new Intent(Paymentscreen.this,Paymentscreen.class);
                        intent.putExtras(bundle1);
                        startActivity(intent);*/

                        //loadFragment(order);
                    } else if (code.equalsIgnoreCase("error")) {
                        Toast.makeText(Paymentscreen.this, "" + meassg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Exception", "" + e);
                    Toast.makeText(Paymentscreen.this, "" + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
// Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void pushFragment(Fragment fragment, String add_to_backstack) {
        Bundle bundle = new Bundle();
        bundle.putString("order_id", "" + shippingmethod);
        Fragment myFragment = new Confirm_Order();
        myFragment.setArguments(bundle);
        Paymentscreen.this.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                0, 0, R.anim.fade_out).replace(R.id.frameLayout_checkout, myFragment).addToBackStack(null).commit();
    }
}
