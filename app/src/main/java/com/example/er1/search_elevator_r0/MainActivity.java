package com.example.er1.search_elevator_r0;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogRecord;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    private EditText edit_text_search;
    private Button button_search;
    private TextView result_around_station;
    String search_station_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setClickListener();
        double[] a;
        a = getLocation();
        Log.v("現在地-----------------",""+a[0]+","+a[1] );
        getListDataApi ();
    }

    private void setViews() {
        edit_text_search = (EditText)findViewById(R.id.editText);
        button_search = (Button)findViewById(R.id.button);
      //  result_around_station = (TextView)findViewById(R.id.textView3);

    }

    View.OnClickListener search_button_listener = new View.OnClickListener() {
        public void onClick(View v) {
            search_station_name = edit_text_search.getText().toString();
            search(search_station_name);
        }
    };

    private void search(String search_station_name) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("searchSta",search_station_name);
        startActivity(intent);
    }

    private void setClickListener() {
        button_search.setOnClickListener(search_button_listener);
    }

    /**
     @auther takeno
     */
    private void getListDataApi () {
        int netStatus = Util.isConnectedNetwork(this);

        if (netStatus > 0) {

            double[] gspinfo = getLocation();

            if (gspinfo == null) {
                Log.v("hello", "couldn't get location data.");

            } else {


                double lat = gspinfo[0];
                double lon = gspinfo[1];

                String api_url = "http://map.simpleapi.net/stationapi?y="+lat+"&x="+lon;
                Thread mythread = new Thread(new MythreadGetListData(api_url));
                mythread.start();

            }


        }

    }
    /**
     *  現在地の位置情報を取得する
     * @return
     * @auther takeno
     */
    public double[] getLocation() {

        double[] locationInfo = new double[2];
        LocationManager locationManager;
        Location location = null;

        try {
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                if (isNetworkEnabled) {
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();

                            locationInfo[0] = lat;
                            locationInfo[1] = lon;

                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                double lat = location.getLatitude();
                                double lon = location.getLongitude();

                                locationInfo[0] = lat;
                                locationInfo[1] = lon;

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationInfo;
    }

    /**
     * 位置情報を元に最寄駅一覧を取得する
     * @auther takeno
     */
    private class MythreadGetListData implements Runnable {

        private String responce_xml;
        private String url = "";
        public String[] output_data = new String[100];

        //MyThread用-----------------------------------
        protected Handler mHandlerLatLng = new Handler() {
            @Override
            public void handleMessage(Message msg) {
//                v("api_responce", "::::" + (String) msg.obj + ":");
            }
        };

        public MythreadGetListData(String url) {
            // TODO 自動生成されたコンストラクター・スタブ
            this.url = url;
           // this.url = "http://map.simpleapi.net/stationapi?y=35.658889&x=139.703333";
        }

        public void run() {

            HttpRequest hr = new HttpRequest();
            responce_xml = hr.doGet(this.url);

            // ハンドラにメッセージを通知
            mHandlerLatLng.sendEmptyMessage(0);
            mHandlerLatLng.post(new Runnable() {

                public void run() {
                    InputStream bais = null;
                    try {
                        bais = new ByteArrayInputStream(responce_xml.getBytes("utf-8"));

                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Node root = builder.parse(bais);

                        Node child = root.getFirstChild();

                        NodeList gchildren = child.getChildNodes();
                        for (int i = 0; i < gchildren.getLength(); i++) {


                            //-------------------------
                            //抜き出した要素の中から順番に、子要素を抜く出す
                            NodeList list = gchildren.item(i).getChildNodes();
                            for (int j = 0, k = 0,r=0; j < list.getLength(); j++)
                            {

                                //抜き出した子要素の中から順番に取りだし、空白であれば登録しない
                                String text = list.item(j).getNodeName();

                                if (text.equals("#text") == false)
                                {


                                    String value = list.item(j).getTextContent().trim();
                                    if (text.equals("name"))
                                    {
                                        Log.v("hello_name", "名前：" + value + ":");
                                        output_data[k] = "" + value;
                                    }

                                    if (text.equals("line"))
                                    {
                                        Log.v("hello_line", "路線：" + value + ":");
            //                            output_data [r]= "路線：" + value+"/";
                                    }

                                    if (text.equals("distanceM"))
                                    {
                                        Log.v("hello_distanceM", "距離：" + value + ":");
  //                                      output_data += "距離：" + value+"/";
                                    }
                                    if (text.equals("traveltime"))
                                    {
                                        Log.v("hello_traveltime", "時間：" + value + ":");
    //                                    output_data += "時間：" + value+"\n\n\n";
                                    }
                                    k++;
                                }
                            }
                        }
                      Toast toast = Toast.makeText(getApplicationContext(), output_data[0],Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT, 0, 0);
                        toast.show();

                //       result_around_station.setText(bais);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        public void setOutput_data(String[] output_data) {
            this.output_data = output_data;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
