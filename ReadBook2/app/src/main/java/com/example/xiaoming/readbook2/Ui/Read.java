package com.example.xiaoming.readbook2.Ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaoming.readbook2.Adapter.MyAdapter;
import com.example.xiaoming.readbook2.Base;
import com.example.xiaoming.readbook2.Diy.Method;
import com.example.xiaoming.readbook2.R;
import com.example.xiaoming.readbook2.SQL.Data;
import com.example.xiaoming.readbook2.Var.All;
import com.example.xiaoming.readbook2.Var.Second;

public class Read extends Base {

    //RecyclerView
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    LinearLayoutManager linearLayoutManager;
    //目录
    public static TextView mulu_title;
    //目录适配器
    SimpleAdapter simpleAdapter;
    public static ListView mululist;
    //设置
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout setting;
    ImageView list, auto, night, tts;
    //进度条
    SeekBar changechapter;//章节
    SeekBar bright;//亮度
    TextView lastchapter, nextchapter;//上一章,下一章
    //背景颜色
    Button white, gray, light_gray, yellow, green, pink;
    //自动阅读速度
    TextView Speed;
    ImageView add, minus;
    //文字大小
    TextView Size;
    ImageView size_add, size_minus;
    //tts
    TextToSpeech textToSpeech;
    //
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //颜色
    LinearLayout color;

    //文字颜色
    @SuppressLint("CommitPrefEdits")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);
        Method.Perstring();
        init();
        linseter();
        Method.GetNowChapter();
        Getinfo();


    }

    private void Getinfo() {
        Second.check3 = sharedPreferences.getBoolean("mode", true);
        Checkmode();

        Second.bright = sharedPreferences.getInt("bright", 127);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = Second.bright / 255.0f;
        getWindow().setAttributes(layoutParams);
        bright.setProgress(Second.bright);
    }

    private void Saveinfo() {
        editor.putString("background", Second.backgroundcolor);
        editor.putInt("bright", Second.bright);
        editor.putBoolean("mode", Second.check3);
        editor.commit();
    }

    private void Gotolastpage() {
        int page1 = Data.Getlastpage(getApplicationContext(), All.BOOKNAME);
        recyclerView.scrollToPosition(All.LASTPAGE);
    }

    @SuppressLint("CommitPrefEdits")
    private void init() {
        recyclerView = findViewById(R.id.rv);


        mulu_title = findViewById(R.id.mulu_title);
        mululist = findViewById(R.id.mulu);

        color = findViewById(R.id.color);
        white = findViewById(R.id.white);
        gray = findViewById(R.id.gray);
        green = findViewById(R.id.green);
        light_gray = findViewById(R.id.light_gray);
        yellow = findViewById(R.id.yellow);
        pink = findViewById(R.id.pink);

        setting = findViewById(R.id.setting);
        lastchapter = findViewById(R.id.up);
        nextchapter = findViewById(R.id.down);
        changechapter = findViewById(R.id.changechapter);
        bright = findViewById(R.id.bright);

        list = findViewById(R.id.list);
        auto = findViewById(R.id.auto);
        night = findViewById(R.id.night);
        tts = findViewById(R.id.tts);

        Speed = findViewById(R.id.auto_speed);
        add = findViewById(R.id.auto_add);
        minus = findViewById(R.id.auto_minus);

        Size = findViewById(R.id.textsize);
        size_add = findViewById(R.id.size_add);
        size_minus = findViewById(R.id.size_minus);

        changechapter.setMax(Second.firstpage.size() - 1);
        changechapter.setProgress(Second.nowchapter);


        simpleAdapter = new SimpleAdapter(getApplicationContext(), Method.Getmulu(), R.layout.item_mulu, new String[]{"text"}, new int[]{R.id.mulu_text});
        mululist.setAdapter(simpleAdapter);

        myAdapter = new MyAdapter(this, Second.perpage);
        recyclerView.setAdapter(myAdapter);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //滑页动画
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        handler2.postDelayed(runnable2, 10);
        Gotolastpage();

        sharedPreferences = getSharedPreferences("info", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void linseter() {
        mululist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recyclerView.scrollToPosition(Second.firstpage.get(position));
            }
        });
        bright.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Window window = getWindow();
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.screenBrightness = progress / 255.0f;
                window.setAttributes(layoutParams);
                Second.bright = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        lastchapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Second.nowchapter != 0) {
                    recyclerView.scrollToPosition(Second.firstpage.get(Second.nowchapter - 1));
                }
            }
        });
        changechapter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                recyclerView.scrollToPosition(Second.firstpage.get(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        nextchapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Second.nowchapter <= Second.firstpage.size()) {
                    recyclerView.scrollToPosition(Second.firstpage.get(Second.nowchapter + 1));
                }

            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mululist.setVisibility(View.VISIBLE);
                mulu_title.setVisibility(View.VISIBLE);
                setting.setVisibility(View.INVISIBLE);
            }
        });
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Second.check2) {
                    handler.postDelayed(runnable, 3000);
                    auto.setImageResource(R.drawable.stop);
                    Second.check2 = true;
                } else {
                    Second.check2 = false;
                    handler.removeCallbacks(runnable);
                    auto.setImageResource(R.drawable.auto);
                }
            }
        });
        night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Second.check3 = !Second.check3;
                if (Second.check3) {//false
                    recyclerView.setBackgroundColor(Color.WHITE);
                    myAdapter.notifyDataSetChanged();
                    night.setImageResource(R.drawable.moon);
                    Second.backgroundcolor="#ffffff";

                    white.setEnabled(true);
                    green.setEnabled(true);
                    gray.setEnabled(true);
                    yellow.setEnabled(true);
                    light_gray.setEnabled(true);
                    pink.setEnabled(true);


                } else {
                    recyclerView.setBackgroundColor(Color.rgb(54, 54, 54));
                    myAdapter.notifyDataSetChanged();
                    night.setImageResource(R.drawable.sun);
//                    Second.backgroundcolor="#363636";


                    white.setEnabled(false);
                    green.setEnabled(false);
                    gray.setEnabled(false);
                    yellow.setEnabled(false);
                    light_gray.setEnabled(false);
                    pink.setEnabled(false);

                }
            }
        });
        tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Read.this, "待添加", Toast.LENGTH_SHORT).show();
