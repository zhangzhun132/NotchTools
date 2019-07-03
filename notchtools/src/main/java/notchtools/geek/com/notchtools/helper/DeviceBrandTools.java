package notchtools.geek.com.notchtools.helper;

import android.os.Build;
import android.text.TextUtils;

/**
 * @author zhangzhun
 * @date 2018/11/4
 */
public class DeviceBrandTools {

    private static DeviceBrandTools sDeviceBrandTools;

    public static DeviceBrandTools getInstance() {
        if (sDeviceBrandTools ==null){
            synchronized (DeviceBrandTools.class){
                if (sDeviceBrandTools == null) {
                    sDeviceBrandTools = new DeviceBrandTools();
                }
            }
        }
        return sDeviceBrandTools;
    }

    public final boolean isHuaWei() {
        String manufacturer = Build.MANUFACTURER;
        if (!TextUtils.isEmpty(manufacturer)){
            if (manufacturer.contains("HUAWEI")) {
                return true;
            }
        }
        return false;
    }

    public final boolean isMiui() {
        String manufacturer = getSystemProperty("ro.miui.ui.version.name");
        if (!TextUtils.isEmpty(manufacturer)){
            return true;
        }
        return false;
    }

    public final boolean isOppo() {
        String manufacturer = Build.MANUFACTURER;
        if("oppo".equalsIgnoreCase(manufacturer)){
            return true;
        }
        return false;
    }

    public final boolean isVivo() {
        String manufacturer = this.getSystemProperty("ro.vivo.os.name");
        if (!TextUtils.isEmpty(manufacturer)){
            return true;
        }
        return false;
    }

    public final boolean isSamsung() {
        String manufacturer = Build.MANUFACTURER;
        if("samsung".equalsIgnoreCase(manufacturer)){
            return true;
        }
        return false;
    }

    private String getSystemProperty(String propName) {
        return SystemProperties.getInstance().get(propName);
    }
}
