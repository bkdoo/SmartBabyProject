package com.example.student.smartbaby;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.student.smartbaby.Model.Member;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailListActivity extends AppCompatActivity {

    TextView tv_date;
    EditText et_memo;
    Button btn_ok, btn_update, btn_delete;

    String date;
    String sleepTime;
    String wakeupTime;
    String totalTime;
    String dayNight;
    String memo;

    final static String URL_MEMO_UPDATE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        tv_date = (TextView) findViewById(R.id.tv_date);
        et_memo = (EditText) findViewById(R.id.et_memo);
        btn_ok= (Button) findViewById(R.id.btn_ok);
        btn_update= (Button) findViewById(R.id.btn_update);

        Intent intentFromMain = getIntent();
        date = intentFromMain.getStringExtra("regDate");
        memo = intentFromMain.getStringExtra("memo");
        tv_date.setText(date);
        et_memo.setText(memo);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest request = new StringRequest(Request.Method.POST, URL_MEMO_UPDATE,
                        // 요청 성공시
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("result", "[" + response + "]");
                            }
                        },

                        //에러 발생시
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("error", "[" + error.getMessage() + "]");
                            }
                        }) {
                    //요청보낼 때 추가로 파라미터가 필요할 경우
                    //URL_JOIN?a=xxx 이런식으로 보내는 대신에 아래처럼 가능.
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("param1", "isGood");
                        return params;
                    }

                };

                queue.add(request);
            }
        });



    }
}
