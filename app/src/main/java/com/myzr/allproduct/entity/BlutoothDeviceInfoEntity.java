package com.myzr.allproduct.entity;

/**
 * @author Areo
 * @description:蓝牙设备信息
 * @date : 2019/12/23 21:56
 */
public class BlutoothDeviceInfoEntity {
    private int drawableId;
    private int rssi;
    private String deviceName;
    private String macAddress;

    public BlutoothDeviceInfoEntity(int drawableId, int rssi, String deviceName, String macAddress) {
        this.drawableId = drawableId;
        this.rssi = rssi;
        this.deviceName = deviceName;
        this.macAddress = macAddress;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

}
