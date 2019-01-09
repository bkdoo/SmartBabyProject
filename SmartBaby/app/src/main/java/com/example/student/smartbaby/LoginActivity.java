package com.example.student.smartbaby;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText et_pw, et_id;
    Button btn_login, btn_join, btn_setUp;
    SharedPreferences sharedPref;
    String userId, password;
    boolean isauthUser;

    String url = "http://70.12.110.69:8090/android_link/android/join";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = (EditText) findViewById(R.id.et_idJoin);
        et_pw = (EditText) findViewById(R.id.et_pwJoin);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_join = (Button) findViewById(R.id.btn_join);
        btn_setUp = (Button) findViewById(R.id.btn_setUp);

        Intent intentFromJoin = getIntent();
        if (intentFromJoin.getStringExtra("result") != null) {
            if (intentFromJoin.getStringExtra("result").equals("OK")) {
                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다. 로그인하세요", Toast.LENGTH_LONG).show();
            }
        }


        sharedPref = getSharedPreferences("login_info", Context.MODE_PRIVATE);


        if (!sharedPref.getString("autoLogin", "").equals("")) {
            Intent intent = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest request = new StringRequest(Request.Method.POST, url,
                        // 요청 성공시
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("response", response);
//                                if (response.equals("OK")) {
//                                    isauthUser = true;
//                                }
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
                        userId = et_id.getText().toString();
                        password = et_pw.getText().toString();
                        params.put("userId", userId);
                        params.put("password", password);
                        return params;
                    }

                };


                queue.add(request);

                if (authUser(et_id.getText().toString(),
                        et_pw.getText().toString())) {

                    if (!et_id.equals("") && !et_pw.equals("")) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("autoLogin", et_id.getText().toString());
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });


        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJoin = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intentJoin);
            }
        });


    }

    private boolean authUser(String id, String pw) {

        if (id.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "ID를 입력해주세요.",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (pw.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "비밀번호를 입력해주세요.",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        // 비밀번호와 패스워드를 검증한다. 현재는 임시 코드
        if (isauthUser) {
            // 아이디와 비밀번호가 맞는 경우
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호가 옳지 않습니다.", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}
