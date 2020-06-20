package com.myzr.allproduct.data.source.local;

import com.myzr.allproduct.app.AppApplication;
import com.myzr.allproduct.data.source.LocalDataSource;
import com.myzr.allproduct.entity.db.ProductInfoData;
import com.myzr.allproduct.entity.db.ProductInfoDataDao;
import com.myzr.allproduct.entity.db.UserActionData;
import com.myzr.allproduct.entity.http.checkversion.CheckUpdateResponseDataEntity;
import com.myzr.allproduct.utils.VersionControlUtil;

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
    private static final String KEY_MAC_FOR_PRODUCT = "MacForProduct";
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
    public void saveProductname(String batchcode, String productname) {
        SPUtils.getInstance().put(batchcode, productname);
    }
    @Override
    public void saveProductnameByMac(String mac, String productname) {
        SPUtils.getInstance().put(KEY_MAC_FOR_PRODUCT+mac, productname);
    }
    @Override
    public String getProductnameByMac(String mac) {
        return  SPUtils.getInstance().getString(KEY_MAC_FOR_PRODUCT+mac, "");
    }

    @Override
    public String getProductname(String batchcode) {
        return  SPUtils.getInstance().getString(batchcode, "明远科技按摩器");
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
            productInfoDataDao.save(productInfoData);
        }else{
            productInfoDataDao.insert(productInfoData);
        }

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
