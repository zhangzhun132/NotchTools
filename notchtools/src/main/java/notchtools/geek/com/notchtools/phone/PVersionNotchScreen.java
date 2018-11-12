package notchtools.geek.com.notchtools.phone;

import android.app.Activity;
import android.view.Window;

import notchtools.geek.com.notchtools.core.AbsNotchScreenSupport;
import notchtools.geek.com.notchtools.core.OnNotchCallBack;
import notchtools.geek.com.notchtools.helper.NotchStatusBarUtils;

/**
 * targetApi>=28才能使用API，有的手机厂商在P上会放弃O适配方案，暂时针对P手机不做特殊处理
 * @author zhangzhun
 * @date 2018/11/5
 */

public class PVersionNotchScreen extends AbsNotchScreenSupport {
    @Override
    public boolean isNotchScreen(Window window) {
        return false;
    }

    @Override
    public int getNotchHeight(Window window) {
        return NotchStatusBarUtils.getStatusBarHeight(window.getContext());
    }

    @Override
    public void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenDontUseStatus(activity, notchCallBack);
    }

}
