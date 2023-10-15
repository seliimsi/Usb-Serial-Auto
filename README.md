# Android_Serial
###By Selim Can ÖVÜR


This repository is created for future projects that involve continuous USB device connections, primarily for communication with embedded systems equipped with serial chips. It is designed to support a specific product with a unique VendorId and ProductId, which can be customized to meet specific requirements.

##Technical
1) The program works as a foreground android service that is triggered by default boot of android system (or can be triggered by android intents that can be send through adb, ... etc.), might require additional permissions.
2) Every time an usb device connects with a certain VendorId and ProductId that is specified in the program, service attends to connect to the device and reclaim an interface and configures the communication.
3) 
