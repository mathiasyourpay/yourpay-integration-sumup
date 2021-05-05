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

// The native Toast API
import android.widget.Toast;
// Cordova-required packages
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;


public class SumupPlugin extends CordovaPlugin {
	private CallbackContext callbackContext;
    private TransactionParameters trans_params;
    private JSONObject options;
    private MposUi ui;
    private String jdk_device_type;
    private String jdk_production;
    private String jdk_merchant_identifier;
    private String jdk_merchant_secret_key;
    private String jdk_method;
    private String jdk_amount;
    private String jdk_currency;
    private String jdk_payment_text;
    private String jdk_transaction_identifier;
    private AccessoryParameters accessoryParameters;

    private void initializeUI() {

        if(jdk_production.equals("1")) {
            ui = MposUi.initialize(cordova.getActivity(), ProviderMode.LIVE, jdk_merchant_identifier, jdk_merchant_secret_key);
        } else {
            ui = MposUi.initialize(cordova.getActivity(), ProviderMode.TEST, jdk_merchant_identifier, jdk_merchant_secret_key);
        }
        if(jdk_device_type.equals("PAXA920")) {
            ui.getConfiguration().setSummaryFeatures(EnumSet.of(
                MposUiConfiguration.SummaryFeature.PRINT_CUSTOMER_RECEIPT,
                MposUiConfiguration.SummaryFeature.PRINT_MERCHANT_RECEIPT)
            );
        }
    }

    private void Accessory() {
        if(jdk_device_type.equals("PAXA920")) {
            accessoryParameters = new AccessoryParameters.Builder(AccessoryFamily.PAX).integrated().build();
            ui.getConfiguration().setTerminalParameters(accessoryParameters);
            ui.getConfiguration().setPrinterParameters(accessoryParameters);
        } else {
            accessoryParameters = new AccessoryParameters.Builder(AccessoryFamily.MIURA_MPI).bluetooth().build();
            ui.getConfiguration().setTerminalParameters(accessoryParameters);
        }
    }

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
	    options = args.getJSONObject(0);

        try {
            jdk_device_type                 = options.getString("device_type");
            jdk_production                  = options.getString("production");
            jdk_merchant_identifier         = options.getString("merchant_identifier");
            jdk_merchant_secret_key         = options.getString("merchant_secret_key");
            jdk_method                      = options.getString("method");
            jdk_amount                      = options.getString("amount").replaceAll(",",".");
            jdk_currency                    = options.getString("currency");
            jdk_payment_text                = options.getString("payment_text");
            jdk_transaction_identifier      = options.getString("transaction_identifier");


            if(jdk_method.equals("refund")) {
                trans_params = new TransactionParameters.Builder()
                    .refund(new BigDecimal(jdk_amount), io.mpos.transactions.Currency.DKK)
                    .subject(jdk_payment_text)
                    .customIdentifier(jdk_transaction_identifier)
                    .build();
            } else {
                trans_params = new TransactionParameters.Builder()
                    .charge(new BigDecimal(jdk_amount), io.mpos.transactions.Currency.DKK)
                    .subject(jdk_payment_text)
                    .customIdentifier(jdk_transaction_identifier)
                    .build();
            }

            initializeUI();
            Accessory();

            this.callbackContext = callbackContext;

            Intent intent = ui.createTransactionIntent(trans_params);
            cordova.setActivityResultCallback(this);
            cordova.getActivity().startActivityForResult(intent, MposUi.REQUEST_CODE_PAYMENT);

    		return true;

        } catch (JSONException e) {
                 callbackContext.error("Error encountered: " + e.getMessage());
                 return false;
        }

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MposUi.REQUEST_CODE_PAYMENT) {
			if (resultCode == MposUi.RESULT_CODE_APPROVED) {
				callbackContext.success("approved");
			} else {
				callbackContext.success("declined");
			}
			//Transaction transaction = MposUi.getInitializedInstance().getTransaction();
		}
	}

}
