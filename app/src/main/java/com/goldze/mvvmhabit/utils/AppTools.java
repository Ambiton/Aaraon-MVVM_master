package com.goldze.mvvmhabit.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.entity.StyleResEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateResponseDataEntity;
import com.goldze.mvvmhabit.entity.http.productinfo.ProductInfoResponseEntity;
import com.goldze.mvvmhabit.ui.main.DeviceListItemViewModel;
import com.goldze.mvvmhabit.ui.main.DeviceListViewModel;
import com.goldze.mvvmhabit.ui.main.LoadingViewModel;
import com.tamsiree.rxtool.RxFileTool;
import com.tamsiree.rxtool.RxImageTool;
import com.tamsiree.rxtool.RxLogTool;
import com.tamsiree.rxtool.RxZipTool;

import java.io.File;
import java.util.ArrayList;

import me.goldze.mvvmhabit.http.DownLoadManager;
import me.goldze.mvvmhabit.http.download.ProgressCallBack;
import me.goldze.mvvmhabit.utils.ToastUtils;
import okhttp3.ResponseBody;

/**
 * @author Areo
 * @description:本APP工具类
 * @date : 2020/3/8 16:49
 */
public class AppTools {
    public static final String CUREENT_SERIONUM="123456";//按摩椅的设备串号
    public static final String APPKEY="1uMqYWpHo3MoLH";
    public static final String APPSIGN="sig-result";
    public static final String KEY_REGISTER_USERINFO="registeruserinfo";
    private static final String TAG="AppTools";
    private static final String FILENAME_ZIP_BANNER="bannerImages";
    private static final String FILENAME_ZIP_LOAD="loadImages";


