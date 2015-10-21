package tuannguyen.csci342.com.project.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import tuannguyen.csci342.com.project.R;
import tuannguyen.csci342.com.project.adapter.DayAdapter;
import tuannguyen.csci342.com.project.model.Forecast;
import tuannguyen.csci342.com.project.model.ForecastData;
import tuannguyen.csci342.com.project.utils.DialogCreator;

public class DayFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView m_lvDays;
    private DayAdapter mDayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_days, container, false);
        m_lvDays = (ListView) v.findViewById(R.id.lv_days);
        m_lvDays.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ForecastData data = (ForecastData)m_lvDays.getAdapter().getItem(position);
        DialogCreator.createDialog(getActivity(), null, data.toString(), null, getString(R.string.ok_button), null, null, null);
    }
}
