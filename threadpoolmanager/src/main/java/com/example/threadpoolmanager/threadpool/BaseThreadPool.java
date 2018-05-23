package com.example.threadpoolmanager.threadpool;

import com.example.threadpoolmanager.utils.CPUHelp;
import com.example.threadpoolmanager.utils.Logs;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2018/5/17.
 */

public class BaseThreadPool extends ThreadPoolExecutor{

    /**        Explain : 核心线程池大小
    * @author LiXiang create at 2018/5/18 15:43*/
    protected   int BASETHREADPOOL_CORE_POOL_SIZE = 4;

    /**        Explain : 最大线程池队列大小
    * @author LiXiang create at 2018/5/18 15:44*/
    private int BASETHREADPOOL_MAXIMUM_POOL_SIZE = 256;

    /**        Explain : 保持存活时间，当线程数大于corePoolSize的空闲线程能保持的最大时间。
    * @author LiXiang create at 2018/5/18 15:44*/
    private long BASETHREADPOOL_KEEP_ALIVE = 1;



//    private static final Priority DEFAULT_PRIORITY = Priority.NORMAL;


    /**        Explain : 为加入的Runnable任务添加ID值,作为识别码
    * @author LiXiang create at 2018/5/18 15:45*/
    private static final AtomicLong SEQ_SEED = new AtomicLong(0);


    public BaseThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        BASETHREADPOOL_CORE_POOL_SIZE = corePoolSize;
        BASETHREADPOOL_MAXIMUM_POOL_SIZE = maximumPoolSize;
        BASETHREADPOOL_KEEP_ALIVE = keepAliveTime;
    }


    /**
     * 线程优先级
     */
    public enum Priority {
        HIGH, NORMAL, LOW
    }

    /**
     * 带有优先级的Runnable类型
     */
     public static class PriorityRunnable implements Runnable {

        public final Priority priority;//任务优先级
        private final Runnable runnable;//任务真正执行者
        long SEQ;//任务唯一标示

        public PriorityRunnable(Priority priority, Runnable runnable) {
            this.priority = priority == null ? Priority.NORMAL : priority;
            this.runnable = runnable;
        }

        @Override
        public final void run() {
            this.runnable.run();
        }
    }

    /**
     * 创建线程工厂
     */
    public static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "ThreadPoolManager #" + mCount.getAndIncrement());
        }
    };


    public static final Comparator<Runnable> FIFO = new Comparator<Runnable>(){
        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            if (lhs instanceof PriorityRunnable && rhs instanceof PriorityRunnable) {
                PriorityRunnable lpr = ((PriorityRunnable) lhs);
                PriorityRunnable rpr = ((PriorityRunnable) rhs);
                /**        Explain : ordinal() : 获取当前枚举类型的序列值
                * @author LiXiang create at 2018/5/18 15:57*/
                int result = lpr.priority.ordinal() - rpr.priority.ordinal();

                /**        Explain : 当A>B,且返回>0,则是升序
                * @author LiXiang create at 2018/5/18 15:58*/
                return result == 0 ? (int) (lpr.SEQ - rpr.SEQ) : result;
            } else {
                return 0;
            }
        }
    };


    /**
     * 线程队列方式 后进先出
     */
    public static final Comparator<Runnable> LIFO = new Comparator<Runnable>() {
        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            if (lhs instanceof PriorityRunnable && rhs instanceof PriorityRunnable) {
                PriorityRunnable lpr = ((PriorityRunnable) lhs);
                PriorityRunnable rpr = ((PriorityRunnable) rhs);
                /**        Explain : ordinal() : 获取当前枚举类型的序列值
                 * @author LiXiang create at 2018/5/18 15:57*/
                int result = lpr.priority.ordinal() - rpr.priority.ordinal();

                /**        Explain : 当A>B,且返回>0,则是升序
                 * @author LiXiang create at 2018/5/18 15:58*/
                return result == 0 ? (int) (rpr.SEQ - lpr.SEQ) : result;
            } else {
                return 0;
            }
        }
    };



    /**
     * 提交任务
     * @param runnable
     */
    @Override
    public void execute(Runnable runnable) {
        if (runnable instanceof PriorityRunnable) {
            ((PriorityRunnable) runnable).SEQ = SEQ_SEED.getAndIncrement();
        }
        super.execute(runnable);
    }


    /**        Explain : 计算线程池数量
    * @author LiXiang create at 2018/5/18 17:17*/
    public static int calculateBestThreadCount(int maximumPoolSize){
        /**        Explain : 获取手机所有内核
        * @author LiXiang create at 2018/5/20 18:24*/
        int allCores = CPUHelp.getAllCores();

        Logs.Log("CPUAllCores",allCores+"");

        /**        Explain : 获取可用内核
        * @author LiXiang create at 2018/5/20 18:24*/
        int availableCores = CPUHelp.getAvailableCores();
        Logs.Log("CPUavailableCores",availableCores+"");
        Logs.Log("CPUBestCores",Math.min(maximumPoolSize,Math.max(allCores,availableCores))+"");
        return Math.min(maximumPoolSize,Math.max(allCores,availableCores));
    }


}
