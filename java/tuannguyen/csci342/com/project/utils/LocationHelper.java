package tuannguyen.csci342.com.project.utils;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by tuannguyen on 25/09/2015.
 */
public class LocationHelper implements android.location.LocationListener {
    private LocationManager mLocationManager;
    private Context mContext;
    private OnUpdateLocationListener mListener;
    private Location mLocation;
    public LocationHelper(Context ctx) {
        mContext = ctx;
        mLocationManager = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);

    }

    public void requestForLocation()
    {
        if (locationIsValid(mLocation))
            mListener.onLocationUpdate(mLocation);
        else
        {
            mLocation = getBestLocation();
            if (locationIsValid(mLocation))
                mListener.onLocationUpdate(mLocation);
            else {

                Criteria criteria = new Criteria();
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                // Register for network location updates
                mLocationManager.requestSingleUpdate(criteria, this, null);
            }
        }

    }

    private Location getBestLocation() {

        List<String> allProviders = mLocationManager.getAllProviders();
        float bestAccuracy = Float.MAX_VALUE;
        Location bestLocation = null;
        for (String provider : allProviders) {
            Location location = mLocationManager.getLastKnownLocation(provider);
            if (location != null) {
                float accuracy = location.getAccuracy();
                if (accuracy < bestAccuracy) {
                    bestLocation = location;
                    bestAccuracy = accuracy;
                }
            }
        }
        return bestLocation;
    }

    public boolean locationIsValid(Location location){
        //if location is not null and if location is recent
        return (mLocation != null && mLocation.getTime() > System.currentTimeMillis() - 60 *1000);
    }

    public void setListener(OnUpdateLocationListener listener)
    {
        if (listener == null)
        {
            //turn off location updates
            mLocationManager.removeUpdates(this);
        }
        mListener = listener;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mListener != null)
            mListener.onLocationUpdate(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public String getAddress(Location location)
    {
        List<Address> addresses;
        String addressString = "";
        if (location != null)
        {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            Geocoder gc = new Geocoder(mContext, Locale.getDefault());
            try
            {
                addresses = gc.getFromLocation(lat, lng, 1);
                if (addresses.size() > 0)
                {
                    Address currentAddress = addresses.get(0);
                    String locality = currentAddress.getLocality();
                    String countryName = currentAddress.getCountryName();
                    if (locality != null)
                    {
                        addressString += locality;
                        addressString += ", ";
                    }
                    if (countryName != null)
                    {
                        addressString += countryName;
                    }
                    return addressString;
                }
            }
            catch(IOException e)
            {

            }
        }
        return null;
    }

    public interface OnUpdateLocationListener
    {
        public void onLocationUpdate(Location loc);
    }
}
