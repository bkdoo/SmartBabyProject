package com.example.student.smartbaby;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.smartbaby.Model.Member;
import com.example.student.smartbaby.Model.Record;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText et_pw, et_id;
    Button btn_login, btn_join, btn_setUp;
    SharedPreferences sharedPref;
    MyTask myTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = (EditText)findViewById(R.id.et_id);
        et_pw = (EditText)findViewById(R.id.et_pw);

        btn_login = (Button)findViewById(R.id.btn_login);
        btn_join = (Button)findViewById(R.id.btn_join);
        btn_setUp = (Button)findViewById(R.id.btn_setUp);

        String id = et_id.getText().toString();

        sharedPref = getSharedPreferences("login_info", Context.MODE_PRIVATE);

        if (myTask == null) {
            myTask = new MyTask();
            myTask.execute();
        }

        if(!sharedPref.getString("autoLogin", "").equals("")) {
            Intent intent = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(authUser(et_id.getText().toString(),
                        et_pw.getText().toString())) {

                    if(!et_id.equals("") && !et_pw.equals("")) {
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
    }

    private boolean authUser(String id, String pw) {

        if(id.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "ID를 입력해주세요.",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if(pw.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "비밀번호를 입력해주세요.",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        // 비밀번호와 패스워드를 검증한다. 현재는 임시 코드
        if(id.equals("user") && pw.equals("1234")) {
            // 아이디와 비밀번호가 맞는 경우
            return true;
        } else {
            // 아이디와 비밀번호가 다른 경우
            return false;
        }
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
