package tuannguyen.csci342.com.project.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Forecast {
    private ForecastData mCurrent;
    private List<ForecastData> mHours;
    private List<ForecastData> mDays;
    private static String mTimeZone;

    private Forecast() {

    }

    //constant
    public static final String CURRENT = "currently";
    public static final String DAYS = "daily";
    public static final String HOURS = "hourly";
    public static final String DATA = "data";
    public static final String CLOUD_COVER = "cloudCover";
    public static final String ICON = "icon";
    public static final String SUMMARY = "summary";
    public static final String WIND_SPEED = "windSpeed";
    public static final String PRECIP_PROBABILITY = "precipProbability";
    public static final String HUMIDITY = "humidity";
    public static final String TEMPERATURE = "temperature";
    public static final String TIME = "time";
    public static final String TIMEZONE = "timezone";
    public static final String TEMPERATURE_MAX = "temperatureMax";

    //factory method
    public static Forecast newInstance(String forecastData) throws JSONException {
        Forecast forecast = new Forecast();
        JSONObject jsonObject = new JSONObject(forecastData);
        mTimeZone = jsonObject.getString(TIMEZONE);
        forecast.mCurrent = getCurrent(jsonObject.getJSONObject(CURRENT));
        forecast.mHours = getHours(jsonObject.getJSONObject(HOURS));
        forecast.mDays = getDays(jsonObject.getJSONObject(DAYS));

        return forecast;
    }

    public List<ForecastData> getDays()
    {
        return mDays;
    }

    public List<ForecastData> getHours()
    {
        return mHours;
    }

    public ForecastData getCurrent()
    {
        return mCurrent;
    }

    private static List<ForecastData> getDays(JSONObject forecastData) throws JSONException {
        List<ForecastData> days = new ArrayList<>();
        JSONArray dayArray = forecastData.getJSONArray(DATA);
        //get individual items
        for (int i = 0; i < dayArray.length(); i++) {
            //create Forecast data object;
            JSONObject day = dayArray.getJSONObject(i);
            ForecastData dayItem = new ForecastData();
            dayItem.setTemperature(day.getDouble(TEMPERATURE_MAX));
            dayItem.setHumid(day.getDouble(HUMIDITY));
            dayItem.setWindSpeed(day.getDouble(WIND_SPEED));
            dayItem.setPrecip(day.getDouble(PRECIP_PROBABILITY));
            dayItem.setIcon(day.getString(ICON));
            dayItem.setTimeZone(mTimeZone);
            dayItem.setSummary(day.getString(SUMMARY));
            dayItem.setCloudCover(day.getDouble(CLOUD_COVER));
            dayItem.setTime(day.getLong(TIME));
            days.add(dayItem);
        }
        return days;
    }

    private static List<ForecastData> getHours(JSONObject forecastData) throws JSONException {
        List<ForecastData> hours = new ArrayList<>();
        JSONArray hourArray = forecastData.getJSONArray(DATA);
        //get individual items
        for (int i = 0; i < hourArray.length(); i++) {
            //create Forecast data object;
            JSONObject hour = hourArray.getJSONObject(i);
            ForecastData hourItem = new ForecastData();
            hourItem.setTemperature(hour.getDouble(TEMPERATURE));
            hourItem.setHumid(hour.getDouble(HUMIDITY));
            hourItem.setWindSpeed(hour.getDouble(WIND_SPEED));
            hourItem.setPrecip(hour.getDouble(PRECIP_PROBABILITY));
            hourItem.setIcon(hour.getString(ICON));
            hourItem.setTimeZone(mTimeZone);
            hourItem.setSummary(hour.getString(SUMMARY));
            hourItem.setCloudCover(hour.getDouble(CLOUD_COVER));
            hourItem.setTime(hour.getLong(TIME));
            hours.add(hourItem);
        }
        return hours;
    }

    private static ForecastData getCurrent(JSONObject forecastData) throws JSONException {
        //create Forecast data object;
        ForecastData current = new ForecastData();
        current.setTemperature(forecastData.getDouble(TEMPERATURE));
        current.setHumid(forecastData.getDouble(HUMIDITY));
        current.setWindSpeed(forecastData.getDouble(WIND_SPEED));
        current.setPrecip(forecastData.getDouble(PRECIP_PROBABILITY));
        current.setIcon(forecastData.getString(ICON));
        current.setTimeZone(mTimeZone);
        current.setSummary(forecastData.getString(SUMMARY));
        current.setCloudCover(forecastData.getDouble(CLOUD_COVER));
        current.setTime(forecastData.getLong(TIME));
        return current;
    }
}
