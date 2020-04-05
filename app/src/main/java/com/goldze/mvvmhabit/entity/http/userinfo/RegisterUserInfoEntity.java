package com.goldze.mvvmhabit.entity.http.userinfo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:用户信息注册
 *
 * {
 *     "weight":"12.12",
 *     "height":"1.65",
 *     "birthday":"2019-10-01 00:00:00",
 *     "sex":"1"
 * }
 * @date : 2020/4/1 22:30
 */
public class RegisterUserInfoEntity implements Parcelable {
    public static final String SEX_MALE="1";
    public static final String SEX_FEMALE="2";
    private String weight;
    private String height;
    private String birthday;
    private String sex;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    protected RegisterUserInfoEntity(Parcel in) {
        this.weight=in.readString();
        this.height=in.readString();
        this.birthday=in.readString();
        this.sex=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.weight);
        dest.writeString(this.height);
        dest.writeString(this.birthday);
        dest.writeString(this.sex);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RegisterUserInfoEntity> CREATOR = new Creator<RegisterUserInfoEntity>() {
        @Override
        public RegisterUserInfoEntity createFromParcel(Parcel in) {
            return new RegisterUserInfoEntity(in);
        }

        @Override
        public RegisterUserInfoEntity[] newArray(int size) {
            return new RegisterUserInfoEntity[size];
        }
    };
}
