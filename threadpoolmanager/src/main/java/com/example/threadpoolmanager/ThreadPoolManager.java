package com.example.threadpoolmanager;

import android.content.Context;
import android.content.IntentFilter;

import com.example.threadpoolmanager.receiver.NetworkChangeReceiver;
import com.example.threadpoolmanager.threadpool.BaseThreadPool;
import com.example.threadpoolmanager.utils.Logs;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/5/16.
 */

public class ThreadPoolManager {

    public static boolean isDebug  = true;
    private static ThreadPoolManager threadPoolManager;
    private static BaseThreadPool imageLoadTheadPoll;
    private static BaseThreadPool downLoadTheadPoll;
    private final Context context;
    private NetworkChangeReceiver networkChangeReceiver;

    /**        Explain : 加载图片核心线程池大小
     * @author LiXiang create at 2018/5/18 15:43*/
    private  static int IMAGELOAD_CORE_POOL_SIZE = 4;

    /**        Explain : 加载图片最大线程池大小
     * @author LiXiang create at 2018/5/18 15:44*/
    private static int IMAGELOAD_MAXIMUM_POOL_SIZE = 4;


    /**        Explain : 加载图片最大线程池队列大小
    * @author LiXiang create at 2018/5/20 18:18*/
    private static int IMAGELOAD_MAXIMUM_BLOCKING_QUEUE_SIZE = 256;

    /**        Explain : 加载图片保持存活时间，当线程数大于corePoolSize的空闲线程能保持的最大时间。
     * @author LiXiang create at 2018/5/18 15:44*/
    private static long IMAGELOAD_KEEP_ALIVE = 0L;


    /**        Explain : 文件下载核心线程池大小
     * @author LiXiang create at 2018/5/18 15:43*/
    private  static int DOWNLOAD_CORE_POOL_SIZE = 0;

    /**        Explain : 文件下载最大线程池大小
     * @author LiXiang create at 2018/5/18 15:44*/
    private static int DOWNLOAD_MAXIMUM_POOL_SIZE = 4;

    /**        Explain : 文件下载最大线程池队列大小
    * @author LiXiang create at 2018/5/20 18:18*/
    private static int DOWNLOAD_MAXIMUM_BLOCKING_QUEUE_SIZE = 256;

    /**        Explain : 文件下载保持存活时间，当线程数大于corePoolSize的空闲线程能保持的最大时间。
     * @author LiXiang create at 2018/5/18 15:44*/
    private static long DOWNLOAD_KEEP_ALIVE = 1L;

    /**@author LiXiang create at 2018/5/16 16:50*/
    /**Explain :
     *
     * @param context :上下文
     */
    private ThreadPoolManager(Context context){
        this.context = context;
        initReceiver();
    }




    /**        Explain : 创建图片加载线程池
    * @author LiXiang create at 2018/5/20 15:57*/
    public static  BaseThreadPool newImageLoadTheadPoll() {
        synchronized(ThreadPoolManager.class){
        if (imageLoadTheadPoll == null) {
        imageLoadTheadPoll = new BaseThreadPool(BaseThreadPool.calculateBestThreadCount(IMAGELOAD_CORE_POOL_SIZE) , BaseThreadPool.calculateBestThreadCount(IMAGELOAD_MAXIMUM_POOL_SIZE), IMAGELOAD_KEEP_ALIVE, TimeUnit.MINUTES, new PriorityBlockingQueue<Runnable>(IMAGELOAD_MAXIMUM_BLOCKING_QUEUE_SIZE,BaseThreadPool.LIFO), BaseThreadPool.sThreadFactory);
        }
        return  imageLoadTheadPoll;
        }
    }

    /**        Explain : 创建图片加载线程池
     * @param  fifo : 等待队列的是否为先进先出.
    * @author LiXiang create at 2018/5/20 16:08*/
    public static  BaseThreadPool newImageLoadTheadPoll(boolean fifo) {
        synchronized(ThreadPoolManager.class){
        if (imageLoadTheadPoll == null) {
            imageLoadTheadPoll = new BaseThreadPool(BaseThreadPool.calculateBestThreadCount(IMAGELOAD_CORE_POOL_SIZE) , BaseThreadPool.calculateBestThreadCount(IMAGELOAD_MAXIMUM_POOL_SIZE), IMAGELOAD_KEEP_ALIVE, TimeUnit.MINUTES, new PriorityBlockingQueue<Runnable>(IMAGELOAD_MAXIMUM_BLOCKING_QUEUE_SIZE,fifo ? BaseThreadPool.FIFO : BaseThreadPool.LIFO), BaseThreadPool.sThreadFactory);
        }
        return  imageLoadTheadPoll;
        }
    }


