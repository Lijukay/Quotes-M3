package com.lijukay.quotesAltDesign.Others;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.lijukay.quotesAltDesign.Activity.SettingsActivity;

public class InternetService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("inside", "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("not yet implement");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(periodicUpdate);
        return START_STICKY;
    }

    public boolean isOnline(Context c){
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnectedOrConnecting();
    }

    final Handler handler = new Handler();
    private final Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(periodicUpdate, 1000 - SystemClock.elapsedRealtime()%1000);
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(SettingsActivity.BroadcastStringForAction);
            broadCastIntent.putExtra("online_status", ""+isOnline(InternetService.this));
            sendBroadcast(broadCastIntent);
        }
    };
}
