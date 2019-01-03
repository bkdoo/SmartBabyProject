package com.example.student.smartbaby;

import android.os.AsyncTask;

import com.example.student.smartbaby.Model.Member;
import com.example.student.smartbaby.Model.Record;
import com.google.gson.Gson;

import java.util.Map;

public class Task extends AsyncTask <Map<String, String>, Integer, String>{

    // IP 추후 입력
    public static String ip = "아이피";


    @Override
    protected String doInBackground(Map<String, String>... maps) {

        HttpClient.Builder http = new HttpClient.Builder("POST", "http://" + ip + "포트번호,서블릿주소" );

        // Parameter 를 전송한다.
        http.addAllParameters(maps[0]);

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
        Record recordData = gson.fromJson(s, Record.class);
        Member memverDate = gson.fromJson(s, Member.class);
    }
}
