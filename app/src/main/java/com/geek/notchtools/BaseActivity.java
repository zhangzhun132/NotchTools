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
        mContentContainer = findViewById(R.id.content_container);
        onBindContentContainer(layoutResID);
    }

    private void onBindContentContainer(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, mContentContainer, true);
    }

    /**
     * 全屏SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN下刘海屏适配需要
     */
    protected void setFakeNotchView() {
        if (mNotchContainer == null) {
            mNotchContainer = findViewById(R.id.notch_container);
        }
        View view = new View(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, NotchTools.getFullScreenTools().getNotchHeight(getWindow())));
        view.setBackgroundColor(Color.BLACK);
        mNotchContainer.addView(view);
    }
}
