package com.myzr.allproduct.entity.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * "remoteId": 13,
 * "actFlag": "forward",
 * "actVal": "1",
 * "actTime": "2019-10-01 00:00:00",
 * "unitId": 3
 */
@Entity
public class UserActionData {
    @Id(autoincrement = true)
    @Property(nameInDb = "remoteId")
    private Long remoteId;
    @Property(nameInDb = "seri_no")
    private String seriNo;
    @Property(nameInDb = "actFlag")
    private String actFlag;
    @Property(nameInDb = "actVal")
    private String actVal;
    @Property(nameInDb = "actTime")
    private String actTime;
    @Property(nameInDb = "unitId")
    private int unitId;
    @Property(nameInDb = "devStatus")
    private String devStatus;
    @Property(nameInDb = "status")
    private String status;

    @Generated(hash = 1157759329)
    public UserActionData(Long remoteId, String seriNo, String actFlag,
            String actVal, String actTime, int unitId, String devStatus,
            String status) {
        this.remoteId = remoteId;
        this.seriNo = seriNo;
        this.actFlag = actFlag;
        this.actVal = actVal;
        this.actTime = actTime;
        this.unitId = unitId;
        this.devStatus = devStatus;
        this.status = status;
    }

    @Generated(hash = 1199631021)
    public UserActionData() {
    }

    public String getSeriNo() {
        return seriNo;
    }

    public void setSeriNo(String seriNo) {
        this.seriNo = seriNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }

    public String getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(String devStatus) {
        this.devStatus = devStatus;
    }

    public String getActFlag() {
        return actFlag;
    }

    public void setActFlag(String actFlag) {
        this.actFlag = actFlag;
    }

    public String getActVal() {
        return actVal;
    }

    public void setActVal(String actVal) {
        this.actVal = actVal;
    }

    public String getActTime() {
        return actTime;
    }

    public void setActTime(String actTime) {
        this.actTime = actTime;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
}