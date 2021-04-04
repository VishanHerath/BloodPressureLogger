package lk.kdu.pulze.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import lk.kdu.pulze.R;
import lk.kdu.pulze.model.PressureModel;

public class PressureListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PressureModel> pressureModelArrayList;

    public PressureListAdapter(Context context, ArrayList<PressureModel> pressureModelArrayList) {
        this.context = context;
        this.pressureModelArrayList = pressureModelArrayList;
    }


    @Override
    public int getCount() {
        return pressureModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return pressureModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_pressure_list_item, null, true);

            holder.sis = (TextView) convertView.findViewById(R.id.sys);
            holder.dias = (TextView) convertView.findViewById(R.id.dias);
            holder.pul = (TextView) convertView.findViewById(R.id.pul);
            holder.day = (TextView) convertView.findViewById(R.id.day);
            holder.not = (TextView) convertView.findViewById(R.id.not);

            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.day.setText(pressureModelArrayList.get(position).getDatetime());
        holder.sis.setText(String.valueOf(pressureModelArrayList.get(position).getSystolic()));
        holder.dias.setText(String.valueOf(pressureModelArrayList.get(position).getDiastolic()));
        holder.pul.setText(String.valueOf(pressureModelArrayList.get(position).getPulse()));
        holder.not.setText(pressureModelArrayList.get(position).getNotes());

        return convertView;
    }

    private class ViewHolder {
        protected TextView sis, dias, pul, day, not;
    }

}