package com.example.xiaoming.readbook2.Ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.os.Build;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.xiaoming.readbook2.R;

import static com.example.xiaoming.readbook2.R.id.percent;
import static com.example.xiaoming.readbook2.R.id.sp;
import static com.example.xiaoming.readbook2.R.id.time;

public class Setting extends PreferenceActivity {
    public static int theme = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getFragmentManager().beginTransaction().
                replace(android.R.id.content, new PerfSetting())
                .commit();
        if (theme != 0) {
            setTheme(theme);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent gomain = new Intent(Setting.this, Main.class);
        startActivity(gomain);
    }
}

@SuppressLint("ValidFragment")
class PerfSetting extends PreferenceFragment implements OnPreferenceClickListener {
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.pref_setting);
        findPreference("mode").setOnPreferenceClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "mode":
                boolean check = sharedPreferences.getBoolean("mode", false);
                if (!check) {
                    Setting.theme = R.style.Day;
                    getActivity().recreate();
                } else {
                    Setting.theme = R.style.Night;
                    getActivity().recreate();
                }
                break;
        }
        return false;
    }
}
