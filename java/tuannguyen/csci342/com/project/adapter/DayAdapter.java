package tuannguyen.csci342.com.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tuannguyen.csci342.com.project.R;
import tuannguyen.csci342.com.project.model.ForecastData;

public class DayAdapter extends ArrayAdapter{

    private List<ForecastData> mDays;
    public DayAdapter(Context context, List<ForecastData> data) {
        super(context, R.layout.item_day, data);
        mDays = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ForecastData data = mDays.get(position);
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_day, parent, false);
            viewHolder.tvTemp = (TextView)convertView.findViewById(R.id.tv_temp);
            viewHolder.tvDay = (TextView)convertView.findViewById(R.id.tv_day);
            viewHolder.imgIcon = (ImageView)convertView.findViewById(R.id.img_icon);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.tvDay.setText(data.getTimeString("EEEE"));
        viewHolder.tvTemp.setText(data.getTemperature() + "°C");
        viewHolder.imgIcon.setImageResource(data.getSmallIconId());
        return convertView;
    }

    class ViewHolder
    {
        TextView tvDay;
        TextView tvTemp;
        ImageView imgIcon;
    }
}
