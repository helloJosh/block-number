package com.example.blocknumber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSReceiver";

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String DirPath = "/data/data/com.example.blocknumber/files";
    private ArrayList<String> array = new ArrayList<>();

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "onReceive() called");

        Bundle bundle = intent.getExtras();

        SmsMessage[] messages = parseSmsMessage(bundle);

        if(messages.length > 0){
            String sender = messages[0].getOriginatingAddress();
            final String content = messages[0].getMessageBody().toString();
            final Date date1 = new Date(messages[0].getTimestampMillis());

            Log.d(TAG, "sender: " + sender);
            Log.d(TAG, "content: " + content);
            Log.d(TAG, "date: " + date1);


            database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
            databaseReference = database.getReference("Number");
            db.collection("number").whereGreaterThanOrEqualTo(sender, 1)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            return;
                        }
                    }
                }
            });
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
                if(sender.equals(array.get(i))){
                    return;
                }
            }

            try {
                FileOutputStream fos = context.openFileOutput(sender, Context.MODE_APPEND);
                fos.write(content.getBytes());

                Toast.makeText(context, sender+" send sms", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle) {
        // PDU: Protocol Data Units
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];

        for(int i=0; i<objs.length; i++){
            messages[i] = SmsMessage.createFromPdu((byte[])objs[i]);
        }

        return messages;
    }
}