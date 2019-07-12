package com.example.xiaoming.readbook2.Var;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Second extends AppCompatActivity {
    //判断变量
    public static boolean check1 = false;
    public static boolean check2 = false;//自动翻页
    public static boolean check3;//白天，夜间模式
    public static boolean check4 = false;
    public static boolean checkclick = false;//点击屏幕打开设置
    public static List<Map<String, Object>> DATALIST;//RecyclerView的数据源
    //目录数据源
    public static List<Map<String, Object>> mulu_datalist;
    //每页内容
    public static List<String> perpage;
    //每一段
    public static String[] lines;
    //目录
    public static List<String> mululist;
    //每个章节第一页页数
    public static List<Integer> firstpage;
    //当前item位置
    public static int nowpage;
    //当前章节
    public static int nowchapter = 0;
    //速度
    public static int nowspeed=5;
    //背景颜色
    public static String backgroundcolor;
    public static int bright;
    static {
        mulu_datalist=new ArrayList<>();
        DATALIST = new ArrayList<>();
        perpage = new ArrayList<>();
        mululist = new ArrayList<>();
        firstpage = new ArrayList<>();
        firstpage.add(0);
    }
}
