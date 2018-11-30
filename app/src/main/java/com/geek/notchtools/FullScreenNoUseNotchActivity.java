package com.geek.notchtools;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import notchtools.geek.com.notchtools.NotchTools;

public class FullScreenNoUseNotchActivity extends BaseActivity {

    private ImageView mBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_no_use_notch);
        mBackView = findViewById(R.id.img_back);
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        NotchTools.getFullScreenTools().fullScreenDontUseStatusForPortrait(this);
    }

}
