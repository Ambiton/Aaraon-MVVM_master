package com.myzr.allproducts.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.myzr.allproducts.R;
import com.myzr.allproducts.app.AppApplication;
import com.myzr.allproducts.entity.LocalBannerInfo;
import com.myzr.allproducts.entity.StyleResEntity;
import com.myzr.allproducts.entity.http.checkversion.CheckUpdateResponseDataEntity;
import com.myzr.allproducts.entity.http.productinfo.ProductInfoResponseEntity;
import com.myzr.allproducts.ui.main.DeviceListViewModel;
import com.myzr.allproducts.ui.main.LoadingViewModel;
import com.tamsiree.rxtool.RxFileTool;
import com.tamsiree.rxtool.RxLogTool;
import com.tamsiree.rxtool.RxZipTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import me.goldze.mvvmhabit.http.DownLoadManager;
import me.goldze.mvvmhabit.http.NetworkUtil;
import me.goldze.mvvmhabit.http.download.ProgressCallBack;
import me.goldze.mvvmhabit.utils.ToastUtils;
import okhttp3.ResponseBody;

/**
 * @author Areo
 * @description:本APP工具类
 * @date : 2020/3/8 16:49
 */
public class AppTools {
    public static final String CUREENT_SERIONUM = "123456";//按摩椅的设备串号
    public static final String APPKEY = "1uMqYWpHo3MoLH";
    public static final String APPSIGN = "sig-result";
    public static final String KEY_REGISTER_USERINFO = "registeruserinfo";
    private static final String TAG = "AppTools";
    private static final String FILENAME_ZIP_BANNER = "bannerImages";
    private static final String FILENAME_ZIP_LOAD = "loadImages";


    public static class UserActionFlagAndValue {
        public static final String FLAG_ROATION_MODE = "roationMode";
        public static final String FLAG_SPEED_VALUE = "speedValue";
        public static final String FLAG_SWITCH_WARM = "warmSwitch";
        public static final String FLAG_SWITCH_DEVICE = "openSwitch";
        public static final String FLAG_VOICE_VOLUME = "voiceVolume";
    }

