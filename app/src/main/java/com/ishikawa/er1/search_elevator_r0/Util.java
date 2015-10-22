package com.ishikawa.er1.search_elevator_r0;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by er1 on 2015/09/30.
 */
public class Util {

    /*
     * ネットに繋がっているかどうかをチェックする
    */
    public static int isConnectedNetwork(Context context) {

        //ネットワーク接続チェック --------------------------------------------------------------
        int network = 0;
        // システムから接続情報をとってくる
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // モバイル回線（３G）の接続状態を取得
        NetworkInfo.State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        // wifiの接続状態を取得
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        //モバイル回線通信
        if (mobile == NetworkInfo.State.CONNECTED) {
            network = 1;

        } else if (wifi == NetworkInfo.State.CONNECTED) {
            //wifi接続
            network = 2;

        } else {
            // ネットワーク未接続
            network = 0;
        }

        return network;
    }
}
