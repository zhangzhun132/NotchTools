package notchtools.geek.com.notchtools.core;

/**
 * 刘海信息
 * @author zhangzhun
 * @date 2018/11/7
 */

public class NotchProperty {

    /**
     * 刘海高度
     */
    private int mNotchHeight;

    /**
     * 是否是刘海屏
     */
    private boolean mIsNotch;

    /**
     * 全屏占用刘海区域时布局需要下移（布局的MarginTop）的距离
     * 和mHeight的区别是，mHeight是刘海的真实高度，但是mMarginTop不一定是刘海高度
     * 因为在vivo刘海屏手机上，全屏是不占用刘海的，所以就无需下移，mMarginTop是0
     */
    private int mMarginTop;

    private int mStatusBarHeight;

    public int getStatusBarHeight() {
        return mStatusBarHeight;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.mStatusBarHeight = statusBarHeight;
    }

    public int geNotchHeight() {
        return mNotchHeight;
    }

    public void setNotchHeight(int mHeight) {
        this.mNotchHeight = mHeight;
    }

    public boolean isNotch() {
        return mIsNotch;
    }

    public void setNotch(boolean mIsNotch) {
        this.mIsNotch = mIsNotch;
    }

    public int getMarginTop() {
        return mMarginTop;
    }

    public void setMarginTop(int mMarginTop) {
        this.mMarginTop = mMarginTop;
    }
}
