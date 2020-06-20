package com.myzr.allproduct.entity.http.productinfo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:
 * @date : 2020/1/1 22:13
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

 */
public class ProductInfoResponseDataEntity implements Parcelable{
    private String prodId;
    private String prodName;
    private String brandResNewestVerno;
    private long brandResFileLength;
    private String brandResFileName;
    private String brandResPackSavepath;
    private String styleResNewestVerno;
    private long styleResFileLength;
    private String styleResFileName;
    private String styleResPackSavepath;

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

    public String getBrandResNewestVerno() {
        return brandResNewestVerno;
    }

    public void setBrandResNewestVerno(String brandResNewestVerno) {
        this.brandResNewestVerno = brandResNewestVerno;
    }

    public long getBrandResFileLength() {
        return brandResFileLength;
    }

    public void setBrandResFileLength(long brandResFileLength) {
        this.brandResFileLength = brandResFileLength;
    }

    public String getBrandResFileName() {
        return brandResFileName;
    }

    public void setBrandResFileName(String brandResFileName) {
        this.brandResFileName = brandResFileName;
    }

    public String getBrandResPackSavepath() {
        return brandResPackSavepath;
    }

    public void setBrandResPackSavepath(String brandResPackSavepath) {
        this.brandResPackSavepath = brandResPackSavepath;
    }

    public String getStyleResNewestVerno() {
        return styleResNewestVerno;
    }

    public void setStyleResNewestVerno(String styleResNewestVerno) {
        this.styleResNewestVerno = styleResNewestVerno;
    }

    public long getStyleResFileLength() {
        return styleResFileLength;
    }

    public void setStyleResFileLength(long styleResFileLength) {
        this.styleResFileLength = styleResFileLength;
    }

    public String getStyleResFileName() {
        return styleResFileName;
    }

    public void setStyleResFileName(String styleResFileName) {
        this.styleResFileName = styleResFileName;
    }

    public String getStyleResPackSavepath() {
        return styleResPackSavepath;
    }

    public void setStyleResPackSavepath(String styleResPackSavepath) {
        this.styleResPackSavepath = styleResPackSavepath;
    }

    public ProductInfoResponseDataEntity(Parcel in) {
        this.prodId=in.readString();
        this.prodName=in.readString();
        this.brandResNewestVerno=in.readString();
        this.brandResFileLength=in.readLong();
        this.brandResFileName=in.readString();
        this.brandResPackSavepath= in.readString();
        this.styleResNewestVerno=in.readString();
        this.styleResFileLength=in.readLong();
        this.styleResFileName=in.readString();
        this.styleResPackSavepath=in.readString();
    }

    public static final Creator<ProductInfoResponseDataEntity> CREATOR = new Creator<ProductInfoResponseDataEntity>() {
        @Override
        public ProductInfoResponseDataEntity createFromParcel(Parcel in) {
            return new ProductInfoResponseDataEntity(in);
        }

        @Override
        public ProductInfoResponseDataEntity[] newArray(int size) {
            return new ProductInfoResponseDataEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.prodId);
        dest.writeString(this.prodName);
        dest.writeString(this.brandResNewestVerno);
        dest.writeLong(this.brandResFileLength);
        dest.writeString(this.brandResFileName);
        dest.writeString(this.brandResPackSavepath);
        dest.writeString(this.styleResNewestVerno);
        dest.writeLong(this.styleResFileLength);
        dest.writeString(this.styleResFileName);
        dest.writeString(this.styleResPackSavepath);
    }
}
