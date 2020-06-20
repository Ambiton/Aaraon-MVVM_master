package com.myzr.allproduct.entity.db;

import com.myzr.allproduct.entity.http.productinfo.ProductInfoResponseDataEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 {
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
@Entity
public class ProductInfoData {
    @Id(autoincrement = true)
    @Property(nameInDb = "id")
    private Long id;
    @Property(nameInDb = "prodId")
    private String prodId;
    @Property(nameInDb = "prodName")
    private String prodName;
    @Property(nameInDb = "brandResNewestVerno")
    private String brandResNewestVerno;
    @Property(nameInDb = "brandResFileLength")
    private long brandResFileLength;
    @Property(nameInDb = "brandResFileName")
    private String brandResFileName;
    @Property(nameInDb = "brandResPackSavepath")
    private String brandResPackSavepath;
    @Property(nameInDb = "styleResNewestVerno")
    private String styleResNewestVerno;
    @Property(nameInDb = "styleResFileLength")
    private long styleResFileLength;
    @Property(nameInDb = "styleResFileName")
    private String styleResFileName;
    @Property(nameInDb = "styleResPackSavepath")
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductInfoData(ProductInfoResponseDataEntity entity) {
        this.prodId = entity.getProdId();
        this.prodName = entity.getProdName();
        this.brandResNewestVerno = entity.getBrandResNewestVerno();
        this.brandResFileLength = entity.getBrandResFileLength();
        this.brandResFileName = entity.getBrandResFileName();
        this.brandResPackSavepath = entity.getBrandResPackSavepath();
        this.styleResNewestVerno = entity.getStyleResNewestVerno();
        this.styleResFileLength = entity.getStyleResFileLength();
        this.styleResFileName = entity.getStyleResFileName();
        this.styleResPackSavepath = entity.getStyleResPackSavepath();
    }
    public ProductInfoData(String prodId, String prodName, String brandResNewestVerno, long brandResFileLength, String brandResFileName,
                           String brandResPackSavepath, String styleResNewestVerno, long styleResFileLength, String styleResFileName, String styleResPackSavepath) {
        this.prodId = prodId;
        this.prodName = prodName;
        this.brandResNewestVerno = brandResNewestVerno;
        this.brandResFileLength = brandResFileLength;
        this.brandResFileName = brandResFileName;
        this.brandResPackSavepath = brandResPackSavepath;
        this.styleResNewestVerno = styleResNewestVerno;
        this.styleResFileLength = styleResFileLength;
        this.styleResFileName = styleResFileName;
        this.styleResPackSavepath = styleResPackSavepath;
    }

    @Generated(hash = 161818907)
    public ProductInfoData(Long id, String prodId, String prodName, String brandResNewestVerno, long brandResFileLength, String brandResFileName,
            String brandResPackSavepath, String styleResNewestVerno, long styleResFileLength, String styleResFileName, String styleResPackSavepath) {
        this.id = id;
        this.prodId = prodId;
        this.prodName = prodName;
        this.brandResNewestVerno = brandResNewestVerno;
        this.brandResFileLength = brandResFileLength;
        this.brandResFileName = brandResFileName;
        this.brandResPackSavepath = brandResPackSavepath;
        this.styleResNewestVerno = styleResNewestVerno;
        this.styleResFileLength = styleResFileLength;
        this.styleResFileName = styleResFileName;
        this.styleResPackSavepath = styleResPackSavepath;
    }

    @Generated(hash = 2086561848)
    public ProductInfoData() {
    }
}