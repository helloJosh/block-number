package com.example.blocknumber;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


//전화 중이면 전화를 받고 아니면 메세지
public class IncomingCallReceiver extends BroadcastReceiver {//현재 전화가 오는지 받는지 끊는지 기본인지
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    List numberList = new ArrayList<>();
    String phonestate;

    private String DirPath = "/data/data/com.example.blocknumber/files";
    private ArrayList<String> array = new ArrayList<>();

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            Toast.makeText(context, "receiver started", Toast.LENGTH_SHORT).show();

            final TelecomManager telephonyManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

            Bundle extras = intent.getExtras();
            if (extras != null) {
                String state = extras.getString(TelephonyManager.EXTRA_STATE); // 현재 폰 상태 가져옴

                if (state.equals(phonestate)) {
                    return;
                } else {
                    phonestate = state;
                }
                String phoneNum = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                    databaseReference = database.getReference("Number");
                    Log.d("qqq", phoneNum + "currentNumber");
                    if (phoneNum != null) {
                        db.collection("number").whereGreaterThanOrEqualTo(phoneNum, 1)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("TAG!!! : ", "fuck");

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                            telephonyManager.endCall();
                                        }
                                    }
                                } else {
                                    Log.w("TAG", "Error getting documents.", task.getException());
                                }
                            }
                        });
                    }

                    File Folder = new File(DirPath);

                    File[] listFiles =(Folder.listFiles());

                    for( File file : listFiles ) {
                        String fileName = file.getName();

                        int idx = fileName.lastIndexOf(".");
                        fileName = fileName.substring(0,idx);

                        Log.i("TAG: value is ", fileName);
                        ((ArrayList) array).add(fileName);
                    }

                    for(int i = 0; i < array.size(); i++){
                        if(phoneNum.equals(array.get(i))){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                telephonyManager.endCall();
                            }
                        }
                    }
                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    Log.d("qqq", "통화중");

                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    Log.d("qqq", "통화종료 혹은 통화벨 종료");
                }

                Log.d("qqq", "phone state : " + state);
                Log.d("qqq", "phone currentPhonestate : " );
            }
        }
    }
}