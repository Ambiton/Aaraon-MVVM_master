package com.myzr.allproducts.entity.http.useraction;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:提交用户行为数据信息接收体Data
 * @date : {
 *   "status": 200,
 *   "message": "成功",
 *   "data": true
 * }
 *
 */
public class SubmitActionDataResponseEntity implements Parcelable{
    private int status;
    private String message;
    private boolean data;

    protected SubmitActionDataResponseEntity(Parcel in) {
        status = in.readInt();
        message = in.readString();
        data = in.readByte() != 0;
    }

    public static final Creator<SubmitActionDataResponseEntity> CREATOR = new Creator<SubmitActionDataResponseEntity>() {
        @Override
        public SubmitActionDataResponseEntity createFromParcel(Parcel in) {
            return new SubmitActionDataResponseEntity(in);
        }

        @Override
        public SubmitActionDataResponseEntity[] newArray(int size) {
            return new SubmitActionDataResponseEntity[size];
        }
    };

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

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.message);
        dest.writeBooleanArray(new boolean[]{this.data});
    }
}
