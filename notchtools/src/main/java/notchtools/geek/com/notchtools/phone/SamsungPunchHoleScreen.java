package notchtools.geek.com.notchtools.phone;

import android.app.Activity;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import java.lang.reflect.Field;

import notchtools.geek.com.notchtools.core.AbsNotchScreenSupport;
import notchtools.geek.com.notchtools.core.OnNotchCallBack;
import notchtools.geek.com.notchtools.helper.NotchStatusBarUtils;

/**
 * 三星钻孔屏 666
 *
 * Created by liukan on 2018/12/18.
 */
public class SamsungPunchHoleScreen extends AbsNotchScreenSupport {
    @Override
    public boolean isNotchScreen(Window window) {
        if (window == null) {
            return false;
        }
        boolean isNotchScreen = false;
        try {
            final Resources res = window.getContext().getResources();
            final int resId = res.getIdentifier("config_mainBuiltInDisplayCutout", "string", "android"); final String spec = resId > 0 ? res.getString(resId): null;
            isNotchScreen = spec != null && !TextUtils.isEmpty(spec);
        } catch (Exception e) {
            return isNotchScreen;
        }
        return isNotchScreen;
    }

    @Override
    public int getNotchHeight(Window window) {
        if (!isNotchScreen(window)) {
            return 0;
        }

        return NotchStatusBarUtils.getStatusBarHeight(window.getContext());
    }

    @Override
    public void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenDontUseStatus(activity, notchCallBack);
        if (isNotchScreen(activity.getWindow())) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            try {
                Field field = lp.getClass().getField("layoutInDisplayCutoutMode");
                field.setAccessible(true);
                // LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT     0
                // LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES 1
                // LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER       2
                field.setInt(lp, 1);
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            NotchStatusBarUtils.setFakeNotchView(activity.getWindow());
        }
    }

    @Override
    public void fullScreenDontUseStatusForPortrait(Activity activity, OnNotchCallBack notchCallBack) {
        fullScreenDontUseStatus(activity, notchCallBack);
    }

    @Override
    public void fullScreenDontUseStatusForLandscape(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenDontUseStatusForPortrait(activity, notchCallBack);
        if (isNotchScreen(activity.getWindow())) {
            NotchStatusBarUtils.removeFakeNotchView(activity.getWindow());
        }
    }

    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenUseStatus(activity, notchCallBack);

        if (isNotchScreen(activity.getWindow())) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            try {
                Field field = lp.getClass().getField("layoutInDisplayCutoutMode");
                field.setAccessible(true);
                // LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT     0
                // LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES 1
                // LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER       2
                field.setInt(lp, 1);
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
