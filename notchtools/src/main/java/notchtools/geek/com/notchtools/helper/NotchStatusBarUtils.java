package notchtools.geek.com.notchtools.helper;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import notchtools.geek.com.notchtools.NotchTools;
import notchtools.geek.com.notchtools.core.NotchProperty;
import notchtools.geek.com.notchtools.core.OnNotchCallBack;

/**
 * @author zhangzhun
 * @date 2018/11/7
 */

public class NotchStatusBarUtils {

    private static int statusBarHeight = -1;
    public static boolean sShowNavigation;

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
        WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(attrs);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            if (!sShowNavigation) {
                systemUiVisibility |=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            }
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
     * 沉浸式透明状态栏
     * @param window
     */
    public static void setStatusBarTransparent(Window window, OnNotchCallBack onNotchCallBack) {
        //先把全屏显示的Flag给clear掉
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int systemUiVisibility = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            try {
                //先catch这个崩溃吧，不像是代码的问题
                //http://mobile.umeng.com/apps/f13f10a340b04265b74bcf25/error_types/show?error_type_id=52fcb47b56240b043a01f31f_7284942895977219141_4.6.3.2
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (!sShowNavigation) {
            systemUiVisibility |=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        }
        NotchProperty notchProperty = new NotchProperty();
        notchProperty.setStatusBarHeight(NotchTools.getFullScreenTools().getStatusHeight(window));
        notchProperty.setNotchHeight(NotchTools.getFullScreenTools().getNotchHeight(window));
        notchProperty.setNotch(NotchTools.getFullScreenTools().isNotchScreen(window));

        if (onNotchCallBack != null) {
            onNotchCallBack.onNotchPropertyCallback(notchProperty);
        }
    }


    private static void setToolbarContainerFillStatusBar(Window window) {
        int statusBarHeight = NotchTools.getFullScreenTools().getStatusHeight(window);
        ViewGroup toolBarContainer = getToolBarContainer(window);
        if (toolBarContainer == null || toolBarContainer.getChildCount() < 1) {
            return;
        }
        View firstChild = toolBarContainer.getChildAt(0);
        if (firstChild != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) firstChild.getLayoutParams();
            int height = lp.height;
            if (height > 0) {
                //如果toolbar最外层layout指定了高度，那么就通过修改layoutparams的方法填充状态栏区域
                lp.height += statusBarHeight;
                firstChild.setLayoutParams(lp);
            } else {
                //如果toolbar最外层layout没有指定高度，那么就通过修改padding的方式填充状态栏区域
                firstChild.setPadding(0, statusBarHeight, 0, 0);
            }
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

    public static ViewGroup getToolBarContainer(Window window) {

        View decorView = window.getDecorView();
        if (decorView == null) {
            return null;
        }
        return decorView.findViewWithTag(NotchTools.TOOLBAR_CONTAINER);
    }

}
