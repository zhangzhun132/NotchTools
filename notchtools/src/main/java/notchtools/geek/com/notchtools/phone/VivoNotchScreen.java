package notchtools.geek.com.notchtools.phone;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Window;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import notchtools.geek.com.notchtools.core.AbsNotchScreenSupport;
import notchtools.geek.com.notchtools.core.OnNotchCallBack;
import notchtools.geek.com.notchtools.helper.NotchStatusBarUtils;

/**
 * https://dev.vivo.com.cn/documentCenter/doc/103
 * Vivo手机的适配（木有适配规则~~o(>_<)o ~~，在FullScreen Flag下永远都是在刘海下面~）
 * @author zhangzhun
 * @date 2018/11/4
 */
public class VivoNotchScreen extends AbsNotchScreenSupport {
    private static final String TAG = VivoNotchScreen.class.getSimpleName();
    private Class mClass;
    private Method mMethod;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isNotchScreen(Window window) {
        if (window == null) {
            return false;
        }
        boolean isNotchScreen = false;
        ClassLoader classLoader = window.getContext().getClassLoader();
        try {
            mClass = classLoader.loadClass("android.util.FtFeature");
            mMethod = mClass.getMethod("isFeatureSupport", Integer.TYPE);
            isNotchScreen =  (boolean) mMethod.invoke(mClass, 0x00000020);
        } catch (ClassNotFoundException e) {
            return false;
        } catch (NoSuchMethodException e) {
            return false;
        } catch (IllegalAccessException e) {
            return false;
        } catch (InvocationTargetException e) {
            return false;
        } finally {
            return isNotchScreen;
        }
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
     * vivo手机没对刘海做出适配方案
     * 在设置里的第三方应用显示比例中，有两种显示模式:
     * 1、安全区域显示
     * 2、全屏显示
     * 这两种模式目前没有方法可供开发者判断，所以在适配时会有差异
     * 尤其是在安全区域显示时的全屏占用刘海的情况下。
     * @param activity
     * @param notchCallBack
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenDontUseStatus(activity, notchCallBack);
        if (isNotchScreen(activity.getWindow())) {
            NotchStatusBarUtils.setFakeNotchView(activity.getWindow());
        }
    }

    /**
     * 竖屏下与fullScreenDontUseStatus保持一致
     * @param activity
     * @param notchCallBack
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void fullScreenDontUseStatusForPortrait(Activity activity, OnNotchCallBack notchCallBack) {
        fullScreenDontUseStatus(activity, notchCallBack);
    }

    /**
     * 横屏下需要把NotchStatusBar隐藏掉，否则有可能会出现横屏上方有条黑边
     * @param activity
     * @param notchCallBack
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void fullScreenDontUseStatusForLandscape(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenDontUseStatusForLandscape(activity, notchCallBack);
        if (isNotchScreen(activity.getWindow())) {
            NotchStatusBarUtils.removeFakeNotchView(activity.getWindow());
        }
    }

    /**
     * vivo手机没对刘海做出适配方案
     * 在设置里的第三方应用显示比例中，有两种显示模式:
     * 1、安全区域显示
     * 2、全屏显示
     * 这两种模式目前没有方法可供开发者判断，所以在适配时会有差异
     * 尤其是在安全区域显示时的全屏占用刘海的情况下。
     * @param activity
     * @param notchCallBack
     */
    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenUseStatus(activity, notchCallBack);
    }

}
