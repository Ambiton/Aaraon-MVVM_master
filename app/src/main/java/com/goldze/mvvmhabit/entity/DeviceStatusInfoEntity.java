package com.goldze.mvvmhabit.entity;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.goldze.mvvmhabit.utils.RxDataTool;

/**
 * @author Areo
 * @description: 明远按摩器状态信息集合
 * @date : 2020/1/12 19:38
 */
public class DeviceStatusInfoEntity extends BaseObservable implements Parcelable {

    public static final byte FLAG_TRUE = 1;
    public static final byte FLAG_FALSE = 0;
    public static final byte FLAG_SPEED_MAX = 2;
    public static final byte FLAG_SPEED_MID = 1;
    public static final byte FLAG_SPEED_MIN = 0;
    public static final byte FLAG_ROATION_AUTO = 0;
    public static final byte FLAG_ROATION_POSISTION = 1;
    public static final byte FLAG_ROATION_REV = 2;
    public static final byte FLAG_VOICE_MAX = 25;
    public static final byte FLAG_VOICE_MID = 20;
    public static final byte FLAG_VOICE_MIN = 15;
    public static final byte FLAG_VOICE_MUTE = 0;
    private byte isDeviceOpen;
    private byte isHeatingOpen;
    private byte deviceSpeed;
    private byte deviceRoation;
    private byte deviceVoice;
    private byte deviceVoiceSwitch;

    public byte getIsDeviceOpen() {
        return isDeviceOpen;
    }

    public void setIsDeviceOpen(byte isDeviceOpen) {
        this.isDeviceOpen = isDeviceOpen;
    }

    public byte getIsHeatingOpen() {
        return isHeatingOpen;
    }

    public void setIsHeatingOpen(byte isHeatingOpen) {
        this.isHeatingOpen = isHeatingOpen;
    }

    public byte getDeviceRoation() {
        return deviceRoation;
    }

    public void setDeviceRoation(byte deviceRoation) {
        this.deviceRoation = deviceRoation;
    }

    public byte getDeviceVoice() {
        return deviceVoice;
    }

    public void setDeviceVoice(byte deviceVoice) {
        this.deviceVoice = deviceVoice;
    }

    public byte getDeviceSpeed() {
        return deviceSpeed;
    }

    public void setDeviceSpeed(byte deviceSpeed) {
        this.deviceSpeed = deviceSpeed;
    }

    public byte getDeviceVoiceSwitch() {
        return deviceVoiceSwitch;
    }

    public void setDeviceVoiceSwitch(byte deviceVoiceSwitch) {
        this.deviceVoiceSwitch = deviceVoiceSwitch;
    }
    public DeviceStatusInfoEntity() {

    }
    public DeviceStatusInfoEntity(byte[]info) {
        if (info.length == 6) {
            this.isDeviceOpen = info[0];
            this.isHeatingOpen = info[1];
            this.deviceSpeed = info[2];
            this.deviceRoation = RxDataTool.getLow4(info[3]);
            this.deviceVoice = info[4];
            this.deviceVoiceSwitch = info[5];
        }
    }


    protected DeviceStatusInfoEntity(Parcel in) {
        this.isDeviceOpen = in.readByte();
        this.isHeatingOpen = in.readByte();
        this.deviceSpeed = in.readByte();
        this.deviceRoation = in.readByte();
        this.deviceVoice = in.readByte();
        this.deviceVoiceSwitch = in.readByte();
    }

    public static final Creator<DeviceStatusInfoEntity> CREATOR = new Creator<DeviceStatusInfoEntity>() {
        @Override
        public DeviceStatusInfoEntity createFromParcel(Parcel in) {
            return new DeviceStatusInfoEntity(in);
        }

        @Override
        public DeviceStatusInfoEntity[] newArray(int size) {
            return new DeviceStatusInfoEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isDeviceOpen);
        dest.writeByte(this.isHeatingOpen);
        dest.writeByte(this.deviceSpeed);
        dest.writeByte(this.deviceRoation);
        dest.writeByte(this.deviceVoice);
        dest.writeByte(this.deviceVoiceSwitch);
    }
}
