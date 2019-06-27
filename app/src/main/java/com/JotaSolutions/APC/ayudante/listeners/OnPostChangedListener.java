

package com.JotaSolutions.APC.ayudante.listeners;

import com.JotaSolutions.APC.modelo.Post;

public interface OnPostChangedListener {
    public void onObjectChanged(Post obj);

    public void onError(String errorText);
}
