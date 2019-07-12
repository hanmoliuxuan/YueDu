package com.example.xiaoming.readbook2.Diy;

import android.util.Log;

import com.example.xiaoming.readbook2.R;
import com.example.xiaoming.readbook2.Ui.Read;
import com.example.xiaoming.readbook2.Var.All;
import com.example.xiaoming.readbook2.Var.First;
import com.example.xiaoming.readbook2.Var.ReadTextSiez;
import com.example.xiaoming.readbook2.Var.Second;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Method {
    public static String Getcode(String path) throws IOException {
        File file = new File(path);
        FileInputStream input = new FileInputStream(file);
        int p = (input.read() << 8) + input.read();
        String code;
        switch (p) {
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16";
                break;
            default:
                code = "Utf-8";
                break;
        }
        return code;
    }

    public static List<Map<String, Object>> Getdata() {
        Second.DATALIST.clear();
        for (int i = 0; i < First.NAMES.size(); i++) {
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("pic", R.drawable.book2);
            maps.put("text", First.NAMES.get(i));
            Second.DATALIST.add(maps);
        }
        return Second.DATALIST;
    }

    public static void Perstring() {
        int j;
        Second.perpage.clear();
        Second.mululist.clear();
        Second.mulu_datalist.clear();
        Second.firstpage.clear();
        Second.firstpage.add(0);
        Second.DATALIST.clear();
        for (int i = 0; i < All.CHAPTER.length; i++) {
            String res = All.CHAPTER[i];
            Second.lines = res.split("\r\n\r\n");
            if (res.length() < ReadTextSiez.MaxLine[All.SIZE] * ReadTextSiez.LineMaxText[All.SIZE]) {
                if (res.equals("\r\n")) {
                    continue;
                } else {
                    Second.perpage.add(res);
                }
            } else {
                StringBuilder lastres = new StringBuilder();
                int nowlines = 0;
                String nowres;
                for (int b = 1; b < Second.lines.length; b++) {
                    nowres = Second.lines[b];
                    nowlines += Math.ceil((double) nowres.length() / (ReadTextSiez.LineMaxText[All.SIZE]));
                    if (nowlines < ReadTextSiez.MaxLine[All.SIZE]) {
                        lastres.append(nowres).append("\r\n");
                        if (b == Second.lines.length - 1) {
                            Second.perpage.add(lastres.toString());
                        }
                    } else {
                        Second.perpage.add(lastres.toString());
                        nowlines = 0;
                        b -= 1;
                        lastres = new StringBuilder();
                    }
                }
            }
            Second.mululist.add(Second.lines[1]);
            Second.firstpage.add(Second.perpage.size());
        }
    }

    public static void GetNowChapter() {
        for (int i = 0; i < Second.firstpage.size(); i++) {
            if (Second.firstpage.get(i) < Second.nowpage && Second.nowchapter < Second.firstpage.get(i + 1)) {
                Second.nowchapter = i;
            }
        }
    }

    public static List<Map<String, Object>> Getmulu() {

        Second.DATALIST.clear();
        for (int i = 0; i < Second.mululist.size(); i++) {
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("text", Second.mululist.get(i));
            Second.DATALIST.add(maps);
        }
        return Second.DATALIST;
    }
}
