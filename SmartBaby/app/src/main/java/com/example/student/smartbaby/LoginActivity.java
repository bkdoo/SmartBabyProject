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
    String userIdLogin, passwordLogin;
    boolean isAuthUser;

    static final String URL_LOGIN = "http://70.12.110.69:8090/android_link/android/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = (EditText) findViewById(R.id.et_idJoin);
        et_pw = (EditText) findViewById(R.id.et_pwJoin);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_join = (Button) findViewById(R.id.btn_join);
        btn_setUp = (Button) findViewById(R.id.btn_setUp);

        isAuthUser = false;

        // 회원 가입 후 로그인 화면으로 돌아왔을 때 처리
        Intent intentFromJoin = getIntent();
        if (intentFromJoin.getStringExtra("result") != null) {
            if (intentFromJoin.getStringExtra("result").equals("OK")) {
                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다. 로그인하세요", Toast.LENGTH_LONG).show();
            }
        }


        sharedPref = getSharedPreferences("login_info", Context.MODE_PRIVATE);

        //로그인 정보 기억
        if (!sharedPref.getString("autoLogin", "").equals("")) {
            Intent intent = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userIdLogin = et_id.getText().toString();
                passwordLogin = et_pw.getText().toString();

                if (isLoginFormFull()){
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    StringRequest request = new StringRequest(Request.Method.POST, URL_LOGIN,
                            // 요청 성공시
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("response", response);
                                    if (response.equals("OK")) {
                                        isAuthUser = true;
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
                        //요청보낼 때 추가로 파라미터가 필요할 경우
                        //URL_JOIN?a=xxx 이런식으로 보내는 대신에 아래처럼 가능.
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("userIdLogin", userIdLogin);
                            params.put("passwordLogin", passwordLogin);
                            return params;
                        }

                    };


                    //test 아이디 비밀번호
                    if (userIdLogin.equals("test") && passwordLogin.equals("123"))
                        isAuthUser = true;

                    queue.add(request);

                    if (isAuthUser) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("autoLogin", userIdLogin);
                        editor.commit();

                        Intent intentToMain = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intentToMain);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호가 옳지 않습니다.", Toast.LENGTH_LONG).show();
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
