package com.example.student.smartbaby;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SetupActivity extends AppCompatActivity {

    Button btn_logout;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        btn_logout= (Button) findViewById(R.id.btn_logout);
        sharedPreferences = getSharedPreferences("login_info", Context.MODE_PRIVATE);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("autoLogin", "");
                editor.commit();

                Intent intentToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentToLogin);
            }
        });
    }
}
