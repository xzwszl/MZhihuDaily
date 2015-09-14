package com.zxw.madaily;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;


public class DailyApplication extends Application{

    private RequestQueue mVolleyQueue = null;
    public static DailyApplication mInstance;
    private Resources mResource;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    private void init(){
        initUIL();
        getVolleyQueue();

        mResource = getResources();
        SharedPreferences preferences = getSharedPreferences("app", Context.MODE_PRIVATE);
        updateNightMode(preferences.getBoolean("mode", false));
    }

    public RequestQueue getVolleyQueue(){

        if (mVolleyQueue == null){

            mVolleyQueue = Volley.newRequestQueue(this);
        }
        return mVolleyQueue;
    }

    public void initUIL(){
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, "MADaily/cache/image");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 *1024 *1024))
                .memoryCacheSize(2*1024*1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheSize(100)
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
                //.writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);
    }

    private void clearMemory() {

        if (mVolleyQueue != null){
            mVolleyQueue.cancelAll(new Object());
        }

        ImageLoader.getInstance().clearMemoryCache();
    }

    @Override
    public void onLowMemory() {

        clearMemory();
        super.onLowMemory();
    }

    public Resources getAppResource() {
        return mResource;
    }

    public void updateNightMode(boolean on) {
        DisplayMetrics dm = mResource.getDisplayMetrics();
        Configuration config = mResource.getConfiguration();
        config.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        config.uiMode |= on ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO;
        mResource.updateConfiguration(config, dm);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
