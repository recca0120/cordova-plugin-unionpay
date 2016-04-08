package com.recca0120.cordova.plugin;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.unionpaysdk.main.ICheckOrderCallback;
import com.unionpaysdk.main.IPaymentCallback;
import com.unionpaysdk.main.UnionPaySDK;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class echoes a string called from JavaScript.
 */
public class Unionpay extends CordovaPlugin {
    Activity activity = null;
    Context ctx = null;
    UnionPaySDK unionPaySDK = null;
    //CP should provide this to UnionSDK
    private String payCallBackUrl = null;
    //this should be on the CP' own server, which has nothing to do with SDK, and should be handle by CP self
    private String orderId = null;
    private double amount = 0.0;
    private String memo = "";
    private Boolean UnionPaySDKInitialize = false;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        activity = cordova.getActivity();
        ctx = cordova.getActivity();

        //this should be applied by CP and will be provided UnionSDK
        unionPaySDK = UnionPaySDK.getInstance();

        //must Initialize first
        // String scode = cordova.getActivity().getResources().getString(cordova.getActivity().getResources().getIdentifier("unionpay_scode", "string", cordova.getActivity().getPackageName()));
        // String key = cordova.getActivity().getResources().getString(cordova.getActivity().getResources().getIdentifier("unionpay_key", "string", cordova.getActivity().getPackageName()));
        // unionPaySDK.Initialize(ctx, scode, key, true);
    }

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("payOrderRequest")) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        payOrderRequest(args, callbackContext);
                    } catch (Exception e) {
                    }
                }
            });
            return true;
        }
        return false;
    }

    private void payOrderRequest(JSONArray args, final CallbackContext callbackContext) throws Exception {
        try {
            orderId = args.getString(0);
            amount = args.getDouble(1);
            memo = args.getString(2);
            payCallBackUrl = args.getString(3);

            if (UnionPaySDKInitialize == false) {
                String scode = args.getString(4);
                String key = args.getString(5);

                AESCrypt aes = new AESCrypt();

                scode = aes.decrypt(scode.trim());
                key = aes.decrypt(key.trim());
                unionPaySDK.Initialize(ctx, scode, key, true);
                UnionPaySDKInitialize = true;
            }

            final JSONObject obj = new JSONObject();
            obj.put("orderId", orderId);
            obj.put("amount", amount);
            obj.put("memo", memo);

            unionPaySDK.payOrderRequest(activity, orderId, amount, memo, payCallBackUrl, new IPaymentCallback() {
                @Override
                public void onOrderFinished() {
                    // after the order is finished, the client should immediately request the response
                    //from CP's own server to get to know the result of the order
                    callbackContext.success(obj);
                }

                @Override
                public void onOrderNotFinished() {
                    callbackContext.error("Order NotFinished~");
                }
            });
        } catch (JSONException e) {
            callbackContext.error(e.getMessage());
        }
    }

    private static String getRandomString(int len) {
        String str = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int idx = (int) (Math.random() * str.length());
            sb.append(str.charAt(idx));
        }
        String result = sb.toString();
        return result;
    }

    private void show(final String message) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private ICheckOrderCallback checkOrderCallBack = new ICheckOrderCallback() {
        @Override
        public void onSuccess(String json) {
            show("json is " + json);
        }

        @Override
        public void onFailed() {

        }

    };
}
