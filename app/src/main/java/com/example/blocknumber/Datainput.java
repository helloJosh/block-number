package com.example.blocknumber;

import android.content.Intent
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import android.database.sqlite.SQLiteException;
import android.widget.SimpleCursorAdapter;
import android.widget.CursorAdapter;

public class Datainput extends AppCompatActivity implements View.OnClickListener{

    EditText edt;
    private Button buttonlogout, buttondelete;
    private FirebaseAuth firebaseAuth;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    boolean isPermitted = false;

    private DBHelper helper;
    private SQLiteDatabase DB;
    private Cursor c;
    private EditText edit;
    private ListView list;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input);

        // SQLiteOpenHelper 클래스의 subclass인 DBHelper 클래스 객체 생성
        helper = new DBHelper(this);
        // DBHelper 객체를 이용하여 DB 생성
        try {
            DB = helper.getWritableDatabase();

        } catch (SQLiteException e) {
            DB = helper.getReadableDatabase();

        }

        // contacts 테이블에서 모든 레코드를 retrieve
        c = DB.rawQuery("SELECT * FROM contacts", null);

        String[] from = {"tel"};
        int[] to = {android.R.id.text1};
        // SimpleCursorAdapter 객체 생성
        // 하나의 리스트 아이템에 1개의 텍스트뷰를 표시하는 레이아웃
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, c, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        // 레이아웃에 정의된 리스트뷰에 대한 참조 객체 얻음
        list = (ListView) findViewById(R.id.list);
        // 리스트뷰 객체에 어댑터 설정
        list.setAdapter(adapter);

        Button b1 = findViewById(R.id.save);
        edt = (EditText) findViewById(R.id.editext_number);
        buttonlogout = (Button) findViewById(R.id.logout);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        Log.i("hello", "world!!!!!!!!!!");

        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DatabaseReference input = FirebaseDatabase.getInstance().getReference("Number");
//                input.push().setValue(edt.getText().toString());
                final Map<String, Object> blockNum = new HashMap<>();
                db.collection("number").whereGreaterThan(edt.getText().toString(), 0).get().
                        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean check = false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        check = true;
                                        blockNum.put(edt.getText().toString(), 1 + Integer.parseInt(document.getData().get(edt.getText().toString()).toString()));
                                        db.collection("number").document("number")
                                                .set(blockNum, SetOptions.merge());
                                    }
                                    if (check == false) {
                                        blockNum.put(edt.getText().toString(), 1);
                                        db.collection("number").document("number")
                                                .set(blockNum, SetOptions.merge());
                                    }
                                }
                            }
                        });
                Toast.makeText(getApplicationContext(), "입력 완료", Toast.LENGTH_LONG).show();
            }
        });

        buttonlogout.setOnClickListener(this);

    }

    public void onClick(View view) {
        if (view == buttonlogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, MainLobby.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        helper.close();
        c.close();
    }

}