    public static boolean install(Context con, String filePath) {
        try {
            if (TextUtils.isEmpty(filePath))
                return false;
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//增加读写权限
            }
            intent.setDataAndType(getPathUri(con, filePath), "application/vnd.android.package-archive");
            con.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(con, "安装失败，请重新下载", Toast.LENGTH_LONG).show();
            return false;
        } catch (Error error) {
            error.printStackTrace();
            Toast.makeText(con, "安装失败，请重新下载", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean checkGpsIsOpen(Context context) {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }


    public static Uri getPathUri(Context context, String filePath) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String packageName = context.getPackageName();
            uri = FileProvider.getUriForFile(context, packageName + ".fileProvider", new File(filePath));
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        return uri;
    }

    public static String getCurrentAppVersion() {
        String versionCode = "01.00";
        try {
            PackageManager manager = AppApplication.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(AppApplication.getInstance().getPackageName(), 0);
            versionCode = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            RxLogTool.e(TAG, "PackageManager.NameNotFoundException: ", e);
        }
        return versionCode;
    }

    public static boolean isNeedUpdate(Context context, String latestVersion) {
        float latest = Float.parseFloat(latestVersion);
        float cur = Float.parseFloat(getCurrentAppVersion());
        return latest > cur;
    }

    public static boolean isVersionNeedUpdate(String cureentVersion, String latestVersion) {
        float latest = Float.parseFloat(latestVersion);
        float cur = Float.parseFloat(cureentVersion);
        return latest > cur;
    }

    private static String getStyleResPackSavePath(Context context, String batchCode) {
        return context.getExternalCacheDir().getPath() + File.separator + "styleRes/" + batchCode + File.separator;
    }


    private static String getBannerPath(Context context) {
        return context.getExternalCacheDir().getPath() + File.separator + "bannerImages/";
    }

    private static String getLoadPath(Context context) {
        return context.getExternalCacheDir().getPath() + File.separator + "loadImages/";
    }

    public static boolean isAutoPlayMode(String playMode) {
        return CheckUpdateResponseDataEntity.PLAYMODE_AUTO.equals(playMode);
    }

    /**
     * 获取控制页面图片
     *
     * @return
     */
    public static boolean isStyleResDrawableTotalComplete(Context context, String batchCode) {
        if(TextUtils.isEmpty(batchCode)){
            return false;
        }
        File fileDir = RxFileTool.getFileByPath(getStyleResPackSavePath(context, batchCode));
        int index = 0;
        if (fileDir.exists() && fileDir.isDirectory() && fileDir.list().length > 0) {
            for (File file : fileDir.listFiles()) {
                Uri uri = Uri.fromFile(file);
                String fileHeadName = file.getName().split("\\.")[0];
                if (fileHeadName.equals(StyleResEntity.FILENAME_PAUSE)) {
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_PILLOW)) {
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_ROATION_POS_HIGH_GIF)) {
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_ROATION_POS_MID_GIF)) {
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_ROATION_POS_LOW_GIF)) {
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_ROATION_REV_HIGH_GIF)) {
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_ROATION_REV_MID_GIF)) {
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_ROATION_REV_LOW_GIF)) {
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_STOP)) {
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_WARMPILLOW)) {
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_BACKGROUND)) {
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_PRODUCT_LOGO)) {
                    index++;
                } else {
                    RxLogTool.e(TAG, "other file condition,uri: " + uri);
                }
            }
        }
        return index == 12;
    }

    /**
     * 获取控制页面图片
     *
     * @return
     */
    public static StyleResEntity getStyleResDrawableEntity(Context context, String batchCode) {
        StyleResEntity styleResEntity = new StyleResEntity();
        File fileDir = RxFileTool.getFileByPath(getStyleResPackSavePath(context, batchCode));
        int index = 0;
        if (fileDir.exists() && fileDir.isDirectory() && fileDir.list().length > 0) {
            for (File file : fileDir.listFiles()) {
                Uri uri = Uri.fromFile(file);
                String fileHeadName = file.getName().split("\\.")[0];
                if (fileHeadName.equals(StyleResEntity.FILENAME_PAUSE)) {
                    styleResEntity.setPauseUri(uri);
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_PILLOW)) {
                    styleResEntity.setPillowUri(uri);
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_ROATION_POS_HIGH_GIF)) {
                    styleResEntity.setRoationPosHighNormalUri(uri);
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_ROATION_POS_MID_GIF)) {
                    styleResEntity.setRoationPosMidNormalUri(uri);
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_ROATION_POS_LOW_GIF)) {
                    styleResEntity.setRoationPosLowNormalUri(uri);
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_ROATION_REV_HIGH_GIF)) {
                    styleResEntity.setRoationRevHighNormalUri(uri);
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_ROATION_REV_MID_GIF)) {
                    styleResEntity.setRoationRevMidNormalUri(uri);
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_ROATION_REV_LOW_GIF)) {
                    styleResEntity.setRoationRevLowNormalUri(uri);
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_STOP)) {
                    styleResEntity.setStopUri(uri);
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_WARMPILLOW)) {
                    styleResEntity.setWarmpillowUri(uri);
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_BACKGROUND)) {
                    styleResEntity.setBackgroundUri(uri);
                    index++;
                } else if (fileHeadName.equals(StyleResEntity.FILENAME_PRODUCT_LOGO)) {
                    styleResEntity.setLogoUri(uri);
                    index++;
                } else {
                    RxLogTool.e(TAG, "other file condition,uri: " + uri);
                }
            }
        }
        return styleResEntity;
    }

    /**
     * 获取轮播图片是否存在
     *
     * @return
     */
    public static ArrayList<Object> getBannerUnZipFilesOld(Context context) {
        ArrayList<Object> arrayList = new ArrayList<>();
        File fileDir = RxFileTool.getFileByPath(getBannerPath(context));
        if (fileDir.exists() && fileDir.isDirectory() && fileDir.list().length > 0) {
            for (File file : fileDir.listFiles()) {
                if (!file.getName().endsWith(".zip")) {
                    arrayList.add(Uri.fromFile(file));
                }
            }
        }
        if (arrayList.size() == 0) {
            arrayList.add(R.mipmap.banner1);
        }
        return arrayList;
    }


    public static boolean isBannerUnZipFilesNormal(Context context) {
        boolean isNormal = false;
        File fileDir = RxFileTool.getFileByPath(getBannerPath(context));
        String jsonContent = "";
        if (fileDir.exists() && fileDir.isDirectory() && fileDir.list().length > 0) {
            for (File file : fileDir.listFiles()) {
                if (file.getName().equals("banner.json")) {
                    isNormal = true;
                }
            }
        }
        return isNormal;
    }

    /**
     * 获取轮播图片是否存在
     *
     * @return
     */
    public static ArrayList<LocalBannerInfo> getBannerUnZipFiles(Context context) {
        ArrayList<LocalBannerInfo> localBannerInfos = new ArrayList<>();
        File fileDir = RxFileTool.getFileByPath(getBannerPath(context));
        String jsonContent = "";
        if (fileDir.exists() && fileDir.isDirectory() && fileDir.list().length > 0) {
            for (File file : fileDir.listFiles()) {
                if (file.getName().equals("banner.json")) {
                    jsonContent = RxFileTool.readFile2String(file, "utf8");
                }
            }
        }
        if (fileDir.exists() && fileDir.isDirectory() && fileDir.list().length > 0) {
            for (File file : fileDir.listFiles()) {
                if ((file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) && file.getName().startsWith("banner")) {
                    String fileHeadName = file.getName().split("\\.")[0];
                    LocalBannerInfo localBannerInfo = new LocalBannerInfo(fileHeadName, Uri.fromFile(file), getBannerPicUrl(jsonContent, fileHeadName));
                    localBannerInfos.add(localBannerInfo);
                }
            }
        }
        if (localBannerInfos.size() == 0) {
            AssetManager manager = context.getAssets();
            try {
                InputStream inputStream = manager.open("banner.json");
                InputStreamReader isr = new InputStreamReader(inputStream,
                        "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String length;
                while ((length = br.readLine()) != null) {
                    sb.append(length + "\n");
                }
                //关流
                br.close();
                isr.close();
                inputStream.close();

                jsonContent = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            localBannerInfos.add(new LocalBannerInfo("banner1", R.mipmap.banner1, getBannerPicUrl(jsonContent, "banner1")));
            localBannerInfos.add(new LocalBannerInfo("banner2", R.mipmap.banner2, getBannerPicUrl(jsonContent, "banner2")));
            localBannerInfos.add(new LocalBannerInfo("banner3", R.mipmap.banner3, getBannerPicUrl(jsonContent, "banner3")));
            localBannerInfos.add(new LocalBannerInfo("banner4", R.mipmap.banner4, getBannerPicUrl(jsonContent, "banner4")));
            localBannerInfos.add(new LocalBannerInfo("banner5", R.mipmap.banner5, getBannerPicUrl(jsonContent, "banner5")));
            localBannerInfos.add(new LocalBannerInfo("banner6", R.mipmap.banner6, getBannerPicUrl(jsonContent, "banner6")));
        }
        return localBannerInfos;
    }

    private static String getBannerPicUrl(String jsonContent, String fileHeadName) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONObject filenameJson = jsonObject.getJSONObject(fileHeadName);
            result = filenameJson.optString("url");
        } catch (JSONException e) {
            RxLogTool.e(TAG, "JSONException is :", e);
        }
        return result;
    }

    /**
     * 获取轮播图片是否存在
     *
     * @return
     */
    public static ArrayList<Object> getLoadImage(Context context) {
        ArrayList<Object> arrayList = new ArrayList<>();
        File fileDir = RxFileTool.getFileByPath(getLoadPath(context));
        if (fileDir.exists() && fileDir.isDirectory() && fileDir.list().length > 0) {
            for (File file : fileDir.listFiles()) {
                if (!file.getName().endsWith(".zip")) {
                    arrayList.add(Uri.fromFile(file));
                }
            }
        }
        if (arrayList.size() == 0) {
            arrayList.add(R.mipmap.mainbackground);
        }
        return arrayList;
    }

    public static void downImageLoadingFiles(Context context, final CheckUpdateResponseDataEntity checkUpdateResponseDataEntity, final DeviceListViewModel deviceListViewModel) {
        final String destFileDir = getLoadPath(context);
        final String destFileName = FILENAME_ZIP_LOAD + ".zip";
        DownLoadManager.getInstance().load(checkUpdateResponseDataEntity.getPackSavepath(), new ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                //ToastUtils.showShort("文件下载完成！");
                boolean isUnzip = RxZipTool.unzipFile(destFileDir + destFileName, destFileDir);
                RxLogTool.d(TAG, "unzip loadImage: " + (destFileDir + destFileName) + " result is " + isUnzip);
                if (isUnzip) {
                    deviceListViewModel.setLoadingVersion(checkUpdateResponseDataEntity.getNewestVerno());
                } else {
                    RxLogTool.e(TAG, "Loading资源文件解压失败");
                }
            }

            @Override
            public void progress(final long progress, final long total) {
            }

            @Override
            public void onError(Throwable e) {
                RxLogTool.e(TAG, "DeviceListView,loading image download failed :", e);
                //ToastUtils.showShort("loading文件下载失败！");
            }
        });
    }

    public static void downImageLoadingFiles(Context context, final CheckUpdateResponseDataEntity checkUpdateResponseDataEntity, final LoadingViewModel loadingViewModel) {
        final String destFileDir = getLoadPath(context);
        final String destFileName = FILENAME_ZIP_LOAD + ".zip";
        DownLoadManager.getInstance().load(checkUpdateResponseDataEntity.getPackSavepath(), new ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                //ToastUtils.showShort("文件下载完成！");
                boolean isUnzip = RxZipTool.unzipFile(destFileDir + destFileName, destFileDir);
                RxLogTool.d(TAG, "unzip loadImage: " + (destFileDir + destFileName) + " result is " + isUnzip);
                if (isUnzip) {
                    loadingViewModel.setLoadingVersion(checkUpdateResponseDataEntity.getNewestVerno());
                } else {
                    RxLogTool.e(TAG, "Loading资源文件解压失败");
                }
//                builder.build().dismiss();
                loadingViewModel.setLoadNewst(true);
            }

            @Override
            public void progress(final long progress, final long total) {
            }

            @Override
            public void onError(Throwable e) {
                loadingViewModel.setLoadNewst(true);
                RxLogTool.e(TAG, "LoadingActivity ,loading image download failed :", e);
//                ToastUtils.showShort("loading文件下载失败！");
            }
        });
    }

    public static boolean isNetCanUse(Context context, boolean isNeedAlert) {
        if (NetworkUtil.isNetworkAvailable(context)) {
            return true;
        } else {
            if (isNeedAlert) {
                ToastUtils.showLong(context.getString(R.string.toast_title_net_error));
            }
            return false;
        }
    }

    public static void downImageBannerFiles(Context context, final CheckUpdateResponseDataEntity checkUpdateResponseDataEntity, final DeviceListViewModel deviceListViewModel) {
        final String destFileDir = getBannerPath(context);
        final String destFileName = FILENAME_ZIP_BANNER + ".zip";
        DownLoadManager.getInstance().load(checkUpdateResponseDataEntity.getPackSavepath(), new ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                //ToastUtils.showShort("文件下载完成！");
                boolean isUnzip = RxZipTool.unzipFile(destFileDir + destFileName, destFileDir);
                RxLogTool.d(TAG, "unzip banner: " + (destFileDir + destFileName) + " result is " + isUnzip);
                if (isUnzip) {
                    deviceListViewModel.setBannerVersion(checkUpdateResponseDataEntity.getNewestVerno());
                    deviceListViewModel.saveBannerPlayIndex(0);
                } else {
                    RxLogTool.e(TAG, "Banner资源文件解压失败");
                }

            }

            @Override
            public void progress(final long progress, final long total) {
            }

            @Override
            public void onError(Throwable e) {
                RxLogTool.e(TAG, "DeviceListView,banner download failed :", e);
                RxLogTool.e(TAG,"banner文件下载失败！");
            }
        });
    }

    public static void downImageBannerFiles(Context context, final CheckUpdateResponseDataEntity checkUpdateResponseDataEntity, final LoadingViewModel loadingViewModel) {
        final String destFileDir = getBannerPath(context);
        final String destFileName = FILENAME_ZIP_BANNER + ".zip";
//        if(!builder.build().isShowing()){
//            builder.build().show();
//        }
        DownLoadManager.getInstance().load(checkUpdateResponseDataEntity.getPackSavepath(), new ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                //ToastUtils.showShort("文件下载完成！");
                boolean isUnzip = RxZipTool.unzipFile(destFileDir + destFileName, destFileDir);
                RxLogTool.d(TAG, "unzip banner: " + (destFileDir + destFileName) + " result is " + isUnzip);
                if (isUnzip) {
                    loadingViewModel.setBannerVersion(checkUpdateResponseDataEntity.getNewestVerno());
                    loadingViewModel.saveBannerPlayIndex(0);
                } else {
                    RxLogTool.e(TAG, "Banner资源文件解压失败");
                }
//                builder.build().dismiss();
                loadingViewModel.setBannerNewst(true);
            }

            @Override
            public void progress(final long progress, final long total) {
            }

            @Override
            public void onError(Throwable e) {
                RxLogTool.e(TAG, "banner download failed :", e);
                ToastUtils.showShort("banner文件下载失败！");
                loadingViewModel.setBannerNewst(true);
            }
        });
    }

    public static void downProductInfoImageFiles(Context context, final ProductInfoResponseEntity productInfoResponseEntity, final DeviceListViewModel deviceListItemViewModel, final String batchCode) {
        if (context == null || productInfoResponseEntity == null || productInfoResponseEntity.getData() == null) {
            deviceListItemViewModel.jumpToControlFragment(batchCode,1);
            return;
        }
        final String destFileDir = getStyleResPackSavePath(context, batchCode);
        final String destFileName = batchCode + ".zip";
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载产品资源文件，请稍后...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DownLoadManager.getInstance().load(productInfoResponseEntity.getData().getBrandResPackSavepath(), new ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                //ToastUtils.showShort("文件下载完成！");
                boolean isUnzip = RxZipTool.unzipFile(destFileDir + destFileName, destFileDir);
                RxLogTool.d(TAG, "unzip BrandRes productinfo: " + (destFileDir + destFileName) + " result is " + isUnzip);
                if (isUnzip) {
                    DownLoadManager.getInstance().load(productInfoResponseEntity.getData().getStyleResPackSavepath(), new ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
                        @Override
                        public void onStart() {
                            super.onStart();
                        }

                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onSuccess(ResponseBody responseBody) {
                            //ToastUtils.showShort("文件下载完成！");
                            boolean isUnzip = RxZipTool.unzipFile(destFileDir + destFileName, destFileDir);
                            RxLogTool.d(TAG, "unzip productinfo: " + (destFileDir + destFileName) + " result is " + isUnzip);
                            if (isUnzip) {
                                deviceListItemViewModel.saveProductInfoToDB(productInfoResponseEntity.getData());
                            } else {
                                RxLogTool.e(TAG, "productinfo资源文件解压失败");
                            }
//                deviceListItemViewModel.jumpToControlFragment("1_1");
                            deviceListItemViewModel.jumpToControlFragment(batchCode,2);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void progress(final long progress, final long total) {
                            progressDialog.setMax((int) total);
                            int realPro=(int) (progress < total ? progress : total);
                            if(realPro>=progressDialog.getProgress()){
                                progressDialog.setProgress(realPro);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            RxLogTool.e(TAG, "download err:", e);
                            ToastUtils.showShort("产品资源文件下载失败！");
                            deviceListItemViewModel.jumpToControlFragment(batchCode,3);
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    RxLogTool.e(TAG, "BrandRes productinfo资源文件解压失败");
                    deviceListItemViewModel.jumpToControlFragment(batchCode,4);
                    progressDialog.dismiss();
                }

            }

            @Override
            public void progress(final long progress, final long total) {

            }

            @Override
            public void onError(Throwable e) {
                RxLogTool.e(TAG, "产品资源文件下载失败！,download err:", e);
                deviceListItemViewModel.jumpToControlFragment(batchCode,5);
                progressDialog.dismiss();
            }
        });

    }

    public static void downFile(final Context context, String url, final LoadingViewModel viewModel) {
        final String destFileDir = context.getExternalCacheDir().getPath();
        final String destFileName = "myzr" + ".apk";
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        DownLoadManager.getInstance().load(url, new ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                //ToastUtils.showShort("文件下载完成！");
                boolean isInstall = AppTools.install(context, destFileDir + File.separator + destFileName);
                if (viewModel != null) {
                    viewModel.setApkNewst(true);
                }
                progressDialog.dismiss();

            }

            @Override
            public void progress(final long progress, final long total) {
                progressDialog.setMax((int) total);
                int realPro=(int) (progress < total ? progress : total);
                if(realPro>=progressDialog.getProgress()){
                    progressDialog.setProgress(realPro);
                }
            }

            @Override
            public void onError(Throwable e) {
                RxLogTool.e(TAG, "File download err:", e);
                ToastUtils.showShort("文件下载失败~");
                if (viewModel != null) {
                    viewModel.setApkNewst(true);
                }
                progressDialog.dismiss();
            }
        });
    }

    public static void displayImage(Context context, Object path, final View view) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
//        eg：
        //Glide 加载图片简单用法
        if (view instanceof ImageView) {
            Glide.with(context).load(path).into((ImageView) view);
        } else {
            Glide.with(context)
                    .asBitmap()
                    .load(path)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            Drawable drawable = new BitmapDrawable(resource);
                            view.setBackground(drawable);
                        }

                    });

        }


        //Picasso 加载图片简单用法
//        Picasso.with(context).load(path).into(imageView);

        //用fresco加载图片简单用法，记得要写下面的createImageView方法
//        Uri uri = Uri.parse((String) path);
//        imageView.setImageURI(uri);
    }

}
