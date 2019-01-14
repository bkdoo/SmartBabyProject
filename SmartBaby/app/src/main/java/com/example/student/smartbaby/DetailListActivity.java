package com.example.student.smartbaby;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
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

import java.util.HashMap;
import java.util.Map;

public class DetailListActivity extends AppCompatActivity {

    TextView tv_date_detail, tv_sleeptime_detail, tv_wakeuptime_detail, tv_totaltime_detail, tv_daynight_detail;
    EditText et_memo;
    Button btn_ok, btn_update, btn_delete;
    Button btn_modify, btn_cancel;

    String boardId;
    String date;
    String sleepTime;
    String wakeupTime;
    String totalTime;
    String dayNight;
    String memo;

    final static String URL_MEMO_UPDATE = "http://70.12.110.69:8090/smartbaby/board/android/editMemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        tv_date_detail = (TextView) findViewById(R.id.tv_date_detail);
        tv_sleeptime_detail= (TextView) findViewById(R.id.tv_sleeptime_detail);
        tv_wakeuptime_detail= (TextView) findViewById(R.id.tv_wakeuptime_detail);
        tv_totaltime_detail= (TextView) findViewById(R.id.tv_totaltime_detail);
        tv_daynight_detail= (TextView) findViewById(R.id.tv_daynight_detail);
        et_memo = (EditText) findViewById(R.id.et_memo);
        btn_ok= (Button) findViewById(R.id.btn_ok);
        btn_update= (Button) findViewById(R.id.btn_update);
        btn_delete= (Button) findViewById(R.id.btn_delete);
        btn_modify = (Button) findViewById(R.id.btn_modify);
        btn_cancel= (Button) findViewById(R.id.btn_cancel);

        Intent intentFromMain = getIntent();
        boardId = intentFromMain.getStringExtra("boardId");
        date = intentFromMain.getStringExtra("regDate");
        sleepTime = intentFromMain.getStringExtra("sleepTime");
        wakeupTime = intentFromMain.getStringExtra("wakeupTime");
        totalTime = intentFromMain.getStringExtra("totalTime");
        dayNight = intentFromMain.getStringExtra("dayNight");
        memo = intentFromMain.getStringExtra("memo");
        memo = memo.equals("null") ? "" : memo;
        et_memo.setText(memo);
        tv_date_detail.setText(date);
        tv_sleeptime_detail.setText("잠든 시간 : " + sleepTime);
        tv_wakeuptime_detail.setText("일어난 시간 : " + setNullOrTime(wakeupTime));
        tv_totaltime_detail.setText("총 수면 시간 : " + setNullOrTime(totalTime));
        tv_daynight_detail.setText(dayNight);

        if (dayNight.equals("DAY")) {
            tv_daynight_detail.setTextColor(ContextCompat.getColor(this, R.color.red));
        } else {
            tv_daynight_detail.setTextColor(ContextCompat.getColor(this, R.color.blue));
        }


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_memo.setEnabled(true);
                btn_ok.setVisibility(View.GONE);
                btn_update.setVisibility(View.GONE);
                btn_delete.setVisibility(View.GONE);
                btn_modify.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);

            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memo = et_memo.getText().toString();
                et_memo.setEnabled(false);
                btn_ok.setVisibility(View.VISIBLE);
                btn_update.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
                btn_modify.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
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
                        params.put("boardId", boardId);
                        params.put("memo", memo);
                        return params;
                    }

                };

                queue.add(request);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_memo.setText(memo);
                et_memo.setEnabled(false);
                btn_ok.setVisibility(View.VISIBLE);
                btn_update.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
                btn_modify.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
            }
        });


    }

    private String setNullOrTime(String time) {
        if (time.equals("null")) {
            return "자는 중이에요~";
        }
        return time;
    }
}
