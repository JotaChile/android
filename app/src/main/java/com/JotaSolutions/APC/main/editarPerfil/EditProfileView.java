

package com.JotaSolutions.APC.main.editarPerfil;

import com.JotaSolutions.APC.main.pickImageBase.PickImageView;

public interface EditProfileView extends PickImageView {
    void setName(String username);

    void setProfilePhoto(String photoUrl);

    String getNameText();

    void setNameError(String string);
}
