package com.zxw.madaily;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.zxw.madaily.http.Utils;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageActivity extends AppCompatActivity {

    private String url;
    private String title;
    private ImageView mImage;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        init();
    }

    private void initView() {

        mImage = (ImageView) findViewById(R.id.iv_image);
        mAttacher = new PhotoViewAttacher(mImage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
    }

    private void init() {
        initView();

        url = getIntent().getStringExtra("url");

        if (url != null) {
            Utils.loadImage(url, mImage);
        }
        mAttacher.update();

        title = getIntent().getStringExtra("title");

        if (title != null) getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {

            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
