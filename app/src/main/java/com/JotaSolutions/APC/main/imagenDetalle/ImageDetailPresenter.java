
package com.JotaSolutions.APC.main.imagenDetalle;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.JotaSolutions.APC.main.base.BasePresenter;


class ImageDetailPresenter extends BasePresenter<ImageDetailView> {

    ImageDetailPresenter(Context context) {
        super(context);
    }

    public int calcMaxImageSide() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);

        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        return width > height ? width : height;
    }
}
