package notchtools.geek.com.notchtools.core;

/**
 * 刘海适配回调接口
 * @author zhangzhun
 * @date 2018/11/7
 */

public interface OnNotchCallBack {

    /**
     * 刘海（状态栏）的属性回调
     * @param notchProperty
     */
    void onNotchPropertyCallback(NotchProperty notchProperty);

}
