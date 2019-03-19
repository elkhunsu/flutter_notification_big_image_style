package com.example.loop_app_flutter_v2;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugins.GeneratedPluginRegistrant;


public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.example.loop_app_flutter_v2/battery";
    public static int NOTIFICATION_ID = 200;
    private static final String TAG = "MainActivity";
    private static String message;
    public static final String KEY_INTENT_APPROVE = "keyintentaccept";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {// to avoid the NullPointerException
            String message = bundle.getString(KEY_INTENT_APPROVE);
            Log.d(TAG, "message get Intent: " + message);

        } else {
            message = "error";
        }


        new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(
                new MethodCallHandler() {
                    @Override
                    public void onMethodCall(MethodCall call, Result result) {
                        // TODO

                        if (call.method.equals("getBatteryLevel")) {
                            int batteryLevel = getBatteryLevel();
                            String dataMessage = getMessageFromFCM();
                            Log.d(TAG, dataMessage);
                            if (batteryLevel != -1) {
                                result.success(message);
                            } else {
                                result.error("UNAVAILABLE", "Battery level not available.", null);
                            }
                        } else {
                            result.notImplemented();
                        }

                    }
                });

    }

    private String getMessageFromFCM() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {// to avoid the NullPointerException

            message = bundle.getString(KEY_INTENT_APPROVE);
            Log.d(TAG, "getMessageFromFCM: " + message);
        } else {
            message = "error";
        }
        return message;
    }

    private int getBatteryLevel() {
        int batteryLevel = -1;
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else {
            Intent intent = new ContextWrapper(getApplicationContext()).
                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }

        return batteryLevel;
    }
}
