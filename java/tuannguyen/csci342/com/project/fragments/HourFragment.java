package tuannguyen.csci342.com.project.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import tuannguyen.csci342.com.project.R;
import tuannguyen.csci342.com.project.adapter.HourAdapter;
import tuannguyen.csci342.com.project.model.Forecast;
import tuannguyen.csci342.com.project.model.ForecastData;
import tuannguyen.csci342.com.project.utils.DialogCreator;

public class HourFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private ListView m_lvHours;
    private HourAdapter mHourAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hours, container, false);
        m_lvHours = (ListView) v.findViewById(R.id.lv_hours);
        m_lvHours.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onUpdateGUI(Forecast data, String address) {
        final List<ForecastData> hours = data.getHours();
        if (data != null) {
            mHourAdapter = new HourAdapter(getActivity(), hours);
            m_lvHours.setAdapter(mHourAdapter);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ForecastData data = (ForecastData)m_lvHours.getAdapter().getItem(position);
        DialogCreator.createDialog(getActivity(), null, data.toString(), null, getString(R.string.ok_button), null, null, null);
    }
}
