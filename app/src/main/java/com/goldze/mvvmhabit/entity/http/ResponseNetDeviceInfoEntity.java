package com.goldze.mvvmhabit.entity.http;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:
 * @date : 2020/3/15 16:52
 * <p>
 * 设备串号在数据库中存在：
 * {
 * "status": 200,
 * "data": {
 * "unitId": 3,
 * "prodId": "MY-TZ-001",
 * "prodName": "MY-TZ-001按摩枕"
 * }
 * }
 * 设备串号在数据库中不存在，则在界面中给出提示，不允许控制设备。
 * {
 * "status": 500,
 * "message": "非法的设备串号"
 * }
 */
public class ResponseNetDeviceInfoEntity implements Parcelable {

    private int status;
    private NetDeviceInfoEntity data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public NetDeviceInfoEntity getData() {
        return data;
    }

    public void setData(NetDeviceInfoEntity data) {
        this.data = data;
    }

    public ResponseNetDeviceInfoEntity(Parcel in) {
        this.status = in.readInt();
        this.data= (NetDeviceInfoEntity) in.readParcelable(NetDeviceInfoEntity.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeParcelable(data,0);
    }

    public static final Creator<ResponseNetDeviceInfoEntity> CREATOR = new Creator<ResponseNetDeviceInfoEntity>() {
        @Override
        public ResponseNetDeviceInfoEntity createFromParcel(Parcel in) {
            return new ResponseNetDeviceInfoEntity(in);
        }

        @Override
        public ResponseNetDeviceInfoEntity[] newArray(int size) {
            return new ResponseNetDeviceInfoEntity[size];
        }
    };

}
