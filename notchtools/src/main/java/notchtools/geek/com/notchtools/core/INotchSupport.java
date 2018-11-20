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
     * 隐藏状态栏，全屏状态下，使用状态栏区域
     * @param activity
     */
    void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack);

}
