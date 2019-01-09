package com.example.student.smartbaby;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.student.smartbaby.Model.Member;
import com.example.student.smartbaby.Model.Record;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

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

        btn_date.setOnClickListener(new  BtnListener());
        btn_dsleep.setOnClickListener(new  BtnListener());
        btn_nsleep.setOnClickListener(new  BtnListener());
        btn_sleep.setOnClickListener(new  BtnListener());

    }

    class MyTask extends AsyncTask<Map<String, String>, Integer, String> {

        // IP 추후 입력
        String ip ;
        HashMap<String, String> map;

//        public MyTask(String ip, HashMap<String, String> map) {
//            this.ip = ip;
//            this.map = map;
//        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST", "http://localhost:8080/bkd/" );

            // Parameter 를 전송한다.
            //http.addAllParameters(maps[0]);

            //Http 요청 전송
            HttpClient post = http.create();
            post.request();

            //응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

            //응답 본문 가져오기
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Gson gson = new Gson();

            Member memberData = gson.fromJson(s, Member.class);
            String userId = memberData.getUserId();



        }
    }

    class BtnListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_date:

                    Intent intent = new Intent(MainActivity.this,ListActivity.class);
                    startActivity(intent);

                    break;
                case R.id.btn_dsleep:

                    Intent intent1 = new Intent(MainActivity.this,ListActivity.class);
                    startActivity(intent1);

                    break;
                case R.id.btn_nsleep:

                    Intent intent2 = new Intent(MainActivity.this,ListActivity.class);
                    startActivity(intent2);

                    break;
                case R.id.btn_sleep:

                    Intent intent3 = new Intent(MainActivity.this,ListActivity.class);
                    startActivity(intent3);

                    break;

            }
        }
    }


}
