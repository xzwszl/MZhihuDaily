package com.zxw.madaily.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxw.madaily.DailyApplication;
import com.zxw.madaily.R;
import com.zxw.madaily.entity.Theme;

import java.util.List;

public class ThemeAdapter extends BaseAdapter{

    private List<Theme> mThemes;

    private int currentpos = 0;

  //  private Context mContext;

    public ThemeAdapter(List<Theme> themes){

   //     this.mContext = context;
        this.mThemes = themes;
    }

    public void setSelectedPos(int pos) {
        currentpos = pos;
        notifyDataSetChanged();
    }

    public int getSelectedPos() {
        return currentpos+1;
    }
    @Override
    public int getCount() {
        return mThemes == null ? 0 : mThemes.size();
    }

    @Override
    public Object getItem(int position) {
        return mThemes.get(position);
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


        int type = getItemViewType(position);

        if (type == 0) {
            ((TextView)convertView).setText("首页");
        } else {
            ((TextView)convertView).setText(mThemes.get(position-1).getName());
        }
        ((TextView)convertView).setTextColor(DailyApplication.mInstance.getAppResource().getColor(R.color.text_color));
        if (position == currentpos) {
            convertView.setBackgroundColor(DailyApplication.mInstance.getAppResource().getColor(R.color.theme_back));
        } else {
            convertView.setBackgroundColor(DailyApplication.mInstance.getAppResource().getColor(R.color.card_back));
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) return 0;
        else return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //    static class ViewHolder{
//        TextView tv;
//    }
}
