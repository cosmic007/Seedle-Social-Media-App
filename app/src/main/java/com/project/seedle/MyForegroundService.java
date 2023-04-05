package com.project.seedle;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;

import com.project.seedle.Activities.MainContentPage;
import com.project.seedle.Fragments.Community;

import java.util.List;

public class MyForegroundService extends Service {

    private Community mFragment;
    private FragmentManager mFragmentManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mFragment = new Community();

        // Get the FragmentManager instance by casting the application's context to an Activity context

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Check if the app is in the foreground
        mFragmentManager = ((MainContentPage) getApplicationContext()).getSupportFragmentManager();
        boolean isAppInForeground = isAppInForeground();
        if (isAppInForeground) {
            // App is in the foreground, show a notification but do not start the service in the foreground
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                    .setContentTitle("Foreground Service")
                    .setContentText("Running")
                    .setSmallIcon(R.mipmap.applogo);
            startForeground(1, builder.build());
        } else {
            // App is not in the foreground, start the service in the foreground
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, "my_channel_id")
                    .setContentTitle("Foreground Service")
                    .setContentText("Running")
                    .setSmallIcon(R.mipmap.applogo)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);
        }

        // Add your Fragment to the layout
        mFragmentManager.beginTransaction().add(R.id.community, mFragment).commit();

        return START_STICKY; // Return START_STICKY to indicate that the service should be restarted if it is killed
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Remove your Fragment from the layout
        mFragmentManager.beginTransaction().remove(mFragment).commit();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Checks if the app is in the foreground
     * @return true if the app is in the foreground, false otherwise
     */
    private boolean isAppInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
        if (runningTasks != null && !runningTasks.isEmpty()) {
            ComponentName topActivity = runningTasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
