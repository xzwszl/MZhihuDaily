package com.zxw.madaily;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zxw.madaily.config.Urls;
import com.zxw.madaily.entity.Content;
import com.zxw.madaily.http.Utils;
import com.zxw.madaily.tool.DataUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;


public class ContentActivity extends AppCompatActivity {

    private int id;
    private Content mContent;
    private Gson gson;
    private ImageView mBackDrop;
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        init();

    }

    private void init() {

        id = getIntent().getIntExtra("id", 0);
        gson = new Gson();
        initView();

        loadMessage();
    }

    private void initView() {

        mBackDrop = (ImageView) findViewById(R.id.backdrop);
        mWebView = (WebView) findViewById(R.id.wv_content);
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//
//                mWebView.getSettings().setLoadsImagesAutomatically(true);
//            }
//        });
        WebSettings settings = mWebView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
//        settings.setLoadsImagesAutomatically(false);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void loadMessage() {

        StringRequest contentRequest = new StringRequest(
                Request.Method.GET,
                Urls.BASE_URL + Urls.NEWS + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mContent = gson.fromJson(response, Content.class);

                        if (mContent != null) {

                            if (TextUtils.isEmpty(mContent.getImage())) {

                            }
                            Utils.loadImage(mContent.getImage(), mBackDrop);

                            loadImage(mContent.getBody());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        contentRequest.setTag(this.getLocalClassName());
        DailyApplication.mInstance.getVolleyQueue().add(contentRequest);
    }

    private void loadImage(String content) {

        if (TextUtils.isEmpty(content)) return;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            List<String> imageUrls = DataUtils.filterString("<img", "src", content);

            if (imageUrls != null && imageUrls.size() > 0) {

               // HashCodeFileNameGenerator fsg = new HashCodeFileNameGenerator();



           //    sds = new ArrayList<String>();

           //     HashCodeFileNameGenerator fsg = new HashCodeFileNameGenerator();

          //      String basePath = StorageUtils.getOwnCacheDirectory(getApplicationContext(),"MADaily/cache/image").getPath();


                for (String iu : imageUrls) {

                  //  String filepath = "file://" + basePath + "/" +  fsg.generate(iu);

                 //   sds.add(filepath);
                    downloadImage(iu);//loadImage(iu, new ImageView(this));
                }
             //   content = DataUtils.replaceString(content, imageUrls, sds);

            }
        }

        mWebView.loadDataWithBaseURL("file:///android_asset/",
                "<?xml version=\"1.0\" encoding=\"utf-8\"?><html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/><link rel='stylesheet' href='news_qa.auto.css' /><script type=\"text/javascript\" src=\"new.js\"></script></head><body onLoad=\"onLoad()\">" + content + "</body></html>",
                //"<html><body><img src=\"" + Environment.getExternalStorageDirectory().toString() + "/MADaily/cache/image/-1472368281" + "\"></body></html>",
                "text/html",
                "charset=utf-8",
                null);

       //mWebView.loadUrl("javascript:");
    }

    public void downloadImage(String url){

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().loadImage(url, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                HashCodeFileNameGenerator fsg = new HashCodeFileNameGenerator();

                String basePath = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "MADaily/cache/image").getPath();

                String filepath = "file://" + basePath + "/" + fsg.generate(imageUri);

                mWebView.loadUrl("javascript:showImage('" + imageUri + "','" + filepath + "')");
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id ==android.R.id.home) {

            onBackPressed();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.clearCache(true);
        DailyApplication.mInstance.getVolleyQueue().cancelAll(this.getLocalClassName());
    }
}
