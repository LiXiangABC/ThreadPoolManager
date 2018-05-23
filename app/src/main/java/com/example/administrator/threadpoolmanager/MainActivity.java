package com.example.administrator.threadpoolmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.threadpoolmanager.ThreadPoolManager;
import com.example.threadpoolmanager.threadpool.BaseThreadPool;
import com.example.threadpoolmanager.utils.Logs;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ThreadPoolManager threadPoolManager = ThreadPoolManager.getInstance(this);

        BaseThreadPool baseThreadPool = threadPoolManager.newImageLoadTheadPoll();


        View start = findViewById(R.id.start);
        start.setOnClickListener(v -> {
            for (int i = 0; i < 20; i++) {
                int finalI = i;
                baseThreadPool.execute(new BaseThreadPool.PriorityRunnable(BaseThreadPool.Priority.NORMAL, new Runnable() {
                    @Override
                    public void run() {
                        Logs.Log("加载图片线程任务", finalI +"ThreadName: " + Thread.currentThread().getName());
                    }
                }));
            }
        });



    }
}
