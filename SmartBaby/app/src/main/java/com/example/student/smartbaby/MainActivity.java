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
    String userId;
    boolean isSleeping;

    BluetoothSPP bluetoothSPP;
    RequestQueue mainQueue;
    RequestQueue blueQueue;

    StringRequest mainRequest;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_setUp = (BootstrapButton) findViewById(R.id.btn_setUp);
        btn_renew = (BootstrapButton) findViewById(R.id.btn_renew);
        btn_dsleep = (BootstrapButton) findViewById(R.id.btn_dsleep);
        btn_nsleep = (BootstrapButton) findViewById(R.id.btn_nsleep);
        btn_sleep = (BootstrapButton) findViewById(R.id.btn_sleep);
        lv_sleep = (ListView) findViewById(R.id.lv_sleep);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        isSleeping = false;

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
                        vibrator.vibrate(new long[]{2000, 2000, 2000, 2000, 2000, 2000}, -1);
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

        sharedPreferences = getSharedPreferences("login_info", Context.MODE_PRIVATE);

        userId = sharedPreferences.getString("autoLogin", "");
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

    //    private void testData() {
//        String response = "[\n" +
//                "\t{\n" +
//                "\t\t\"id\": 1,\n" +
//                "\t\t\"regDate\": \"19/01/07\",\n" +
//                "\t\t\"sleepTime\": \"19/01/07 16:33:47\",\n" +
//                "\t\t\"wakeupTime\": \"19/01/07 17:55:14\",\n" +
//                "\t\t\"totalTime\": \"0\",\n" +
//                "\t\t\"dayNight\": \"DAY\",\n" +
//                "\t\t\"memo\": \"cursus in,\",\n" +
//                "\t\t\"updateDate\": \"19/01/07\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"id\": 2,\n" +
//                "\t\t\"regDate\": \"19/01/07\",\n" +
//                "\t\t\"sleepTime\": \"19/01/07 22:21:03\",\n" +
//                "\t\t\"wakeupTime\": \"19/01/08 03:51:56\",\n" +
//                "\t\t\"totalTime\": \"0\",\n" +
//                "\t\t\"dayNight\": \"NIGHT\",\n" +
//                "\t\t\"memo\": \"mattis velit\",\n" +
//                "\t\t\"updateDate\": \"19/01/08\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"id\": 3,\n" +
//                "\t\t\"regDate\": \"19/01/07\",\n" +
//                "\t\t\"sleepTime\": \"19/01/07 21:24:36\",\n" +
//                "\t\t\"wakeupTime\": \"19/01/08 04:18:22\",\n" +
//                "\t\t\"totalTime\": \"0\",\n" +
//                "\t\t\"dayNight\": \"NIGHT\",\n" +
//                "\t\t\"memo\": \"Nulla eget metus eu erat\",\n" +
//                "\t\t\"updateDate\": \"19/01/08\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"id\": 4,\n" +
//                "\t\t\"regDate\": \"19/01/07\",\n" +
//                "\t\t\"sleepTime\": \"19/01/07 06:35:29\",\n" +
//                "\t\t\"wakeupTime\": \"19/01/07 09:31:56\",\n" +
//                "\t\t\"totalTime\": \"0\",\n" +
//                "\t\t\"dayNight\": \"DAY\",\n" +
//                "\t\t\"memo\": \"sem, consequat nec, mollis\",\n" +
//                "\t\t\"updateDate\": \"19/01/07\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"id\": 5,\n" +
//                "\t\t\"regDate\": \"19/01/07\",\n" +
//                "\t\t\"sleepTime\": \"19/01/07 11:36:08\",\n" +
//                "\t\t\"wakeupTime\": \"19/01/07 14:40:16\",\n" +
//                "\t\t\"totalTime\": \"0\",\n" +
//                "\t\t\"dayNight\": \"DAY\",\n" +
//                "\t\t\"memo\": \"Suspendisse sed\",\n" +
//                "\t\t\"updateDate\": \"19/01/07\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"id\": 6,\n" +
//                "\t\t\"regDate\": \"19/01/07\",\n" +
//                "\t\t\"sleepTime\": \"19/01/07 16:29:03\",\n" +
//                "\t\t\"wakeupTime\": \"19/01/07 18:21:48\",\n" +
//                "\t\t\"totalTime\": \"0\",\n" +
//                "\t\t\"dayNight\": \"DAY\",\n" +
//                "\t\t\"memo\": \"enim. Mauris quis turpis\",\n" +
//                "\t\t\"updateDate\": \"19/01/07\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"id\": 7,\n" +
//                "\t\t\"regDate\": \"19/01/07\",\n" +
//                "\t\t\"sleepTime\": \"19/01/07 22:30:24\",\n" +
//                "\t\t\"wakeupTime\": \"19/01/08 05:26:30\",\n" +
//                "\t\t\"totalTime\": \"0\",\n" +
//                "\t\t\"dayNight\": \"NIGHT\",\n" +
//                "\t\t\"memo\": \"scelerisque mollis. Phasellus libero\",\n" +
//                "\t\t\"updateDate\": \"19/01/08\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"id\": 8,\n" +
//                "\t\t\"regDate\": \"19/01/07\",\n" +
//                "\t\t\"sleepTime\": \"19/01/07 14:04:36\",\n" +
//                "\t\t\"wakeupTime\": \"19/01/07 15:18:36\",\n" +
//                "\t\t\"totalTime\": \"0\",\n" +
//                "\t\t\"dayNight\": \"DAY\",\n" +
//                "\t\t\"memo\": \"eu dui. Cum\",\n" +
//                "\t\t\"updateDate\": \"19/01/07\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"id\": 9,\n" +
//                "\t\t\"regDate\": \"19/01/07\",\n" +
//                "\t\t\"sleepTime\": \"19/01/07 06:25:52\",\n" +
//                "\t\t\"wakeupTime\": \"19/01/07 12:32:00\",\n" +
//                "\t\t\"totalTime\": \"0\",\n" +
//                "\t\t\"dayNight\": \"DAY\",\n" +
//                "\t\t\"memo\": \"sapien, cursus in, hendrerit consectetuer,\",\n" +
//                "\t\t\"updateDate\": \"19/01/07\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"id\": 10,\n" +
//                "\t\t\"regDate\": \"19/01/07\",\n" +
//                "\t\t\"sleepTime\": \"19/01/07 20:08:01\",\n" +
//                "\t\t\"wakeupTime\": \"19/01/08 05:27:53\",\n" +
//                "\t\t\"totalTime\": \"0\",\n" +
//                "\t\t\"dayNight\": \"NIGHT\",\n" +
//                "\t\t\"memo\": \"at auctor ullamcorper, nisl\",\n" +
//                "\t\t\"updateDate\": \"19/01/08\"\n" +
//                "\t}\n" +
//                "]";
//        JSONObject root;
//        JSONArray jsonArray;
//        try {
//            jsonArray = new JSONArray(response);
//            for (int i = jsonArray.length()-1; i >=0 ; i--) {
//                root = jsonArray.getJSONObject(i);
//                int boardId = Integer.valueOf(root.getString("id"));
//                String date = root.getString("regDate");
//                String wakeupTime = root.getString("wakeupTime");
//                String sleepTime = root.getString("sleepTime");
//                String totalTime = root.getString("totalTime");
//                String dayNight = root.getString("dayNight");
//                String memo = root.getString("memo");
//                arrayList.add(new ListViewItem(boardId, date, wakeupTime, sleepTime, totalTime, dayNight, memo));
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    class BluetoothTask extends AsyncTask<Void, String, String> {
//
//        BluetoothSPP bluetoothSPP;
//
//        RequestQueue blueQueue;
//
//        @Override
//        protected void onPreExecute() {
//            bluetoothSPP = new BluetoothSPP(MainActivity.this);
//            blueQueue = Volley.newRequestQueue(MainActivity.this);
//
//
//        }
//
//        @Override
//        protected String doInBackground(Void... voids) {
//
//        }
//    }

    private String testboolean(){
        if (isSleeping) {
            return "true";
        } else {
            return "false";
        }
    }

}
