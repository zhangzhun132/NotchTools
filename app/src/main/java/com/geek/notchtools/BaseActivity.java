package com.geek.notchtools;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import notchtools.geek.com.notchtools.NotchTools;

/**
 * @author zhangzhun
 * @date 2018/11/12
 */

public class BaseActivity extends AppCompatActivity {

    /**
     * 刘海容器
     */
    private FrameLayout mNotchContainer;
    /**
     * 主内容区
     */
    private FrameLayout mContentContainer;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        mNotchContainer = findViewById(R.id.notch_container);
        mNotchContainer.setTag(NotchTools.NOTCH_CONTAINER);
        mContentContainer = findViewById(R.id.content_container);
        onBindContentContainer(layoutResID);
    }

    private void onBindContentContainer(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, mContentContainer, true);
    }
}
