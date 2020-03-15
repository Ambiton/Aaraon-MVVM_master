package me.goldze.mvvmhabit.deviceinterface;

/**
 * @author Areo
 * @description:
 * @date : 2020/3/15 17:31
 */
public interface OnDeviceInfoListener {
    /**
     * 判断当前设备是否可用
     * @param isCanUse
     */
    void onDeviceCanUseResult(boolean isCanUse);
}
