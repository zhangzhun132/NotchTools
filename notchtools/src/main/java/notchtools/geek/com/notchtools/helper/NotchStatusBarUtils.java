package notchtools.geek.com.notchtools.helper;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import notchtools.geek.com.notchtools.NotchTools;

/**
 * @author zhangzhun
 * @date 2018/11/7
 */

public class NotchStatusBarUtils {

    private static int statusBarHeight = -1;

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight != -1) {
            return statusBarHeight;
        }
        if (statusBarHeight <= 0) {
            int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resId > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resId);
            }
        }
        return statusBarHeight;
    }

    /**
     * 全屏flag设置
     * @param window
     * @param setListener 是否开启setOnSystemUiVisibilityChangeListener监听哦
     */
    public static void setFullScreenWithSystemUi(final Window window, boolean setListener) {
        int systemUiVisibility = 0;
        //setAttributes防止弹出DialogFragment时会出现页面抖动
        WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(attrs);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            systemUiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);

        if (setListener) {
            window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if (visibility == 0) {
                        setFullScreenWithSystemUi(window, false);
                    }
                }
            });
        }
    }


    /**
     * 全屏SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN下刘海屏适配需要
     */
    public static void setFakeNotchView(Window window) {
        ViewGroup notchContainer = removeFakeNotchView(window);
        if (notchContainer == null) {
            return;
        }
        View view = new View(window.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                NotchTools.getFullScreenTools().getNotchHeight(window)));
        view.setBackgroundColor(Color.BLACK);
        notchContainer.addView(view);
    }

    /**
     * 全屏SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN下刘海屏适配需要
     */
    public static ViewGroup removeFakeNotchView(Window window) {
        ViewGroup notchContainer = getNotchContainer(window);
        if (notchContainer == null) {
            return null;
        }
        int childCount = notchContainer.getChildCount();
        if (childCount > 0) {
            notchContainer.removeAllViews();
        }
        return notchContainer;
    }

    public static ViewGroup getNotchContainer(Window window) {

        View decorView = window.getDecorView();
        if (decorView == null) {
            return null;
        }
        return decorView.findViewWithTag(NotchTools.NOTCH_CONTAINER);
    }

}
