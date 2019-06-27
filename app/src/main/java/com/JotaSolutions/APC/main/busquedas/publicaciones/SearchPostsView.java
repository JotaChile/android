

package com.JotaSolutions.APC.main.busquedas.publicaciones;

import com.JotaSolutions.APC.main.base.BaseFragmentView;
import com.JotaSolutions.APC.modelo.Post;

import java.util.List;

public interface SearchPostsView extends BaseFragmentView {
    void onSearchResultsReady(List<Post> posts);
    void showLocalProgress();
    void hideLocalProgress();
    void showEmptyListLayout();
}
