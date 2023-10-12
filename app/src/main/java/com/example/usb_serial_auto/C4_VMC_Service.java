package com.example.usb_serial_auto;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;




public class C4_VMC_Service extends Service {
    private static UsbReceiver usbReceiver;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        Intent intent = new Intent(this, C4_VMC_Service.class);
        startService(intent);

        AppContextManager.getInstance().setAppContext(this);
        super.onCreate();

        //Creating an instance of UsbReceiver object
        usbReceiver=new UsbReceiver();
        usbReceiver.InitialConnection();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        usbReceiver.disconnectDevice();
        usbReceiver.closePort();
    } 

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY; // Servis to be started automatically
    }

}
