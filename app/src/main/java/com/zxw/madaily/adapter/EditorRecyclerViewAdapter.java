package com.zxw.madaily.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxw.madaily.R;
import com.zxw.madaily.entity.Editor;
import com.zxw.madaily.http.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sony on 2015/9/16.
 */
public class EditorRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Editor> editors;

    public EditorRecyclerViewAdapter(List<Editor> editors) {
        this.editors = editors;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.editor_item, parent, false);
        return new FaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        FaceViewHolder fvh =  (FaceViewHolder)holder;
        String url = editors.get(position) == null ? null : editors.get(position).getAvatar();
        if (url != null && fvh.face.getTag() == null || fvh.face.getTag().equals(url)) {
            Utils.loadImage(url, fvh.face);
        }
    }

    @Override
    public int getItemCount() {
        return editors == null ? 0 : editors.size();
    }

    public static class FaceViewHolder extends RecyclerView.ViewHolder {

        public final CircleImageView face;
        public FaceViewHolder(View itemView) {
            super(itemView);
            face = (CircleImageView) itemView.findViewById(R.id.cv_face);
        }
    }
}
