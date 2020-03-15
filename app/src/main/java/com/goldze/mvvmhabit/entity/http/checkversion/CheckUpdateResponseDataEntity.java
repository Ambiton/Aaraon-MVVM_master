package com.goldze.mvvmhabit.entity.http.checkversion;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:
 * @date : 2020/1/1 22:13
无需升级资源
即app端的资源都是最新的
{
"status": 200,
"data": []
}
需要升级资源
{
"status": 200,
"data": [
{
"type": "1",
"appKey": "",
"newestVerno": "01.01",
"forceUpgrade": "1",
"playMode": "1",
"fileLength": 145089,
"desc": "01.01升级版",
"verName": "开机广告01.01版",
"verType": "upgrade",
"fileName": "bootad20200311.zip",
"packSavepath": "http://res.myzr.com.cn/myzrcloud/bootad/bootad20200311.zip"
},
{
"type": "2",
"appKey": "",
"newestVerno": "01.01",
"forceUpgrade": "1",
"playMode": "1",
"fileLength": 373129,
"desc": "01.01升级版",
"verName": "内部广告01.01版",
"verType": "upgrade",
"fileName": "banner20200311.zip",
"packSavepath": "http://res.myzr.com.cn/myzrcloud/banner/banner20200311.zip"
},
{
"type": "3",
"appKey": "1uMqYWpHo3MoLH",
"newestVerno": "01.01",
"forceUpgrade": "1",
"playMode": "1",
"fileLength": 21581824,
"desc": "01.01升级版",
"verName": "安卓app第01.01版",
"verType": "upgrade",
"fileName": "MYZR_200307_1812.apk",
"packSavepath": "http://res.myzr.com.cn/myzrcloud/app/MYZR_200307_1812.apk"
}
]
}

 */
public class CheckUpdateResponseDataEntity implements Parcelable{

    public static final String FORCE_UPGRADE="1";
    public static final String NOTFORCE_UPGRADE="0";
    public static final String TYPE_LOAD_RES="1";
    public static final String TYPE_BANNER="2";
    public static final String TYPE_APP="3";
    /**
     * playMode	1=每进去一次换一张
     * 2=进去以后一直循环播放广告图片
     */
    public static final String PLAYMODE_ONCE="1";
    public static final String PLAYMODE_AUTO="2";

    private String type;
    private String appKey;
    private String newestVerno;
    private String forceUpgrade;
    private String desc;
    private String verName;
    private String verType;
    private String fileName;
    private String packSavepath;
    private String playMode;
    private long fileLength;

    public String getPlayMode() {
        return playMode;
    }

    public void setPlayMode(String playMode) {
        this.playMode = playMode;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

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
        this.playMode=in.readString();
        this.fileLength=in.readLong();
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
        dest.writeString(this.playMode);
        dest.writeLong(this.fileLength);
    }
}
