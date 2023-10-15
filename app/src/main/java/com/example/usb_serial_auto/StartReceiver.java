package com.example.usb_serial_auto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


public class StartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
     if (intent.getAction() != null && intent.getAction().equals("com.example.usb_serial_auto.START_COMMAND")||Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
         startHelloService(context);
         Log.d("Receiver","Receiver has been triggered.");
        }
        else if(intent.getAction() != null && intent.getAction().equals("com.example.usb_serial_auto.STOP_COMMAND")) {
            stopHelloService();
            Log.d("Intent","Intent null");
        }
        else {
        Log.e("StartReceiver INTENT","Unknown Intent");
        }
    }

    public void startHelloService(Context context) {
        Intent intent = new Intent(context,Service_.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }
    public void stopHelloService() {
        Intent intent = new Intent(AppContextManager.getInstance().getAppContext(),Service_.class);
        AppContextManager.getInstance().getAppContext().stopService(intent);
    }
}

