package com.goldze.mvvmhabit.data.source.local;

import com.goldze.mvvmhabit.app.AppApplication;
import com.goldze.mvvmhabit.data.source.LocalDataSource;
import com.goldze.mvvmhabit.entity.db.ProductInfoData;
import com.goldze.mvvmhabit.entity.db.ProductInfoDataDao;
import com.goldze.mvvmhabit.entity.db.UserActionData;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateResponseDataEntity;
import com.goldze.mvvmhabit.utils.VersionControlUtil;

import org.greenrobot.greendao.query.Query;

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
    private static final String KEY_USERID = "userid";
    private static final String KEY_UNITID = "unitid";
    private static final String KEY_SMSTOKEN = "smstoken";
    private static final String KEY_VERSION_BANNER = "bannerversion";
    private static final String KEY_VERSION_LOADING = "loadversion";
    private static final String KEY_PLAY_MODE_BANNER = "bannerplaymode";
    private static final String KEY_PLAY_INDEX_BANNER = "bannerplayindex";
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
        SPUtils.getInstance().put(KEY_SMSTOKEN, token);
    }

    @Override
    public void saveToken(String token) {
        SPUtils.getInstance().put(KEY_TOKEN, token);
    }

    @Override
    public void saveUserID(int userId) {
        SPUtils.getInstance().put(KEY_USERID, userId);
    }

    @Override
    public void saveUnitId(int unitId) {
        SPUtils.getInstance().put(KEY_UNITID, unitId);
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
    public void saveBannerVersion(String version) {
        SPUtils.getInstance().put(KEY_VERSION_BANNER, version);
    }

    @Override
    public void saveLoadingVersion(String version) {
        SPUtils.getInstance().put(KEY_VERSION_LOADING, version);
    }

    @Override
    public void saveBannerPlayMode(String bannerPlayMode) {
        SPUtils.getInstance().put(KEY_PLAY_MODE_BANNER, bannerPlayMode);
    }

    @Override
    public void saveBannerPlayIndex(int bannerPlayIndex) {
        SPUtils.getInstance().put(KEY_PLAY_INDEX_BANNER, bannerPlayIndex);
    }

    @Override
    public int getBannerPlayIndex() {
        return SPUtils.getInstance().getInt(KEY_PLAY_INDEX_BANNER,0);
    }

    @Override
    public String getBannerPlayMode() {
        return   SPUtils.getInstance().getString(KEY_PLAY_MODE_BANNER, CheckUpdateResponseDataEntity.PLAYMODE_ONCE);
    }

    @Override
    public String getLoadingVersion() {
        return SPUtils.getInstance().getString(KEY_VERSION_LOADING, VersionControlUtil.VERSION_LOADING);
    }

    @Override
    public String getBannerVersion() {
        return SPUtils.getInstance().getString(KEY_VERSION_BANNER, VersionControlUtil.VERSION_BANNER);
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

    @Override
    public int getUnitID() {
        return SPUtils.getInstance().getInt(KEY_UNITID);
    }

    @Override
    public void saveUserActionDataToDB(UserActionData userActionData) {
        AppApplication.getInstance().getDaoSession().getUserActionDataDao().save(userActionData);
    }

    @Override
    public void saveProductInfoDataToDB(ProductInfoData productInfoData) {
        ProductInfoDataDao productInfoDataDao=AppApplication.getInstance().getDaoSession().getProductInfoDataDao();
        ProductInfoData productInfoData1=getProductInfoData(productInfoData.getProdId());
        if(productInfoData1!=null){
            productInfoData.setId(productInfoData1.getId());
        }
        productInfoDataDao.save(productInfoData);
    }

    @Override
    public ProductInfoData getProductInfoData(String productId) {
        ProductInfoDataDao productInfoDataDao=AppApplication.getInstance().getDaoSession().getProductInfoDataDao();
        Query<ProductInfoData> query = productInfoDataDao.queryBuilder().where(ProductInfoDataDao.Properties.ProdId.eq(productId))
                .orderDesc(ProductInfoDataDao.Properties.Id).build();
        return query.unique();
    }

    @Override
    public void deleteUserActionDataToDB(UserActionData... userActionDatas) {
        AppApplication.getInstance().getDaoSession().getUserActionDataDao().deleteInTx(userActionDatas);
    }
}
