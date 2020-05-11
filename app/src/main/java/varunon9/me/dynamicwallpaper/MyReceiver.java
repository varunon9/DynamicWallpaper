package varunon9.me.dynamicwallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MyReceiver extends BroadcastReceiver {
    private String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive called");

        //ContextCompat.startForegroundService(context, new Intent(context, MyService.class));

        // We are starting MyService via a worker and not directly because since Android 7
        // (but officially since Lollipop!), any process called by a BroadcastReceiver
        // (only manifest-declared receiver) is run at low priority and hence eventually
        // killed by Android. Docs: https://developer.android.com/guide/components/broadcasts#effects-process-state
        WorkManager workManager = WorkManager.getInstance(context);
        OneTimeWorkRequest startServiceRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        workManager.enqueue(startServiceRequest);
    }
}
