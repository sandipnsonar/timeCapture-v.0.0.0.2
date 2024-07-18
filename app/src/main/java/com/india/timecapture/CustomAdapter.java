package com.india.timecapture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


//this is used for formating output data in colum format
public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> dataList;

    public CustomAdapter(Context context, ArrayList<HashMap<String, String>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        TextView tvDateTime = convertView.findViewById(R.id.tvDateTime);
        TextView tvLatitude = convertView.findViewById(R.id.tvLatitude);
        TextView tvLongitude = convertView.findViewById(R.id.tvLongitude);

        HashMap<String, String> data = dataList.get(position);
        tvDateTime.setText(data.get(DB_Helper.COLUMN_DATETIME));
        tvLatitude.setText(data.get(DB_Helper.COLUMN_LATITUDE));
        tvLongitude.setText(data.get(DB_Helper.COLUMN_LONGITUDE));

        return convertView;
    }
}
