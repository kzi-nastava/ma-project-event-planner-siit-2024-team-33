package com.example.myapplication.data.dto.userDTO;

public class UpdatePassword {
    private String oldPassword;
    private String newPassword;

    public UpdatePassword() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
