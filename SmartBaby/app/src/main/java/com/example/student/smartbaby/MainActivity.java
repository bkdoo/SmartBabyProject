package com.example.student.smartbaby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
