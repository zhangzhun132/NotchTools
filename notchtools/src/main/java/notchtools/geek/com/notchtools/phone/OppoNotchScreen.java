package notchtools.geek.com.notchtools.phone;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Window;

import notchtools.geek.com.notchtools.core.AbsNotchScreenSupport;
import notchtools.geek.com.notchtools.core.OnNotchCallBack;

/**
 * https://open.oppomobile.com/service/message/detail?id=61876
 * @author zhangzhun
 * @date 2018/11/4
 */
public class OppoNotchScreen extends AbsNotchScreenSupport {

    private static final String TAG = OppoNotchScreen.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isNotchScreen(Window window) {
        if (window == null) {
            return false;
        }
        return window.getContext().getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getNotchHeight(Window window) {
        int statusBarHeight = 0;
        int resourceId = window.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = window.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight ;
    }

    /**
     * oppo手机，如果想全屏但不使用状态栏内容，需要加一个fake的假刘海
     * @param activity
     * @param notchCallBack
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenDontUseStatus(activity, notchCallBack);
        if (isNotchScreen(activity.getWindow())) {
            if (notchCallBack != null && isNotchScreen(activity.getWindow())) {
                notchCallBack.onNeedAddNotchStatusBar(true);
            }
        }
    }

    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenUseStatus(activity, notchCallBack);
    }

}
