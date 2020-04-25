package com.goldze.mvvmhabit.utils;

import android.util.Log;

import com.goldze.mvvmhabit.app.AppApplication;
import com.goldze.mvvmhabit.entity.DeviceStatusInfoEntity;
import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleReadRssiResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.tamsiree.rxtool.RxLogTool;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import me.goldze.mvvmhabit.utils.StringUtils;

/**
 * @author Areo
 * @description:明远按摩器 蓝牙通信工具类
 * @date : 2020/1/12 13:51
 */
public class BleOption {
    public static final int REQUEST_TIMEOUT = 0x01;
    private static final String TAG = "BleOption";
    public final static String DEVICE_NAME = "BLE_MYZR_";

    private static final UUID UUID_SERVICE_CHANNEL
            = UUID.fromString("0000ae00-0000-1000-8000-00805f9b34fb");

    private static final UUID UUID_CHARACTERISTIC_CHANNEL_WRITE_READ
            = UUID.fromString("0000ae01-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_CHARACTERISTIC_CHANNEL_NOTIFY
            = UUID.fromString("0000ae02-0000-1000-8000-00805f9b34fb");


    private static final byte OPTION_TRUN_ON_DEVICE = 0x01;
    private static final byte OPTION_TRUN_OFF_DEVICE = 0x02;
    private static final byte OPTION_HEATING_ON = 0x03;
    private static final byte OPTION_HEATING_OFF = 0x04;
    private static final byte OPTION_SPEED_ADD = 0x05;
    private static final byte OPTION_SPEED_REDUCE = 0x06;
    private static final byte OPTION_ROATION_POSITIVE = 0x07;
    private static final byte OPTION_ROATION_REVERSAL = 0x08;
    private static final byte OPTION_ROATION_AUTO = 0x09;
    private static final byte OPTION_TRUN_ON_VOICE = 0x0A;
    private static final byte OPTION_TRUN_OFF_VOICE = 0x0B;
    private static final byte OPTION_ADD_VOICE = 0x0C;
    private static final byte OPTION_REDUCE_VOICE = 0x0D;

    private static final byte OPTION_SPEED_MAX = 0x0E;
    private static final byte OPTION_SPEED_MID = 0x0F;
    private static final byte OPTION_SPEED_MIN = 0x10;

    private static final byte OPTION_VOL_MAX = 0x11;
    private static final byte OPTION_VOL_MID = 0x12;
    private static final byte OPTION_VOL_MIN = 0x13;
    private static final byte OPTION_GET_DEVICEINFO = 0x14;
    private static final byte OPTION_VOL_SILENT = 0x15;

    private static final int DELAY_WRITEDATA = 10;
    private static final int PERIOD_WRITEDATA = 200;

    private static final int MAX_REWRITEDATA_TIMES = 4;

    private static BleOption instance;
    private String curDeviceMac;
    private volatile boolean isDeviceConnected;
    private Timer deviceInfoTimer;
    private Timer getOnceDeviceInfoTimer;
    private Timer writeDataTimer;
    private CopyOnWriteArrayList<BleWriteData> writeCommondList = new CopyOnWriteArrayList<>();
    private int resendTimeCount = 0;

    public static HashMap<Byte, Byte> COMMOND_SET = new HashMap<>();

    private static void initCommondSet() {
        COMMOND_SET.put(OPTION_TRUN_ON_DEVICE, DeviceStatusInfoEntity.FLAG_TRUE);
        COMMOND_SET.put(OPTION_TRUN_OFF_DEVICE, DeviceStatusInfoEntity.FLAG_FALSE);
        COMMOND_SET.put(OPTION_HEATING_ON, DeviceStatusInfoEntity.FLAG_TRUE);
        COMMOND_SET.put(OPTION_HEATING_OFF, DeviceStatusInfoEntity.FLAG_FALSE);
        COMMOND_SET.put(OPTION_TRUN_ON_VOICE, DeviceStatusInfoEntity.FLAG_TRUE);
        COMMOND_SET.put(OPTION_TRUN_OFF_VOICE, DeviceStatusInfoEntity.FLAG_FALSE);

        COMMOND_SET.put(OPTION_ROATION_AUTO, DeviceStatusInfoEntity.FLAG_ROATION_AUTO);
        COMMOND_SET.put(OPTION_ROATION_POSITIVE, DeviceStatusInfoEntity.FLAG_ROATION_POSISTION);
        COMMOND_SET.put(OPTION_ROATION_REVERSAL, DeviceStatusInfoEntity.FLAG_ROATION_REV);

        COMMOND_SET.put(OPTION_VOL_SILENT, DeviceStatusInfoEntity.FLAG_VOICE_MUTE);
        COMMOND_SET.put(OPTION_VOL_MIN, DeviceStatusInfoEntity.FLAG_VOICE_MIN);
        COMMOND_SET.put(OPTION_VOL_MID, DeviceStatusInfoEntity.FLAG_VOICE_MID);
        COMMOND_SET.put(OPTION_VOL_MAX, DeviceStatusInfoEntity.FLAG_VOICE_MAX);

        COMMOND_SET.put(OPTION_SPEED_MIN, DeviceStatusInfoEntity.FLAG_SPEED_MIN);
        COMMOND_SET.put(OPTION_SPEED_MID, DeviceStatusInfoEntity.FLAG_SPEED_MID);
        COMMOND_SET.put(OPTION_SPEED_MAX, DeviceStatusInfoEntity.FLAG_SPEED_MAX);
    }

    public static synchronized BleOption getInstance() {
        if (instance == null) {
            instance = new BleOption();
            initCommondSet();
        }
        return instance;
    }

    public void uninitWriteDataEnv() {
        clearWriteDataList();
        uninitWriteDataTimer();
    }

    /**
     * 判断当前的infoEntity 返回的对应状态是否为我们需要设置的状态
     *
     * @param infoEntity
     * @return
     */
    public boolean isCurrentSetReturn(DeviceStatusInfoEntity infoEntity) {
        BleWriteData writeData = currentData();
        if (writeData != null && writeData.getCommonds().length > 0 && writeData.getCommonds()[0] == OPTION_GET_DEVICEINFO) {
            return true;
        }
        if (writeData == null || writeData.getCommonds().length <= 0 || COMMOND_SET == null || COMMOND_SET.get(writeData.getCommonds()[0]) == null) {
            RxLogTool.e(TAG, "writeData == null is " + (writeData == null));
            return false;
        }
        boolean result = false;
        byte needStatus = COMMOND_SET.get(writeData.getCommonds()[0]).byteValue();
        byte currentStatus = 0;
        switch (writeData.getCommonds()[0]) {
            case OPTION_TRUN_ON_DEVICE:
            case OPTION_TRUN_OFF_DEVICE:
                currentStatus = infoEntity.getIsDeviceOpen();
                break;
            case OPTION_HEATING_ON:
            case OPTION_HEATING_OFF:
                currentStatus = infoEntity.getIsHeatingOpen();
                break;
            case OPTION_TRUN_ON_VOICE:
            case OPTION_TRUN_OFF_VOICE:
                currentStatus = infoEntity.getDeviceVoiceSwitch();
                break;

            case OPTION_ROATION_AUTO:
            case OPTION_ROATION_POSITIVE:
            case OPTION_ROATION_REVERSAL:
                currentStatus = infoEntity.getDeviceRoationMode();
                break;

            case OPTION_VOL_SILENT:
            case OPTION_VOL_MIN:
            case OPTION_VOL_MID:
            case OPTION_VOL_MAX:
                currentStatus = infoEntity.getDeviceVoice();
                break;

            case OPTION_SPEED_MIN:
            case OPTION_SPEED_MID:
            case OPTION_SPEED_MAX:
                currentStatus = infoEntity.getDeviceSpeed();
                break;
            default:
                break;
        }
        RxLogTool.d(TAG, "need status is " + needStatus + ";**************current status is " + currentStatus);
        return needStatus == currentStatus;
    }

    private BleWriteData currentData() {
        if (writeCommondList == null || writeCommondList.size() <= 0) {
            return null;
        }
        return writeCommondList.get(0);
    }

    private synchronized void initResendTimeCount() {
        resendTimeCount = 0;
    }

    private synchronized void addResendTimeCount() {
        resendTimeCount++;
    }

    public synchronized int getResendTimeCount() {
        return resendTimeCount;
    }

    /**
     * 要能写数据必须要初始化该方法
     */
    public void initWriteDataEnv() {
        initWriteDataTimer();
        clearWriteDataList();
        writeDataTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (writeCommondList == null || writeCommondList.size() == 0) {
                    initResendTimeCount();
                    return;
                }
                if (getResendTimeCount() >= MAX_REWRITEDATA_TIMES) {
                    writeCommondList.get(0).getBleWriteResponse().onResponse(REQUEST_TIMEOUT);
                    clearLatestWriteCommondAndFlag();
                }
                if (writeCommondList == null || writeCommondList.size() == 0) {
                    return;
                }
                realWrite();
                addResendTimeCount();
            }
        }, DELAY_WRITEDATA, PERIOD_WRITEDATA);
    }

    private void clearWriteDataList() {
        writeCommondList.clear();
    }

    private void initWriteDataTimer() {
        uninitWriteDataTimer();
        writeDataTimer = new Timer();
    }

    private void uninitWriteDataTimer() {
        if (writeDataTimer != null) {
            writeDataTimer.cancel();
            writeDataTimer = null;
        }
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
        AppApplication.getBluetoothClient(AppApplication.getInstance()).read(this.curDeviceMac, UUID_SERVICE_CHANNEL, UUID_CHARACTERISTIC_CHANNEL_WRITE_READ, bleReadResponse);

    }

    /**
     * 开启获取设备信息的定时器，每5秒获取一次
     *
     * @param bleWriteResponse
     */
    public void startTimerGetDeviceInfo(final BleWriteResponse bleWriteResponse) {
        if (deviceInfoTimer != null) {
            deviceInfoTimer.cancel();
            deviceInfoTimer = null;
            RxLogTool.e(TAG, "deviceInfoTimer.cancel...");
        }
        deviceInfoTimer = new Timer();
        deviceInfoTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getDeviceInfo(bleWriteResponse);
            }
        }, 100, 3 * 1000);
    }

    /**
     * 获取设备信息
     *
     * @param bleWriteResponse
     */
    public void getDeviceInfo(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_GET_DEVICEINFO}, bleWriteResponse);
    }

    /**
     * 停止获取设备信息的定时器
     */
    public void stopGetDeviceInfo() {
        if (deviceInfoTimer != null) {
            deviceInfoTimer.cancel();
            deviceInfoTimer = null;
        }
        if (getOnceDeviceInfoTimer != null) {
            getOnceDeviceInfoTimer.cancel();
            getOnceDeviceInfoTimer = null;
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
        addWriteDateCommond(new byte[]{OPTION_TRUN_ON_DEVICE}, bleWriteResponse);
    }

    /**
     * 关闭设备
     *
     * @param bleWriteResponse
     */
    public void turnOffDevice(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_TRUN_OFF_DEVICE}, bleWriteResponse);
    }

    /**
     * 开启设备音量
     *
     * @param bleWriteResponse
     */
    public void turnOnVoice(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_TRUN_ON_VOICE}, bleWriteResponse);
    }

    /**
     * 关闭设备音量
     *
     * @param bleWriteResponse
     */
    public void turnOffVoice(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_TRUN_OFF_VOICE}, bleWriteResponse);
    }

    /**
     * 设备正转
     *
     * @param bleWriteResponse
     */
    public void roationPositive(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_ROATION_POSITIVE}, bleWriteResponse);
    }

    /**
     * 设备反转
     *
     * @param bleWriteResponse
     */
    public void roationReversal(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_ROATION_REVERSAL}, bleWriteResponse);
    }


    /**
     * 设备自动转
     *
     * @param bleWriteResponse
     */
    public void roationAuto(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_ROATION_AUTO}, bleWriteResponse);
    }

    /**
     * 设备最快速
     *
     * @param bleWriteResponse
     */
    public void setMaxSpeed(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_SPEED_MAX}, bleWriteResponse);
    }

    /**
     * 设备mid速
     *
     * @param bleWriteResponse
     */
    public void setMidSpeed(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_SPEED_MID}, bleWriteResponse);
    }

    /**
     * 设备min速
     *
     * @param bleWriteResponse
     */
    public void setMinSpeed(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_SPEED_MIN}, bleWriteResponse);
    }

    /**
     * 设备加速
     *
     * @param bleWriteResponse
     */
    public void addSpeed(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_SPEED_ADD}, bleWriteResponse);
    }

    /**
     * 设备减速
     *
     * @param bleWriteResponse
     */
    public void reduceSpeed(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_SPEED_REDUCE}, bleWriteResponse);
    }

    /**
     * 设备音量加
     *
     * @param bleWriteResponse
     */
    public void addVoice(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_ADD_VOICE}, bleWriteResponse);
    }

    /**
     * 设备音量Max
     *
     * @param bleWriteResponse
     */
    public void setMaxVoice(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_VOL_MAX}, bleWriteResponse);
    }

    /**
     * 设备音量Mid
     *
     * @param bleWriteResponse
     */
    public void setMidVoice(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_VOL_MID}, bleWriteResponse);
    }

    /**
     * 设备音量Min
     *
     * @param bleWriteResponse
     */
    public void setMinVoice(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_VOL_MIN}, bleWriteResponse);
    }

    /**
     * 设备音量Silent
     *
     * @param bleWriteResponse
     */
    public void setSilentVoice(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_VOL_SILENT}, bleWriteResponse);
    }

    /**
     * 设备音量减
     *
     * @param bleWriteResponse
     */
    public void reduceVoice(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_REDUCE_VOICE}, bleWriteResponse);
    }

    /**
     * 关闭设备加热
     *
     * @param bleWriteResponse
     */
    public void turnOffHeating(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_HEATING_OFF}, bleWriteResponse);
    }

    /**
     * 开启设备加热
     *
     * @param bleWriteResponse
     */
    public void turnOnHeating(BleWriteResponse bleWriteResponse) {
        addWriteDateCommond(new byte[]{OPTION_HEATING_ON}, bleWriteResponse);
    }


    private synchronized void addWriteDateCommond(byte[] datas, BleWriteResponse bleWriteResponse) {
        if (StringUtils.isEmpty(this.curDeviceMac) || AppApplication.getBluetoothClient(AppApplication.getInstance()).getConnectStatus(this.curDeviceMac) != Constants.STATUS_DEVICE_CONNECTED) {
            Log.e(TAG, "curDeviceMac is empty or disconnected,cannot do any option,mac is " + this.curDeviceMac + ";connect statu is " + AppApplication.getBluetoothClient(AppApplication.getInstance()).getConnectStatus(this.curDeviceMac));
            bleWriteResponse.onResponse(Code.REQUEST_FAILED);
            return;
        }
        BleWriteData bleWriteData = new BleWriteData(datas, bleWriteResponse);
        writeCommondList.add(bleWriteData);
        Log.e(TAG, "add date is  " + RxDataTool.bytes2HexString(datas));
        // AppApplication.getBluetoothClient(AppApplication.getInstance()).write(this.curDeviceMac, UUID_SERVICE_CHANNEL, UUID_CHARACTERISTIC_CHANNEL_WRITE_READ, datas, bleWriteResponse);
    }

    /**
     * 命令集合中删除掉最近发送的命令;接受成功或三次超时时使用
     */
    public void clearLatestWriteCommondAndFlag() {
        initResendTimeCount();
        if (writeCommondList == null || writeCommondList.size() == 0) {
            return;
        }
        writeCommondList.remove(0);
    }

    private void realWrite() {
        BleWriteData bleWriteData = null;
        if (writeCommondList == null || writeCommondList.size() <= 0) {
            RxLogTool.e(TAG, "nodata to write,writeCommondList == null || writeCommondList.size() <= 0");
            return;
        }
        bleWriteData = writeCommondList.get(0);
        if (bleWriteData != null && bleWriteData.getCommonds() != null && bleWriteData.getBleWriteResponse() != null) {
            byte[] datas = bleWriteData.getCommonds();
            BleWriteResponse bleWriteResponse = bleWriteData.getBleWriteResponse();
            if (StringUtils.isEmpty(this.curDeviceMac) || AppApplication.getBluetoothClient(AppApplication.getInstance()).getConnectStatus(this.curDeviceMac) != Constants.STATUS_DEVICE_CONNECTED) {
                Log.e(TAG, "curDeviceMac is empty or disconnected,cannot do any option,mac is " + this.curDeviceMac + ";connect statu is " + AppApplication.getBluetoothClient(AppApplication.getInstance()).getConnectStatus(this.curDeviceMac));
                bleWriteResponse.onResponse(Code.REQUEST_FAILED);
                return;
            }
            Log.e(TAG, "real write date is  " + RxDataTool.bytes2HexString(datas));
            AppApplication.getBluetoothClient(AppApplication.getInstance()).write(this.curDeviceMac, UUID_SERVICE_CHANNEL, UUID_CHARACTERISTIC_CHANNEL_WRITE_READ, datas, bleWriteResponse);
        } else {
            RxLogTool.e(TAG, "bleWriteData == null|| bleWriteData.getCommonds() == null || bleWriteData.getBleWriteResponse() == null");
        }
    }

    public void registerConnectListener(BleConnectStatusListener mBleConnectStatusListener) {
        AppApplication.getBluetoothClient(AppApplication.getInstance()).registerConnectStatusListener(this.curDeviceMac, mBleConnectStatusListener);
    }

    public void unregisterConnectStatusListener(BleConnectStatusListener mBleConnectStatusListener) {
        AppApplication.getBluetoothClient(AppApplication.getInstance()).unregisterConnectStatusListener(this.curDeviceMac, mBleConnectStatusListener);
    }

}
