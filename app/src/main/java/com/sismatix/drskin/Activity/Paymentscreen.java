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

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
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
    private ArrayList<InvoiceItem> invoiceItems = new ArrayList<InvoiceItem>();
    String grand_tot_cart,productlist,shippingMethod,address,paymentCode,shippingprice,discount_pay;
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
            shippingprice = bundle.getString("shippingprice");
            discount_pay = bundle.getString("discount_pay");
            paymentCode = bundle.getString("paymentCode");
            address = bundle.getString("address");
            Log.e("grand_tot_cart",""+grand_tot_cart);
            Log.e("discount_pay",""+discount_pay);
            try{
                JSONArray array = new JSONArray(productlist);
                Log.e("jsonarr_cart", "" + array);
                for (int i = 0; i < array.length(); i++) {
                    try {
                        InvoiceItem itemm = new InvoiceItem();
                        JSONObject vac_object = array.getJSONObject(i);
                        Log.e("Name", "" + vac_object.getString("product_name"));
                        String currentString = vac_object.getString("product_price");
                        String[] separated = currentString.split("KWD");
                        String A =separated[0]; // this will contain "Fruit"
                        Double totel_pay= (Double.parseDouble(separated[1])); // this will contain " they taste good"
                        Log.e("product_price",""+totel_pay);
                        itemm.setProductName(vac_object.getString("product_name"));
                        itemm.setQuantity(Integer.valueOf(vac_object.getString("product_qty")));
                        itemm.setUnitPrice(Double.valueOf(totel_pay));
                        invoiceItems.add(itemm);
                    } catch (Exception e) {
                        Log.e("Exception", "" + e);
                    } finally {
                    }
                }
                Log.e("array_stack","");
                InvoiceItem item = new InvoiceItem();
                item.setProductName("Shipping Amount");
                item.setQuantity(1);
                item.setUnitPrice(Double.valueOf(shippingprice));
                invoiceItems.add(item);
                if (discount_pay !="null" || discount_pay != "" || discount_pay!= null && discount_pay != "0.00"){
                    InvoiceItem item1 = new InvoiceItem();
                    item1.setProductName("Discount Amount");
                    item1.setQuantity(1);
                    item1.setUnitPrice(Double.valueOf("-"+discount_pay));
                    invoiceItems.add(item1);
                }

            }catch (Exception e){
            }finally {
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
        invoiceModel.setCustomerAddress(address);
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
        new iOSDialogBuilder(Paymentscreen.this)
                .setTitle("Payment Canceled")
                .setSubtitle(s)
                .setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener(("OK"),new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {

                        Intent intent=new Intent(Paymentscreen.this,Bottom_navigation.class);
                        startActivity(intent);
                        dialog.dismiss();

                    }
                })
                .build().show();
    }
    @Override
    public void onFailed(int i, @NotNull String s) {
        Log.e("onFailed",""+s);
        new iOSDialogBuilder(Paymentscreen.this)
                .setTitle("Payment Filed")
                .setSubtitle(s)
                .setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener(("OK"),new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        Intent intent=new Intent(Paymentscreen.this,Bottom_navigation.class);
                        startActivity(intent);
                        dialog.dismiss();

                    }
                })
                .build().show();
    }
    @Override
    public void onSuccess(@NotNull TransactionResponseModel transactionResponseModel) {

        String text = "Success\n\nResponse:\n\n" + new Gson().toJson(transactionResponseModel);
        Log.e("response",""+new Gson().toJson(transactionResponseModel));
        Log.e("payment_response",""+text);
        Log.e("AuthorizationId",""+transactionResponseModel.getAuthorizationId());
        String Authorization=transactionResponseModel.getAuthorizationId();
        Paymentsucessapi(Authorization);
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
                        Log.e("order",""+order);
                        //  paymentapi();
                        Bundle bundle = new Bundle();
                        bundle.putString("order", "" + order);
                        Intent intent=new Intent(Paymentscreen.this,Order_summery_activty.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
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
}
