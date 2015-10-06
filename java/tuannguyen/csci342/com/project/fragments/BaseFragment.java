package tuannguyen.csci342.com.project.fragments;

import android.support.v4.app.Fragment;

import tuannguyen.csci342.com.project.model.Forecast;


public abstract class BaseFragment extends Fragment {
    public abstract  void onUpdateGUI(Forecast data, String location);
}
