package com.ishikawa.er1.search_elevator_r0;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ishikawa.er1.search_elevator_r0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ResultActivity extends AppCompatActivity {
    TextView search_station_name_text;
    TextView result_textview;
    String search_station_name;
    private JSONObject jsonObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setViews();
        Intent intent = getIntent();
        search_station_name = intent.getStringExtra("searchSta");
        search_station_name_text.setText(search_station_name);
        getStationCodefromApi_and_set_elev_info(search_station_name);
    }

    private void setViews() {
        search_station_name_text = (TextView)findViewById(R.id.textView5);
        result_textview = (TextView)findViewById(R.id.textView4);
    }

    private void getStationCodefromApi_and_set_elev_info (String station_name) {
        byte[] strByte = new byte[0];
        String station_name_utf8 = "";
        try {
            strByte = station_name.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            station_name_utf8 = new String(strByte, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http");
        uri.authority("api.ekispert.com");
        uri.path("v1/json/station");
        uri.appendQueryParameter("key", "Tz7zMBQarrxLSZf3");
        uri.appendQueryParameter("oldName", station_name_utf8);

        if (Util.isConnectedNetwork(this) > 0) {
            Log.v("URI------------------", "::::" + uri.toString() + ":");
            Thread threadNews = new Thread(new MythreadGetListData_for_station_code(uri.toString()));
            threadNews.start();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "ネットワークに繋がっておりません。", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private class MythreadGetListData_for_station_code implements Runnable {

        private String responce_json;
        private String url = "";

        protected Handler mHandlerLatLng = new Handler() {
            @Override
            public void handleMessage(Message msg) {
//                v("api_responce", "::::" + (String) msg.obj + ":");


            }
        };

        //
        public MythreadGetListData_for_station_code(String url) {
            // TODO 自動生成されたコンストラクター・スタブ
            this.url = url;
        }

        //Httprequestと同期し、JSONの値を返す
        public void run() {

            HttpRequest hr = new HttpRequest();
            responce_json = hr.doGet(this.url);


            // ハンドラにメッセージを通知
            mHandlerLatLng.sendEmptyMessage(0);
            mHandlerLatLng.post(new Runnable() {

                public void run() {

                    try {

                        if (responce_json != null) {
                            jsonObj = new JSONObject(responce_json);

                            getJson_station_code(jsonObj);

//                            JSONObject resultsObj = addr_json.getJSONObject("Results");
//                            JSONArray resultsArg = resultsObj.getJSONArray("Data");
                        }

                    } catch (JSONException e) {
                        // TODO 自動生成された catch ブロック
                        e.printStackTrace();

                    }


                }

            });

        }
    }

    private void getJson_station_code(JSONObject jsonObj) {
        try {
            // 各 ATND イベントのタイトルを配列へ格納
            JSONObject resultObj = jsonObj.getJSONObject("ResultSet");
            // Log.d("------------JSON-------", resultObj.get(0).toString());
            JSONObject PointObj = resultObj.getJSONObject("Point");
            JSONObject StationObj = (JSONObject)PointObj.getJSONObject("Station");
            //JSONObject welfareFacilitiesObj= (JSONObject)InformationArray.get(3);
            //JSONArray elevatorArray = welfareFacilitiesObj.getJSONArray("1");
            //Log.d("------------JSON-------", elevatorArray.getString(1));
            int station_code = StationObj.getInt("code");
            getJSONObjectfromApi_Take(station_code);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("------JSON--------", "データを取得できませんでした。");
        }
    }

    private void getJSONObjectfromApi_Take (int station_code) {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http");
        uri.authority("api.ekispert.com");
        uri.path("v1/json/station/info");
        uri.appendQueryParameter("key", "Tz7zMBQarrxLSZf3");
        uri.appendQueryParameter("type", "rail:nearrail:exit:welfare");
        //uri.appendQueryParameter("name",station_name_utf8);
        uri.appendQueryParameter("code",""+station_code);

        if (Util.isConnectedNetwork(this) > 0) {
            Log.v("URI------------------", "::::" + uri.toString() + ":");
            Thread threadNews = new Thread(new MythreadGetListData(uri.toString()));
            threadNews.start();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "ネットワークに繋がっておりません。", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private class MythreadGetListData implements Runnable {

        private String responce_json;
        private String url = "";

        protected Handler mHandlerLatLng = new Handler() {
            @Override
            public void handleMessage(Message msg) {
//                v("api_responce", "::::" + (String) msg.obj + ":");
            }
        };

        //
        public MythreadGetListData(String url) {
            // TODO 自動生成されたコンストラクター・スタブ
            this.url = url;
        }
        //Httprequestと同期し、JSONの値を返す
        public void run() {
            HttpRequest hr = new HttpRequest();
            responce_json = hr.doGet(this.url);
            // ハンドラにメッセージを通知
            mHandlerLatLng.sendEmptyMessage(0);
            mHandlerLatLng.post(new Runnable() {

                public void run() {
                    try {

                        if (responce_json != null) {
                            jsonObj = new JSONObject(responce_json);

                            getJson(jsonObj);

//                            JSONObject resultsObj = addr_json.getJSONObject("Results");
//                            JSONArray resultsArg = resultsObj.getJSONArray("Data");
                        }

                    } catch (JSONException e) {
                        // TODO 自動生成された catch ブロック
                        e.printStackTrace();

                    }


                }

            });

        }
    }

    //えきすぱーとAPIのJSON-JSO情報から必要なエレベーター情報抜き出し
    private void getJson(JSONObject result) {
        try {
            // 各 ATND イベントのタイトルを配列へ格納
            JSONObject resultObj = result.getJSONObject("ResultSet");
            // Log.d("------------JSON-------", resultObj.get(0).toString());
            JSONArray InformationArray = resultObj.getJSONArray("Information");
            JSONObject ExitObj = (JSONObject)InformationArray.get(2);
            JSONObject welfareFacilitiesObj= (JSONObject)InformationArray.get(3);
            //JSONArray elevatorArray = welfareFacilitiesObj.getJSONArray("1");
            //Log.d("------------JSON-------", elevatorArray.getString(1));
            String type = welfareFacilitiesObj.getString("Type");
            JSONArray welfareFacilitiesArray = welfareFacilitiesObj.getJSONArray("WelfareFacilities");

            //countここで表示
            int count = welfareFacilitiesArray.length();
            for (int c = 0;c<count ;c++) {
                JSONObject welFarrObj = (JSONObject) welfareFacilitiesArray.get(1);
                String name = welFarrObj.getString("Name");
                String comment = welFarrObj.getString("Comment");
                // Log.d("------JSON--------","name:"+name+"||comment:"+comment);
                result_textview.setText(comment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("------JSON--------", "データを取得できませんでした。");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
