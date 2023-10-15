package com.example.usb_serial_auto;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hoho.android.usbserial.driver.UsbSerialPort;


public class Service_ extends Service {
    private static UsbReceiver usbReceiver;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        Intent intent = new Intent(this, Service_.class);
        startService(intent);

        AppContextManager.getInstance().setAppContext(this);
        super.onCreate();

        //Creating an instance of UsbReceiver object
        usbReceiver=new UsbReceiver();
        usbReceiver.InitialConnection();
        usbReceiver.setComParams(9600,0,8, 1);
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
