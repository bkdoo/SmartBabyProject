package com.example.student.smartbaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailListActivity extends AppCompatActivity {

    TextView tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        tv_date = (TextView)findViewById(R.id.tv_date);

        tv_date.setText(Calendar.getInstance().get(Calendar.YEAR) +

                "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) +

                "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));


    }
}
