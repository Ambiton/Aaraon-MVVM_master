package com.goldze.mvvmhabit.entity.http.register;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:注册发送体
 * @date : 2020/1/1 22:13
 *  {
 *         "smsToken":"1e1c3c4ee4fd418ebbac8b9bc6c38aa4",
 *         "userName":"18518461971",
 *         "password":"222727",
 *         "type":"1"
 *     }
 */
public class RegisterBodyEntity implements Parcelable{
    private String smsToken;
    private String userName;
    private String password;
    private String type;

    public RegisterBodyEntity(String smsToken, String userName, String password, String type) {
        this.smsToken = smsToken;
        this.userName = userName;
        this.password = password;
        this.type = type;
    }

    protected RegisterBodyEntity(Parcel in) {
        smsToken = in.readString();
        userName = in.readString();
        password = in.readString();
        type = in.readString();
    }

    public static final Creator<RegisterBodyEntity> CREATOR = new Creator<RegisterBodyEntity>() {
        @Override
        public RegisterBodyEntity createFromParcel(Parcel in) {
            return new RegisterBodyEntity(in);
        }

        @Override
        public RegisterBodyEntity[] newArray(int size) {
            return new RegisterBodyEntity[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.smsToken);
        dest.writeString(this.userName);
        dest.writeString(this.password);
        dest.writeString(this.type);
    }
}
