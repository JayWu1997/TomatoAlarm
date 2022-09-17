package cn.jay.tomatoalarm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import cn.jay.tomatoalarm.service.AlarmService;
import cn.jay.tomatoalarm.utils.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button bt_start;
    private TextView tv_time;
    private TextView tv_today_times;
    private boolean isRunning = false;
    private Vibrator vibrator;
    private AlarmService alarmService;
    public static final long TOMATO_TIME_LENGTH_MILLIS = 25*60*1000;


    @Override
    public void initLocalData() {

    }

    @Override
    public void initView() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 设置状态栏颜色
        getWindow().setStatusBarColor(getColor(R.color.bg));

        bt_start = (Button) findViewById(R.id.bt_start);
        bt_start.setOnClickListener(this);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_today_times = (TextView) findViewById(R.id.tv_today_times);

        alarmService = new AlarmService("AlarmService");
        alarmService.setBt_start(bt_start);
        alarmService.setTv_time(tv_time);
        alarmService.setTv_today_times(tv_today_times);
        vibrator = (Vibrator) MainActivity.this.getSystemService(MainActivity.VIBRATOR_SERVICE);
        alarmService.setVibrator(vibrator);
        alarmService.setContext(this);


        Date date = new Date();
        String todayStr = ""+date.getYear()+date.getMonth()+date.getDate();
        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        String dateOfDate = sp.getString("date", "");
        if(!todayStr.equals(dateOfDate)) {
            editor.putString("date", todayStr);
            editor.putInt("finish_times", 0);
            editor.apply();
        }

        int times = sp.getInt("finish_times", 0);
        tv_today_times.setText("今日完成" + times + "次");


    }

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View view) {
        if (bt_start.equals(view)) {
            if(alarmService.isRunning())
                alarmService.stop();
            else
                alarmService.start();
        }
    }


}