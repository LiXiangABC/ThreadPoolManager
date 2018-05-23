package com.example.threadpoolmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2018/5/16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {


    public NetworkChangeReceiver(networkChangeListener networkChangeListener){
        this.networkChangeListener = networkChangeListener;
    }

    public interface networkChangeListener{
        public void networkChange(NetworkType networkType);
    }

    private networkChangeListener networkChangeListener ;
    /**@author LiXiang create at 2018/5/16 15:07*/
    /**Explain : 不同网络状态下的枚举类型
     *
     */
    public enum NetworkType{
        WIFI,
        _4G,
        _3G,
        _2G,
        NO_NETWORK
    }

    /**        Explain : 当前的网络状态
    * @author LiXiang create at 2018/5/16 15:07*/
    public static  NetworkType NetworkState ;

    /**        Explain : 默认网络状态,当前4G普及率高,所以默认设定为4G
    * @author LiXiang create at 2018/5/16 15:08*/
    public static NetworkType DEFAULT_NETWORK_STATE = NetworkType._4G;


    /**        Explain : 对外提供获取网络状态
    * @author LiXiang create at 2018/5/16 15:06*/
    public static NetworkType getNetworkState(){
        if (NetworkState != null) {
            return NetworkState;
        }else {

            return DEFAULT_NETWORK_STATE;
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        getNetworkState(connectivityManager.getActiveNetworkInfo());
    }

    /**        Explain : 获取当前的网络状态
    * @author LiXiang create at 2018/5/16 14:45*/
    private void getNetworkState(NetworkInfo info) {
        /**        Explain : 在WLAN设置界面

         1，显示连接已保存，但标题栏没有，即没有实质连接上; 输出为：not connect， available

         2，显示连接已保存，标题栏也有已连接上的图标;       输出为：connect， available

         3，选择不保存后;                                   输出为：not connect， available

         4，选择连接，在正在获取IP地址时;                   输出为：not connect， not available

         5，连接上后;                                       输出为：connect， avai
        * @author LiXiang create at 2018/5/21 14:06*/
        if (info == null || !info.isConnectedOrConnecting()|| !info.isAvailable()) {
            networkChangeListener.networkChange(NetworkState = NetworkType.NO_NETWORK);
            return;
        }
        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_WIMAX:
            case ConnectivityManager.TYPE_ETHERNET:
                networkChangeListener.networkChange(NetworkState = NetworkType.WIFI);
                break;
            case ConnectivityManager.TYPE_MOBILE:
                switch (info.getSubtype()) {
//                    4G
                    case TelephonyManager.NETWORK_TYPE_LTE:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        networkChangeListener.networkChange(NetworkState = NetworkType._4G);
                        break;
//                        3G
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        networkChangeListener.networkChange(NetworkState = NetworkType._3G);
                        break;
//                        2G
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        networkChangeListener.networkChange(NetworkState = NetworkType._2G);
                        break;
                    default:
                        networkChangeListener.networkChange(NetworkState = NetworkType._4G);

                }
                break;
            default:
                networkChangeListener.networkChange(NetworkState = NetworkType._4G);
        }

    }
}
