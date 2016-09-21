package pl.zazakretem.thewatcher;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Process;
import android.util.Log;


public class WatcherService extends Service implements SensorEventListener {
    public static final String TAG = WatcherService.class.getName();
    public static final int SCREEN_OFF_RECEIVER_DELAY = 500;

    private SensorManager mSensorManager = null;
    private PowerManager.WakeLock mWakeLock = null;

    private AccelerometerValue accelerometerValue = new AccelerometerValue();
    private SpeedValue speedValue = new SpeedValue();
    private LocationManager locationManager;
    private SpeedListener speedListener;

    /*
     * Register this as a sensor event listener.
     */
    private void registerListener() {
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * Un-register this as a sensor event listener.
     */
    private void unregisterListener() {
        mSensorManager.unregisterListener(this);
        if(speedListener != null) {
            Log.i(TAG, "Stopping SpeedListener.");
            locationManager.removeUpdates(speedListener);
        }
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                return;
            }

            Runnable runnable = new Runnable() {
                public void run() {
                    unregisterListener();
                    registerListener();
                }
            };

            new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);
        }
    };

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            accelerometerValue.setAccelerometerValues(event.values);
            Log.i(TAG, "Accelerometer change.");
            if(Math.max(accelerometerValue.getGForceX(), Math.max(accelerometerValue.getGForceY(), accelerometerValue.getGForceZ())) > 1) {
                Log.i(TAG, "CRASH!");
                Log.i(TAG, "Accessed Localization");
                locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                speedListener = new SpeedListener();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, speedListener);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        PowerManager manager =
                (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        unregisterListener();
        mWakeLock.release();
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        startForeground(Process.myPid(), new Notification());
        registerListener();
        mWakeLock.acquire();

        return START_STICKY;
    }

    private class SpeedListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "Location changes");
            if(location.getAccuracy() < 10) {
                speedValue.setSpeedValue(location.getSpeed());
                Log.i(TAG, "Speed:" + String.valueOf(speedValue.getSpeed()));
                locationManager.removeUpdates(this);
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("KITTEN", "Status changed");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("KITTEN", "Enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("KITTEN", "Disabled");
        }
    }
}
