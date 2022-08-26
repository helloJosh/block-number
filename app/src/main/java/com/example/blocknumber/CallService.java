package com.example.blocknumber;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("MissingPermission")
public class CallService extends Service {
    NotificationManager notificationManager;
    NotificationChannel mChannel;
    Notification noti;
    Notification.Builder notiBuilder;
    List numberList = new ArrayList<>();
    private ArrayAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    BroadcastReceiver receiver;



    @Override
    public void onCreate() {
        super.onCreate();

        if(Build.VERSION.SDK_INT >= 26) {
            Log.d("서비스", "서비스");

            mChannel = new NotificationChannel("service_id", "service",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            notiBuilder = new Notification.Builder(this, mChannel.getId());
        } else {
            notiBuilder = new Notification.Builder(this);
        }
        Intent intent = new Intent(this, LoginWithGoogle.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        noti = notiBuilder.setContentTitle("Music service")
                .setContentText("Service is running... start an activity")
                .setContentIntent(pIntent)
                .build();
        startForeground(1, noti);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        receiver = new IncomingCallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(receiver, filter);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("서비스가 멈춰버렷!!!!!");
//        unregisterReceiver(receiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
