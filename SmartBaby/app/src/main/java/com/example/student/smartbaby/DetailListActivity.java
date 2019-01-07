package com.example.student.smartbaby;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.student.smartbaby.Model.Member;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
}
