package com.goldze.mvvmhabit.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "USERNAME")  
    private String username;  
    @Property(nameInDb = "NICKNAME")  
    private String nickname;
    @Property
    private String birthday;
    @Property
    private int sex;
    @Property
    private int userage;
    @Property
    private String password;
    @Generated(hash = 947738200)
    public User(Long id, String username, String nickname, String birthday, int sex,
            int userage, String password) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.birthday = birthday;
        this.sex = sex;
        this.userage = userage;
        this.password = password;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getBirthday() {
        return this.birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public int getSex() {
        return this.sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public int getUserage() {
        return this.userage;
    }
    public void setUserage(int userage) {
        this.userage = userage;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}