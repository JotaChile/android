

package com.JotaSolutions.APC;

import com.JotaSolutions.APC.main.interacciones.PostInteractor;

public class Application extends android.app.Application {

    public static final String TAG = Application.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationHelper.initDatabaseHelper(this);
        PostInteractor.getInstance(this).subscribeToNewPosts();
    }
}
