package tuannguyen.csci342.com.project.model;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import tuannguyen.csci342.com.project.utils.LocationHelper;
import tuannguyen.csci342.com.project.utils.TemperatureHelper;

/**
 * Created by tuannguyen on 26/09/2015.
 */
public class MyApplication extends Application {
    private LocationHelper mLocationHelper;
    private TemperatureHelper mTemperatureHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationHelper = new LocationHelper(this);
        mTemperatureHelper = new TemperatureHelper(this);
    }

    public void setLocationHelperListener(LocationHelper.OnUpdateLocationListener listener)
    {
        mLocationHelper.setListener(listener);
    }
    public void setTemperatureHelper(TemperatureHelper.OnTemperatureUpdateListener listener)
    {
        mTemperatureHelper.setListener(listener);
    }

    public void requestForLocation()
    {
        mLocationHelper.requestForLocation();
    }

    public void requestForTemperature(Location location)
    {
        mTemperatureHelper.getForecast(location);
    }

    public String resolveAddress(Location location)
    {
        return mLocationHelper.getAddress(location);
    }

    public boolean checkNetworkInfo()
    {
        //check network state
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        //the device is not connected to the internet
        if (info == null || !info.isConnected())
        {
            return false;
        }
        return true;
    }

}
