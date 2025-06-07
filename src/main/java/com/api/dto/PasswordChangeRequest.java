package com.api.dto;

public class PasswordChangeRequest {

    private String oldPassword;
    private String newPassword;
    private String passwordConfirm;

    public PasswordChangeRequest() {
    }

    public PasswordChangeRequest(String oldPassword, String newPassword, String passwordConfirm) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.passwordConfirm = passwordConfirm;
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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

}
