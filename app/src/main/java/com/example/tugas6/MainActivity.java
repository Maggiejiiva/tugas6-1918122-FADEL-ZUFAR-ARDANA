package com.example.tugas6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.BackoffPolicy;
import androidx.work.Data;
import androidx.work.Operation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button btnStartPeriodicWork,btnGetPeriodicWorkUpdates,btnStopPeriodicWork;
    TextView txtPeriodicResult;

    private PeriodicWorkRequest periodicWorkRequest;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartPeriodicWork = findViewById(R.id.btnStartPeriodicWork);
        btnGetPeriodicWorkUpdates = findViewById(R.id.btnGetPeriodicWorkUpdates);
        btnStopPeriodicWork = findViewById(R.id.btnStopPeriodicWork);
        txtPeriodicResult = findViewById(R.id.txtPeriodicResult);
        this.context = this;
        btnStartPeriodicWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "Memulai Task");
                Toast.makeText(context, "Memulai Task", Toast.LENGTH_SHORT).show();
                Data data = new Data.Builder().putString("key", "1918122 Periodic work").build();
                periodicWorkRequest = new PeriodicWorkRequest.Builder(PeriodicWork.class, 15,
                        TimeUnit.MINUTES)
                        .addTag("periodicWork" + System.currentTimeMillis())
                        .setBackoffCriteria(BackoffPolicy.LINEAR,
                                PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .setInputData(data)
                        .build();

                WorkManager.getInstance(context).enqueue(periodicWorkRequest);
            }
        });
        btnGetPeriodicWorkUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    WorkInfo workInfo =
                            WorkManager.getInstance(context).getWorkInfoById(periodicWorkRequest.getId())
                    .get();
                    Log.d("TAG", workInfo.toString());
                    String update =
                            "\nState: " + workInfo.getState() + "\nProses: " + workInfo.getProgress()
                            + "\nOutput Data: " + workInfo.getOutputData();
                    txtPeriodicResult.setText("Update data periodic: " + update);
                } catch (ExecutionException e){
                    e.printStackTrace();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        btnStopPeriodicWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Operation operation =
                        WorkManager.getInstance(context).cancelWorkById(periodicWorkRequest.getId());
                PeriodicWork.isStopped = true;
                Log.d("TAG", "Menghentikan Task");
                Toast.makeText(context,"Menghentikan Task" + operation.getState(),
                        Toast.LENGTH_SHORT).show();
            }
        });
}


}