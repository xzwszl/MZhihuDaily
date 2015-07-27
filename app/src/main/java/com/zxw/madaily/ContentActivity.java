package com.zxw.madaily;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
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
        WebSettings settings = mWebView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setLoadsImagesAutomatically(false);
        settings.setAllowFileAccess(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String a = new Md5FileNameGenerator().generate("http://news.at.zhihu.com/css/news_qa.auto.css?v=811bb");

        System.out.println();

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
                           // Utils.loadImage(mContent.getImage(), mBackDrop);

                            loadImage(mContent.getBody());
                           // mWebView.loadData(, "text/html", "charset=utf-8");
                           // mWebView.loadUrl("http://slashdot.org/");
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


        content = "<img src=\"http://pic4.zhimg.com/36eb1c96863b4e611ccb7e82b6fced8f_is.jpg\">";

        if (TextUtils.isEmpty(content)) return;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            List<String> imageUrls = DataUtils.filterString("<img", "src", content);

            if (imageUrls != null && imageUrls.size() > 0) {

                HashCodeFileNameGenerator fsg = new HashCodeFileNameGenerator();

                String basePath = StorageUtils.getOwnCacheDirectory(getApplicationContext(),"MADaily/cache/image").getPath();

                File file = new File("file://" + basePath + "/" + "-1472368281");

                if (file.exists())
                    System.out.println(file.exists());

                List<String> sds = new ArrayList<String>();

                for (String iu : imageUrls) {

                    sds.add("file://" + basePath + "/-1472368281");
       //             Utils.downloadImage(iu);//loadImage(iu, new ImageView(this));
                }

                content = DataUtils.replaceString(content, imageUrls, sds);

            }
        }

        mWebView.loadDataWithBaseURL("file:///android_asset/",
             //   "<?xml version=\"1.0\" encoding=\"utf-8\"?><html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/><link rel='stylesheet' href='news_qa.auto.css' /></head><body>" + content + "</body></html>",
             "<html><body>" + content + "</body></html>",
                "text/html",
                "charset=utf-8",
                null);
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
        DailyApplication.mInstance.getVolleyQueue().cancelAll(this.getLocalClassName());
    }
}
