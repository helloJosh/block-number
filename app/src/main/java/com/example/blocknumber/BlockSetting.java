package com.example.blocknumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BlockSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_setting);
    }
    public void onClick(View view) {
        Intent serviceIntent;
        switch(view.getId()) {
            case R.id.startBlock:
                serviceIntent = new Intent(this, CallService.class);
//                serviceIntent.setAction(intent.getAction());
//                serviceIntent.putExtras(intent.getExtras());
                this.startService(serviceIntent);
                break;
            case R.id.stopBlock:
                serviceIntent = new Intent(this, CallService.class);
                this.stopService(serviceIntent);
                break;
        }
    }
}