package tuannguyen.csci342.com.project.model;
import android.app.Application;
import android.location.Location;

import tuannguyen.csci342.com.project.utils.LocationHelper;

/**
 * Created by tuannguyen on 26/09/2015.
 */
public class MyApplication extends Application {
    private LocationHelper mLocationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationHelper = new LocationHelper(this);
    }

    public void setLocationHelperListener(LocationHelper.OnUpdateLocationListener listener)
    {
        mLocationHelper.setListener(listener);
    }

    public void requestForLocation()
    {
        mLocationHelper.requestForLocation();
    }


    public String resolveAddress(Location location)
    {
        return mLocationHelper.getAddress(location);
    }

}
