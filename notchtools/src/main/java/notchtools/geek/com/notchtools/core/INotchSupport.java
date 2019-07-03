package notchtools.geek.com.notchtools.core;

import android.app.Activity;
import android.view.Window;


/**
 * @author zhangzhun
 * @date 2018/11/4
 */
public interface INotchSupport {

    /**
     * 判断当前是否是刘海屏手机
     * @param window
     * @return
     */
    boolean isNotchScreen(Window window);

    /**
     * 获取刘海屏的高度
     * @param window
     * @return
     */
    int getNotchHeight(Window window);

    /**
     * 获取状态栏的高度
     * 新增此方法，与状态栏高度完全区分
     * @param window
     * @return
     */
    int getStatusHeight(Window window);

    /**
     * 隐藏状态栏，全屏状态下，但不使用状态栏区域
     * @param activity
     */
    void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack);

    /**
     * 竖屏下隐藏状态栏，全屏状态下且不占用刘海屏区域
     * @param activity
     * @param notchCallBack
     */
    void fullScreenDontUseStatusForPortrait(Activity activity, OnNotchCallBack notchCallBack);

    /**
     * 横屏下隐藏状态栏，全屏状态下且不占用刘海屏区域
     * @param activity
     * @param notchCallBack
     */
    void fullScreenDontUseStatusForLandscape(Activity activity, OnNotchCallBack notchCallBack);

    /**
     * 隐藏状态栏，全屏状态下，使用状态栏区域
     * @param activity
     */
    void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack);

    /**
     * 透明状态栏
     * @param activity
     */
    void translucentStatusBar(Activity activity);

    /**
     * 带回调的透明状态栏
     * @param activity
     * @param onNotchCallBack
     */
    void translucentStatusBar(Activity activity, OnNotchCallBack onNotchCallBack);

}
