package notchtools.geek.com.notchtools.phone;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Window;
import java.lang.reflect.Method;

import notchtools.geek.com.notchtools.core.AbsNotchScreenSupport;
import notchtools.geek.com.notchtools.core.OnNotchCallBack;
import notchtools.geek.com.notchtools.helper.NotchStatusBarUtils;
import notchtools.geek.com.notchtools.helper.SystemProperties;

/**
 * https://dev.mi.com/console/doc/detail?pId=1293
 * @author zhangzhun
 * @date 2018/11/4
 */
public class MiuiNotchScreen extends AbsNotchScreenSupport {

    private static final String TAG = MiuiNotchScreen.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isNotchScreen(Window window) {
        return "1".equals(SystemProperties.getInstance().get("ro.miui.notch"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getNotchHeight(Window window) {
        int result;
        if (window == null) {
            return 0;
        }
        Context context = window.getContext();
        result = NotchStatusBarUtils.getStatusBarHeight(context);
        return result;
    }

    /**
     * 0x00000100 | 0x00000200 竖屏绘制到耳朵区
     * 0x00000100 | 0x00000400 横屏绘制到耳朵区
     * 0x00000100 | 0x00000200 | 0x00000400 横竖屏都绘制到耳朵区
     * @param activity
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenDontUseStatus(activity, notchCallBack);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isNotchScreen(activity.getWindow())) {
            //开启配置
            int FLAG_NOTCH = 0x00000100 | 0x00000400;
            try {
                Method method = Window.class.getMethod("addExtraFlags", int.class);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(activity.getWindow(), FLAG_NOTCH);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 0x00000100 | 0x00000200 竖屏绘制到耳朵区
     * 0x00000100 | 0x00000400 横屏绘制到耳朵区
     * 0x00000100 | 0x00000200 | 0x00000400 横竖屏都绘制到耳朵区
     * @param activity
     */
    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenUseStatus(activity, notchCallBack);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isNotchScreen(activity.getWindow())) {
            //开启配置
            int FLAG_NOTCH = 0x00000100 | 0x00000200 | 0x00000400;
            try {
                Method method = Window.class.getMethod("addExtraFlags", int.class);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(activity.getWindow(), FLAG_NOTCH);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
