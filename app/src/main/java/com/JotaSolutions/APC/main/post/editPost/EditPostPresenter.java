
package com.JotaSolutions.APC.main.post.editPost;

import android.content.Context;

import com.JotaSolutions.APC.R;
import com.JotaSolutions.APC.main.post.BaseCreatePostPresenter;
import com.JotaSolutions.APC.ayudante.PostManager;
import com.JotaSolutions.APC.ayudante.listeners.OnPostChangedListener;
import com.JotaSolutions.APC.modelo.Post;

class EditPostPresenter extends BaseCreatePostPresenter<EditPostView> {

    private Post post;

    EditPostPresenter(Context context) {
        super(context);
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    protected int getSaveFailMessage() {
        return R.string.error_fail_update_post;
    }

    @Override
    protected boolean isImageRequired() {
        return false;
    }

    private void updatePostIfChanged(Post updatedPost) {
        if (post.getLikesCount() != updatedPost.getLikesCount()) {
            post.setLikesCount(updatedPost.getLikesCount());
        }

        if (post.getCommentsCount() != updatedPost.getCommentsCount()) {
            post.setCommentsCount(updatedPost.getCommentsCount());
        }

        if (post.getWatchersCount() != updatedPost.getWatchersCount()) {
            post.setWatchersCount(updatedPost.getWatchersCount());
        }

    }

    @Override
    protected void savePost(final String title, final String description) {
        ifViewAttached(view -> {
            view.showProgress(R.string.message_saving);

            post.setTitle(title);
            post.setDescription(description);

            if (view.getImageUri() != null) {
                postManager.createOrUpdatePostWithImage(view.getImageUri(), this, post);
            } else {
                postManager.createOrUpdatePost(post);
                onPostSaved(true);
            }
        });
    }

    public void addCheckIsPostChangedListener() {
        PostManager.getInstance(context.getApplicationContext()).getPost(context, post.getId(), new OnPostChangedListener() {
            @Override
            public void onObjectChanged(Post obj) {
                if (obj == null) {
                    ifViewAttached(view -> view.showWarningDialog(R.string.error_post_was_removed, (dialog, which) -> {
                        view.openMainActivity();
                        view.finish();
                    }));
                } else {
                    updatePostIfChanged(obj);
                }
            }

            @Override
            public void onError(String errorText) {
                ifViewAttached(view -> view.showWarningDialog(errorText, (dialog, which) -> {
                    view.openMainActivity();
                    view.finish();
                }));
            }
        });
    }

    public void closeListeners() {
        postManager.closeListeners(context);
    }
}
