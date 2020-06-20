package com.myzr.allproduct.entity;

import androidx.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.myzr.allproduct.utils.RxDataTool;

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
    public static final byte FLAG_ROATION_POSISTION_AND_REV = 3;
    public static final byte FLAG_ROATION_DIRECT_STOP0 = 0;
    public static final byte FLAG_ROATION_DIRECT_POSISION = 1;
    public static final byte FLAG_ROATION_DIRECT_REV = 2;
    public static final byte FLAG_ROATION_DIRECT_STOP3 = 0;
    public static final byte FLAG_VOICE_MAX = 25;
    public static final byte FLAG_VOICE_MID = 20;
    public static final byte FLAG_VOICE_MIN = 15;
    public static final byte FLAG_VOICE_MUTE = 0;
    private byte isDeviceOpen;
    private byte isHeatingOpen;
    private byte deviceSpeed;
    private byte deviceRoationDirect;
    private byte deviceRoationMode;
    private byte deviceVoice;
    private byte deviceVoiceSwitch;

    private byte productName;
    private byte style;
    private byte productTypeAndVersion;
    private byte bleVersion;

    public byte getProductName() {
        return productName;
    }

    public void setProductName(byte productName) {
        this.productName = productName;
    }

    public byte getStyle() {
        return style;
    }

    public byte getProductType(){
        return RxDataTool.getHeight4(productTypeAndVersion);
    }

    public byte getProductVersion(){
        return RxDataTool.getLow4(productTypeAndVersion);
    }

    public String getBatchCode() {
        return getProductType() +"_"+getProductVersion()+"_"+style+"_"+bleVersion+"_"+productName;
    }

    public void setStyle(byte style) {
        this.style = style;
    }

    public byte getProductTypeAndVersion() {
        return productTypeAndVersion;
    }

    public void setProductTypeAndVersion(byte productTypeAndVersion) {
        this.productTypeAndVersion = productTypeAndVersion;
    }

    public byte getBleVersion() {
        return bleVersion;
    }

    public void setBleVersion(byte bleVersion) {
        this.bleVersion = bleVersion;
    }

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

    public byte getDeviceRoationMode() {
        return deviceRoationMode;
    }

    public void setDeviceRoationMode(byte deviceRoationMode) {
        this.deviceRoationMode = deviceRoationMode;
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

    public byte getDeviceRoationDirect() {
        return deviceRoationDirect;
    }

    public void setDeviceRoationDirect(byte deviceRoationDirect) {
        this.deviceRoationDirect = deviceRoationDirect;
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
        if (info.length == 6||info.length == 10) {
            this.isDeviceOpen = info[0];
            this.isHeatingOpen = info[1];
            this.deviceSpeed = info[2];
            this.deviceRoationDirect = RxDataTool.getHeight4(info[3]);
            this.deviceRoationMode = RxDataTool.getLow4(info[3]);
            this.deviceVoice = info[4];
            this.deviceVoiceSwitch = info[5];
            if(info.length == 10){
                this.productName =info[6];
                this.style=info[7];
                this.productTypeAndVersion =info[8];
                this.bleVersion =info[9];

//                this.productName =1;
//                this.style=1;
//                this.productTypeAndVersion =0;
//                this.bleVersion =0;
            }
        }
    }


    protected DeviceStatusInfoEntity(Parcel in) {
        this.isDeviceOpen = in.readByte();
        this.isHeatingOpen = in.readByte();
        this.deviceSpeed = in.readByte();
        this.deviceRoationDirect=in.readByte();
        this.deviceRoationMode = in.readByte();
        this.deviceVoice = in.readByte();
        this.deviceVoiceSwitch = in.readByte();
        this.productName =in.readByte();
        this.style=in.readByte();
        this.productTypeAndVersion =in.readByte();
        this.bleVersion =in.readByte();
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
        dest.writeByte(this.deviceRoationDirect);
        dest.writeByte(this.deviceRoationMode);
        dest.writeByte(this.deviceVoice);
        dest.writeByte(this.deviceVoiceSwitch);
        dest.writeByte(this.productName);
        dest.writeByte(this.style);
        dest.writeByte(this.productTypeAndVersion);
        dest.writeByte(this.bleVersion);
    }
}
