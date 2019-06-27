
package com.JotaSolutions.APC.servicios;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.JotaSolutions.APC.main.interacciones.ProfileInteractor;
import com.JotaSolutions.APC.utilidades.LogUtil;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LogUtil.logDebug(TAG, "Refreshed token: " + refreshedToken);


        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        ProfileInteractor.getInstance(getApplicationContext()).updateRegistrationToken(token);
    }
}
