package notchtools.geek.com.notchtools.phone;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Window;

import notchtools.geek.com.notchtools.core.AbsNotchScreenSupport;
import notchtools.geek.com.notchtools.core.OnNotchCallBack;
import notchtools.geek.com.notchtools.helper.NotchStatusBarUtils;

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
        if (!isNotchScreen(window)) {
            return 0;
        }

        return NotchStatusBarUtils.getStatusBarHeight(window.getContext());
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
