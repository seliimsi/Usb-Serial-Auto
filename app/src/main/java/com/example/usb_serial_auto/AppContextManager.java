package com.example.usb_serial_auto;

import android.content.Context;

public class AppContextManager {
    private static AppContextManager instance;
    private Context appContext;

    private AppContextManager() {

    }

    public static AppContextManager getInstance() {
        if (instance == null) {
            instance = new AppContextManager();
        }
        return instance;
    }

    public void setAppContext(Context context) {
        this.appContext = context.getApplicationContext();
    }

    public Context getAppContext() {
        return appContext;
    }
}
