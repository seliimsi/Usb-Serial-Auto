package com.example.usb_serial_auto;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;




public class Permission extends Communicate{

    private static final String ACTION_USB_PERMISSION = "com.example.c4vmcservice.USB_PERMISSION";

    private PendingIntent permissionIntent;
    private Context context;


    public Permission(UsbDevice usbDevice, UsbManager usbManager,Context context) {
        super(usbDevice,usbManager);
        this.context=context;
        this.permissionIntent = PendingIntent.getBroadcast(this.context, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
        this.findEndpoints();
    }

    public void requestUsbPermission() {
        if (this.usbManager.hasPermission(this.selectedDevice)) {
            this.usbDeviceConnection=this.usbManager.openDevice(this.selectedDevice);
            System.out.println("Connected to the device");
            if(this.usbDeviceConnection.claimInterface(this.usbInterface,true)){
                setportConfigurations();
                System.out.println("Interface has been claimed, communication has been configured");
            }
            else {
                System.out.println("Interface failed");
            }
        } else {
            //Request Permission
            System.out.println("Permission has been requested");
            this.usbManager.requestPermission(this.selectedDevice, this.permissionIntent);
        }
    }

    protected void registerPermissionReceiver() {
        IntentFilter filter = new IntentFilter("com.example.c4vmcservice.USB_PERMISSION");
        this.context.registerReceiver(usbPermissionReceiver, filter);
    }

    protected void unregisterPermissionReceiver() {
        this.context.unregisterReceiver(usbPermissionReceiver);
    }




    private BroadcastReceiver usbPermissionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(usbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (selectedDevice != null) {
                            usbDeviceConnection=usbManager.openDevice(selectedDevice);
                            System.out.println("Permission has been accepted");
                            if(usbDeviceConnection.claimInterface(usbInterface,true)){
                                setportConfigurations();
                                System.out.println("Interface has been claimed, communication has been configured");
                            }
                            else {
                                System.out.println("Arayüz alınamadı.");
                            }
                        } else {
                            Log.e("usbPermissionReceiver", "NULL USB Device");
                        }
                    }
                    else {
                        requestUsbPermission();
                        Log.d("usbReceiver","Permission has been denied");
                    }
                }
            }
        }
    };


}
