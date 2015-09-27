package tuannguyen.csci342.com.project.activity;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import tuannguyen.csci342.com.project.R;
import tuannguyen.csci342.com.project.model.MyApplication;
import tuannguyen.csci342.com.project.utils.DialogCreator;
import tuannguyen.csci342.com.project.utils.LocationHelper;


public class MainActivity extends Activity {

    private LocationHelper.OnUpdateLocationListener mLocationListener = new LocationHelper.OnUpdateLocationListener() {
        @Override
        public void onLocationUpdate(Location currentLocation) {
            toggleRefresh(true);
            if (currentLocation != null) {
                mAddress = mApplication.resolveAddress(currentLocation);
                Log.d("Current Location", mAddress + ", " + currentLocation);
            } else {
                DialogCreator.createDialog(MainActivity.this, getString(R.string.error_title), getString(R.string.error_location_message), null, getString(R.string.ok_button), null, null, null);
            }
        }
    };
    private MyApplication mApplication;
    private String mAddress;
    private MenuItem mRefreshItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApplication = (MyApplication) getApplication();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApplication.setLocationHelperListener(mLocationListener);
        toggleRefresh(false);
        mApplication.requestForLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mRefreshItem = menu.findItem(R.id.action_refresh);
        mRefreshItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh)
            mApplication.requestForLocation();
        return super.onOptionsItemSelected(item);
    }

    private void toggleRefresh(boolean visible) {
        setProgressBarIndeterminateVisibility(!visible);
        if (mRefreshItem != null)
            mRefreshItem.setVisible(visible);
    }
}
