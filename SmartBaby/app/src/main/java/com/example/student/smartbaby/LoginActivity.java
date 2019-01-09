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

    // 아이디, 비밀번호 입력 받을 String 객체
    String userIdLogin, passwordLogin;
    // 로그인 정보를 저장할 SharedPreferences 객체
    SharedPreferences sharedPref;
    // 회원가입된 아이디 & 비밀번호인지 확인
    boolean isAuthUser;

    // URL 주소
    static final String URL_LOGIN = "http://70.12.110.69:8090/smartbaby/account/android/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 객체생성
        et_id = (EditText) findViewById(R.id.et_idJoin);
        et_pw = (EditText) findViewById(R.id.et_pwJoin);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_join = (Button) findViewById(R.id.btn_join);
        btn_setUp = (Button) findViewById(R.id.btn_setUp);



        // 회원 가입 후 로그인 화면으로 돌아왔을 때 처리
        Intent intentFromJoin = getIntent();
        // intent 의 result 값 유무 판단 (회원가입 후 왔으면 값이 존재)
        if (intentFromJoin.getStringExtra("result") != null) {
            // 그 값이 "OK" 일 때(회원가입 승인) Toast 메시지 출력
            if (intentFromJoin.getStringExtra("result").equals("OK")) {
                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다. 로그인하세요", Toast.LENGTH_LONG).show();
            }
        }

        // "login_info" 라는 이름으로 sharedPref 객체 생성
        sharedPref = getSharedPreferences("login_info", Context.MODE_PRIVATE);

        // 자동 로그인 기능 - sharedPref 에 autoLogin 값이 존재하면 바로 MainActivity 로 이동
        if (!sharedPref.getString("autoLogin", "").equals("")) {
            Intent intent = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // 로그인 버튼 클릭 리스너
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 아이디와 비밀번호를 String 으로 전환
                userIdLogin = et_id.getText().toString();
                passwordLogin = et_pw.getText().toString();
                // 등록된 아이디 & 비밀번호 확인 초기화
                isAuthUser = false;

                // 로그인 정보를 모두 입력했는지 확인 - isLoginFormFull 메소드
                if (isLoginFormFull()){

                    // 서버로 request 전송 ( 가입된 올바른 아이디 & 비밀번호 인지 확인)
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest request = new StringRequest(Request.Method.POST, URL_LOGIN,
                            // 요청 성공시
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("LOGINresponse", response);
                                    // "ok"를 전달받으면 등록된 아이디 & 비밀번호 임을 확인
                                    if (response.equals("ok")) {
                                        isAuthUser = true;
                                    }

                                    // "fail"을 전달받으면 등록되지 않은 아이디거나 틀린 비밀번호임을 Toast 메시지로 출력
                                    if (response.equals("fail")) {
                                        Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호가 옳지 않습니다.", Toast.LENGTH_LONG).show();
                                    }

                                    // 로그인에 성공하면
                                    if (isAuthUser) {
                                        // sharedPref 의 autoLogin 키값에 로그인 된 아이디를 value 값으로 저장
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("autoLogin", userIdLogin);
                                        editor.commit();

                                        // MainActivity 로 이동
                                        Intent intentToMain = new Intent(LoginActivity.this,
                                                MainActivity.class);
                                        startActivity(intentToMain);
                                        finish();
                                    }
                                }
                            },

                            //에러 발생시
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("error", "[" + error.getMessage() + "]");

                                }
                            }) {

                        // 요청시 아이디와 비밀번호를 파라미터로 전달
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            // HashMap 형태로 아이디와 비밀번호를 전달
                            Map<String, String> params = new HashMap<>();
                            params.put("userId", userIdLogin);
                            params.put("password", passwordLogin);
                            return params;
                        }

                    };
                    // 요청을 queue 에 저장
                    queue.add(request);
                }
            }
        });


        // 회원 가입 버튼 클릭 리스너
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // JoinActivity 로 이동
                Intent intentJoin = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intentJoin);
            }
        });
    }

    // 로그인 창에 아이디와 비밀번호를 입력했는지 확인하는 메소드 - return 타입 : boolean
    // 미입력시 Toast 메시지 출력하며 false 를 리턴
    private boolean isLoginFormFull() {

        if (userIdLogin.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "ID를 입력해주세요.",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (passwordLogin.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "비밀번호를 입력해주세요.",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

}
