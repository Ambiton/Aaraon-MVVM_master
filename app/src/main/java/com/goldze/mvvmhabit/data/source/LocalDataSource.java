package com.goldze.mvvmhabit.data.source;

import com.goldze.mvvmhabit.entity.db.UserActionData;

/**
 * Created by goldze on 2019/3/26.
 */
public interface LocalDataSource {

    /**
     * 保存smsToken
     */
    void saveSmsToken(String smsToken);

    /**
     * 保存token
     */
    void saveToken(String token);

    /**
     * 保存用户ID
     */
    void saveUserID(int userId);
    /**
     * 保存设备唯一ID
     */
    void saveUnitId(int unitId);
    /**
     * 保存用户名
     */
    void saveUserName(String userName);

    /**
     * 保存用户密码
     */
    void savePassword(String password);

    /**
     * 保存Banner版本
     */
    void saveBannerVersion(String version);

    /**
     * 保存Loading版本
     */
    void saveLoadingVersion(String version);

    /**
     * 保存BannerPlayMode
     */
    void saveBannerPlayMode(String bannerPlayMode);

    /**
     * 保存bannerPlayIndex
     */
    void saveBannerPlayIndex(int bannerPlayIndex);

    /**
     * 获取BannerPlayMode
     */
    int getBannerPlayIndex();

    /**
     * 获取BannerPlayMode
     */
    String getBannerPlayMode();
    /**
     * 获取Loading版本
     */
    String getLoadingVersion();

    /**
     * 获取Banner版本
     */
    String getBannerVersion();

    /**
     * 获取用户名
     */
    String getUserName();

    /**
     * 获取用户密码
     */
    String getPassword();

    /**
     * 获取Token
     */
    String getToken();

    /**
     * 获取smsToken
     */
    String getSmsToken();

    /**
     * 获取UserID
     */
    int getUserID();

    /**
     * 获取设备唯一ID
     * @return
     */
    int getUnitID();

    /**
     * save 用户数据
     */
    void saveUserActionDataToDB(UserActionData userActionData);

    /**
     * delete 用户数据
     */
    void deleteUserActionDataToDB(UserActionData ...userActionDatas);
}
