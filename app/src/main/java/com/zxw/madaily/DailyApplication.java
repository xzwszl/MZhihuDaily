package com.zxw.madaily;

import android.app.Application;

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

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    private void init(){
        initUIL();
        getVolleyQueue();
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

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
