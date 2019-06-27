

package com.JotaSolutions.APC.ayudante.listeners;

import com.JotaSolutions.APC.modelo.PostListResult;

public interface OnPostListChangedListener<Post> {

    public void onListChanged(PostListResult result);

    void onCanceled(String message);
}
