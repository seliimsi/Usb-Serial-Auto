package com.example.usb_serial_auto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;

public class Service_USB_Listener extends BroadcastReceiver{
    private static final int VendorId=6790;
    private static final int ProductId=29987;

    public Service_USB_Listener(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        UsbDevice usbDevice=intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
            if(usbDevice.getVendorId()==VendorId&&usbDevice.getProductId()==ProductId){
                startHelloService(context);
            }
            else {}
        } else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
            if(usbDevice.getVendorId()==VendorId&&usbDevice.getProductId()==ProductId){
                stopHelloService();
            }
            else {}
        }
    }

    public void stopHelloService() {
        Intent intent = new Intent(AppContextManager.getInstance().getAppContext(),C4_VMC_Service.class);
        AppContextManager.getInstance().getAppContext().stopService(intent);
    }
    public void startHelloService(Context context) {
        Intent intent = new Intent(context,C4_VMC_Service.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }
}
