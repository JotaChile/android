

package com.JotaSolutions.APC.main.login;

import com.google.firebase.auth.AuthCredential;
import com.JotaSolutions.APC.main.base.BaseView;


public interface LoginView extends BaseView {
    void startCreateProfileActivity();

    void signInWithGoogle();

    void signInWithFacebook();

    void setProfilePhotoUrl(String url);

    void firebaseAuthWithCredentials(AuthCredential credential);
}
