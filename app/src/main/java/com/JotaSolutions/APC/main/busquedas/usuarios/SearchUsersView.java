package com.JotaSolutions.APC.main.busquedas.usuarios;

import com.JotaSolutions.APC.main.base.BaseFragmentView;
import com.JotaSolutions.APC.modelo.Profile;

import java.util.List;


public interface SearchUsersView extends BaseFragmentView {
    void onSearchResultsReady(List<Profile> profiles);

    void showLocalProgress();

    void hideLocalProgress();

    void showEmptyListLayout();

    void updateSelectedItem();
}
