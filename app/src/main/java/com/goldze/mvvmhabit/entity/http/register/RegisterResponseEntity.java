package com.goldze.mvvmhabit.entity.http.register;

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
public class RegisterResponseEntity implements Parcelable{
    private int status;
    private RegisterResponseDataEntity registerResponseDataEntity;

    protected RegisterResponseEntity(Parcel in) {
        status = in.readInt();
        registerResponseDataEntity = in.readParcelable(RegisterResponseDataEntity.class.getClassLoader());
    }

    public static final Creator<RegisterResponseEntity> CREATOR = new Creator<RegisterResponseEntity>() {
        @Override
        public RegisterResponseEntity createFromParcel(Parcel in) {
            return new RegisterResponseEntity(in);
        }

        @Override
        public RegisterResponseEntity[] newArray(int size) {
            return new RegisterResponseEntity[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeParcelable(this.registerResponseDataEntity,flags);
    }
}
