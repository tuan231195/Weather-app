package tuannguyen.csci342.com.project.model;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

import tuannguyen.csci342.com.project.utils.LocationHelper;
import tuannguyen.csci342.com.project.utils.TemperatureHelper;

/**
 * Created by tuannguyen on 26/09/2015.
 */
public class MyApplication extends Application {
    private LocationHelper mLocationHelper;
    private TemperatureHelper mTemperatureHelper;
    public static final String PACKAGE_NAME = "tuannguyen.csci342.com.project";
    public static final String LATITUDE_KEY = "latitude";
    public static final String LONGITUDE_KEY = "longitude";
    private SharedPreferences preferences;
    @Override
    public void onCreate() {
        super.onCreate();
        mLocationHelper = new LocationHelper(this);
        mTemperatureHelper = new TemperatureHelper(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
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

    public LatLng getLocation()
    {
        LatLng location = null;
        String latitudeStr = preferences.getString(LATITUDE_KEY, "null");
        //user has not selected any location
        if (!latitudeStr.equals("null")){
            double latitude = Double.parseDouble(latitudeStr);
            String longitudeStr = preferences.getString(LONGITUDE_KEY , "null");
            if (!longitudeStr.equals("null"))
            {
                double longitude = Double.parseDouble(longitudeStr);
                location = new LatLng(latitude, longitude);
            }
        }
        return location;
    }

    public void saveLocation(LatLng savedLocation)
    {
        SharedPreferences.Editor edit = preferences.edit();
        if (savedLocation != null)
        {
            edit.putString(LATITUDE_KEY, String.valueOf(savedLocation.latitude));
            edit.putString(LONGITUDE_KEY, String.valueOf(savedLocation.longitude));
        }
        else
        {
            edit.putString(LATITUDE_KEY, "null");
            edit.putString(LONGITUDE_KEY, "null");
        }
        edit.commit();
    }


}
