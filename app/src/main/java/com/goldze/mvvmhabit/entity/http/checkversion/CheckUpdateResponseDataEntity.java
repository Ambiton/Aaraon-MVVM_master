package com.goldze.mvvmhabit.entity.http.checkversion;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:
 * @date : 2020/1/1 22:13
 *  {
 *  *       "type": "1",
 *  *       "appKey": "",
 *  *       "newestVerno": "01.00",
 *  *       "forceUpgrade": "0",
 *  *       "desc": "1.0版初始版本",
 *  *       "verName": "开机广告1.0版",
 *  *       "verType": "init",
 *  *       "fileName": "test1.zip",
 *  *       "packSavepath": "http://www.baidu.com/test1.zip"
 *  *     }
 *        或
 *  *     {
 *  *       "type": "2",
 *  *       "appKey": "",
 *  *       "newestVerno": "01.00",
 *  *       "forceUpgrade": "0",
 *  *       "desc": "1.0版初始版",
 *  *       "verName": "内部广告1.0版",
 *  *       "verType": "init",
 *  *       "fileName": "test.zip",
 *  *       "packSavepath": "http://www.larkiv.com/upload/upgrade/Larksmarl0002.zip"
 *  *     }
 *        或
 *  *     {
 *  *       "type": "3",
 *  *       "appKey": "1uMqYWpHo3MoLH",
 *  *       "newestVerno": "01.00",
 *  *       "forceUpgrade": "0",
 *  *       "desc": "1.0版初始版",
 *  *       "verName": "安卓app第一版",
 *  *       "verType": "init",
 *  *       "fileName": "Firmware-7618-code2368_000100_10101010.bin",
 *  *       "packSavepath": "http://www.larkiv.com/upload/upgrade/v1/Firmware-7618-code2368_000100_10101010.bin"
 *  *     }
 */
public class CheckUpdateResponseDataEntity implements Parcelable{
    private String type;
    private String appKey;
    private String newestVerno;
    private String forceUpgrade;
    private String desc;
    private String verName;
    private String verType;
    private String fileName;
    private String packSavepath;

    public CheckUpdateResponseDataEntity(Parcel in) {
        this.type=in.readString();
        this.appKey=in.readString();
        this.newestVerno=in.readString();
        this.forceUpgrade=in.readString();
        this.desc=in.readString();
        this.verName= in.readString();
        this.verType=in.readString();
        this.fileName=in.readString();
        this.packSavepath=in.readString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getNewestVerno() {
        return newestVerno;
    }

    public void setNewestVerno(String newestVerno) {
        this.newestVerno = newestVerno;
    }

    public String getForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(String forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public String getVerType() {
        return verType;
    }

    public void setVerType(String verType) {
        this.verType = verType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPackSavepath() {
        return packSavepath;
    }

    public void setPackSavepath(String packSavepath) {
        this.packSavepath = packSavepath;
    }

    public static final Creator<CheckUpdateResponseDataEntity> CREATOR = new Creator<CheckUpdateResponseDataEntity>() {
        @Override
        public CheckUpdateResponseDataEntity createFromParcel(Parcel in) {
            return new CheckUpdateResponseDataEntity(in);
        }

        @Override
        public CheckUpdateResponseDataEntity[] newArray(int size) {
            return new CheckUpdateResponseDataEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.appKey);
        dest.writeString(this.newestVerno);
        dest.writeString(this.forceUpgrade);
        dest.writeString(this.desc);
        dest.writeString(this.verName);
        dest.writeString(this.verType);
        dest.writeString(this.fileName);
        dest.writeString(this.packSavepath);
    }
}
