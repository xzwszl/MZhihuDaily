package com.zxw.madaily;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zxw.madaily.config.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity{

    private ImageView mLoading;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init(){
        initView();

        loadingMessage();
    }

    private void initView(){

        mLoading = (ImageView) findViewById(R.id.iv_loading);
        mText = (TextView) findViewById(R.id.tv_loading);
    }

    private void loadingMessage(){

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        JsonObjectRequest jor = new JsonObjectRequest(
                Urls.BASE_URL + Urls.LOADING_IMAGE_URL + screenHeight + "*" + screenWidth,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.has("img"))
                                loadImage(response.getString("img"));

                            if (response.has("text"))
                                mText.setText(response.getString("text"));

                        } catch ( JSONException je){

                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }});

        jor.setTag(this.getLocalClassName());
        DailyApplication.mInstance.getVolleyQueue().add(jor);

    }

    private void loadImage(String url){



        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(url, mLoading, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                //start scale animation for mLoading
                animateLoading();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    private void animateLoading() {

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.15f, 1.0f, 1.15f,
                Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);



        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);
        mLoading.startAnimation(scaleAnimation);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                View view = (View) mLoading.getParent();

                AlphaAnimation alphaAnimation =  new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation.setDuration(1000);
                alphaAnimation.setFillAfter(true);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));

                        overridePendingTransition(0, 0);

                        SplashActivity.this.finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(alphaAnimation);





             //   overridePendingTransition(0,R.anim.activity_close);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DailyApplication.mInstance.getVolleyQueue().cancelAll(this.getLocalClassName());

    }
}
