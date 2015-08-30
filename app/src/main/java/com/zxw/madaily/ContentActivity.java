package com.zxw.madaily;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zxw.madaily.config.Urls;
import com.zxw.madaily.db.TableHandler;
import com.zxw.madaily.entity.Content;
import com.zxw.madaily.entity.Extra;
import com.zxw.madaily.fragment.SettingFragment;
import com.zxw.madaily.http.Utils;
import com.zxw.madaily.tool.DataUtils;

import java.util.List;


public class ContentActivity extends AppCompatActivity implements View.OnClickListener {

    private int shortComments;
    private int longComments;
    private int id;
    private Content mContent;
    private Extra mExtra;
    private Gson gson;
    private ImageView mBackDrop;
    private WebView mWebView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView mCopyRight;
    private TextView mComments, mPopularity;
    private FloatingActionButton mShare;

    private TableHandler mTableHandler;

    //private List<String> imageUrls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        init();

    }

    private void init() {

        id = getIntent().getIntExtra("id", 0);
        gson = new Gson();
        mTableHandler = new TableHandler(getApplicationContext(), TableHandler.TABLE_CONTENT);
        initView();

        loadMessage();
        loadComments();
    }

    private void initView() {
        mShare = (FloatingActionButton) findViewById(R.id.fab_share);
        mShare.setOnClickListener(this);

        mComments = (TextView) findViewById(R.id.tv_comments);
        mComments.setOnClickListener(this);
        mPopularity = (TextView) findViewById(R.id.tv_popularity);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        mCopyRight = (TextView) findViewById(R.id.tv_top_right);

        mBackDrop = (ImageView) findViewById(R.id.backdrop);
        mWebView = (WebView) findViewById(R.id.wv_content);
        WebSettings settings = mWebView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(false);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.addJavascriptInterface(new JsInteration(), "control");
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.getSettings().setLoadsImagesAutomatically(true);
                mWebView.loadUrl("javascript:onLoaded()");
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient());

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WebView.setWebContentsDebuggingEnabled(true);
//        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void loadMessage() {

        String content = mTableHandler.findContentById(String.valueOf(id));

        if (!TextUtils.isEmpty(content)) {
            try {
                parseResponse(content);
                return;
            } catch (JsonSyntaxException e) {

            }
        }

        StringRequest contentRequest = new StringRequest(
                Request.Method.GET,
                Urls.BASE_URL + Urls.NEWS + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       try {
                           parseResponse(response);
                           mTableHandler.insertContentById(String.valueOf(id), response);

                       } catch (JsonSyntaxException e) {

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

    private void parseResponse(String response) {

        mContent = gson.fromJson(response, Content.class);

        if (mContent != null) {

            if (!TextUtils.isEmpty(mContent.getImage())) {
                Utils.loadImage(mContent.getImage(), mBackDrop);
            }

            if (!TextUtils.isEmpty(mContent.getTitle())) {
                mCollapsingToolbarLayout.setTitle(mContent.getTitle());
            }

            if (!TextUtils.isEmpty(mContent.getImage_source())) {
                // mCopyRight.setText(mContent.getImage_source());
            }

      //      if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(SettingFragment.NO_PICTURE, false)) {
                loadImage(mContent.getBody());
      //      }

        }
    }
    private void loadComments() {

        StringRequest commentRequest = new StringRequest(
                Request.Method.GET,
                Urls.BASE_URL + Urls.STORY_EXTRA + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            mExtra = gson.fromJson(response, Extra.class);

                            if (mExtra != null) {
                                mComments.setText(String.valueOf(mExtra.getComments()));
                                mPopularity.setText(String.valueOf(mExtra.getPopularity()));
                            }
                        } catch (JsonSyntaxException e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        commentRequest.setTag(this.getLocalClassName());
        DailyApplication.mInstance.getVolleyQueue().add(commentRequest);
    }

    private void loadImage(String content) {

        if (TextUtils.isEmpty(content)) return;
            content = content.replace("src","src=\"default_pic_content_image_download_dark.png\" img-src");

        String load = "";
        if (Utils.isWifiAvailable(getApplicationContext()) || !PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(SettingFragment.NO_PICTURE, false)) {
           // load = "onLoad=\"onLoaded()\"";
        }
        mWebView.loadDataWithBaseURL("file:///android_asset/",
                "<?xml version=\"1.0\" encoding=\"utf-8\"?><html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/><link rel='stylesheet' href='news_qa.auto.css' /><script type=\"text/javascript\" src=\"new.js\"></script></head><body " + load + ">" + content + "</body></html>",
                //"<html><body><img src=\"" + Environment.getExternalStorageDirectory().toString() + "/MADaily/cache/image/-1472368281" + "\"></body></html>",
                "text/html",
                "charset=utf-8",
                null);
    }

    public void downloadImage(String url){

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .cacheInMemory(false)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().loadImage(url, new ImageSize(0,0), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(final String imageUri, View view, Bitmap loadedImage) {

                HashCodeFileNameGenerator fsg = new HashCodeFileNameGenerator();

                String basePath = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "MADaily/cache/image").getPath();

                final String filepath = "file://" + basePath + "/" + fsg.generate(imageUri);

//                mWebView.post(new Runnable() {
//                    @Override
//                    public void run() {
                        mWebView.loadUrl("javascript:showImage('" + imageUri + "','" + filepath + "')");
//                       // mWebView.requestLayout();
//                    }
//                });

                if (loadedImage != null &&!loadedImage.isRecycled()) {
                    loadedImage.recycle();
                }
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
        mWebView.removeAllViews();
        mWebView.removeJavascriptInterface("control");
        mWebView.destroy();
        DailyApplication.mInstance.getVolleyQueue().cancelAll(this.getLocalClassName());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_comments:
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra("id",id);

                if (mExtra != null) {
                    intent.putExtra("long", mExtra.getLong_comments());
                    intent.putExtra("short", mExtra.getShort_comments());
                }
                startActivity(intent);
                break;

            case R.id.fab_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mContent.getShare_url());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }
    }

    public class JsInteration {

        @JavascriptInterface
        public void displayImage(String url) {

            Intent intent = new Intent(ContentActivity.this, ImageActivity.class);
            intent.putExtra("url",url);
            intent.putExtra("title", mContent != null ? mContent.getTitle() : null);
            startActivity(intent);
        }

        @JavascriptInterface
        public void loadImage(final String url) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    downloadImage(url);
                }
                });
        }
    }
}
