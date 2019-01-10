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
        return list.size();
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

        tv_item_date.setText(list.get(pos).getRegDate());
        tv_item_dayNight.setText(list.get(pos).getDayNight());
        tv_item_sleepTime_db.setText(list.get(pos).getSleepTime());
        tv_item_wakeTime_db.setText(list.get(pos).getWakeupTime());
        tv_item_totalTime_db.setText(list.get(pos).getTotalTime());

        if (list.get(pos).getDayNight().equals("DAY")) {
            tv_item_dayNight.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else {
            tv_item_dayNight.setTextColor(ContextCompat.getColor(context, R.color.blue));
        }


        return view;
    }
}
