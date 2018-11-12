package notchtools.geek.com.notchtools.core;

/**
 * @author zhangzhun
 * @date 2018/11/7
 */

public interface OnNotchCallBack {

    /**
     * 刘海（状态栏）的属性回调
     * @param notchProperty
     */
    void onNotchPropertyCallback(NotchProperty notchProperty);

    /**
     * 是否需要下移布局
     * @param needAddNocth
     */
    void onNeedAddNotchStatusBar(boolean needAddNocth);
}
