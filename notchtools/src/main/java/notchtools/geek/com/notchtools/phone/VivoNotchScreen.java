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
        if (mClass == null) {
            ClassLoader classLoader = window.getContext().getClassLoader();
            try {
                mClass = classLoader.loadClass("android.util.FtFeature");
                mMethod = mClass.getMethod("isFeatureSupport", Integer.TYPE);
                return (boolean) mMethod.invoke(mClass, 0x00000020);
            } catch (ClassNotFoundException e) {
                return false;
            } catch (NoSuchMethodException e) {
                return false;
            } catch (IllegalAccessException e) {
                return false;
            } catch (InvocationTargetException e) {
                return false;
            }
        } else {
            if (mMethod == null) {
                try {
                    mMethod = mClass.getMethod("isFeatureSupport", Integer.TYPE);
                } catch (NoSuchMethodException e) {
                    return false;
                }
                try {
                    return (boolean) mMethod.invoke(mClass, 0x00000020);
                } catch (IllegalAccessException e) {
                    return false;
                } catch (InvocationTargetException e) {
                    return false;
                }
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getNotchHeight(Window window) {
        return NotchStatusBarUtils.getStatusBarHeight(window.getContext());
    }

    /**
     * vivo手机没对刘海做出适配方案，不管怎么样，全屏永远是不占用刘海
     * @param activity
     * @param notchCallBack
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenDontUseStatus(activity, notchCallBack);
    }

    /**
     * vivo手机没对刘海做出适配方案，不管怎么样，全屏永远是不占用刘海
     * 所以全屏不占用刘海情况下，MarginTop就设为0即可
     * @param activity
     * @param notchCallBack
     */
    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        onBindCallBackWithNotchProperty(activity, 0, notchCallBack);
        NotchStatusBarUtils.setFullScreenWithSystemUi(activity.getWindow(), true);
    }
}
