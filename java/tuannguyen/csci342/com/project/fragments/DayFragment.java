package tuannguyen.csci342.com.project.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import tuannguyen.csci342.com.project.R;
import tuannguyen.csci342.com.project.adapter.DayAdapter;
import tuannguyen.csci342.com.project.model.Forecast;
import tuannguyen.csci342.com.project.model.ForecastData;

public class DayFragment extends BaseFragment {

    private ListView m_lvDays;
    private DayAdapter mDayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_days, container, false);
        m_lvDays = (ListView) v.findViewById(R.id.lv_days);
        return v;
    }

    @Override
    public void onUpdateGUI(Forecast data, String address) {
        final List<ForecastData> days = data.getDays();
        if (data != null) {
            mDayAdapter = new DayAdapter(getActivity(), days);
            m_lvDays.setAdapter(mDayAdapter);
        }
    }
}