    public static class UserActionFlagAndValue{
        public static final String FLAG_ROATION_MODE="roationMode";
        public static final String FLAG_SPEED_VALUE="speedValue";
        public static final String FLAG_SWITCH_WARM="warmSwitch";
        public static final String FLAG_SWITCH_DEVICE="openSwitch";
        public static final String FLAG_VOICE_VOLUME="voiceVolume";
    }
    public static boolean install(Context con, String filePath) {
        try {
            if(TextUtils.isEmpty(filePath))
                return false;
            File file = new File(filePath);
            if(!file.exists()){
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

    private static String getCurrentAppVersion(Context context){
        String versionCode="01.00";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionCode;
    }

    public static boolean isNeedUpdate(Context context,String latestVersion){
        float latest=Float.parseFloat(latestVersion);
        float cur=Float.parseFloat(getCurrentAppVersion(context));
        return latest>cur;
    }

    public static boolean isVersionNeedUpdate(String cureentVersion,String latestVersion){
        float latest=Float.parseFloat(latestVersion);
        float cur=Float.parseFloat(cureentVersion);
        return latest>cur;
    }

    private static String getStyleResPackSavePath(Context context,String batchCode){
        return context.getExternalCacheDir().getPath()+File.separator+"styleRes/"+batchCode+File.separator;
    }

    private static String getBannerPath(Context context){
        return context.getExternalCacheDir().getPath()+File.separator+"bannerImages/";
    }
    private static String getLoadPath(Context context){
        return context.getExternalCacheDir().getPath()+File.separator+"loadImages/";
    }

    public static boolean isAutoPlayMode(String playMode){
        return CheckUpdateResponseDataEntity.PLAYMODE_AUTO.equals(playMode);
    }

    /**
     * 获取控制页面图片
     * @return
     */
    public static StyleResEntity getStyleResDrawableEntity(Context context, String batchCode) {
        StyleResEntity styleResEntity = new StyleResEntity();
        File fileDir = RxFileTool.getFileByPath(getStyleResPackSavePath(context, batchCode));
        if (fileDir.exists() && fileDir.isDirectory() && fileDir.list().length > 0) {
            for (File file : fileDir.listFiles()) {
                Uri uri = Uri.fromFile(file);
                if (file.getName().equals(StyleResEntity.FILENAME_PAUSE)) {
                    styleResEntity.setPauseUri(uri);
                } else if (file.getName().equals(StyleResEntity.FILENAME_PILLOW)) {
                    styleResEntity.setPillowUri(uri);
                } else if (file.getName().equals(StyleResEntity.FILENAME_ROATION_POS_HIGH_GIF)) {
                    styleResEntity.setRoationPosHighNormalUri(uri);
                } else if (file.getName().equals(StyleResEntity.FILENAME_ROATION_POS_MID_GIF)) {
                    styleResEntity.setRoationPosMidNormalUri(uri);
                } else if (file.getName().equals(StyleResEntity.FILENAME_ROATION_POS_LOW_GIF)) {
                    styleResEntity.setRoationPosLowNormalUri(uri);
                } else if (file.getName().equals(StyleResEntity.FILENAME_ROATION_REV_HIGH_GIF)) {
                    styleResEntity.setRoationRevHighNormalUri(uri);
                } else if (file.getName().equals(StyleResEntity.FILENAME_ROATION_REV_MID_GIF)) {
                    styleResEntity.setRoationRevMidNormalUri(uri);
                } else if (file.getName().equals(StyleResEntity.FILENAME_ROATION_REV_LOW_GIF)) {
                    styleResEntity.setRoationRevLowNormalUri(uri);
                } else if (file.getName().equals(StyleResEntity.FILENAME_STOP)) {
                    styleResEntity.setStopUri(uri);
                } else if (file.getName().equals(StyleResEntity.FILENAME_WARMPILLOW)) {
                    styleResEntity.setWarmpillowUri(uri);
                } else {
                    RxLogTool.e(TAG, "other file condition,uri: " + uri);
                }
            }
        }
        return styleResEntity;
    }

    /**
     * 获取轮播图片是否存在
     * @return
     */
    public static ArrayList<Object> getBannerUnZipFiles(Context context){
        ArrayList<Object>arrayList=new ArrayList<>();
        File fileDir= RxFileTool.getFileByPath(getBannerPath(context));
        if(fileDir.exists()&&fileDir.isDirectory()&&fileDir.list().length>0){
            for(File file:fileDir.listFiles()){
                if(!file.getName().endsWith(".zip")){
                    arrayList.add(Uri.fromFile(file));
                }
            }
        }
        if(arrayList.size()==0){
            arrayList.add(R.mipmap.banner1);
        }
        return arrayList;
    }

    /**
     * 获取轮播图片是否存在
     * @return
     */
    public static ArrayList<Object> getLoadImage(Context context){
        ArrayList<Object>arrayList=new ArrayList<>();
        File fileDir= RxFileTool.getFileByPath(getLoadPath(context));
        if(fileDir.exists()&&fileDir.isDirectory()&&fileDir.list().length>0){
            for(File file:fileDir.listFiles()){
                if(!file.getName().endsWith(".zip")){
                    arrayList.add(Uri.fromFile(file));
                }
            }
        }
        if(arrayList.size()==0){
            arrayList.add(R.mipmap.mainbackground);
        }
        return arrayList;
    }

    public static void downImageLoadingFiles(Context context,final CheckUpdateResponseDataEntity checkUpdateResponseDataEntity, final LoadingViewModel loadingViewModel) {
        final String destFileDir = getLoadPath(context);
        final String destFileName = FILENAME_ZIP_LOAD + ".zip";
//        if(!builder.build().isShowing()){
//            builder.build().show();
//        }
        DownLoadManager.getInstance().load(checkUpdateResponseDataEntity.getPackSavepath(), new ProgressCallBack<ResponseBody>(destFileDir, destFileName){
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
                boolean isUnzip=RxZipTool.unzipFile(destFileDir+destFileName,destFileDir);
                RxLogTool.d(TAG,"unzip loadImage: "+(destFileDir+destFileName)+" result is "+isUnzip);
                if(isUnzip){
                    loadingViewModel.setLoadingVersion(checkUpdateResponseDataEntity.getNewestVerno());
                }else{
                    RxLogTool.e(TAG,"Loading资源文件解压失败");
                }
//                builder.build().dismiss();
                loadingViewModel.setLoadNewst(true);
            }

            @Override
            public void progress(final long progress, final long total) {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                ToastUtils.showShort("loading文件下载失败！");
            }
        });
    }

    public static void downImageBannerFiles(Context context, final CheckUpdateResponseDataEntity checkUpdateResponseDataEntity,  final LoadingViewModel loadingViewModel) {
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
                boolean isUnzip=RxZipTool.unzipFile(destFileDir+destFileName,destFileDir);
                RxLogTool.d(TAG,"unzip banner: "+(destFileDir+destFileName)+" result is "+isUnzip);
                if(isUnzip){
                    loadingViewModel.setBannerVersion(checkUpdateResponseDataEntity.getNewestVerno());
                }else{
                    RxLogTool.e(TAG,"Banner资源文件解压失败");
                }
//                builder.build().dismiss();
                loadingViewModel.setBannerNewst(true);
            }

            @Override
            public void progress(final long progress, final long total) {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                ToastUtils.showShort("banner文件下载失败！");
            }
        });
    }

    public static void downProductInfoImageFiles(Context context, final ProductInfoResponseEntity productInfoResponseEntity, final DeviceListViewModel deviceListItemViewModel,final String batchCode) {
        if (context == null || productInfoResponseEntity == null || productInfoResponseEntity.getData() == null) {
            deviceListItemViewModel.jumpToControlFragment(batchCode);
            return;
        }
        final String destFileDir = getStyleResPackSavePath(context, batchCode);
        final String destFileName = batchCode + ".zip";
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载产品资源文件，请稍后...");
        progressDialog.setCancelable(false);
        progressDialog.show();
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
                boolean isUnzip=RxZipTool.unzipFile(destFileDir+destFileName,destFileDir);
                RxLogTool.d(TAG,"unzip productinfo: "+(destFileDir+destFileName)+" result is "+isUnzip);
                if(isUnzip){
                    deviceListItemViewModel.saveProductInfoToDB(productInfoResponseEntity.getData());
                }else{
                    RxLogTool.e(TAG,"productinfo资源文件解压失败");
                }
//                deviceListItemViewModel.jumpToControlFragment("1_1");
                deviceListItemViewModel.jumpToControlFragment(batchCode);
                progressDialog.dismiss();
            }

            @Override
            public void progress(final long progress, final long total) {
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int)(progress<total?progress:total));
            }

            @Override
            public void onError(Throwable e) {
                RxLogTool.e(TAG, "download err:", e);
                ToastUtils.showShort("产品资源文件下载失败！");
                deviceListItemViewModel.jumpToControlFragment(batchCode);
                progressDialog.dismiss();
            }
        });
    }
    public static void downFile(final Context context, String url,final LoadingViewModel viewModel) {
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
                boolean isInstall=AppTools.install(context,destFileDir+File.separator+destFileName);
                if(viewModel!=null){
                    viewModel.setApkNewst(true);
                }
                progressDialog.dismiss();

            }

            @Override
            public void progress(final long progress, final long total) {
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int)(progress<total?progress:total));
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                ToastUtils.showShort("文件下载失败！");
                progressDialog.dismiss();
            }
        });
    }

    public static void displayImage(Context context, Object path, ImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
//        eg：

        //Glide 加载图片简单用法
        Glide.with(context).load(path).into(imageView);

        //Picasso 加载图片简单用法
//        Picasso.with(context).load(path).into(imageView);

        //用fresco加载图片简单用法，记得要写下面的createImageView方法
//        Uri uri = Uri.parse((String) path);
//        imageView.setImageURI(uri);
    }
}
