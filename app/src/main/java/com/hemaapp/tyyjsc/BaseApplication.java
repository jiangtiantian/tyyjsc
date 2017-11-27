package com.hemaapp.tyyjsc;


import android.content.Context;
import android.support.multidex.MultiDex;

import com.hemaapp.HemaConfig;
import com.hemaapp.hm_FrameWork.HemaApplication;
import com.hemaapp.tyyjsc.db.SysInfoDBHelper;
import com.hemaapp.tyyjsc.db.UserDBHelper;
import com.hemaapp.tyyjsc.model.City;
import com.hemaapp.tyyjsc.model.SysInitInfo;
import com.hemaapp.tyyjsc.model.User;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;

import xtom.frame.XtomConfig;
import xtom.frame.util.XtomLogger;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 该项目自定义Application
 */
public class BaseApplication extends HemaApplication {
    private static final String TAG = BaseApplication.class.getSimpleName();
    private SysInitInfo sysInitInfo;// 系统初始化信息
    private User user;
    private City cityInfo;
    private static BaseApplication application;

    private double lng = 0;
    private double lat = 0;

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //微信
        PlatformConfig.setWeixin("wxb8224c75f984680b", "905f42a53dbe2f5f0ed3a976b301e4de");
        //QQ
        PlatformConfig.setQQZone("1106162734", "IR3IPqeIQ45ekFFt");
    }

    public static BaseApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        application = this;
        XtomConfig.LOG = BaseConfig.DEBUG;
        HemaConfig.UMENG_ENABLE = BaseConfig.UMENG_ENABLE;
        XtomConfig.DIGITAL_CHECK = true;
        XtomConfig.DATAKEY = "DSZtGBc4WkJ0flId";//后台提供
        String iow = XtomSharedPreferencesUtil.get(this, "imageload_onlywifi");
        XtomConfig.IMAGELOAD_ONLYWIFI = "true".equals(iow);
        XtomLogger.i(TAG, "onCreate");
        initImageLoader();
        UMShareAPI.get(this);
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 初始化 imageloader
     *
     * @param
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                context);
        config.threadPriority(Thread.NORM_PRIORITY);
        config.memoryCache(new WeakMemoryCache());
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(20 * 1024 * 1024); // 20 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        config.threadPoolSize(5);// 线程池内加载的数量
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());// 全局初始化此配置
    }
    public void initImageLoader() {
        L.writeLogs(false);
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .memoryCacheExtraOptions(480, 800)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                //.memoryCache(new UsingFreqLimitedMemoryCache(1 * 1024 * 1024))
                .memoryCache(new WeakMemoryCache())
                // You can pass your own memory cache
                // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(256 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100)
                // 缓存的文件数量
                .discCache(new UnlimitedDiskCache(cacheDir))
                // 自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(
                        new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout
                // (5
                // s),
                // readTimeout
                // (30
                // s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();// 开始构建
        ImageLoader.getInstance().init(config);
    }
    @Override
    public void onLowMemory() {
        XtomLogger.i(TAG, "onLowMemory");
        ImageLoader.getInstance().clearMemoryCache();
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        XtomLogger.i(TAG, "onTerminate");
        super.onTerminate();
    }

    /**
     * @return 当前用户
     */
    public User getUser() {
        if (user == null) {
            UserDBHelper helper = new UserDBHelper(this);
            String username = XtomSharedPreferencesUtil.get(this, "username");
            if (username == null || "".equals(username)
                    || "null".equals(username))
                return null;
            else {
                if ("false".equals(XtomSharedPreferencesUtil.get(application,
                        "isAutoLogin")))
                    return null;
                else {
                    user = helper.select(username);
                    helper.close();
                }
            }
        }
        return user;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * 设置保存当前用户
     *
     * @param user 当前用户
     */
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            UserDBHelper helper = new UserDBHelper(this);
            helper.insertOrUpdate(user);
            helper.close();
        }
    }

    /**
     * 删除用户信息
     */
    public void delUser(User user) {
        if (user != null) {
            UserDBHelper helper = new UserDBHelper(this);
            helper.delUser(user);
            helper.close();
        }
        this.user = null;
    }

    /**
     * @return 系统初始化信息
     */
    public SysInitInfo getSysInitInfo() {
        if (sysInitInfo == null) {
            SysInfoDBHelper helper = new SysInfoDBHelper(this);
            sysInitInfo = helper.select();
            helper.close();
        }
        return sysInitInfo;
    }

    /**
     * 设置保存系统初始化信息
     *
     * @param sysInitInfo 系统初始化信息
     */
    public void setSysInitInfo(SysInitInfo sysInitInfo) {
        this.sysInitInfo = sysInitInfo;
        if (sysInitInfo != null) {
            SysInfoDBHelper helper = new SysInfoDBHelper(this);
            helper.insertOrUpdate(sysInitInfo);
            helper.close();
        }
    }

    public City getCityInfo() {

        return cityInfo;
    }

    public void setCityInfo(City cityInfo) {
        this.cityInfo = cityInfo;
    }
}
