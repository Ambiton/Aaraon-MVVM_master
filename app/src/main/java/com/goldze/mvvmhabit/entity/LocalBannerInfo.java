package com.goldze.mvvmhabit.entity;

public class LocalBannerInfo{
        private String fileHeadName;
        private Object picUrl;
        private String clickUrl;

    public LocalBannerInfo(String fileHeadName, Object picUrl, String clickUrl) {
        this.fileHeadName = fileHeadName;
        this.picUrl = picUrl;
        this.clickUrl = clickUrl;
    }

    public String getFileHeadName() {
        return fileHeadName;
    }

    public void setFileHeadName(String fileHeadName) {
        this.fileHeadName = fileHeadName;
    }

    public Object getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(Object picUrl) {
        this.picUrl = picUrl;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }
}