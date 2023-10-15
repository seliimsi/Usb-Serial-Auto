# USB Serial Auto


[[EN](https://github.com/selimsii/Usb-Serial-Auto/blob/master/svg/en.svg)](README.md)

### Yazan Selim Can ÖVÜR

Bu repo, seri haberleşme çipleri bulunduran gömülü sistemlerle iletişim için sürekli USB cihaz bağlantılarını gerektiren gelecekteki projeler için oluşturulmuştur. Tek bir ürüne özgü benzersiz bir VendorId ve ProductId ile cihaza bağlanabilmek için tasarlanmıştır.

## Teknik
Gradle Versiyonu --> 8.0
### SDK
+ Compile SDK Sürümü --> API 33
+ Hedef SDK Sürümü --> API 33
+ Minimum SDK Sürümü --> API 22

### Bağımlılıklar
+ appcombat:1.6.1
+ espresso-core:3.5.1
+ junit:1.1.5
+ junit:4.13.2
+ material:1.9.0
+ nanohttpd:2.3.1
+ usb-serial-for-android:3.6.0

### İzinler
```Java
<uses-feature android:name="android.hardware.usb.host" />
<uses-permission android:name="android.permission.USB_PERMISSION" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
''''

### Açıklama

1) Program, Android sisteminin varsayılan önyüklemesi ile tetiklenen (veya adb vb. yoluyla gönderilebilen Android intentleri tarafından tetiklenebilen) bir ön plan Android hizmeti olarak çalışır ve ek izinler gerektirebilir.
2) Bir USB cihazı programda belirtilen belirli bir VendorId ve ProductId ile her bağlandığında, servis uygulaması başlatılır ve cihaza bağlanarak ve bir bağlantı arayüzü almak için devreye girer ve iletişimi yapılandırır.
3) Ana servis veya ana aktivite sınıfının mevcut contexti, statik context tutucu bir sınıfa setlenerek diğer sınıflar arasında paylaşılır.

**Başlatıcı ve Durdurucu Intentler**
```Java
<intent-filter>
     <action android:name="com.example.usb_serial_auto.START_COMMAND" />
    <action android:name="com.example.usb_serial_auto.STOP_COMMAND" />
    <action android:name="android.intent.action.BOOT_COMPLETED" />
</intent-filter>
''''
**Hizmeti adb ile başlatmak için:**
   - 1) Terminal üzerinden usb hata ayıklaması yapılmış android cihazları bulun:
''''
       -adb devices
''''
   - 2) Cihaz adını bulduktan sonra şunu deneyin:
''''
       am broadcast -a com.example.usb_serial_auto.STOP_COMMAND -n com.example.usb_serial_auto/.StartReceiver
''''

**Hizmeti adb ile durdurmak için:**
  - 1) Terminal üzerinden usb hata ayıklaması yapılmış android cihazları bulun:
''''
       -adb devices
''''
   - 2) Cihaz adını bulduktan sonra şunu deneyin:
''''
       am broadcast -a com.example.usb_serial_auto.STOP_COMMAND -n com.example.usb_serial_auto/.StartReceiver
''''
**!!!!Ayrıca; usb cihazını çıkardığınızda hizmet duracaktır.**
  
## Kullanım

### 1) usbReceiver sınıfının bir örneğini oluşturun
```Java
private static UsbReceiver usbReceiver;
''''

### 2)
### +Activity veya servis sınıfınızın onCreate yönteminde oluşturulan usbReceiver nesnesini başlatın ve UsbReceiver.InitialConnection() yöntemiyle ön bağlantı ayarlarını yapılandırın.
### +UsbReceiver.setComParams() fonksiyonu ile iletişim parametrelerini ayarlayabilirsiniz.
### +Son olarak AppContextManager sınıfının contextini ayarlayın.
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
''''
### 3) disconnectDevice() ve closePort() metodlarını activity veya servis sınıfınızın onDestroy() yöntemine ekleyin
```Java
  @Override
    public void onDestroy() {
        super.onDestroy();

        usbReceiver.disconnectDevice();
        usbReceiver.closePort();
    }
''''
### 4) Son olarak yöntemleri kullanmaya hazırız:
+ public String UsbReceiver.readHex() --> Bağlı cihazdan gelen mesajın onaltılık değerini döndürür
+ public void UsbReceiver.sendHex(String hexData) --> Bağlı cihaza onaltılık bir String gönderir
+ public ArrayList<UsbDevice> getConnectedDevices() --> Bağlı cihazların listesini döndürür.