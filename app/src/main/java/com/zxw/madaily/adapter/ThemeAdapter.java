package com.zxw.madaily.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxw.madaily.R;
import com.zxw.madaily.entity.Theme;

import java.util.List;
import java.util.zip.Inflater;


public class ThemeAdapter extends BaseAdapter{

    private List<Theme> mThemes;
  //  private Context mContext;

    public ThemeAdapter(List<Theme> themes){

   //     this.mContext = context;
        this.mThemes = themes;
    }
    @Override
    public int getCount() {
        return mThemes == null ? 0 : mThemes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       // ViewHolder holder = null;

        if (convertView == null){
            convertView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.themes_item, parent, false);
        }

        ((TextView)convertView).setText(mThemes.get(position).getName());


        return convertView;
    }

//    static class ViewHolder{
//        TextView tv;
//    }
}
