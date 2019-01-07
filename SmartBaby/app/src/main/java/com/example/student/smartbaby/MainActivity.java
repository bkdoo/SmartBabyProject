package com.example.student.smartbaby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_setUp, btn_memo, btn_date,btn_dsleep,btn_nsleep, btn_sleep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_setUp = (Button)findViewById(R.id.btn_setUp);
        btn_memo = (Button)findViewById(R.id.btn_memo);
        btn_date = (Button)findViewById(R.id.btn_date);
        btn_dsleep = (Button)findViewById(R.id.btn_dsleep);
        btn_nsleep = (Button)findViewById(R.id.btn_nsleep);
        btn_sleep = (Button)findViewById(R.id.btn_sleep);

        btn_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_setUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
