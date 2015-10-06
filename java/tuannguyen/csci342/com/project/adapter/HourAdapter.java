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

public class HourAdapter extends ArrayAdapter<ForecastData>{

    private List<ForecastData> mHours;
    public HourAdapter(Context context, List<ForecastData> data) {
        super(context, R.layout.item_hour, data);
        mHours = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ForecastData data = mHours.get(position);
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_hour, parent, false);
            viewHolder.tvTemp = (TextView)convertView.findViewById(R.id.tv_temp);
            viewHolder.tvHours = (TextView)convertView.findViewById(R.id.tv_hour);
            viewHolder.imgIcon = (ImageView)convertView.findViewById(R.id.img_icon);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.tvHours.setText(data.getTimeString("HH:mm"));
        viewHolder.tvTemp.setText(data.getTemperature() + "°C");
        viewHolder.imgIcon.setImageResource(data.getSmallIconId());
        return convertView;
    }

    class ViewHolder
    {
        TextView tvHours;
        TextView tvTemp;
        ImageView imgIcon;
    }
}
