package com.zxw.madaily.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxw.madaily.R;
import com.zxw.madaily.entity.Comment;
import com.zxw.madaily.http.Utils;
import com.zxw.madaily.tool.DataUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sony on 2015/8/2.
 */
public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Comment> mComments;

    public CommentRecyclerViewAdapter(List<Comment> mComments) {
        this.mComments = mComments;
    }

    public void setmComments(List<Comment> mComments) {
        this.mComments = mComments;
    }

    public List<Comment> getmComments() {
        return mComments;
    }

    public void addComment(Comment comment) {

        if (mComments == null) {
            mComments = new ArrayList<>();
        }

        mComments.add(comment);
    }

    public void addComments(List<Comment> comments) {

        if (mComments == null) {

            mComments = new ArrayList<>();
        }

        mComments.addAll(comments);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0 || viewType == 2) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_type, parent, false);
            return new CommentTypeViewHolder(view);

        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_content, parent, false);
            return new CommentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int type = getItemViewType(position);

//        if (type == 0) {
//
//            CommentTypeViewHolder typeViewHolder = (CommentTypeViewHolder) holder;
//
//            int count = getLongCommentsSize();
//
//            typeViewHolder.type.setText("长评论 : " + count + "条");
//        } else if (type == 2) {
//
//            CommentTypeViewHolder typeViewHolder = (CommentTypeViewHolder) holder;
//
//            int count = getShortCommentsSize();
//
//            typeViewHolder.type.setText("短评论 : " + count + "条");
//
//        } else {

            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;

            Comment comment = mComments.get(position);
            Utils.loadImage(comment.getAvatar(), commentViewHolder.face);
            commentViewHolder.auther.setText(comment.getAuthor());
            commentViewHolder.like.setText(comment.getLikes());
            commentViewHolder.conntent.setText(comment.getContent());
            commentViewHolder.time.setText(DataUtils.getDate(comment.getTime()));

//        }
    }


    @Override
    public int getItemCount() {

       return mComments == null ? 0 : mComments.size();
    }

    @Override
    public int getItemViewType(int position) {

//        if (position == 0) return 0;

        return 1;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView face;
        private TextView auther,like,time,conntent;

        public CommentViewHolder(View itemView) {

            super(itemView);
            face = (CircleImageView) itemView.findViewById(R.id.cv_face);
            auther = (TextView) itemView.findViewById(R.id.tv_author);
            like = (TextView) itemView.findViewById(R.id.tv_like);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            conntent = (TextView) itemView.findViewById(R.id.tv_content);

        }
    }

   public static class CommentTypeViewHolder extends RecyclerView.ViewHolder {

        private TextView type;

        public CommentTypeViewHolder(View itemView) {

            super(itemView);

            type = (TextView) itemView;

        }
    }
}
