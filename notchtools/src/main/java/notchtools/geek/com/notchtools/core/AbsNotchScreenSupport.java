package notchtools.geek.com.notchtools.core;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import notchtools.geek.com.notchtools.helper.NotchStatusBarUtils;


/**
 * @author zhangzhun
 * @date 2018/11/4
 */

public abstract class AbsNotchScreenSupport implements INotchSupport {

    @Override
    public void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        onBindCallBackWithNotchProperty(activity, notchCallBack);
        NotchStatusBarUtils.setFullScreenWithSystemUi(activity.getWindow(), true);
    }

    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        onBindCallBackWithNotchProperty(activity, notchCallBack);
        NotchStatusBarUtils.setFullScreenWithSystemUi(activity.getWindow(), true);
    }


    protected void onBindCallBackWithNotchProperty(Activity activity, OnNotchCallBack notchCallBack) {
        if (notchCallBack != null) {
            NotchProperty notchProperty = new NotchProperty();
            notchProperty.setNotchHeight(getNotchHeight(activity.getWindow()));
            notchProperty.setNotch(isNotchScreen(activity.getWindow()));
            if (notchCallBack != null) {
                notchCallBack.onNotchPropertyCallback(notchProperty);
            }
        }
    }

    protected void addFakeNotchViewOnActivity(Activity activity, ViewGroup toolbarContainer) {
        if (toolbarContainer == null) {
            return;
        }
        //如果系统在4.4以下,不支持沉浸式.
        View view = new View(activity);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getNotchHeight(activity.getWindow())));
        view.setBackgroundColor(Color.BLACK);
        toolbarContainer.addView(view);
    }

}
