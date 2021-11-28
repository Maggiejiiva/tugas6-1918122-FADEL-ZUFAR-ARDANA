package com.example.tugas6;


import android.content.Context;
import android.os.Looper;
import android.os.Handler;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
public class PeriodicWork extends Worker {
    private long timeMillis;
    private static int counter = 0;
    private Handler handler;
    private Context context;
    public static boolean isStopped;

    public PeriodicWork(
            @NonNull Context context, @NonNull
            WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        handler = new Handler(Looper.getMainLooper());
        setProgressAsync(new Data.Builder().putString("Proses", "Waktu" + 0 + "_" + "Counter: " + 0).build());
        isStopped = false;
    }

    @NonNull
    @Override
    public Result doWork() {
        timeMillis = System.currentTimeMillis();
        counter++;
        setProgressAsync(new Data.Builder().putString("Proses",
                "Waktu: " + timeMillis + "_" + "Counter: " + counter).build());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Waktu: " + timeMillis + "_&_" + "Counter: " + counter,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return Result.success();
    }
}
