package com.example.student.smartbaby.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.student.smartbaby.R;
import com.example.student.smartbaby.form.ListViewItem;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    Context context;
    int item_layout;
    ArrayList<ListViewItem> list;
    LayoutInflater layoutInflater;

    final static int LIST_COUNT = 5;

    public ListViewAdapter(Context context, int item_layout, ArrayList<ListViewItem> list) {
        this.context = context;
        this.item_layout = item_layout;
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        int count = list.size() > LIST_COUNT ? LIST_COUNT : list.size();
        return count;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;

        if (view == null) {
            view = layoutInflater.inflate(item_layout, viewGroup, false);
        }

        TextView tv_item_date= (TextView) view.findViewById(R.id.tv_item_date);
        TextView tv_item_dayNight= (TextView) view.findViewById(R.id.tv_item_dayNight);
        TextView tv_item_sleepTime_db= (TextView) view.findViewById(R.id.tv_item_sleepTime_db);
        TextView tv_item_wakeTime_db= (TextView) view.findViewById(R.id.tv_item_wakeTime_db);
        TextView tv_item_totalTime_db= (TextView) view.findViewById(R.id.tv_item_totalTime_db);

        String date_item = list.get(pos).getRegDate();
        String dayNight_item =list.get(pos).getDayNight();
        String sleepTime_item = list.get(pos).getSleepTime();
        String wakeupTime_item = list.get(pos).getWakeupTime();
        String totalTime_item = list.get(pos).getTotalTime();
        tv_item_date.setText(date_item);
        tv_item_dayNight.setText(dayNight_item);
        tv_item_sleepTime_db.setText(sleepTime_item);
        tv_item_wakeTime_db.setText(setNullOrTime(wakeupTime_item));
        tv_item_totalTime_db.setText(setNullOrTime(totalTime_item));

        if (list.get(pos).getDayNight().equals("DAY")) {
            tv_item_dayNight.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else {
            tv_item_dayNight.setTextColor(ContextCompat.getColor(context, R.color.blue));
        }


        return view;
    }

    private String setNullOrTime(String time) {
        if (time.equals("null")) {
            return "아직 자는 중이에요~";
        }
        return time;
    }
}
