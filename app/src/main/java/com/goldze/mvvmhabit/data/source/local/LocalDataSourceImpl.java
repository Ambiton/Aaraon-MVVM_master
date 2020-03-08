package com.goldze.mvvmhabit.data.source.local;

import com.goldze.mvvmhabit.data.source.LocalDataSource;

import me.goldze.mvvmhabit.utils.SPUtils;

/**
 * 本地数据源，可配合Room框架使用
 * Created by goldze on 2019/3/26.
 */
public class LocalDataSourceImpl implements LocalDataSource {
    private volatile static LocalDataSourceImpl INSTANCE = null;
    private static final String KEY_USERNAME = "UserName";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TOKEN = "Token";
    private static final String KEY_USERID ="userid";
    private static final String KEY_SMSTOKEN ="smstoken";

    public static LocalDataSourceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (LocalDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalDataSourceImpl();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private LocalDataSourceImpl() {
        //数据库Helper构建
    }

    @Override
    public void saveSmsToken(String token) {
        SPUtils.getInstance().put(KEY_SMSTOKEN,token);
    }

    @Override
    public void saveToken(String token) {
        SPUtils.getInstance().put(KEY_TOKEN, token);
    }

    @Override
    public void saveUserID(int userId) {
        SPUtils.getInstance().put(KEY_USERID,userId);
    }

    @Override
    public void saveUserName(String userName) {
        SPUtils.getInstance().put(KEY_USERNAME, userName);
    }

    @Override
    public void savePassword(String password) {
        SPUtils.getInstance().put(KEY_PASSWORD, password);
    }

    @Override
    public String getUserName() {
        return SPUtils.getInstance().getString(KEY_USERNAME);
    }

    @Override
    public String getPassword() {
        return SPUtils.getInstance().getString(KEY_PASSWORD);
    }

    @Override
    public String getToken() {
        return SPUtils.getInstance().getString(KEY_TOKEN);
    }

    @Override
    public String getSmsToken() {
        return SPUtils.getInstance().getString(KEY_SMSTOKEN);
    }

    @Override
    public int getUserID() {
        return SPUtils.getInstance().getInt(KEY_USERID);
    }
}
