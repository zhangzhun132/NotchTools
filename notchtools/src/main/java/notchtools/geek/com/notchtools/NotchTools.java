package notchtools.geek.com.notchtools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Window;

import notchtools.geek.com.notchtools.core.INotchSupport;
import notchtools.geek.com.notchtools.core.OnNotchCallBack;
import notchtools.geek.com.notchtools.helper.DeviceBrandTools;
import notchtools.geek.com.notchtools.helper.NotchStatusBarUtils;
import notchtools.geek.com.notchtools.helper.ThreadUtils;
import notchtools.geek.com.notchtools.phone.CommonScreen;
import notchtools.geek.com.notchtools.phone.HuaWeiNotchScreen;
import notchtools.geek.com.notchtools.phone.MiuiNotchScreen;
import notchtools.geek.com.notchtools.phone.OppoNotchScreen;
import notchtools.geek.com.notchtools.phone.PVersionNotchScreen;
import notchtools.geek.com.notchtools.phone.VivoNotchScreen;

/**
 * 适配全面屏入口，在Activity的onCreate方法中使用FullScreenTools.fullScreenDontUseStatus()完成适配
 * 目前支持：
 * 1、全屏但不占用刘海屏区域
 * 2、全屏且占用刘海屏区域
 *
 * 注意事项：
 * 1、{@link VivoNotchScreen}
 *    Vivo手机对于O版本只提供了判断是否是刘海屏机型，但没有对全屏时刘海做处理，默认是全屏不占用刘海
 * 2、{@link OppoNotchScreen}
 *    OPPO手机同vivo手机，但是默认是全屏且占用刘海
 * 3、{@link MiuiNotchScreen}
 *    MIUI手机对O版本的横屏、竖屏时是否占用刘海分别都做了适配
 * 4、{@link HuaWeiNotchScreen}
 *    华为手机对于O版本的横屏、竖屏是否占用刘海分别都做了适配
 *
 * 对于需要下移刘海高度的操作情况：
 * 1、全屏且占用刘海高度：可以在{@link OnNotchCallBack}的onNotchPropertyCallback方法根据状态栏高度做下移适配
 * 2、全屏但不占用刘海高度，目前只有oppo、vivo刘海手动添加黑色刘海。
 * 具体为在根布局中添加一个FrameLayout作为fake的假刘海的父布局，然后给该父布局添加TAG。
 * NotchTools适配工具通过findViewWithTag在内部处理OV手机的刘海情况。
 *
 * @author zhangzhun
 * @date 2018/11/4
 */
public class NotchTools implements INotchSupport{

    private static NotchTools sFullScreenTolls;

    /**
     * 刘海的容器的Tag
     */
    public static final String NOTCH_CONTAINER = "notch_container";
    private static final int CURRENT_SDK = android.os.Build.VERSION.SDK_INT;
    /**
     * Android P版本号
     */
    public static final int VERSION_P = 28;
    private INotchSupport notchScreenSupport;
    private boolean mHasJudge;
    private boolean mIsNotchScreen;


    public static NotchTools getFullScreenTools() {
        if (sFullScreenTolls == null) {
            synchronized (NotchTools.class) {
                if (sFullScreenTolls == null) {
                    sFullScreenTolls = new NotchTools();
                }
            }
        }
        return sFullScreenTolls;
    }

    private NotchTools() {
        notchScreenSupport = null;
    }

    /**
     * 判断是否是刘海屏
     *
     * @param Window the window
     * @return the boolean
     */
    @Override
    public boolean isNotchScreen(Window Window) {
        if (!mHasJudge) {
            if (notchScreenSupport == null) {
                checkScreenSupportInit(Window);
            }
            if (notchScreenSupport == null) {
                mHasJudge = true;
                mIsNotchScreen = false;
            } else {
                mIsNotchScreen = notchScreenSupport.isNotchScreen(Window);
            }
        }
        return mIsNotchScreen;
    }

