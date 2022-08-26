package com.example.blocknumber;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class NumberRelease extends AppCompatActivity {

    private DBHelper helper;
    private SQLiteDatabase db;
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

        edit = findViewById(R.id.edit);

        // SQLiteOpenHelper 클래스의 subclass인 DBHelper 클래스 객체 생성
        helper = new DBHelper(this);
        // DBHelper 객체를 이용하여 DB 생성
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }

    }

    // 추가 버튼을 눌렀을 때 호출되는 메소드
    public void onClick(View v) {
        String tel = edit.getText().toString();

        // 이름과 전화번호를 가지고 INSERT 문을 만들어 실행
        db.execSQL("INSERT INTO contacts VALUES (null, '" + tel + "');");
        Toast.makeText(getApplicationContext(), "성공적으로 추가되었음", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        helper.close();
    }
}