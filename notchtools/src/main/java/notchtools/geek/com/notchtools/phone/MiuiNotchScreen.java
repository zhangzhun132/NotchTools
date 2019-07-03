package notchtools.geek.com.notchtools.phone;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
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
        if (!isNotchScreen(window)) {
            return 0;
        }

        int result = 0;
        if (window == null) {
            return 0;
        }
        Context context = window.getContext();
        if (isHideNotch(window.getContext())) {
            result = NotchStatusBarUtils.getStatusBarHeight(context);
        } else {
            result = getRealNotchHeight(context);
        }
        return result;
    }

    private int getRealNotchHeight(Context context) {
        int result;
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        } else {
            result = NotchStatusBarUtils.getStatusBarHeight(context);
        }
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
        this.fullScreenUseStatus(activity, notchCallBack);
        if (isNotchScreen(activity.getWindow())) {
            NotchStatusBarUtils.setFakeNotchView(activity.getWindow());
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

    /**
     * MIUI 针对 Notch 设备，有一个“隐藏屏幕刘海”的设置项（设置-全面屏-隐藏屏幕刘海
     * 具体表现是：系统会强制盖黑状态栏（无视应用的Notch使用声明）
     * 视觉上达到隐藏刘海的效果。但会给某些应用带来适配问题（控件/内容遮挡或过于靠边等）。
     * 因此开发者在适配时，还需要检查开启“隐藏屏幕刘海”后，应用的页面是否显示正常。
     * @param activity
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isHideNotch(Context activity) {
       return Settings.Global.getInt(activity.getContentResolver(),
               "force_black", 0) == 1;
    }
}