    /**
     * 获取刘海屏的高度
     */
    @Override
    public int getNotchHeight(Window window) {
        if (notchScreenSupport == null) {
            checkScreenSupportInit(window);
        }
        if (notchScreenSupport == null) {
            return 0;
        }
        return notchScreenSupport.getNotchHeight(window);
    }

    /**
     * 获取状态栏高度
     * @param window
     * @return
     */
    @Override
    public int getStatusHeight(Window window) {
        return NotchStatusBarUtils.getStatusBarHeight(window.getContext());
    }

    /**
     * 全屏显示，但是不占用刘海屏区域（在刘海的下方显示内容）
     */
    public void fullScreenDontUseStatus(Activity activity) {
        fullScreenDontUseStatus(activity, null);
    }

    /**
     * 全屏显示，但是不占用刘海屏区域（在刘海的下方显示内容）
     */
    @Override
    public void fullScreenDontUseStatus(final Activity activity, final OnNotchCallBack notchCallBack) {
        ThreadUtils.post2UI(new Runnable() {
            @Override
            public void run() {
                if (notchScreenSupport == null) {
                    checkScreenSupportInit(activity.getWindow());
                }
                if (notchScreenSupport != null) {
                    notchScreenSupport.fullScreenDontUseStatus(activity, notchCallBack);
                }
            }
        });
    }

    public void fullScreenDontUseStatusForPortrait(Activity activity) {
        fullScreenDontUseStatusForPortrait(activity, null);
    }

    @Override
    public void fullScreenDontUseStatusForPortrait(Activity activity, OnNotchCallBack notchCallBack) {
        fullScreenDontUseStatus(activity, notchCallBack);
    }

    public void fullScreenDontUseStatusForLandscape(final Activity activity) {
        fullScreenDontUseStatusForLandscape(activity, null);
    }

    @Override
    public void fullScreenDontUseStatusForLandscape(final Activity activity, final OnNotchCallBack notchCallBack) {
        ThreadUtils.post2UI(new Runnable() {
            @Override
            public void run() {
                if (notchScreenSupport == null) {
                    checkScreenSupportInit(activity.getWindow());
                }
                if (notchScreenSupport != null) {
                    notchScreenSupport.fullScreenDontUseStatusForLandscape(activity, notchCallBack);
                }
            }
        });
    }

    /**
     * 全屏显示，且占用刘海屏区域（在刘海区域也显示内容）
     */
    public void fullScreenUseStatus(Activity activity) {
        fullScreenUseStatus(activity, null);
    }

    @Override
    public void fullScreenUseStatus(final Activity activity, final OnNotchCallBack notchCallBack) {
        ThreadUtils.post2UI(new Runnable() {
            @Override
            public void run() {
                if (notchScreenSupport == null) {
                    checkScreenSupportInit(activity.getWindow());
                }
                if (notchScreenSupport != null) {
                    notchScreenSupport.fullScreenUseStatus(activity, notchCallBack);
                }
            }
        });

    }

    private void checkScreenSupportInit(Window window) {
        if (notchScreenSupport != null) {
          return;
        }

        //小于O版本的，采用通用处理方案
        if (CURRENT_SDK < Build.VERSION_CODES.O) {
            notchScreenSupport = new CommonScreen();
            return;
        }

        //O版本的机型，进行刘海屏相关处理
        if (CURRENT_SDK < VERSION_P) {
            DeviceBrandTools deviceBrandTools = DeviceBrandTools.getInstance();
            if (deviceBrandTools.isHuaWei()) {
                notchScreenSupport = new HuaWeiNotchScreen();
            } else if (deviceBrandTools.isMiui()) {
                notchScreenSupport = new MiuiNotchScreen();
            } else if (deviceBrandTools.isVivo()) {
                notchScreenSupport = new VivoNotchScreen();
            } else if (deviceBrandTools.isOppo()) {
                notchScreenSupport = new OppoNotchScreen();
            } else {
                notchScreenSupport = new CommonScreen();
            }
            return;
        }

        //O以上版本，单独处理
        if (CURRENT_SDK >= VERSION_P) {
            notchScreenSupport = new PVersionNotchScreen();
            return;
        }
    }

}
