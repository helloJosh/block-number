package com.example.blocknumber;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextFileManager {
    private Context mContext;
    private String fm;

    public TextFileManager(Context _context, String f) {
        mContext = _context;
        fm = f;
    }

    // 파일에 문자열 데이터를 쓰는 메소드
    public void save(String data) {
        if (data == null || data.isEmpty() == true) {
            return;
        }
        FileOutputStream fos;
        try {
            fos = mContext.openFileOutput(fm, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일에서 데이터를 읽고 문자열 데이터로 반환하는 메소드
    public String load() {
        try {
            FileInputStream fis = mContext.openFileInput(fm);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            return new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 파일 삭제 메소드
    public void delete() {
        mContext.deleteFile(fm);
    }
}