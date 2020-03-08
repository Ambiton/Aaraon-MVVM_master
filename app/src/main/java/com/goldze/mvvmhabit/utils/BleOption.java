package com.goldze.mvvmhabit.utils;

import android.util.Log;

import com.goldze.mvvmhabit.app.AppApplication;
import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleReadRssiResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import me.goldze.mvvmhabit.utils.StringUtils;

/**
 * @author Areo
 * @description:明远按摩器 蓝牙通信工具类
 * @date : 2020/1/12 13:51
 */
public class BleOption {
    private static final String TAG = "BleOption";
    public final static String DEVICE_NAME = "BLE_MYZR_";

    private static final UUID UUID_SERVICE_CHANNEL
            = UUID.fromString("0000ae00-0000-1000-8000-00805f9b34fb");

    private static final UUID UUID_CHARACTERISTIC_CHANNEL_WRITE_READ
            = UUID.fromString("0000ae01-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_CHARACTERISTIC_CHANNEL_NOTIFY
            = UUID.fromString("0000ae02-0000-1000-8000-00805f9b34fb");


    private static final int OPTION_TRUN_ON_DEVICE = 0x01;
    private static final int OPTION_TRUN_OFF_DEVICE = 0x02;
    private static final int OPTION_HEATING_ON = 0x03;
    private static final int OPTION_HEATING_OFF = 0x04;
    private static final int OPTION_SPEED_ADD = 0x05;
    private static final int OPTION_SPEED_REDUCE = 0x06;
    private static final int OPTION_ROATION_POSITIVE = 0x07;
    private static final int OPTION_ROATION_REVERSAL = 0x08;
    private static final int OPTION_ROATION_AUTO = 0x09;
    private static final int OPTION_TRUN_ON_VOICE = 0x0A;
    private static final int OPTION_TRUN_OFF_VOICE = 0x0B;
    private static final int OPTION_ADD_VOICE = 0x0C;
    private static final int OPTION_REDUCE_VOICE = 0x0D;

    private static final int OPTION_SPEED_MAX = 0x0E;
    private static final int OPTION_SPEED_MID = 0x0F;
    private static final int OPTION_SPEED_MIN = 0x10;

    private static final int OPTION_VOL_MAX = 0x11;
    private static final int OPTION_VOL_MID = 0x12;
    private static final int OPTION_VOL_MIN = 0x13;
    private static final int OPTION_GET_DEVICEINFO = 0x14;
    private static final int OPTION_VOL_SILENT = 0x15;

    private static BleOption instance;
    private String curDeviceMac;
    private volatile boolean isDeviceConnected;
    private Timer deviceInfoTimer;

    public static synchronized BleOption getInstance() {
        if (instance == null) {
            instance = new BleOption();
        }
        return instance;
    }

    public boolean isDeviceConnected() {
        return isDeviceConnected;
    }

    public void setDeviceConnected(boolean deviceConnected) {
        isDeviceConnected = deviceConnected;
    }

    public void setMac(String mac) {
        this.curDeviceMac = mac;
    }

    public String getMac() {
        return this.curDeviceMac;
    }

    /**
     * 是否是明远按摩器的蓝牙名称
     *
     * @param deviceName
     * @return
     */
    public boolean isDeviceBluetooth(String deviceName) {
        return !StringUtils.isEmpty(deviceName) && deviceName.startsWith(DEVICE_NAME);
    }

    public void getNotifyData(BleNotifyResponse bleNotifyResponse) {
        AppApplication.getBluetoothClient(AppApplication.getInstance()).notify(this.curDeviceMac, UUID_SERVICE_CHANNEL, UUID_CHARACTERISTIC_CHANNEL_NOTIFY, bleNotifyResponse);
    }

    public void getReadData(BleReadResponse bleReadResponse) {
        AppApplication.getBluetoothClient(AppApplication.getInstance()).read(this.curDeviceMac, UUID_SERVICE_CHANNEL, UUID_CHARACTERISTIC_CHANNEL_NOTIFY, bleReadResponse);

    }

    /**
     * 开启获取设备信息的定时器，每2秒获取一次
     *
     * @param bleWriteResponse
     */
    public synchronized void startGetDeviceInfo(final BleWriteResponse bleWriteResponse) {
        if (deviceInfoTimer != null) {
            deviceInfoTimer.cancel();
            deviceInfoTimer = null;
        }
        deviceInfoTimer = new Timer();
        deviceInfoTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                writeDate(new byte[]{OPTION_GET_DEVICEINFO}, bleWriteResponse);
            }
        }, 500, 5 * 1000);

    }

    /**
     * 停止获取设备信息的定时器
     */
    public void stopGetDeviceInfo() {
        if (deviceInfoTimer != null) {
            deviceInfoTimer.cancel();
            deviceInfoTimer = null;
        }
    }

    public void getRssiData(BleReadRssiResponse bleReadRssiResponse) {
        AppApplication.getBluetoothClient(AppApplication.getInstance()).readRssi(this.curDeviceMac, bleReadRssiResponse);
    }

    /**
     * 连接设备蓝牙
     *
     * @param curDeviceMac
     * @param bleConnectResponse
     */
    public void connectDevice(String curDeviceMac, BleConnectResponse bleConnectResponse) {
        setMac(curDeviceMac);
        AppApplication.getBluetoothClient(AppApplication.getInstance()).connect(BluetoothUtils.getRemoteDevice(curDeviceMac).getAddress(), bleConnectResponse);
    }

    /**
     * 开启设备
     *
     * @param bleWriteResponse
     */
    public void turnOnDevice(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_TRUN_ON_DEVICE}, bleWriteResponse);
    }

    /**
     * 关闭设备
     *
     * @param bleWriteResponse
     */
    public void turnOffDevice(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_TRUN_OFF_DEVICE}, bleWriteResponse);
    }

    /**
     * 开启设备音量
     *
     * @param bleWriteResponse
     */
    public void turnOnVoice(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_TRUN_ON_VOICE}, bleWriteResponse);
    }

    /**
     * 关闭设备音量
     *
     * @param bleWriteResponse
     */
    public void turnOffVoice(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_TRUN_OFF_VOICE}, bleWriteResponse);
    }

    /**
     * 设备正转
     *
     * @param bleWriteResponse
     */
    public void roationPositive(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_ROATION_POSITIVE}, bleWriteResponse);
    }

    /**
     * 设备反转
     *
     * @param bleWriteResponse
     */
    public void roationReversal(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_ROATION_REVERSAL}, bleWriteResponse);
    }


    /**
     * 设备自动转
     *
     * @param bleWriteResponse
     */
    public void roationAuto(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_ROATION_AUTO}, bleWriteResponse);
    }

    /**
     * 设备最快速
     *
     * @param bleWriteResponse
     */
    public void setMaxSpeed(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_SPEED_MAX}, bleWriteResponse);
    }

    /**
     * 设备mid速
     *
     * @param bleWriteResponse
     */
    public void setMidSpeed(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_SPEED_MID}, bleWriteResponse);
    }

    /**
     * 设备min速
     *
     * @param bleWriteResponse
     */
    public void setMinSpeed(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_SPEED_MIN}, bleWriteResponse);
    }

    /**
     * 设备加速
     *
     * @param bleWriteResponse
     */
    public void addSpeed(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_SPEED_ADD}, bleWriteResponse);
    }

    /**
     * 设备减速
     *
     * @param bleWriteResponse
     */
    public void reduceSpeed(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_SPEED_REDUCE}, bleWriteResponse);
    }

    /**
     * 设备音量加
     *
     * @param bleWriteResponse
     */
    public void addVoice(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_ADD_VOICE}, bleWriteResponse);
    }

    /**
     * 设备音量Max
     *
     * @param bleWriteResponse
     */
    public void setMaxVoice(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_VOL_MAX}, bleWriteResponse);
    }

    /**
     * 设备音量Mid
     *
     * @param bleWriteResponse
     */
    public void setMidVoice(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_VOL_MID}, bleWriteResponse);
    }

    /**
     * 设备音量Min
     *
     * @param bleWriteResponse
     */
    public void setMinVoice(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_VOL_MIN}, bleWriteResponse);
    }

    /**
     * 设备音量Silent
     *
     * @param bleWriteResponse
     */
    public void setSilentVoice(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_VOL_SILENT}, bleWriteResponse);
    }

    /**
     * 设备音量减
     *
     * @param bleWriteResponse
     */
    public void reduceVoice(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_REDUCE_VOICE}, bleWriteResponse);
    }

    /**
     * 关闭设备加热
     *
     * @param bleWriteResponse
     */
    public void turnOffHeating(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_HEATING_OFF}, bleWriteResponse);
    }

    /**
     * 开启设备加热
     *
     * @param bleWriteResponse
     */
    public void turnOnHeating(BleWriteResponse bleWriteResponse) {
        writeDate(new byte[]{OPTION_HEATING_ON}, bleWriteResponse);
    }


    private synchronized void writeDate(byte[] datas, BleWriteResponse bleWriteResponse) {
        if (StringUtils.isEmpty(this.curDeviceMac) || AppApplication.getBluetoothClient(AppApplication.getInstance()).getConnectStatus(this.curDeviceMac) != Constants.STATUS_DEVICE_CONNECTED) {
            Log.e(TAG, "curDeviceMac is empty or disconnected,cannot do any option,mac is " + this.curDeviceMac + ";connect statu is " + AppApplication.getBluetoothClient(AppApplication.getInstance()).getConnectStatus(this.curDeviceMac));
            bleWriteResponse.onResponse(Code.REQUEST_FAILED);
            return;
        }
        Log.e(TAG, "write date is  " + RxDataTool.bytes2HexString(datas));
        AppApplication.getBluetoothClient(AppApplication.getInstance()).write(this.curDeviceMac, UUID_SERVICE_CHANNEL, UUID_CHARACTERISTIC_CHANNEL_WRITE_READ, datas, bleWriteResponse);
    }
}
