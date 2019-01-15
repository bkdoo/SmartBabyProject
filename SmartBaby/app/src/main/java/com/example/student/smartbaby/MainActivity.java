package com.example.student.smartbaby;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.student.smartbaby.adapter.ListViewAdapter;
import com.example.student.smartbaby.form.ListViewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {

    BootstrapButton btn_setUp, btn_renew, btn_dsleep, btn_nsleep, btn_sleep;
    final static String URL_DATA = "http://70.12.110.69:8090/smartbaby/board/android/list";
    final static String URL_SLEEP = "http://70.12.110.69:8090/smartbaby/board/android/create";

    ListView lv_sleep;
    ListViewAdapter listViewAdapter;
    ArrayList<ListViewItem> arrayList;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences_setup;
    String userId;
    boolean isSleeping;
    boolean isBluetoothOn, isVibratorOn;

    BluetoothSPP bluetoothSPP;
    RequestQueue mainQueue;
    RequestQueue blueQueue;

    StringRequest mainRequest;
    Vibrator vibrator;
    public static Activity _Main_Activity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _Main_Activity = MainActivity.this;

        btn_setUp = (BootstrapButton) findViewById(R.id.btn_setUp);
        btn_renew = (BootstrapButton) findViewById(R.id.btn_renew);
        btn_dsleep = (BootstrapButton) findViewById(R.id.btn_dsleep);
        btn_nsleep = (BootstrapButton) findViewById(R.id.btn_nsleep);
        btn_sleep = (BootstrapButton) findViewById(R.id.btn_sleep);
        lv_sleep = (ListView) findViewById(R.id.lv_sleep);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        isSleeping = false;

        sharedPreferences = getSharedPreferences("login_info", Context.MODE_PRIVATE);

        userId = sharedPreferences.getString("autoLogin" , "");

        sharedPreferences_setup = getSharedPreferences("setup_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor setupEditor = sharedPreferences_setup.edit();
        isBluetoothOn = sharedPreferences_setup.getBoolean("bluetooth", true);
        isVibratorOn = sharedPreferences_setup.getBoolean("vibrator", true);
        setupEditor.putBoolean("bluetooth", isBluetoothOn);
        setupEditor.putBoolean("vibrator", isVibratorOn);



        bluetoothSPP = new BluetoothSPP(MainActivity.this);
        if (!(bluetoothSPP.getServiceState() == BluetoothState.STATE_CONNECTED)) {
            Intent intent = new Intent(getApplicationContext(), DeviceList.class);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        }



        if (!bluetoothSPP.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }





        blueQueue = Volley.newRequestQueue(MainActivity.this);

        bluetoothSPP.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                Log.d("sleeping_response", testboolean());

                Log.d("arduino_response", message);
                if (!isSleeping){
                    if (message.equals("sleeping")) {
                        StringRequest blueRequest = new StringRequest(Request.Method.POST, URL_SLEEP,
                                // 요청 성공시
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("Listener_response", response);
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
                                params.put("userId", userId);
                                params.put("flag", "1");
                                return params;
                            }

                        };
                        isSleeping = true;
                        blueQueue.add(blueRequest);
                    }
                }

                if (isSleeping) {
                    if (message.equals("wake")) {
                        StringRequest blueRequest = new StringRequest(Request.Method.POST, URL_SLEEP,
                                // 요청 성공시
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("Listener_response", response);
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
                                params.put("userId", userId);
                                params.put("flag", "2");
                                return params;
                            }

                        };
                        blueQueue.add(blueRequest);
                        isSleeping = false;
                        bluetoothSPP.send("1", true);
                        isVibratorOn = sharedPreferences_setup.getBoolean("vibrator", true);
                        if (isVibratorOn){
                            vibrator.vibrate(new long[]{2000, 2000, 2000, 2000, 2000, 2000}, -1);
                        }
                    }
                }
            }
        });





        btn_setUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(intent);

            }
        });




        arrayList = new ArrayList<>();
        //testData();


        listViewAdapter = new ListViewAdapter(MainActivity.this, R.layout.list_view_item, arrayList);
        lv_sleep.setAdapter(listViewAdapter);

        lv_sleep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentToDetail = new Intent(getApplicationContext(), DetailListActivity.class);
                intentToDetail.putExtra("boardId", arrayList.get(position).getBoardId());
                intentToDetail.putExtra("regDate", arrayList.get(position).getRegDate());
                intentToDetail.putExtra("sleepTime", arrayList.get(position).getSleepTime());
                intentToDetail.putExtra("wakeupTime", arrayList.get(position).getWakeupTime());
                intentToDetail.putExtra("totalTime", arrayList.get(position).getTotalTime());
                intentToDetail.putExtra("dayNight", arrayList.get(position).getDayNight());
                intentToDetail.putExtra("memo", arrayList.get(position).getMemo());
                startActivity(intentToDetail);
            }
        });

        mainQueue = Volley.newRequestQueue(this);

        requestList();

        btn_renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestList();
            }
        });
        btn_dsleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDayList();
            }
        });
        btn_nsleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNightList();
            }
        });
        btn_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestList();
            }
        });

        bluetoothSPP.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
                isBluetoothOn = true;

            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestList() {
        mainRequest = new StringRequest(Request.Method.POST, URL_DATA,
                // 요청 성공시
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Listresponse", response);
                        JSONObject root;
                        JSONArray jsonArray;
                        try {
                            jsonArray = new JSONArray(response);
                            arrayList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                root = jsonArray.getJSONObject(i);
                                String boardId = root.getString("boardId");
                                String date = root.getString("regDateStr");
                                String wakeupTime = root.getString("wakeupTime");
                                String sleepTime = root.getString("sleepTime");
                                String totalTime = root.getString("totalTime");
                                String dayNight = root.getString("dayNight");
                                String memo = root.getString("memo");
                                arrayList.add(new ListViewItem(boardId, date, sleepTime, wakeupTime, totalTime, dayNight, memo));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listViewAdapter.notifyDataSetChanged();
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
                params.put("userId", userId);
                return params;
            }

        };

        mainQueue.add(mainRequest);
    }
    private void requestDayList() {
        mainRequest = new StringRequest(Request.Method.POST, URL_DATA,
                // 요청 성공시
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DayList_response", response);
                        JSONObject root;
                        JSONArray jsonArray;
                        try {
                            jsonArray = new JSONArray(response);
                            arrayList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                root = jsonArray.getJSONObject(i);
                                String dayNight = root.getString("dayNight");
                                if (!dayNight.equals("DAY")) {
                                    continue;
                                }
                                String boardId = root.getString("boardId");
                                String date = root.getString("regDateStr");
                                String wakeupTime = root.getString("wakeupTime");
                                String sleepTime = root.getString("sleepTime");
                                String totalTime = root.getString("totalTime");
                                String memo = root.getString("memo");
                                arrayList.add(new ListViewItem(boardId, date, sleepTime, wakeupTime, totalTime, dayNight, memo));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listViewAdapter.notifyDataSetChanged();
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
                params.put("userId", userId);
                return params;
            }

        };

        mainQueue.add(mainRequest);
    }
    private void requestNightList() {
        mainRequest = new StringRequest(Request.Method.POST, URL_DATA,
                // 요청 성공시
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("NIGHTList_response", response);
                        JSONObject root;
                        JSONArray jsonArray;
                        try {
                            jsonArray = new JSONArray(response);
                            arrayList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                root = jsonArray.getJSONObject(i);
                                String dayNight = root.getString("dayNight");
                                if (!dayNight.equals("NIGHT")){
                                    continue;
                                }
                                String boardId = root.getString("boardId");
                                String date = root.getString("regDateStr");
                                String wakeupTime = root.getString("wakeupTime");
                                String sleepTime = root.getString("sleepTime");
                                String totalTime = root.getString("totalTime");
                                String memo = root.getString("memo");
                                arrayList.add(new ListViewItem(boardId, date, sleepTime, wakeupTime, totalTime, dayNight, memo));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listViewAdapter.notifyDataSetChanged();
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
                params.put("userId", userId);
                return params;
            }

        };

        mainQueue.add(mainRequest);
    }



    @Override
    protected void onResume() {
        super.onResume();
        requestList();
        listViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!bluetoothSPP.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bluetoothSPP.isServiceAvailable()) {
                bluetoothSPP.setupService();
                bluetoothSPP.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bluetoothSPP.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetoothSPP.setupService();
                bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }



    private String testboolean(){
        if (isSleeping) {
            return "true";
        } else {
            return "false";
        }
    }

}
