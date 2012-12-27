package com.example;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class GsmRegisterService extends Service {

    final String LOG_TAG = "myLogs";

    private GsmCellChangingListener gsmCellChangingListener = new GsmCellChangingListener();
    private TelephonyManager telephonyManager;
    private GsmCellLocation cellLocation, tempLocation;
    private MediaPlayer mediaPlayer;
    private Notification serviceRunningNotification;
    private int MAIN_SERVICE_NOTIFICATION_ID = 1;
    private PowerManager pm;
    private PowerManager.WakeLock pWakeLock;

    @Override
    public void onCreate() {
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(gsmCellChangingListener, PhoneStateListener.LISTEN_CELL_LOCATION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "on startCommand run");
        Log.d(LOG_TAG, "must turn on display");
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        pWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "WAKE_LOCK_TAG");
        pWakeLock.acquire(1);

        cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

        if (cellLocation != null) {
            serviceRunningNotification = createServiceActiveNotification("GSM location testing","cell: " + cellLocation.getCid());
        } else {
            serviceRunningNotification = createServiceActiveNotification("GSM location testing","no signal");
        }

        startForeground(MAIN_SERVICE_NOTIFICATION_ID, serviceRunningNotification);

       // Log.d(LOG_TAG, "in init: " + cellLocation.toString());
        stopForeground(true);

        return super.onStartCommand(intent, flags, startId);
    }

    private void reloadData() {

        tempLocation = (GsmCellLocation) telephonyManager.getCellLocation();

        if (tempLocation != null && !tempLocation.equals(cellLocation)) {
            Log.d(LOG_TAG, "reload: " + tempLocation.toString());
            cellLocation = tempLocation;
            play();
        }
    }

    private void play() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.grow);
        if (mediaPlayer != null) mediaPlayer.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "on bind method run");
        return null;
    }

    public class GsmCellChangingListener extends PhoneStateListener {
        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            reloadData();
            if (MetroStations.getMetroStations().containsKey(cellLocation.getCid()) && !"true".equals(load("isUnderground"))) {
                createServiceActiveNotification("Subway detected", "It seems you are in the metro");
                save("isUnderground", "true");
            } else if (!MetroStations.getMetroStations().containsKey(cellLocation.getCid()) && "true".equals(load("isUnderground"))){
                createServiceActiveNotification("Out of subway", "It seems you are out of metro");
                save("isUnderground", "false");
            }
        }
    }

    private Notification createServiceActiveNotification(String title, String text) {

        Notification notification = new Notification(R.drawable.ic_stat_example, text, System.currentTimeMillis());
        notification.flags = Notification.FLAG_NO_CLEAR;
        Intent intent = new Intent(this, MyActivity.class);
        PendingIntent launchIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.setLatestEventInfo(this, title, text, launchIntent);

        return notification;
    }

    public void save(String key, String value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public String load(String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(key,"");
    }
}
