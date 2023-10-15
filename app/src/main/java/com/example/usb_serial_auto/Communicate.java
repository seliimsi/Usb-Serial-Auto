package com.example.usb_serial_auto;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class Communicate {

    protected UsbDevice selectedDevice;
    protected UsbEndpoint usbEndpointIn;
    protected UsbEndpoint usbEndpointOut;
    protected UsbInterface usbInterface;
    protected UsbManager usbManager;
    protected UsbDeviceConnection usbDeviceConnection;
    protected List<UsbSerialDriver> availableDrivers;
    protected UsbSerialDriver driver;
    protected UsbSerialPort port;
    protected byte[] buffer=new byte[1024];
    protected int baudRate;
    protected int parityBit;
    protected int stopBit;
    protected int dataBits;


    Communicate(UsbDevice selectedDevice, UsbManager usbManager){
        this.selectedDevice=selectedDevice;
        this.usbEndpointIn=null;
        this.usbEndpointOut=null;
        this.usbManager=usbManager;
        this.usbInterface=selectedDevice.getInterface(0);
        availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(this.usbManager);
        this.driver=availableDrivers.get(0);
        this.port=this.driver.getPorts().get(0);
    }

    protected void findEndpoints(){
        for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
            UsbEndpoint endpoint = usbInterface.getEndpoint(i);
            if (endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (endpoint.getDirection() == UsbConstants.USB_DIR_IN) {
                    this.usbEndpointIn = endpoint;
                } else if (endpoint.getDirection() == UsbConstants.USB_DIR_OUT) {
                    this.usbEndpointOut = endpoint;
                    System.out.println(this.usbEndpointOut);
                }
            }
        }
    }

    public UsbDevice getSelectedDevice(){
        return this.selectedDevice;
    }

    public UsbEndpoint getUsbEndpointOut(){
        return usbEndpointOut;
    }

    public UsbEndpoint getUsbEndpointIn() {
        return usbEndpointIn;
    }


    protected static byte[] hexStringToByteArray(String hex) {

        int length = hex.length();
        byte[] data = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    public static String byteArrayToHexString(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder();

        for (byte b : byteArray) {
            // Each byte is formatted as hex and appended to hexString
            hexString.append(String.format("%02X", b));
        }

        return hexString.toString();
    }

    public void sendHex(String hexData) throws InterruptedException {


        byte[] dataToSend = hexStringToByteArray(hexData);
        int bytesSent = usbDeviceConnection.bulkTransfer(this.getUsbEndpointOut(), dataToSend, dataToSend.length, 100);

        if (bytesSent > 0) {
            System.out.println("Sending was succesfull");
        } else {
            System.out.println("Sending failed");
        }

    }

    private void readHex(){
        int bytesRead = usbDeviceConnection.bulkTransfer(this.getUsbEndpointIn(), buffer, buffer.length, 100); // Veriyi okuyun

        if (bytesRead > 0) {
            byte[] receivedData = Arrays.copyOf(buffer, bytesRead);
            System.out.println("Reading was successfull");
        } else {
            System.out.println("Reading failed");
        }
    }
    protected void closePort(){
        try {
            port.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String getRead(){
        readHex();
        String sample=byteArrayToHexString(buffer);
        int indexofD=sample.indexOf('D')+1;
        return sample.substring(0, indexofD + 1);
    }

    protected void setportConfigurations(){
        try {
            this.port.open(usbDeviceConnection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.port.setParameters(baudRate,dataBits,stopBit,parityBit);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
