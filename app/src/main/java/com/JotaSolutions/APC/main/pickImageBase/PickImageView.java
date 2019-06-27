

package com.JotaSolutions.APC.main.pickImageBase;

import android.net.Uri;

import com.JotaSolutions.APC.main.base.BaseView;


public interface PickImageView extends BaseView {
    void hideLocalProgress();

    void loadImageToImageView(Uri imageUri);
}
