package com.myzr.allproducts.entity.http;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:
 * @date : 2020/3/15 16:57
 * *         "unitId": 3,
 *  *                 "prodId": "MY-TZ-001",
 *  *                 "prodName": "MY-TZ-001按摩枕"
 */
public class NetDeviceInfoEntity implements Parcelable {
    private int unitId;
    private String prodId;
    private String prodName;

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(unitId);
        dest.writeString(prodId);
        dest.writeString(prodName);
    }
    public NetDeviceInfoEntity(Parcel in) {
        this.unitId=in.readInt();
        this.prodId=in.readString();
        this.prodName=in.readString();
    }

    public static final Creator<NetDeviceInfoEntity> CREATOR = new Creator<NetDeviceInfoEntity>() {
        @Override
        public NetDeviceInfoEntity createFromParcel(Parcel in) {
            return new NetDeviceInfoEntity(in);
        }

        @Override
        public NetDeviceInfoEntity[] newArray(int size) {
            return new NetDeviceInfoEntity[size];
        }
    };

}
