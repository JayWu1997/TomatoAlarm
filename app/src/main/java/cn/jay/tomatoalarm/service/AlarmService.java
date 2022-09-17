package cn.jay.tomatoalarm.service;

import static cn.jay.tomatoalarm.MainActivity.TOMATO_TIME_LENGTH_MILLIS;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.Preference;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Date;

import cn.jay.tomatoalarm.R;

public class AlarmService extends IntentService {

    private CountDownTimer timer;
    private TextView tv_time;
    private TextView tv_today_times;
    private Button bt_start;
    private Vibrator vibrator;
    private boolean isRunning;
    private Context context;

    /**
     * @param name
     * @deprecated
     */
    public AlarmService(String name) {
        super(name);
        this.timer = new CountDownTimer(TOMATO_TIME_LENGTH_MILLIS, 1000) {
            @Override
            public void onTick(long l) {
                long min = (TOMATO_TIME_LENGTH_MILLIS - l)/1000/60;
                long sec = (TOMATO_TIME_LENGTH_MILLIS - l)/1000%60;
                if(min < 10){
                    if(sec < 10)
                        tv_time.setText("0" + min + ":0" + sec);
                    else
                        tv_time.setText("0" + min + ":" + sec);
                }
                else {
                    if(sec < 10)
                        tv_time.setText(min + ":0" + sec);
                    else
                        tv_time.setText(min + ":" + sec);
                }
            }

            @Override
            public void onFinish() {
                tv_time.setText("" + TOMATO_TIME_LENGTH_MILLIS/1000/60 + ":00");
                long[] patern = {0,1000,1000};
                AudioAttributes audioAttributes = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    audioAttributes = new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION) //key
                            .build();
                    vibrator.vibrate(patern, 0, audioAttributes);  // 重复一分钟
                }else {
                    vibrator.vibrate(patern, 0);
                }

                Date date = new Date();
                String todayStr = ""+date.getYear()+date.getMonth()+date.getDate();
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                String dateOfDate = sp.getString("date", "");
                if(!todayStr.equals(dateOfDate)){
                    editor.putString("date", todayStr);
                    editor.putInt("finish_times", 1);
                    editor.apply();
                    tv_today_times.setText("今日完成1次");
                }
                else{
                    int times = sp.getInt("finish_times", 0);
                    times++;
                    tv_today_times.setText("今日完成"+times+"次");
                    editor.putInt("finish_times", times);
                    editor.apply();
                }
            }
        };
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        timer.start();
    }

    public void setVibrator(Vibrator vibrator) {
        this.vibrator = vibrator;
    }

    public void setTv_time(TextView tv_time) {
        this.tv_time = tv_time;
    }

    public void setBt_start(Button bt_start) {
        this.bt_start = bt_start;
    }

    public void setTv_today_times(TextView tv_today_times) {
        this.tv_today_times = tv_today_times;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isRunning(){
        return isRunning;
    }

    public void start(){
        changeButtonToEndStyle();
        timer.start();
    }

    public void stop(){
        changeButtonToStartStyle();
        tv_time.setText("00:00");
        vibrator.cancel();
        timer.cancel();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeButtonToEndStyle(){
        isRunning = true;
        bt_start.setText("结束");
        bt_start.setBackground(context.getDrawable(R.drawable.main_button_round_corner_end));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeButtonToStartStyle(){
        isRunning = false;
        bt_start.setText("开始");
        bt_start.setBackground(context.getDrawable(R.drawable.main_button_round_corner_start));
    }

}
