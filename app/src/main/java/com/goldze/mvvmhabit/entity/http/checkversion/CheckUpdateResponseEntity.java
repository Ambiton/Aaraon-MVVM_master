package com.goldze.mvvmhabit.entity.http.checkversion;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:
 * @date : 2020/1/1 22:13
 * 无需升级资源
 *     {
 *        "status": 200,
 *        "data": []
 *      }
 * 需要升级资源
 *   {
 *   "status": 200,
 *   "data": [
 *     {
 *       "type": "1",
 *       "appKey": "",
 *       "newestVerno": "01.00",
 *       "forceUpgrade": "0",
 *       "desc": "1.0版初始版本",
 *       "verName": "开机广告1.0版",
 *       "verType": "init",
 *       "fileName": "test1.zip",
 *       "packSavepath": "http://www.baidu.com/test1.zip"
 *     },
 *     {
 *       "type": "2",
 *       "appKey": "",
 *       "newestVerno": "01.00",
 *       "forceUpgrade": "0",
 *       "desc": "1.0版初始版",
 *       "verName": "内部广告1.0版",
 *       "verType": "init",
 *       "fileName": "test.zip",
 *       "packSavepath": "http://www.larkiv.com/upload/upgrade/Larksmarl0002.zip"
 *     },
 *     {
 *       "type": "3",
 *       "appKey": "1uMqYWpHo3MoLH",
 *       "newestVerno": "01.00",
 *       "forceUpgrade": "0",
 *       "desc": "1.0版初始版",
 *       "verName": "安卓app第一版",
 *       "verType": "init",
 *       "fileName": "Firmware-7618-code2368_000100_10101010.bin",
 *       "packSavepath": "http://www.larkiv.com/upload/upgrade/v1/Firmware-7618-code2368_000100_10101010.bin"
 *     }
 *   ]
 * }
 */
public class CheckUpdateResponseEntity implements Parcelable{
    private int status;
    private CheckUpdateResponseDataEntity []responseDataEntityArray;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CheckUpdateResponseDataEntity[] getResponseDataEntityArray() {
        return responseDataEntityArray;
    }

    public void setResponseDataEntityArray(CheckUpdateResponseDataEntity[] responseDataEntityArray) {
        this.responseDataEntityArray = responseDataEntityArray;
    }

    public CheckUpdateResponseEntity(Parcel in) {
        this.status=in.readInt();
        this.responseDataEntityArray= (CheckUpdateResponseDataEntity[]) in.readParcelableArray(CheckUpdateResponseDataEntity.class.getClassLoader());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeArray(this.responseDataEntityArray);
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
