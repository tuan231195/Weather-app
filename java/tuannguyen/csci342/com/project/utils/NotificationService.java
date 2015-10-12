package tuannguyen.csci342.com.project.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import tuannguyen.csci342.com.project.R;
import tuannguyen.csci342.com.project.activity.MainActivity;
import tuannguyen.csci342.com.project.model.Forecast;
import tuannguyen.csci342.com.project.model.MyApplication;

public class NotificationService extends Service implements TemperatureHelper.OnTemperatureUpdateListener, LocationHelper.OnUpdateLocationListener {

    private MyApplication mApplication;
    private int id = 1339;
    private NotificationManager mNotificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mApplication = (MyApplication) getApplication();
        if (!mApplication.checkNetworkInfo()) {
            return super.onStartCommand(intent, flags, startId);
        }
        mNotificationManager = (NotificationManager) this.getApplicationContext().getSystemService(mApplication.NOTIFICATION_SERVICE);
        mApplication.setTemperatureHelper(this);
        LatLng latLng = mApplication.getLocation();
        if (latLng == null) {
            //request for location
            mApplication.setLocationHelperListener(this);
            mApplication.requestForLocation();
        } else {
            Location location = new Location("Selected location");
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            location.setTime(new Date().getTime());
            mApplication.requestForTemperature(location);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void OnTemperatureUpdate(Forecast data) {
        if (data == null) {
            return;
        }
        Intent activityIntent = new Intent(mApplication, MainActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mApplication);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(getString(R.string.notification_title));
        builder.setContentText(getString(R.string.notification_content) + data.getCurrent().getTemperature() + ". " + data.getCurrent().getSummary());
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setTicker(getString(R.string.notification_ticker));
        builder.setSound(Uri.parse("android.resource://" + MyApplication.PACKAGE_NAME + "/" + R.raw.notification));
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        mNotificationManager.notify(id, notification);
        mApplication.setLocationHelperListener(null);
        mApplication.setTemperatureHelper(null);
        stopSelf();
    }

    @Override
    public void onLocationUpdate(Location loc) {
        if (loc != null) {
            mApplication.requestForTemperature(loc);
        }
    }
}
