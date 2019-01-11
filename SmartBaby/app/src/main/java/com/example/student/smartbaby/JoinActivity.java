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
    EditText et_name, et_email, et_bname1;

    Button btn_id_check, btn_join;

    // EditText 의 문자를 받아올 String 객체들
    String userIdJoin, passwordJoin, password2Join;
    String parName, babyName, email;

    // ID 중복여부, 회원가입 승인여부
    Boolean isUnusedId, isJoinOK;

    // URL 주소 상수
    final static String URL_JOIN = "http://70.12.110.69:8090/smartbaby/account/android/join";
    final static String URL_IDCHECK = "http://70.12.110.69:8090/smartbaby/account/android/join/idcheck";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // 객체생성
        et_idJoin = (EditText) findViewById(R.id.et_idJoin);
        et_pwJoin = (EditText) findViewById(R.id.et_pwJoin);
        et_pw2Join = (EditText) findViewById(R.id.et_pw2Join);
        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_bname1 = (EditText) findViewById(R.id.et_bname1);

        btn_id_check = (Button) findViewById(R.id.btn_id_check);
        btn_join = (Button) findViewById(R.id.btn_join);


        // 아이디 사용가능 확인 버튼 클릭 리스너
        btn_id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText 의 값을 읽어옴
                userIdJoin = et_idJoin.getText().toString();
                // 아이디 사용가능 여부 초기화
                isUnusedId = false;

                //EditText 가 입력되었는지 확인 - isIdInputted 메소드
                if (isIdInputted()) {
                    // 아이디 길이 제한을 지켰는지 확인 = isRightID
                    if (isRightID()) {

                        // Volley 라이브러리를 이용하여 RequestQueue 생성
                        RequestQueue idQueue = Volley.newRequestQueue(JoinActivity.this);

                        //서버로 요청할 request 객체 생성
                        StringRequest request = new StringRequest(Request.Method.POST, URL_IDCHECK,

                                // 요청 성공시
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("IDresponse", response);

                                        // 서버에서 아이디 중복이 아니라면 ok 응답을 보내고 이를 확인
                                        if (response.equals("ok")) {
                                            // 사용할 수 있는 아이디로 boolean 값 변환
                                            isUnusedId = true;
                                        }

                                        // 아이디 사용가능 여부에 따라 Toast 메시지 출력
                                        if (isUnusedId) {
                                            Toast.makeText(getApplicationContext(), "사용 가능한 ID 입니다.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "이미 사용중인 ID 입니다.", Toast.LENGTH_SHORT).show();
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
                                // HashMap 을 통해 서버에 파라미터 전달
                                Map<String, String> params = new HashMap<>();
                                //"userId"라는 key 에 입력한 아이디를 value 로 map 에 넣고 전달
                                params.put("userId", userIdJoin);
                                return params;
                            }

                        };

                        //요청한 request 를 큐에 추가
                        idQueue.add(request);

                        // ID 길이 제한을 어겼을 시
                    } else {
                        Toast.makeText(getApplicationContext(), "20자 이내로 입력하세요.", Toast.LENGTH_SHORT).show();
                    }

                    // 아이디 칸이 공백일 시
                } else {
                    Toast.makeText(getApplicationContext(), "ID를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }

        });


        //회원 가입 버튼 클릭 리스너
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 가능 여부 초기화
                isJoinOK = false;
                // EditText 에 들어있는 데이터들을 String 으로 전환하는 메소드 = getFormText(아이디, 비밀번호, 이메일, 부모이름, 아이이름)
                getFormText();
                // 모든 항목에 값이 입력되었는지 확인 - isFormFull
                if (isFormFull()) {
                    //비밀번호 확인이 일치하는지 검사 - isPasswordEqual 메소드
                    if (isPasswordEqual()) {

                        RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
                        StringRequest request = new StringRequest(Request.Method.POST, URL_JOIN,

                                // 요청 성공시
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // response 로부터 응답받은 데이터 처리
                                        Log.d("JOINresponse", response);
                                        if (response.equals("ok")) {
                                            isJoinOK = true;
                                        }

                                        // 서버로부터 회원가입 ok를 받고 나면 LoginActivity 로 이동
                                        if (isJoinOK) {
                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            // intent 에 회원가입 결과를 넣고 이를 통해 로그인화면에서 Toast 메시지 출력
                                            intent.putExtra("result", "OK");
                                            startActivity(intent);
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
                                // 파라미터로 전달할 HashMap 객체에 각 데이터들을 입력 후 전송
                                Map<String, String> params = new HashMap<>();
                                params.put("userId", userIdJoin);
                                params.put("password", passwordJoin);
                                params.put("parName", parName);
                                params.put("babyName", babyName);
                                params.put("email", email);
                                return params;
                            }

                        };

                        queue.add(request);

                    // 비밀번호가 일치하지 않을 경우
                    } else {
                        // 토스트 메시지로 안내
                        Toast.makeText(getApplicationContext(), "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                        // 비밀번호 확인란에 포커스를 줌
                        et_pw2Join.requestFocus();
                    }

                // 비어있는 항목이 있다면 Toast 메시지 출력
                } else {
                    Toast.makeText(getApplicationContext(), "입력 사항을 모두 입력하세요.", Toast.LENGTH_LONG).show();
                }


            }


        });


    }

    // 회원가입 화면에서 모든 항목을 입력했는지 확인하는 메소드 - return 타입 : boolean
    private boolean isFormFull() {
        if (userIdJoin.isEmpty() || passwordJoin.isEmpty() || parName.isEmpty()
                || babyName.isEmpty() || email.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

    // 아이디 길이 규정을 지켰는지 확인하는 메소드 - return 타입 : boolean
    private boolean isRightID() {
        if (userIdJoin.length() <= 20 && userIdJoin.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // 아이디 중복 체크시 아이디가 입력되었는지 확인하는 메소드 - return 타입 : boolean
    private boolean isIdInputted() {
        if (userIdJoin.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    // EditText 에 입력된 항목을 String 형태로 변환하는 메소드 - return 타입 : void
    private void getFormText() {
        userIdJoin = et_idJoin.getText().toString();
        passwordJoin = et_pwJoin.getText().toString();
        password2Join = et_pw2Join.getText().toString();
        parName = et_name.getText().toString();
        email = et_email.getText().toString();
        babyName = et_bname1.getText().toString();
    }

    // 비밀번호 확인이 일치하는지 검사하는 메소드 - return 타입 : boolean
    private boolean isPasswordEqual() {
        if (passwordJoin.equals(password2Join)) {
            return true;
        } else {
            return false;
        }
    }

}
