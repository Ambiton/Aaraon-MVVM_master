package com.goldze.mvvmhabit.entity;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description: 明远按摩器状态信息集合
 * @date : 2020/1/12 19:38
 */
public class DeviceStatusInfoEntity extends BaseObservable implements Parcelable {

    public static final byte FLAG_TRUE=0x01;
    public static final byte FLAG_FALSE=0x00;
    private byte isDeviceOpen;
    private byte isHeatingOpen;
    private byte isRoationPositive;
    private byte isRoationReversal;
    private byte isRoationAuto;
    private byte isVoiceHigh;
    private byte isVoiceMid;
    private byte isVoiceLow;
    private byte isVoiceMute;
    private byte isSpeedHigh;
    private byte isSpeedMid;
    private byte isSpeedLow;

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

    public byte getIsRoationPositive() {
        return isRoationPositive;
    }

    public void setIsRoationPositive(byte isRoationPositive) {
        this.isRoationPositive = isRoationPositive;
    }

    public byte getIsRoationReversal() {
        return isRoationReversal;
    }

    public void setIsRoationReversal(byte isRoationReversal) {
        this.isRoationReversal = isRoationReversal;
    }

    public byte getIsRoationAuto() {
        return isRoationAuto;
    }

    public void setIsRoationAuto(byte isRoationAuto) {
        this.isRoationAuto = isRoationAuto;
    }

    public byte getIsVoiceHigh() {
        return isVoiceHigh;
    }

    public void setIsVoiceHigh(byte isVoiceHigh) {
        this.isVoiceHigh = isVoiceHigh;
    }

    public byte getIsVoiceMid() {
        return isVoiceMid;
    }

    public void setIsVoiceMid(byte isVoiceMid) {
        this.isVoiceMid = isVoiceMid;
    }

    public byte getIsVoiceLow() {
        return isVoiceLow;
    }

    public void setIsVoiceLow(byte isVoiceLow) {
        this.isVoiceLow = isVoiceLow;
    }

    public byte getIsVoiceMute() {
        return isVoiceMute;
    }

    public void setIsVoiceMute(byte isVoiceMute) {
        this.isVoiceMute = isVoiceMute;
    }

    public byte getIsSpeedHigh() {
        return isSpeedHigh;
    }

    public void setIsSpeedHigh(byte isSpeedHigh) {
        this.isSpeedHigh = isSpeedHigh;
    }

    public byte getIsSpeedMid() {
        return isSpeedMid;
    }

    public void setIsSpeedMid(byte isSpeedMid) {
        this.isSpeedMid = isSpeedMid;
    }

    public byte getIsSpeedLow() {
        return isSpeedLow;
    }

    public void setIsSpeedLow(byte isSpeedLow) {
        this.isSpeedLow = isSpeedLow;
    }

    public DeviceStatusInfoEntity() {
    }

    protected DeviceStatusInfoEntity(Parcel in) {
        this.isDeviceOpen=in.readByte();
        this.isHeatingOpen=in.readByte();
        this.isRoationPositive=in.readByte();
        this.isRoationReversal=in.readByte();
        this.isRoationAuto=in.readByte();
        this.isVoiceHigh=in.readByte();
        this.isVoiceMid=in.readByte();
        this.isVoiceLow=in.readByte();
        this.isVoiceMute=in.readByte();
        this.isSpeedHigh=in.readByte();
        this.isSpeedMid=in.readByte();
        this.isSpeedLow=in.readByte();
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
        dest.writeByte(this.isRoationPositive);
        dest.writeByte(this.isRoationReversal);
        dest.writeByte(this.isRoationAuto);
        dest.writeByte(this.isVoiceHigh);
        dest.writeByte(this.isVoiceMid);
        dest.writeByte(this.isVoiceLow);
        dest.writeByte(this.isVoiceMute);
        dest.writeByte(this.isSpeedHigh);
        dest.writeByte(this.isSpeedMid);
        dest.writeByte(this.isSpeedLow);
    }
}
