package com.example.threadpoolmanager.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/5/16.
 */

public class CPUHelp {
    public static final String CPU_LOCATION = "/sys/devices/system/cpu/";

    public static final String CPU_NAME_REGEX = "cpu[0-9]+";

    public static int allCores = 0;
    public static int AvailableCores = 0;

    /**        Explain : 获取当前所有的内核数量
    * @author LiXiang create at 2018/5/16 16:46*/
    public static int getAllCores(){
        return allCores == 0 ? getCPUCoresCount():allCores;
    }


    /**        Explain : 一些设备根据系统负载已经关闭一个或多个内核的cpu，
     *                  对于这些设备，availableProcessors()返回的是可用的内核数，
     *                  这个数字一般小于内核总数：
     * @author LiXiang create at 2018/5/16 16:47*/
    public static int getAvailableCores(){
        return AvailableCores == 0 ? getAvailableCoresCount():AvailableCores;
    }


    public static int  getCPUCoresCount() {
        File[] cpus = null;
        try {
            File cpuInfo = new File(CPU_LOCATION);
            final Pattern cpuNamePattern = Pattern.compile(CPU_NAME_REGEX);
            cpus = cpuInfo.listFiles((file, s) -> cpuNamePattern.matcher(s).matches());
        }catch (Throwable t) {
        return  allCores = 1;
        }
        return  allCores = cpus.length;
    }


    public static int getAvailableCoresCount() {
        return  AvailableCores = Math.max(1,Runtime.getRuntime().availableProcessors());
    }

}
