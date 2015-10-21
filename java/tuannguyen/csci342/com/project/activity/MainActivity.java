package tuannguyen.csci342.com.project.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;

import tuannguyen.csci342.com.project.R;
import tuannguyen.csci342.com.project.adapter.PagerAdapter;
import tuannguyen.csci342.com.project.fragments.BaseFragment;
import tuannguyen.csci342.com.project.model.Forecast;
import tuannguyen.csci342.com.project.model.MyApplication;
import tuannguyen.csci342.com.project.utils.DialogCreator;
import tuannguyen.csci342.com.project.utils.LocationHelper;
import tuannguyen.csci342.com.project.utils.MyReceiver;
import tuannguyen.csci342.com.project.utils.TemperatureHelper;


public class MainActivity extends FragmentActivity {

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String DAILY_NOTIFICATION = "notification";

    private LocationHelper.OnUpdateLocationListener mLocationListener = new LocationHelper.OnUpdateLocationListener() {
        @Override
        public void onLocationUpdate(Location currentLocation) {

            if (currentLocation != null) {
                mApplication.requestForTemperature(currentLocation);
                mAddress = mApplication.resolveAddress(currentLocation);
            } else {
                toggleRefresh(true);
                DialogCreator.createDialog(MainActivity.this, getString(R.string.error_title), getString(R.string.error_location_message), null, getString(R.string.ok_button), null, null, null);
            }
        }
    };
    private TemperatureHelper.OnTemperatureUpdateListener mTemperatureListener = new TemperatureHelper.OnTemperatureUpdateListener() {
        @Override
        public void OnTemperatureUpdate(Forecast data) {
            mForecast = data;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toggleRefresh(true);
                    if (mForecast != null) {
                        mViewPager.setBackgroundResource(0);
                        mViewPager.setBackgroundResource(mForecast.getCurrent().getBackgroundId());
                        for (int i = 0; i < mAdapter.getCount(); i++) {
                            BaseFragment fragment = (BaseFragment) mAdapter.getRegisteredFragment(i);
                            fragment.onUpdateGUI(mForecast, mAddress);
                        }
                    } else {
                        DialogCreator.createDialog(MainActivity.this, getString(R.string.error_title), getString(R.string.error_temperature_message), null, getString(R.string.ok_button), null, null, null);
                    }
                }
            });
        }
    };


    private MyApplication mApplication;
    private String mAddress;
    private MenuItem mRefreshItem;
    private Forecast mForecast;
    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private PendingIntent mIntent;
    private AlarmManager mAlarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApplication = (MyApplication) getApplication();

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(2);
        mAdapter = new PagerAdapter(getSupportFragmentManager());

        Intent intent = new Intent(this, MyReceiver.class);
        mIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        mViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApplication.setLocationHelperListener(mLocationListener);
        mApplication.setTemperatureHelper(mTemperatureListener);
        requestForForecast();
    }

    private void requestForForecast() {
        LatLng selectedLocation = mApplication.getLocation();
        if (selectedLocation == null)
        {
            requestWeatherForCurrentLocation();
        }
        else{
            //user wants to check the weather at other location
            requestWeatherForLocation(selectedLocation);
        }
    }

    private boolean isNetworkAvailable() {
        if (!mApplication.checkNetworkInfo())
        {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(intent);
                }
            };
            DialogCreator.createDialog(MainActivity.this, getString(R.string.error_title), getString(R.string.no_network_string), null, "Turn on", listener, getString(R.string.cancel_button), null);
            return false;
        }
        return true;
    }

    private void requestWeatherForLocation(LatLng selectedLocation) {
        if (!isNetworkAvailable()) return;
        toggleRefresh(false);
        Location location = new Location("selected location");
        location.setLatitude(selectedLocation.latitude);
        location.setLongitude(selectedLocation.longitude);
        location.setTime(new Date().getTime());
        mAddress = mApplication.resolveAddress(location);
        mApplication.requestForTemperature(location);
    }


    public void requestWeatherForCurrentLocation() {
        if (!isNetworkAvailable()) return;
        toggleRefresh(false);
        mApplication.requestForLocation();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mRefreshItem = menu.findItem(R.id.action_refresh);
        if (mApplication.checkNetworkInfo())
        {
            mRefreshItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh)
            requestForForecast();
        if (id == R.id.action_subscribe)
            displayNotificationDialog();
        if (id == R.id.action_selectLocation)
        {
            //allow the user to check the weather at a particular location
            displayLocationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleRefresh(boolean visible) {
        setProgressBarIndeterminateVisibility(!visible);
        if (mRefreshItem != null)
            mRefreshItem.setVisible(visible);
    }

    private void cancelRequest() {
        mApplication.setLocationHelperListener(null);
        mApplication.setTemperatureHelper(null);
    }

    @Override
    protected void onPause() {
        //prevent memory leaks
        cancelRequest();
        toggleRefresh(true);
        super.onPause();

    }

    private void displayNotificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final SharedPreferences.Editor editor = prefs.edit();
        //get the old selection from SharedPreferences
        int oldSelection = prefs.getInt(DAILY_NOTIFICATION, 1);

        builder.setTitle(getString(R.string.dialog_daily_notification_title));
        builder.setSingleChoiceItems(new String[]{"Yes", "No"}, oldSelection, null);
        builder.setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lv = ((AlertDialog) dialog).getListView();
                int position = lv.getCheckedItemPosition();
                editor.putInt(DAILY_NOTIFICATION, position);
                if (position == 1) {
                    mAlarmManager.cancel(mIntent);
                    editor.commit();
                    //No
                    dialog.dismiss();
                } else {

                    Calendar calendar = Calendar.getInstance();
                    int hour = prefs.getInt(HOUR, calendar.get(Calendar.HOUR_OF_DAY));
                    int minute = prefs.getInt(MINUTE, calendar.get(Calendar.MINUTE));
                    //display a dialog with a TimePicker
                    dialog.dismiss();
                    TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            if (view.isShown()) {
                                editor.putInt(HOUR, hourOfDay);
                                editor.putInt(MINUTE, minute);
                                editor.commit();
                                startAlarm(hourOfDay, minute);
                            }

                        }
                    };
                    new TimePickerDialog(MainActivity.this, mTimeListener, hour, minute, true).show();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel_button), null);
        builder.create().show();
    }

    private void startAlarm(int hourOfDay, int minute) {

        mAlarmManager.cancel(mIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis())
        {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400 * 1000, mIntent);


    }

    private void displayLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a location");
        builder.setSingleChoiceItems(new String[]{"Current Location", "Other Location"}, 1, null);
        builder.setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lv = ((AlertDialog) dialog).getListView();
                int position = lv.getCheckedItemPosition();
                if (position == 1) {
                    //start map activity
                    Intent i = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(i);
                    dialog.dismiss();
                } else {
                    //use the current location
                    mApplication.saveLocation(null);
                    dialog.dismiss();
                    requestWeatherForCurrentLocation();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
}
