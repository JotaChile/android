package com.JotaSolutions.APC.adaptadores.holders;

import android.view.View;

import com.JotaSolutions.APC.main.base.BaseActivity;
import com.JotaSolutions.APC.ayudante.listeners.OnPostChangedListener;
import com.JotaSolutions.APC.modelo.FollowingPost;
import com.JotaSolutions.APC.modelo.Post;
import com.JotaSolutions.APC.utilidades.LogUtil;


public class FollowPostViewHolder extends PostViewHolder {


    public FollowPostViewHolder(View view, OnClickListener onClickListener, BaseActivity activity) {
        super(view, onClickListener, activity);
    }

    public FollowPostViewHolder(View view, OnClickListener onClickListener, BaseActivity activity, boolean isAuthorNeeded) {
        super(view, onClickListener, activity, isAuthorNeeded);
    }

    public void bindData(FollowingPost followingPost) {
        postManager.getSinglePostValue(followingPost.getPostId(), new OnPostChangedListener() {
            @Override
            public void onObjectChanged(Post obj) {
                bindData(obj);
            }

            @Override
            public void onError(String errorText) {
                LogUtil.logError(TAG, "bindData", new RuntimeException(errorText));
            }
        });
    }

}
