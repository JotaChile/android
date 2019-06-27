

package com.JotaSolutions.APC.main.busquedas;

import android.app.Activity;

import com.JotaSolutions.APC.main.base.BasePresenter;


class SearchPresenter extends BasePresenter<SearchView> {

    private String currentUserId;
    private Activity activity;

    SearchPresenter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public void search(String query) {

    }
}
