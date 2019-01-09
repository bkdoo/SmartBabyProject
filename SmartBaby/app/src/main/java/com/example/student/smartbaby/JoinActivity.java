package com.example.student.smartbaby;

import android.content.Intent;
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

public class JoinActivity extends AppCompatActivity {

    EditText et_idJoin, et_pwJoin, et_pw2Join;
    EditText et_name, et_email, et_birth, et_bname1, et_bbirth1;

    Button btn_id_check, btn_join;

    // EditText의 문자를 받아올 String 객체들
    String userIdJoin, passwordJoin, password2Join;
    String parName, parBirth, babyName, babyBirth, email;

    // ID 중복여부, 회원가입 승인여부
    Boolean isUnusedId, isJoinOK;

    // URL_JOIN 주소
    final static String URL_JOIN = "http://70.12.110.69:8090/android_link/android/join";
    final static String URL_IDCHECK = "http://70.12.110.69:8090/android_link/android/join/idcheck";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        et_idJoin = (EditText) findViewById(R.id.et_idJoin);
        et_pwJoin = (EditText) findViewById(R.id.et_pwJoin);
        et_pw2Join = (EditText) findViewById(R.id.et_pw2Join);
        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_birth = (EditText) findViewById(R.id.et_birth);
        et_bname1 = (EditText) findViewById(R.id.et_bname1);
        et_bbirth1 = (EditText) findViewById(R.id.et_bbirth1);

        btn_id_check = (Button) findViewById(R.id.btn_id_check);
        btn_join = (Button) findViewById(R.id.btn_join);


        isJoinOK = false; //임시값
        isUnusedId = false;

        btn_id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userIdJoin = et_idJoin.getText().toString();
                if (isIdInputed()) {
                    if (isRightID()) {
                        RequestQueue idQueue = Volley.newRequestQueue(JoinActivity.this);
                        StringRequest request = new StringRequest(Request.Method.POST, URL_IDCHECK,

                                // 요청 성공시
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // response로 부터 응답받은 데이터 처리
                                        Log.d("IDresponse", response);
                                        if (response.equals("OK")) {
                                            isUnusedId = true;
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

                            // 요청시 파라미터 전달
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("userIdLogin", userIdJoin);
                                return params;
                            }

                        };

                        idQueue.add(request);

                        if (isUnusedId) {
                            Toast.makeText(getApplicationContext(), "사용 가능한 ID 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "이미 사용중인 ID 입니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "20자 이내로 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ID를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }

        });


        //회원 가입 버튼 클릭 리스너
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFormText();
                if (isFormFull()) {
                    RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
                    StringRequest request = new StringRequest(Request.Method.POST, URL_JOIN,

                            // 요청 성공시
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // response로 부터 응답받은 데이터 처리
                                    Log.d("response", response);
                                    if (response.equals("OK")) {
                                        isJoinOK = true;
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

                        // 요청시 파라미터 전달
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("userIdLogin", userIdJoin);
                            params.put("passwordLogin", passwordJoin);
                            params.put("parName", parName);
                            params.put("parBirth", parBirth);
                            params.put("babyName", babyName);
                            params.put("babyBirth", babyBirth);
                            params.put("email", email);
                            return params;
                        }

                    };

                    queue.add(request);

                    if (isJoinOK) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("result", "OK");
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "필수 입력 사항을 모두 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }


        });


    }

    private boolean isFormFull() {
        if (userIdJoin.isEmpty() || passwordJoin.isEmpty() || parName.isEmpty()
                || parBirth.isEmpty() || babyName.isEmpty() || email.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

    private boolean isRightID() {
        if (userIdJoin.length() <= 20 && userIdJoin.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isIdInputed() {
        if (userIdJoin.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private void getFormText() {
        userIdJoin = et_idJoin.getText().toString();
        passwordJoin = et_pwJoin.getText().toString();
        password2Join = et_pw2Join.getText().toString();
        parName = et_name.getText().toString();
        parBirth = et_birth.getText().toString();
        email = et_email.getText().toString();
        babyName = et_bname1.getText().toString();
        babyBirth = et_bbirth1.getText().toString();
    }

}
