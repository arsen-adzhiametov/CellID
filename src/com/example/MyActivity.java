package com.example;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.bugsense.trace.BugSenseHandler;
import com.crittercism.app.Crittercism;
import java.util.Calendar;

public class MyActivity extends Activity {

    private TextView textCID;
    private TextView textLAC;
    private TelephonyManager telephonyManager;
    private GsmCellLocation cellLocation;
    private PhoneStateListener gsmCellChangingListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BugSenseHandler.initAndStartSession(getApplicationContext(), "1205829e");
        Crittercism.init(getApplicationContext(), "50a4d8ad7e69a3691e000003");

        setContentView(R.layout.main);

        textCID = (TextView) findViewById(R.id.cid);
        textLAC = (TextView) findViewById(R.id.lac);
        Button stopButton = (Button)findViewById(R.id.stop);
        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, GsmRegisterService.class);
                MyActivity.this.stopService(intent);
                MyActivity.this.finish();
            }});

        Intent newintent = new Intent(this, GsmRegisterService.class);
        PendingIntent pi = PendingIntent.getService(MyActivity.this, 0, newintent, 0);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        AlarmManager alarms = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarms.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 10000, pi);

        gsmCellChangingListener = new GsmCellChangingListener();

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(gsmCellChangingListener, PhoneStateListener.LISTEN_CELL_LOCATION);

        reloadData();

    }

    private void reloadData() {
        cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
        if (cellLocation != null) {
            int cid = cellLocation.getCid();
            int lac = cellLocation.getLac();
            setText(cid, lac);
        } else setText(0, 0);
    }

    private void setText(int cid, int lac) {
        textCID.setText("gsm cell id: " + String.valueOf(cid));
        textLAC.setText("gsm location area code: " + String.valueOf(lac));
    }

    private class GsmCellChangingListener extends PhoneStateListener {
        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            reloadData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}