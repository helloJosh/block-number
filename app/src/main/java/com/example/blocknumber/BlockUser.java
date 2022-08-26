package com.example.blocknumber;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class BlockUser extends AppCompatActivity {
    private ListView m_ListView;
    private ArrayAdapter<String> m_Adapter;
    private String [] files = null;
    private String DirPath = "/data/data/com.example.blocknumber/files";
    private ArrayList<String> values = new ArrayList<>();
    String text , filename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blockuser);
        File Folder = new File(DirPath);

        if (!Folder.exists()) {
            try{Folder.mkdir(); }
            catch(Exception e){e.getStackTrace();}
        }

        //해당 경로를 설정하여 파일배열에 넣어줍니다.
        File[] listFiles =(Folder.listFiles());

        for( File file : listFiles ) {
            // 확장자를 제거해서 어레이 리스트에 넣기
            String fileName = file.getName();

            int idx = fileName.lastIndexOf(".");
            fileName = fileName.substring(0,idx);

            ((ArrayList) values).add(fileName);
        }

        // Android에서 제공하는 String 문자열 하나를 출력하는 layout으로 어댑터 생성
        m_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);

        // 이상한 두 파일 제거 뷰리스트, 어레이에서 제거
        String r = m_Adapter.getItem(0);
        String a = m_Adapter.getItem(values.size()-1);
        m_Adapter.remove(r);
        m_Adapter.remove(a);

        // layout xml 파일에 정의된 ListView의 객체
        m_ListView = findViewById(R.id.list);
        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);
        registerForContextMenu(m_ListView);
    }

    // 아이템 롱 터치 이벤트 리스너 구현
    // startActionMode() 메소드가 호출될 때 호출되는 콜백 메소드
    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index= info.position;
        String s2 = m_Adapter.getItem(index) + ".txt";
        TextFileManager mFileMgr = new TextFileManager(this,s2);

        switch(item.getItemId()) {
            case R.id.menu_delete:
                mFileMgr.delete();
                // 리스트의 마지막 아이템을 얻음
                String r = m_Adapter.getItem(index);
                // 해당 아이템을 adapter에서 삭제
                m_Adapter.remove(r);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    //button functions
    public void onClick(View v) {
        int viewId = v.getId();
        EditText p_number = findViewById(R.id.editTextPhone);
        text = p_number.getText().toString();
        filename = p_number.getText().toString() + ".txt";
        switch (viewId) {
            case R.id.button_add:
                File Folder = new File(DirPath);
                TextFileManager mFileMgr = new TextFileManager(this, filename);
                if (!Folder.exists()) {
                    Folder.mkdir();
                    mFileMgr.save(text);
                    text = "";
                } else {
                    mFileMgr.save(text);
                    text = "";
                }
                break;
        }
    }
}
