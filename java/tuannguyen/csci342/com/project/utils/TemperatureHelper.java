package tuannguyen.csci342.com.project.utils;


import android.content.Context;
import android.location.Location;
import android.util.Log;


import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;

import tuannguyen.csci342.com.project.model.Forecast;

public class TemperatureHelper {
    private static final String TAG = TemperatureHelper.class.getSimpleName();
    private Context mContext;
    private String mForecast;
    private OnTemperatureUpdateListener mListener;
    Call mCall;

    public TemperatureHelper(Context context)
    {
        mContext = context;
    }


    public void setListener(OnTemperatureUpdateListener listener)
    {
        mListener = listener;
    }


    public void getForecast(Location location) throws RuntimeException{

        if (location == null)
            return;
        String forecastUrl = "https://api.forecast.io/forecast/d2331162249e7e05a5bff4cbf3c217d7/"+ location.getLatitude() + "," + location.getLongitude();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(forecastUrl).build();
        mCall = client.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //fail to connect to the website
                if (mListener != null)
                {
                    mListener.OnTemperatureUpdate(null);
                }
                mCall = null;
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful())
                {
                    //successful
                    mForecast = response.body().string();
                    Log.d(TAG, "Forecast data: " + mForecast);
                    Forecast forecast = null;
                    try {
                        forecast = Forecast.newInstance(mForecast);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (mListener != null)
                    {
                        mListener.OnTemperatureUpdate(forecast);
                    }
                }
                else
                {
                    //failure
                    if (mListener != null)
                    {
                        mListener.OnTemperatureUpdate(null);
                    }
                }
                mCall = null;
            }
        });

    }



    public interface OnTemperatureUpdateListener
    {
        public void OnTemperatureUpdate(Forecast data);
    }
}
