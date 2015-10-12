package tuannguyen.csci342.com.project.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import tuannguyen.csci342.com.project.R;
import tuannguyen.csci342.com.project.model.MyApplication;

public class MapActivity extends FragmentActivity implements GoogleMap.OnMapLongClickListener {

    private LatLng currentLocation;
    private Marker currenMarker;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        currentLocation = ((MyApplication)getApplication()).getLocation();
        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null)
            {
                mMap.setOnMapLongClickListener(this);
                showMarker();
            }
        }
    }
    private void showMarker() {
        if (currentLocation != null)
        {
            MarkerOptions option = new MarkerOptions();
            option.position(currentLocation);
            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    currentLocation).zoom(12).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            currenMarker = mMap.addMarker(option);
        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (currenMarker != null)
        {
            currenMarker.remove();
        }

        currentLocation = latLng;
        showMarker();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //save the location
        ((MyApplication)getApplication()).saveLocation(currentLocation);

    }
}
