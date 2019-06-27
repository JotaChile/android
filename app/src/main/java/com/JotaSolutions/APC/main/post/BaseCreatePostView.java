

package com.JotaSolutions.APC.main.post;

import android.net.Uri;

import com.JotaSolutions.APC.main.pickImageBase.PickImageView;

public interface BaseCreatePostView extends PickImageView {
    void setDescriptionError(String error);

    void setTitleError(String error);

    String getTitleText();

    String getDescriptionText();

    void requestImageViewFocus();

    void onPostSavedSuccess();

    Uri getImageUri();
}

