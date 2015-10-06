package tuannguyen.csci342.com.project.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import tuannguyen.csci342.com.project.R;
import tuannguyen.csci342.com.project.adapter.PagerAdapter;
import tuannguyen.csci342.com.project.fragments.BaseFragment;
import tuannguyen.csci342.com.project.model.Forecast;
import tuannguyen.csci342.com.project.model.MyApplication;
import tuannguyen.csci342.com.project.utils.DialogCreator;
import tuannguyen.csci342.com.project.utils.LocationHelper;
import tuannguyen.csci342.com.project.utils.TemperatureHelper;


public class MainActivity extends FragmentActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApplication = (MyApplication) getApplication();

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(2);
        mAdapter = new PagerAdapter(getSupportFragmentManager());

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
        }
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
}
