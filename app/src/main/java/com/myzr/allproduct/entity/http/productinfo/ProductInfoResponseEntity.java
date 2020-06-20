package com.myzr.allproduct.entity.http.productinfo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:
 * @date : 2020/1/1 22:13
{
"status": 200,
"message": null,
"data": {
"prodId": "2_1_1",
"prodName": "宇视科技_头枕式按摩器",
"brandResNewestVerno": "01.00",
"brandResFileLength": 11254,
"brandResFileName": "2_1brand.zip",
"brandResPackSavepath": "http://res.myzr.com.cn/brand/2_1brand.zip",
"styleResNewestVerno": "01.00",
"styleResFileLength": 13512562,
"styleResFileName": "2_1style.zip",
"styleResPackSavepath": "http://res.myzr.com.cn/style/2_1style.zip"
}
}

批次编码在数据库中不存在，则在界面中给出提示，不允许控制设备。
{
"status": 500,
"message": "非法的产品批次信息"
}

 */
public class ProductInfoResponseEntity implements Parcelable{
    private int status;
    private String message;
    private ProductInfoResponseDataEntity data;

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

    public ProductInfoResponseDataEntity getData() {
        return data;
    }

    public void setData(ProductInfoResponseDataEntity data) {
        this.data = data;
    }

    public ProductInfoResponseEntity(Parcel in) {
        this.status=in.readInt();
        this.data= (ProductInfoResponseDataEntity) in.readParcelable(ProductInfoResponseDataEntity.class.getClassLoader());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeParcelable(this.data,flags);
    }


    public static final Creator<ProductInfoResponseEntity> CREATOR = new Creator<ProductInfoResponseEntity>() {
        @Override
        public ProductInfoResponseEntity createFromParcel(Parcel in) {
            return new ProductInfoResponseEntity(in);
        }

        @Override
        public ProductInfoResponseEntity[] newArray(int size) {
            return new ProductInfoResponseEntity[size];
        }
    };
}
