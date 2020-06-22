package com.myzr.allproducts.entity.http.checkversion;

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
public class CheckUpdateResponseEntity implements Parcelable{
    private int status;
    private String message;
    private CheckUpdateResponseDataEntity []data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CheckUpdateResponseDataEntity[] getResponseDataEntityArray() {
        return data;
    }

    public void setResponseDataEntityArray(CheckUpdateResponseDataEntity[] responseDataEntityArray) {
        this.data = responseDataEntityArray;
    }

    public CheckUpdateResponseEntity(Parcel in) {
        this.status=in.readInt();
        this.message=in.readString();
        this.data= (CheckUpdateResponseDataEntity[]) in.readParcelableArray(CheckUpdateResponseDataEntity.class.getClassLoader());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.message);
        dest.writeArray(this.data);
    }


    public static final Creator<CheckUpdateResponseEntity> CREATOR = new Creator<CheckUpdateResponseEntity>() {
        @Override
        public CheckUpdateResponseEntity createFromParcel(Parcel in) {
            return new CheckUpdateResponseEntity(in);
        }

        @Override
        public CheckUpdateResponseEntity[] newArray(int size) {
            return new CheckUpdateResponseEntity[size];
        }
    };
}
