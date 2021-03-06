package io.sumup;

import android.content.Context;
import android.content.Intent;


import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.EnumSet;

import io.mpos.accessories.AccessoryFamily;
import io.mpos.accessories.parameters.AccessoryParameters;
import io.mpos.provider.ProviderMode;
import io.mpos.transactionprovider.processparameters.steps.tipping.TippingProcessStepParameters;
import io.mpos.transactions.parameters.TransactionParameters;
import io.mpos.ui.shared.MposUi;
import io.mpos.ui.shared.model.MposUiConfiguration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.sumup.merchant.reader.models.TransactionInfo;
import com.sumup.merchant.reader.api.SumUpAPI;
import com.sumup.merchant.reader.api.SumUpLogin;
import com.sumup.merchant.reader.api.SumUpPayment;
import com.sumup.merchant.reader.api.SumUpState;

import java.math.BigDecimal;
import java.util.UUID;



// The native Toast API
import android.widget.Toast;
// Cordova-required packages
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import com.sumup.merchant.reader.api.SumUpAPI;
import com.sumup.merchant.reader.api.SumUpLogin;


public class SumupPlugin extends CordovaPlugin {
    private static final int REQUEST_CODE_LOGIN = 1;
    private static final int REQUEST_CODE_PAYMENT = 2;
    private static final int REQUEST_CODE_PAYMENT_SETTINGS = 3;
    private static final int REQUEST_CODE_LOGIN_SETTING = 4;

    private CallbackContext callback = null;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        SumUpState.init(cordova.getActivity());
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        Log.wtf("hej","made it to the plugin");
        Log.wtf("hej",action);
        String affiliateKey = "2358c34d-4160-4ea9-a5b5-a41bd1c5076e";

        if(action.equals("login")) {
            Log.wtf("hej","MADE IT TO LOGIN SECTION");
            callback = callbackContext;
            cordova.setActivityResultCallback(this);

            SumUpLogin sumUplogin = SumUpLogin.builder(affiliateKey).build();
            SumUpAPI.openLoginActivity(cordova.getActivity(), sumUplogin, REQUEST_CODE_LOGIN);
            return true;
        }

        if (action.equals("pay")) {

            /************ Parse args ******************/
            Double amount = null;
            try {
                amount = Double.parseDouble(args.get(0).toString());
            } catch (Exception e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Can't parse amount"));
                return false;
            }

            SumUpPayment.Currency currency = null;
            try {
                currency = SumUpPayment.Currency.valueOf(args.get(1).toString());
            } catch (Exception e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Can't parse currency"));
                return false;
            }

            String email = "";
            try {
                email = args.get(2).toString();
            } catch (Exception e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Can't parse email"));
                return false;
            }

            String tel = "";
            try {
                tel = args.get(3).toString();
            } catch (Exception e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Can't parse tel"));
                return false;
            }

            SumUpPayment payment = SumUpPayment.builder()
                    // mandatory parameters
                    // Your affiliate key is bound to the applicationID entered in the SumUp dashboard at https://me.sumup.com/integration-tools
                    //.affiliateKey(affiliateKey)
                    //.productAmount(amount)
                    .total(BigDecimal.valueOf(amount))
                    .currency(currency)
                    // optional: add details
                    .receiptEmail(email)
                    .receiptSMS(tel)
                    //.productTitle("Taxi Ride")
                    // optional: Add metadata
                    //.addAdditionalInfo("AccountId", "taxi0334")
                    //.addAdditionalInfo("From", "Paris")
                    //.addAdditionalInfo("To", "Berlin")
                    // optional: foreign transaction ID, must be unique!
                    .foreignTransactionId(UUID.randomUUID().toString()) // can not exceed 128 chars
                    .skipSuccessScreen()
                    .build();

            callback = callbackContext;
            cordova.setActivityResultCallback(this);

            //SumUpAPI.openPaymentActivity(this.cordova.getActivity(), payment, REQUEST_CODE_PAYMENT);
            SumUpAPI.checkout(this.cordova.getActivity(), payment, REQUEST_CODE_PAYMENT);
            return true;
        }

