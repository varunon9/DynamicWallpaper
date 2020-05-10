package varunon9.me.dynamicwallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MyReceiver extends BroadcastReceiver {
    private String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive called");

        // starting via WorkManager and not directly
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, MyService.class));
        } else {
            context.startService(new Intent(context, MyService.class));
        }*/

        // We are starting MyService via a worker and not directly because since Android 7
        // (but officially since Lollipop!), any process called by a BroadcastReceiver is run at
        // low priority and hence eventually killed by Android.
        WorkManager workManager = WorkManager.getInstance(context);
        OneTimeWorkRequest startServiceRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        workManager.enqueue(startServiceRequest);
    }
}
