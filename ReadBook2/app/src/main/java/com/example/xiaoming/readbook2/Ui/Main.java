package com.example.xiaoming.readbook2.Ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.MalformedJsonException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuAdapter;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.xiaoming.readbook2.Adapter.Adapter;
import com.example.xiaoming.readbook2.Base;
import com.example.xiaoming.readbook2.Diy.Method;
import com.example.xiaoming.readbook2.R;
import com.example.xiaoming.readbook2.SQL.Data;
import com.example.xiaoming.readbook2.Var.All;
import com.example.xiaoming.readbook2.Var.First;
import com.example.xiaoming.readbook2.Var.Second;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Base {
    SwipeMenuListView listView;
    FloatingActionButton choose;
    Adapter simpleAdapter;
    SwipeMenuCreator creator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Setting.theme == 0) {

        } else {
            setTheme(Setting.theme);
        }
        setContentView(R.layout.main);
        init();
        simpleAdapter = new Adapter(getApplicationContext(), First.NAMES);
        listView.setAdapter(simpleAdapter);
        listener();


        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem delitem = new SwipeMenuItem(getApplicationContext());
                delitem.setBackground(new ColorDrawable(Color.RED));
                delitem.setTitle("删除");
                delitem.setTitleSize(18);
                delitem.setTitleColor(Color.WHITE);
                delitem.setWidth(260);
                menu.addMenuItem(delitem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
                        builder.setTitle("删除");
                        builder.setMessage("确定删除？");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Data.Delbook(getApplicationContext(), First.NAMES.get(position));
                                Second.DATALIST.remove(position);
                                First.NAMES.remove(position);
                                First.PATHS.remove(position);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                        break;
                }
                return false;
            }
        });
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
            case R.id.setting:
                Intent gosetting = new Intent(Main.this, Setting.class);
                startActivity(gosetting);
                this.finish();
                break;
        }
        return true;
    }


    private void init() {
        listView = findViewById(R.id.listview);
        choose = findViewById(R.id.choose);
        Data.ReadData(getApplicationContext());
    }

    private void listener() {
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(First.PATHS.get(position));
                try {
                    int length = (int) file.length();
                    byte[] buff = new byte[length];
                    FileInputStream fin = new FileInputStream(file);
                    fin.read(buff);
                    fin.close();
                    All.BOOKNAME = First.NAMES.get(position);
                    All.BOOKRESULT = new String(buff, Method.Getcode(First.PATHS.get(position)));
                    try {
                        All.CHAPTER = All.BOOKRESULT.split("------------");
                        All.KIND = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        All.KIND = false;
                    }
                    All.LASTPAGE = Data.Getlastpage(getApplicationContext(), All.BOOKNAME);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent read = new Intent(Main.this, Read.class);
                startActivity(read);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //获取路径
            if (resultCode == -1) {
                Uri uri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA};
                assert uri != null;
                @SuppressLint("Recycle")
                Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                assert cursor != null;
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(index);
                try {
                    int start = path.lastIndexOf("/");
                    int end = path.lastIndexOf(".");
                    String name = path.substring(start + 1, end);
                    if (!Data.FindBook(getApplicationContext(), name)) {
                        First.NAMES.add(name);
                        First.PATHS.add(path);
                        Method.Getdata();
                        simpleAdapter.notifyDataSetChanged();
                        Data.Savebook(getApplicationContext(), name, path);
                    } else {
                        Toast.makeText(getApplicationContext(), "已存在", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(),"选择失败",Toast.LENGTH_SHORT);
                }
            }
        }
    }
}

