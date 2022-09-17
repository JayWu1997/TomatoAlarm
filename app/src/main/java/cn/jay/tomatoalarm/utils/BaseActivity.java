package cn.jay.tomatoalarm.utils;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        initLocalData();
        initView();
        initInternetData();
    }

    public abstract void initLocalData();

    public abstract void initView();

    public abstract int setLayout();

    public void initInternetData(){};

}
