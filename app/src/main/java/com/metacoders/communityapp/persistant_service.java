package com.metacoders.communityapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.metacoders.communityapp.activities.HomePage;

public class persistant_service extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
        Intent service = new Intent(getApplicationContext(), HomePage.class);
        startService(service);
        return super.onStartCommand(intent, flags, startId);
    }
}