package com.myzr.allproducts.entity.http.register;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:注册接收体
 * @date : 2020/1/1 22:13
 *  注册成功
 *                {
 *  		    "status": 200,
 *   	    	"message": "ok",
 *             "data": {
 *               "userId": 6,
 *               "token": "aeb80a93ac01aa6fe1a1314fc7b7cbda"
 *              }
 *          }
 * 已被注册
 *        {
 *   			"status": 500,
 *   			"message": "该手机号已经被注册过!"
 *        }
 * 验证码错误或过期
 *
 *      {
 *        "status": 500,
 *        "message": "验证码错误或过期!"
 *      }
 */
public class RegisterOrLoginResponseEntity implements Parcelable{
    private int status;
    private String message;
    private RegisterResponseDataEntity data;

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

    public RegisterResponseDataEntity getRegisterResponseDataEntity() {
        return data;
    }

    public void setRegisterResponseDataEntity(RegisterResponseDataEntity registerResponseDataEntity) {
        this.data = registerResponseDataEntity;
    }

    protected RegisterOrLoginResponseEntity(Parcel in) {
        status = in.readInt();
        message = in.readString();
        data = in.readParcelable(RegisterResponseDataEntity.class.getClassLoader());
    }

    public static final Creator<RegisterOrLoginResponseEntity> CREATOR = new Creator<RegisterOrLoginResponseEntity>() {
        @Override
        public RegisterOrLoginResponseEntity createFromParcel(Parcel in) {
            return new RegisterOrLoginResponseEntity(in);
        }

        @Override
        public RegisterOrLoginResponseEntity[] newArray(int size) {
            return new RegisterOrLoginResponseEntity[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.message);
        dest.writeParcelable(this.data,0);
    }
}
