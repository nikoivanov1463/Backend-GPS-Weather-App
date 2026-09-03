package com.tracking.tracking_app.EmailSenders;

import com.tracking.tracking_app.DTOs.ChangePasswordRequestDTO;

public interface ResetPasswordInterface {
    void resetPassword(ChangePasswordRequestDTO changePasswordRequestDTO, String link);
}
