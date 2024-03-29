
package com.JotaSolutions.APC.main.post.editPost;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.JotaSolutions.APC.R;
import com.JotaSolutions.APC.main.main.MainActivity;
import com.JotaSolutions.APC.main.post.BaseCreatePostActivity;
import com.JotaSolutions.APC.modelo.Post;
import com.JotaSolutions.APC.utilidades.GlideApp;
import com.JotaSolutions.APC.utilidades.ImageUtil;

public class EditPostActivity extends BaseCreatePostActivity<EditPostView, EditPostPresenter> implements EditPostView {
    private static final String TAG = EditPostActivity.class.getSimpleName();
    public static final String POST_EXTRA_KEY = "EditPostActivity.POST_EXTRA_KEY";
    public static final int EDIT_POST_REQUEST = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Post post = (Post) getIntent().getSerializableExtra(POST_EXTRA_KEY);
        presenter.setPost(post);
        showProgress();
        fillUIFields(post);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.addCheckIsPostChangedListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.closeListeners();
    }

    @NonNull
    @Override
    public EditPostPresenter createPresenter() {
        if (presenter == null) {
            return new EditPostPresenter(this);
        }
        return presenter;
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(EditPostActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void fillUIFields(Post post) {
        titleEditText.setText(post.getTitle());
        descriptionEditText.setText(post.getDescription());
        loadPostDetailsImage(post.getImagePath());
        hideProgress();
    }

    private void loadPostDetailsImage(String imagePath) {
        ImageUtil.loadImageCenterCrop(GlideApp.with(this), imagePath, imageView, new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editar_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                presenter.doSavePost(imageUri);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
