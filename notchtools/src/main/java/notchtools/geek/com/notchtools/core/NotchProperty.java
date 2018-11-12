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
    private int mHeight;

    /**
     * 是否是刘海屏
     */
    private boolean mIsNotch;


    public int geNotchHeight() {
        return mHeight;
    }

    public void setNotchHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public boolean isNotch() {
        return mIsNotch;
    }

    public void setNotch(boolean mIsNotch) {
        this.mIsNotch = mIsNotch;
    }
}
