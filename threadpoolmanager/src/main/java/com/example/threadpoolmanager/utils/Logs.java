package com.example.threadpoolmanager.utils;

import android.util.Log;

import com.example.threadpoolmanager.ThreadPoolManager;

/**
 * Created by Administrator on 2018/5/21.
 */

public class Logs {

    public static void Log(String name, String value) {
        if (ThreadPoolManager.isDebug) {
            if (name != null && value != null) {
                Log.i("ThreadPoolManager  "+name, value);
            }
        }
    }
}
