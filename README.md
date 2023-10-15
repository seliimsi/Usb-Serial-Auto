# USB Serial Auto


[![tr](https://github.com/selimsii/Usb-Serial-Auto/blob/master/svg/tr.svg)](README_TR.md)

### By Selim Can ÖVÜR

This repository is created for future projects that involve continuous USB device connections, primarily for communication with embedded systems equipped with serial chips. It is designed to support a specific product with a unique VendorId and ProductId, which can be customized to meet specific requirements.

## Technical
Gradle Version --> 8.0
### SDK
+ Compile SDK Version --> API 33
+ Target SDK Version --> API 33
+ Min SDK Version --> API 22

### Dependencies
+ appcombat:1.6.1
+ espresso-core:3.5.1
+ junit:1.1.5
+ junit:4.13.2
+ material:1.9.0
+ nanohttpd:2.3.1
+ usb-serial-for-android:3.6.0

### Permissions
```Java
<uses-feature android:name="android.hardware.usb.host" />
<uses-permission android:name="android.permission.USB_PERMISSION" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
```

### Explanation

1) The program works as a foreground android service that is triggered by default boot of android system (or can be triggered by android intents that can be send through adb, ... etc.), might require additional permissions.
2) Every time an usb device connects with a certain VendorId and ProductId that is specified in the program, service attends to connect to the device and reclaim an interface and configures the communication.
3) The current context of main service or main activity class is shared between other classes by simply setting a context into a static context holder class.

**Intent of Triggers:**
```Java
<intent-filter>
    <action android:name="com.example.usb_serial_auto.START_COMMAND" />
   <action android:name="com.example.usb_serial_auto.STOP_COMMAND" />
   <action android:name="android.intent.action.BOOT_COMPLETED" />
</intent-filter>
```
**To start the service by adb:**
  - 1) Find the usb debugged android devices by writing on the terminal:
```
      adb devices
```
  - 2) After finding the device name try:
```
      am broadcast -a com.example.usb_serial_auto.START_COMMAND -n com.example.usb_serial_auto/.StartReceiver
```

**To stop the service by adb:**
 - 1) Find the usb debugged android devices by writing on the terminal:
```
      adb devices
```
  - 2) After finding the device name try:
```
      am broadcast -a com.example.usb_serial_auto.STOP_COMMAND -n com.example.usb_serial_auto/.StartReceiver
```
**!!!!Apart from this; the service is gonna stop, once you unplug the device.**
  
## Usage

### 1) Create an instance of usbReceiver class
```Java
private static UsbReceiver usbReceiver;
```

### 2) 
### +Initialize the instance in onCreate method of your Activity or Service Class and initialize it with UsbReceiver.InitialConnection() method. 
### +You can set the communication parameters with UsbReceiver.setComParams() function. 
### +Lastly, set the context of AppContextManager class

```Java
@Override
    public void onCreate() {
        Intent intent = new Intent(this, Service_.class);
        startService(intent);

        AppContextManager.getInstance().setAppContext(this);
        super.onCreate();

        //Creating an instance of UsbReceiver object
        usbReceiver=new UsbReceiver();
        usbReceiver.InitialConnection();
        usbReceiver.setComParams(9600,0,8, 1); //default baudRate:9600 parityBit:None, dataBits:8 bits, stopBit:1
    }
```
### 3) Insert these two methods In onDestroy() method of your activity or service class
```Java
  @Override
    public void onDestroy() {
        super.onDestroy();

        usbReceiver.disconnectDevice();
        usbReceiver.closePort();
    }
```
### 4) Finally, ready to use the methods:
+ public String UsbReceiver.readHex() --> Returns hexadecimal value of the message from the connected device
+ public void UsbReceiver.sendHex(String hexData) --> Sends a hex of String to the connected device
+ public  ArrayList<UsbDevice> getConnectedDevices() --> Returns the list of connected devices.