//                textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//                    @Override
//                    public void onInit(int status) {
//                        if (status==TextToSpeech.SUCCESS)
//                        {
//                            int language=textToSpeech.setLanguage(Locale.CHINESE);
//                            if (language!=TextToSpeech.LANG_AVAILABLE&&language!=TextToSpeech.LANG_COUNTRY_AVAILABLE)
//                            {
//                                Toast.makeText(getApplicationContext(),"不支持",Toast.LENGTH_SHORT).show();
//                            }
//                            else {
//                                textToSpeech.speak("123",TextToSpeech.QUEUE_FLUSH,null,null);
//                            }
//                        }
//                    }
//                });

            }
        });
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Second.nowpage = linearLayoutManager.findFirstVisibleItemPosition();
            }
        });
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setBackgroundColor(Color.parseColor("#ffffff"));
                Second.backgroundcolor = "#ffffff";
            }
        });
        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setBackgroundColor(Color.parseColor("#4d4d4d"));
                Second.backgroundcolor = "#4d4d4d";
            }
        });
        light_gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setBackgroundColor(Color.parseColor("#c1c1c1"));
                Second.backgroundcolor = "#c1c1c1";
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setBackgroundColor(Color.parseColor("#f7ca6f"));
                Second.backgroundcolor = "#f7ca6f";

            }
        });
        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setBackgroundColor(Color.parseColor("#ff9a9a"));
                Second.backgroundcolor = "#ff9a9a";

            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setBackgroundColor(Color.parseColor("#6bbe89"));
                Second.backgroundcolor = "#6bbe89";
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Second.nowspeed += 1;
                Speed.setText(String.valueOf(Second.nowspeed));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Second.nowspeed > 1) {
                    Second.nowspeed -= 1;
                    Speed.setText(String.valueOf(Second.nowspeed));
                }
            }
        });
        size_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (All.SIZE < 3) {
                    All.SIZE += 1;
                    Size.setText(String.valueOf(All.SIZE));
                    Method.Perstring();
                    myAdapter = new MyAdapter(getApplicationContext(), Second.perpage);
                    recyclerView.setAdapter(myAdapter);
                    recyclerView.scrollToPosition(Second.nowpage);
                    simpleAdapter = new SimpleAdapter(getApplicationContext(), Method.Getmulu(), R.layout.item_mulu, new String[]{"text"}, new int[]{R.id.mulu_text});
                    mululist.setAdapter(simpleAdapter);
                }
            }
        });
        size_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (All.SIZE > 1) {
                    All.SIZE -= 1;
                    Size.setText(String.valueOf(All.SIZE));
                    Method.Perstring();
                    myAdapter = new MyAdapter(getApplicationContext(), Second.perpage);
                    recyclerView.setAdapter(myAdapter);
                    recyclerView.scrollToPosition(Second.nowpage);
                    simpleAdapter = new SimpleAdapter(getApplicationContext(), Method.Getmulu(), R.layout.item_mulu, new String[]{"text"}, new int[]{R.id.mulu_text});
                    mululist.setAdapter(simpleAdapter);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, Second.nowspeed * 1000);
            recyclerView.scrollToPosition(Second.nowpage + 1);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        handler2.removeCallbacks(runnable2);
        Data.Savelastpage(getApplicationContext(), linearLayoutManager.findFirstVisibleItemPosition());
        Intent gomain = new Intent(Read.this, Main.class);
        startActivity(gomain);
        Second.DATALIST.clear();
        Second.firstpage.clear();
        Second.nowchapter = 0;
        Second.nowpage = 0;
        Saveinfo();

    }

    Handler handler2 = new Handler();
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            handler2.postDelayed(this, 100);
            for (int i = 0; i < Second.firstpage.size(); i++) {
                if (linearLayoutManager.findFirstVisibleItemPosition() == Second.firstpage.get(i)) {
                    Second.nowchapter = i;
                }
            }
            changechapter.setProgress(Second.nowchapter);
        }
    };

    public void Checkmode() {
        if (Second.check3) {
            recyclerView.setBackgroundColor(Color.WHITE);
            night.setImageResource(R.drawable.moon);
            simpleAdapter = new SimpleAdapter(getApplicationContext(), Method.Getmulu(), R.layout.item_mulu, new String[]{"text"}, new int[]{R.id.mulu_text});
            mululist.setAdapter(simpleAdapter);
            Second.backgroundcolor = "#ffffff";

            white.setEnabled(true);
            green.setEnabled(true);
            gray.setEnabled(true);
            yellow.setEnabled(true);
            light_gray.setEnabled(true);
            pink.setEnabled(true);


            Second.backgroundcolor = sharedPreferences.getString("background", "");
            if (Second.backgroundcolor.equals("")) {
            } else {
                recyclerView.setBackgroundColor(Color.parseColor(Second.backgroundcolor));
            }


        } else {
            recyclerView.setBackgroundColor(Color.parseColor("#363636"));
            night.setImageResource(R.drawable.sun);
            Second.backgroundcolor = "#363636";

            white.setEnabled(false);
            green.setEnabled(false);
            gray.setEnabled(false);
            yellow.setEnabled(false);
            light_gray.setEnabled(false);
            pink.setEnabled(false);
        }
    }
}
