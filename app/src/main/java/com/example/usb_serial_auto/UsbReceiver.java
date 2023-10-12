package com.example.usb_serial_auto;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class UsbReceiver{

    private UsbDevice usbDevice;
    private UsbManager usbManager;
    private Context context;
    protected Permission permission;
    private int vendorId;
    private HashMap<String, UsbDevice> usbDevices;
    private int productId;
    private String deviceName;
    private static final int VendorId=6790;
    private static final int ProductId=29987;


    public UsbReceiver(){
        this.vendorId=0;
        this.productId=0;
        this.deviceName="";
        this.context=AppContextManager.getInstance().getAppContext();
        this.usbManager= (UsbManager) this.context.getSystemService(this.context.USB_SERVICE);
        this.usbDevices=this.usbManager.getDeviceList();
        this.usbDevice=null;
    }

    // USB cihazlarını saklamak için ArrayList tanımlayın
    private ArrayList<UsbDevice> connectedDevices = new ArrayList<>();

    public void InitialConnection(){
        for(UsbDevice device:usbDevices.values()){
            if(device.getVendorId()==VendorId&&ProductId==device.getProductId()){
                Log.d("usbDevice","Cihaz bulundu.");
                this.usbDevice=device;
                this.permission=new Permission(this.usbDevice,this.usbManager,this.context);
                this.permission.registerPermissionReceiver();
                this.permission.requestUsbPermission();
            }
            else {
            }
            addConnectedDevice(device);
        }
    }

    public  ArrayList<UsbDevice> getConnectedDevices(){
        return connectedDevices;
    }
    public void addConnectedDevice(UsbDevice sample){
        connectedDevices.add(sample);
    }
    public void closePort(){this.permission.closePort();}

    public void disconnectDevice(){
        this.permission.unregisterPermissionReceiver();
    }

    public void sendHex(String hexData) throws InterruptedException {
        this.permission.sendHex(hexData);
    }
    public String readHex(){
        return this.permission.getRead();
    }

}