        if (action.equals("payWithToken")) {

            /************ Parse args ******************/
            Double amount = null;
            try {
                amount = Double.parseDouble(args.get(0).toString());
            } catch (Exception e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Can't parse amount"));
                return false;
            }

            SumUpPayment.Currency currency = null;
            try {
                currency = SumUpPayment.Currency.valueOf(args.get(1).toString());
            } catch (Exception e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Can't parse currency"));
                return false;
            }

            String email = "";
            try {
                email = args.get(2).toString();
            } catch (Exception e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Can't parse email"));
                return false;
            }

            String tel = "";
            try {
                tel = args.get(3).toString();
            } catch (Exception e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Can't parse tel"));
                return false;
            }

            String token = "";
            try {
                token = args.get(4).toString();
            } catch (Exception e) {
            }

            SumUpPayment payment = SumUpPayment.builder()
                    // mandatory parameters
                    // Your affiliate key is bound to the applicationID entered in the SumUp dashboard at https://me.sumup.com/integration-tools
                    //.affiliateKey(affiliateKey)
                    //.accessToken(token)
                    //.productAmount(amount)
                    .total(BigDecimal.valueOf(amount))
                    .currency(currency)
                    // optional: add details
                    .receiptEmail(email)
                    .receiptSMS(tel)
                    //.productTitle("Taxi Ride")
                    // optional: Add metadata
                    //.addAdditionalInfo("AccountId", "taxi0334")
                    //.addAdditionalInfo("From", "Paris")
                    //.addAdditionalInfo("To", "Berlin")
                    // optional: foreign transaction ID, must be unique!
                    .foreignTransactionId(UUID.randomUUID().toString()) // can not exceed 128 chars
                    .skipSuccessScreen()
                    .build();

            callback = callbackContext;
            cordova.setActivityResultCallback(this);

            //SumUpAPI.openPaymentActivity(this.cordova.getActivity(), payment, REQUEST_CODE_PAYMENT);
            SumUpAPI.checkout(cordova.getActivity(), payment, REQUEST_CODE_PAYMENT);
            return true;
        }

        if(action.equals("settings")) {
            callback = callbackContext;
            cordova.setActivityResultCallback(this);

            SumUpLogin sumUplogin = SumUpLogin.builder(affiliateKey).build();
            SumUpAPI.openLoginActivity(this.cordova.getActivity(), sumUplogin, REQUEST_CODE_LOGIN_SETTING);
            return true;
        }

        if(action.equals("logout")) {
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    SumUpAPI.logout();
                }
            });
            return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_LOGIN) {
            Bundle extra = data.getExtras();
            int code = extra.getInt(SumUpAPI.Response.RESULT_CODE);
            String message = extra.getString(SumUpAPI.Response.MESSAGE);

            JSONObject res = new JSONObject();
            try {
                res.put("code", code);
                res.put("message", message);
            } catch (Exception e) {}

            PluginResult result = new PluginResult(PluginResult.Status.OK, res);
            result.setKeepCallback(true);
            callback.sendPluginResult(result);
        }

        if(requestCode == REQUEST_CODE_PAYMENT) {

            Bundle extras = data.getExtras();

            String code = "";
            String txcode = "";
            String message = "";
            if (extras != null) {
                message = "" + extras.getString(SumUpAPI.Response.MESSAGE);
                txcode = "" + extras.getString(SumUpAPI.Response.TX_CODE);
                code = "" + extras.getInt(SumUpAPI.Response.RESULT_CODE);
            }

            JSONObject res = new JSONObject();
            try {
                res.put("code", code);
                res.put("message", message);
                res.put("txcode", txcode);
            } catch (Exception e) {}

            PluginResult result = new PluginResult(PluginResult.Status.OK, res);
            result.setKeepCallback(true);
            callback.sendPluginResult(result);
        }

        if(requestCode == REQUEST_CODE_LOGIN_SETTING) {
            SumUpAPI.openPaymentSettingsActivity(this.cordova.getActivity(), REQUEST_CODE_PAYMENT_SETTINGS);
        }

        if(requestCode == REQUEST_CODE_PAYMENT_SETTINGS) {
            Bundle extra = data.getExtras();
            int code = extra.getInt(SumUpAPI.Response.RESULT_CODE);
            String message = extra.getString(SumUpAPI.Response.MESSAGE);

            JSONObject res = new JSONObject();
            try {
                res.put("code", code);
                res.put("message", message);
            } catch (Exception e) {}

            PluginResult result = new PluginResult(PluginResult.Status.OK, res);
            result.setKeepCallback(true);
            callback.sendPluginResult(result);
        }

    }
}
