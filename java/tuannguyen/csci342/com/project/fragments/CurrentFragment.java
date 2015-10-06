package tuannguyen.csci342.com.project.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import tuannguyen.csci342.com.project.R;
import tuannguyen.csci342.com.project.model.Forecast;
import tuannguyen.csci342.com.project.model.ForecastData;

public class CurrentFragment extends BaseFragment {
    private TextView m_tvLocation;
    private TextView m_tvTemp;
    private TextView m_tvCloudCover;
    private TextView m_tvHumidity;
    private TextView m_tvWindSpeed;
    private TextView m_tvPrecip;
    private TextView m_tvSummary;
    private ImageView m_icon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current, container, false);
        m_tvCloudCover = (TextView) v.findViewById(R.id.tv_cloud);
        m_tvTemp = (TextView) v.findViewById(R.id.tv_temp);
        m_tvSummary = (TextView) v.findViewById(R.id.tv_summary);
        m_tvLocation = (TextView) v.findViewById(R.id.tv_location);
        m_tvPrecip = (TextView) v.findViewById(R.id.tv_preci);
        m_tvHumidity = (TextView) v.findViewById(R.id.tv_humidity);
        m_tvWindSpeed = (TextView) v.findViewById(R.id.tv_wind);
        m_icon = (ImageView) v.findViewById(R.id.weather_icon);
        return v;
    }

    @Override
    public void onUpdateGUI(Forecast data, String address) {
        if (data != null) {

            final ForecastData current = data.getCurrent();
            m_tvCloudCover.setText(current.getCloudCover() + "%");
            m_tvHumidity.setText(current.getHumid() + "%");
            m_tvSummary.setText(current.getSummary());
            m_tvPrecip.setText(current.getPrecip() + "%");
            m_tvWindSpeed.setText(current.getWindSpeed() + "");
            m_tvTemp.setText(current.getTemperature() + "°C");
            m_icon.setImageResource(current.getSmallIconId());

        }

        final String addressStr = address;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (addressStr != null && !addressStr.isEmpty())
                    m_tvLocation.setText(addressStr);
                else
                    m_tvLocation.setText("Unknown");
            }
        });
    }
}
