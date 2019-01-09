package com.example.student.smartbaby;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        tv_date = (TextView)findViewById(R.id.tv_date);

        tv_date.setText(Calendar.getInstance().get(Calendar.YEAR) +

                "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) +

                "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));


        String url = "";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
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
            //URL?a=xxx 이런식으로 보내는 대신에 아래처럼 가능.
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("param1", "isGood");
                return  params;
            }

        };

        queue.add(request);

    }
}
