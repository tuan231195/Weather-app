package tuannguyen.csci342.com.project.model;

import tuannguyen.csci342.com.project.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ForecastData {
    private int mPrecip;
    private String mSummary;
    private String mIcon;
    private String mTimeZone;
    private double mWindSpeed;
    private int mHumid;
    private int mCloudCover;
    private int  mSmallIconId;
    private int mBackgroundId;
    private long mTime;
    private int mTemperature;

    public int getSmallIconId() {
        return mSmallIconId;
    }



    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = getCelsiusDegree(temperature);
    }

    public int getPrecip() {
        return mPrecip;
    }

    public void setPrecip(double precip) {
        mPrecip = (int)(precip * 100);
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
        convertToIconId(icon);
    }

    public int getBackgroundId() {
        return mBackgroundId;
    }


    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public double getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        mWindSpeed = windSpeed;
    }

    public int getHumid() {
        return mHumid;
    }

    public void setHumid(double humid) {
        mHumid = (int)(humid * 100);
    }

    public int getCloudCover() {
        return mCloudCover;
    }

    public void setCloudCover(double cloudCover) {
        mCloudCover = (int)(cloudCover * 100);
    }

    private void convertToIconId(String icon)
    {
        mBackgroundId = R.drawable.clearday;
        mSmallIconId = R.drawable.cleardayicon;
        if (icon.equals("clear-day")) {

            mBackgroundId = R.drawable.clearday;
            mSmallIconId = R.drawable.cleardayicon;
        }
        else if (icon.equals("clear-night")) {
            mBackgroundId = R.drawable.clearnight;
            mSmallIconId = R.drawable.clearnighticon;
        }
        else if (icon.equals("rain")) {
            mBackgroundId = R.drawable.rain;
            mSmallIconId = R.drawable.rainicon;
        }
        else if (icon.equals("snow")) {
            mBackgroundId = R.drawable.snow;
            mSmallIconId = R.drawable.snowicon;
        }
        else if (icon.equals("sleet")) {
            mBackgroundId = R.drawable.sleet;
            mSmallIconId = R.drawable.sleeticon;
        }
        else if (icon.equals("wind")) {
            mBackgroundId = R.drawable.wind;
            mSmallIconId = R.drawable.windicon;
        }
        else if (icon.equals("fog")) {
            mBackgroundId = R.drawable.fog;
            mSmallIconId = R.drawable.fogicon;
        }
        else if (icon.equals("cloudy")) {
            mBackgroundId = R.drawable.cloudy;
            mSmallIconId = R.drawable.cloudyicon;
        }
        else if (icon.equals("partly-cloudy-day")) {
            mSmallIconId = R.drawable.cloudydayicon;
            mBackgroundId = R.drawable.cloudyday;
        }
        else if (icon.equals("partly-cloudy-night")) {
            mSmallIconId = R.drawable.cloudynighticon;
            mBackgroundId = R.drawable.cloudynight;
        }
    }

    public String getTimeString(String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone(mTimeZone));
        return formatter.format(new Date(mTime * 1000));
    }


    private static int getCelsiusDegree(double farenDegree)
    {
        return (int)Math.round((farenDegree - 32)/1.8);
    }
}
