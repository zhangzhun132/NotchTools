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
        onBindCallBackWithNotchProperty(activity, getNotchHeight(activity.getWindow()), notchCallBack);
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

    protected void onBindCallBackWithNotchProperty(Activity activity, int marginTop, OnNotchCallBack notchCallBack) {
        if (notchCallBack != null) {
            NotchProperty notchProperty = new NotchProperty();
            notchProperty.setNotchHeight(getNotchHeight(activity.getWindow()));
            notchProperty.setNotch(isNotchScreen(activity.getWindow()));
            notchProperty.setMarginTop(marginTop);
            if (notchCallBack != null) {
                notchCallBack.onNotchPropertyCallback(notchProperty);
            }
        }
    }

}