    /**@author LiXiang create at 2018/5/20 16:12*/
    /**Explain : 自定义创建图片加载线程池
     * @param corePoolSize 核心线程池数量
     * @param maximumPoolSize 最大线程池数量
     * @param keepAliveTime 非核心线程休闲时存活时间
     * @param unit 存活时间单位
     * @param workQueue 任务队列
     * @param threadFactory 线程工厂
     * @return
     */
    public static  BaseThreadPool newImageLoadTheadPoll(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        synchronized(ThreadPoolManager.class){
        if (imageLoadTheadPoll == null) {
            imageLoadTheadPoll = new BaseThreadPool(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }
        return  imageLoadTheadPoll;
        }
    }


    /**        Explain : 创建文件下载线程池
     * @author LiXiang create at 2018/5/20 15:57*/
    public static  BaseThreadPool newDownLoadTheadPoll() {
        synchronized(ThreadPoolManager.class){
        if (downLoadTheadPoll == null) {
            downLoadTheadPoll = new BaseThreadPool(DOWNLOAD_CORE_POOL_SIZE , BaseThreadPool.calculateBestThreadCount(DOWNLOAD_MAXIMUM_POOL_SIZE), DOWNLOAD_KEEP_ALIVE, TimeUnit.MINUTES, new PriorityBlockingQueue<Runnable>(DOWNLOAD_MAXIMUM_BLOCKING_QUEUE_SIZE,BaseThreadPool.LIFO), BaseThreadPool.sThreadFactory);
        }
        return  downLoadTheadPoll;
        }
    }

    /**        Explain : 创建文件下载线程池
     * @param  fifo : 等待队列的是否为先进先出.
     * @author LiXiang create at 2018/5/20 16:08*/
    public static  BaseThreadPool newDownLoadTheadPoll(boolean fifo) {
        synchronized(ThreadPoolManager.class){
        if (downLoadTheadPoll == null) {
            downLoadTheadPoll = new BaseThreadPool(DOWNLOAD_CORE_POOL_SIZE, BaseThreadPool.calculateBestThreadCount(DOWNLOAD_MAXIMUM_POOL_SIZE), DOWNLOAD_KEEP_ALIVE, TimeUnit.MINUTES, new PriorityBlockingQueue<Runnable>(DOWNLOAD_MAXIMUM_BLOCKING_QUEUE_SIZE,fifo ? BaseThreadPool.FIFO : BaseThreadPool.LIFO), BaseThreadPool.sThreadFactory);
        }
        return  downLoadTheadPoll;
        }
    }


    /**@author LiXiang create at 2018/5/20 16:12*/
    /**Explain : 自定义文件下载线程池
     * @param corePoolSize 核心线程池数量
     * @param maximumPoolSize 最大线程池数量
     * @param keepAliveTime 非核心线程休闲时存活时间
     * @param unit 存活时间单位
     * @param workQueue 任务队列
     * @param threadFactory 线程工厂
     * @return
     */
    public static  BaseThreadPool newDownLoadTheadPoll(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        synchronized(ThreadPoolManager.class){
        if (downLoadTheadPoll == null) {
            downLoadTheadPoll = new BaseThreadPool(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }
        }
        return  downLoadTheadPoll;
    }






    /**        Explain : 对外提供使用对象
     * @param context :上下文
    * @author LiXiang create at 2018/5/16 19:14*/
    public static ThreadPoolManager getInstance(Context context){
        synchronized(ThreadPoolManager.class){
        if (threadPoolManager == null) {
        threadPoolManager = new ThreadPoolManager(context);
        }
        }
        return threadPoolManager;
    }

    /**        Explain : 接受动态网络状态的改变监听
    * @author LiXiang create at 2018/5/20 18:11*/
    public NetworkChangeReceiver.networkChangeListener networkChangeListener = networkType -> {
        int threadCount = 1;
        switch (networkType) {
            case WIFI:
                threadCount = 4;
                Logs.Log("NetworkChange","WIFI");
                break;
            case _4G:
                threadCount = 4;
                Logs.Log("NetworkChange","_4G");
                break;
            case _3G:
                threadCount = 3;
                Logs.Log("NetworkChange","_3G");
                break;
            case _2G:
                threadCount = 2;
                Logs.Log("NetworkChange","_2G");
                break;
            case NO_NETWORK:
                Logs.Log("NetworkChange","NO_NETWORK");
                break;
        }
        changeCorePoolSize(threadCount);
    };


    /**        Explain : 改变线程池数量
    * @author LiXiang create at 2018/5/20 18:07*/
    private void changeCorePoolSize(int threadCount) {
        if (downLoadTheadPoll != null) {
            downLoadTheadPoll.setMaximumPoolSize(threadCount);
        Logs.Log("DownLoadTheadCorePollSize",downLoadTheadPoll.getCorePoolSize()+"");
        Logs.Log("DownLoadTheadPollSize",downLoadTheadPoll.getMaximumPoolSize()+"");
        }
        if (imageLoadTheadPoll != null) {
            imageLoadTheadPoll.setCorePoolSize(threadCount);
            imageLoadTheadPoll.setMaximumPoolSize(threadCount);
        Logs.Log("ImageLoadTheadCorePollSize",imageLoadTheadPoll.getCorePoolSize()+"");
        Logs.Log("ImageLoadTheadPollSize",imageLoadTheadPoll.getMaximumPoolSize()+"");
        }
    }

    /**        Explain : 初始化监听网络动态改变的广播
    * @author LiXiang create at 2018/5/16 19:07
     * */
    private void initReceiver() {
        networkChangeReceiver = new NetworkChangeReceiver(networkChangeListener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(networkChangeReceiver,intentFilter);
    }


    /**        Explain : 终止线程池的使用
    * @author LiXiang create at 2018/5/16 19:23*/
    public void terminateThreadPool(){
        if (downLoadTheadPoll != null) {
        downLoadTheadPoll.shutdown();
        }
        if (imageLoadTheadPoll != null) {
        imageLoadTheadPoll.shutdown();
        }
        context.unregisterReceiver(networkChangeReceiver);
    }


}
