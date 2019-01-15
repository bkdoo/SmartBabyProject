package com.example.student.smartbaby;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SetupActivity extends AppCompatActivity {

    Button btn_logout;
    Switch sw_vibrator, sw_bluetooth;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences_setup;
    boolean isBluetoothOn_setup, isVibratorOn_setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        btn_logout= (Button) findViewById(R.id.btn_logout);
        sw_bluetooth= (Switch) findViewById(R.id.sw_bluetooth);
        sw_vibrator= (Switch) findViewById(R.id.sw_vibrator);
        sharedPreferences = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences_setup = getSharedPreferences("setup_info", Context.MODE_PRIVATE);
        isBluetoothOn_setup = sharedPreferences_setup.getBoolean("bluetooth", true);
        isVibratorOn_setup = sharedPreferences_setup.getBoolean("vibrator", true);

        final SharedPreferences.Editor editor = sharedPreferences_setup.edit();

        sw_bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("bluetooth", isChecked);
                editor.apply();
            }
        });

        sw_vibrator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("vibrator", isChecked);
                editor.apply();
            }
        });

        sw_bluetooth.setChecked(isBluetoothOn_setup);
        sw_vibrator.setChecked(isVibratorOn_setup);



        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("autoLogin", "");
                editor.commit();

                Intent intentToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentToLogin);
                MainActivity ma = (MainActivity) MainActivity._Main_Activity;
                ma.finish();
                finish();
            }
        });
    }
}
