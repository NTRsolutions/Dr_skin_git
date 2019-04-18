package com.sismatix.drskin.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.sismatix.drskin.Model.Cart_Model;
import com.sismatix.drskin.Model.Cuntrylist_model;
import com.sismatix.drskin.Preference.Config;
import com.sismatix.drskin.Preference.Login_preference;
import com.sismatix.drskin.Preference.MyAddress_Preference;
import com.sismatix.drskin.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.sismatix.drskin.Fragment.MyCart.qt;

public class Paymentscreen extends AppCompatActivity implements MFSDKListener, View.OnClickListener{
    private ArrayList<InvoiceItem> invoiceItems = null;
    String grand_tot_cart,productlist,orederid,address;
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
            orederid = bundle.getString("orederid");
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
        MFSDK.INSTANCE.setUpActionBar("MyFatoorah Payment", R.color.colorPrimary,
                R.color.title_text_color, true);
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
    }
    @Override
    public void onFailed(int i, @NotNull String s) {
        Log.e("onFailed",""+s);
    }
    @Override
    public void onSuccess(@NotNull TransactionResponseModel transactionResponseModel) {

        String text = "Success\n\nResponse:\n\n" + new Gson().toJson(transactionResponseModel);
        Log.e("AuthorizationId",""+transactionResponseModel.getAuthorizationId());

        /*try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(transactionResponseModel));
            Log.e("jsonObject",""+jsonObject);

            JSONObject object=new JSONObject(String.valueOf(jsonObject));
            Log.e("","InvoiceDisplayValue"+object.getString("InvoiceDisplayValue"));
        } catch (Exception e) {
            Log.e("exception",""+e);
        }*/
    }
}
