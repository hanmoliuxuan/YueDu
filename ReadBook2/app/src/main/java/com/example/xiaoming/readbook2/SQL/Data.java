package com.example.xiaoming.readbook2.SQL;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AlertDialogLayout;
import android.util.Log;
import android.widget.Toast;

import com.example.xiaoming.readbook2.Diy.Method;
import com.example.xiaoming.readbook2.Var.All;
import com.example.xiaoming.readbook2.Var.First;

import java.util.ArrayList;

public class Data {
    static Mysql mysql;
    static SQLiteDatabase db;
    static ContentValues values;

    public static void Savebook(Context context, String name, String path) {
        NewSQL(context);
        values.put("name", name);
        values.put("path", path);
        db.insert("shuju", null, values);
        values.clear();
    }

    public static void Savelastpage(Context context, int page) {
        NewSQL(context);
        values.put("lastpage", page);
        db.update("shuju", values, "name=?", new String[]{All.BOOKNAME});
        values.clear();
    }

    public static int Getlastpage(Context context, String bookname) {
        int lastpage = 0;
        NewSQL(context);
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("select lastpage from shuju where name=?", new String[]{bookname});
        cursor.moveToNext();
        lastpage = cursor.getInt(cursor.getColumnIndex("lastpage"));
        return lastpage;
    }

    public static String Findpath(Context context, String bookname) {
        NewSQL(context);
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("select path from shuju where name=?", new String[]{bookname});
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex("path"));
        return path;
    }

    public static void ReadData(Context context) {
        First.NAMES.clear();
        First.PATHS.clear();
        NewSQL(context);
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("select * from shuju", null);
        while (cursor.moveToNext()) {
            First.NAMES.add(cursor.getString(cursor.getColumnIndex("name")));
            First.PATHS.add(cursor.getString(cursor.getColumnIndex("path")));
        }
        Method.Getdata();
    }

    public static void Delbook(Context context, String bookname) {
        mysql = new Mysql(context);
        db = mysql.getReadableDatabase();
        db.delete("shuju", "name=?", new String[]{bookname});
    }

    public static boolean FindBook(Context context,String bookname)
    {
        String bname=null;
        mysql=new Mysql(context);
        @SuppressLint("Recycle")
        Cursor cursor=db.rawQuery("select * from shuju where name=?",new String[]{bookname});
        if (cursor.moveToFirst())
        {
            do
            {
                bname=cursor.getString(cursor.getColumnIndex("name"));
            }
            while (cursor.moveToNext());
        }
        if (bookname.equals(bname))
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    private static void NewSQL(Context context) {
        mysql = new Mysql(context);
        db = mysql.getReadableDatabase();
        values = new ContentValues();
    }
}
