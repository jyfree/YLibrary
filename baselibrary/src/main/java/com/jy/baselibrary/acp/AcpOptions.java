package com.jy.baselibrary.acp;

/**
 * @Author Administrator
 * @Date 2019/10/12-9:36
 * @TODO
 */
public class AcpOptions {

    private String rationalMessage;
    private String deniedMessage;
    private String deniedCloseBtn;
    private String deniedSettingBtn;
    private String rationalBtn;
    private String[] permissions;
    private boolean dialogCancelable;
    private boolean dialogCanceledOnTouchOutside;
    private boolean canShowDeniedDialog;

    private AcpOptions(Builder builder) {
        rationalMessage = builder.rationalMessage;
        deniedMessage = builder.deniedMessage;
        deniedCloseBtn = builder.deniedCloseBtn;
        deniedSettingBtn = builder.deniedSettingBtn;
        rationalBtn = builder.rationalBtn;
        permissions = builder.permissions;
        dialogCancelable = builder.dialogCancelable;
        dialogCanceledOnTouchOutside = builder.dialogCanceledOnTouchOutside;
        canShowDeniedDialog = builder.canShowDeniedDialog;
    }

    public String getRationalMessage() {
        return rationalMessage;
    }

    public String getDeniedMessage() {
        return deniedMessage;
    }

    public String getDeniedCloseBtn() {
        return deniedCloseBtn;
    }

    public String getDeniedSettingBtn() {
        return deniedSettingBtn;
    }

    public String getRationalBtn() {
        return rationalBtn;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public boolean isDialogCancelable() {
        return dialogCancelable;
    }

    public boolean isDialogCanceledOnTouchOutside() {
        return dialogCanceledOnTouchOutside;
    }

    public boolean isCanShowDeniedDialog() {
        return canShowDeniedDialog;
    }

    public static Builder beginBuilder() {
        return new Builder();
    }

    public static class Builder {

        private static final String DEF_RATIONAL_MESSAGE = "此功能需要您授权，否则将不能正常使用";
        private static final String DEF_DENIED_MESSAGE = "您拒绝权限申请，此功能将不能正常使用，您可以去设置页面重新授权";
        private static final String DEF_DENIED_CLOSE_BTN_TEXT = "关闭";
        private static final String DEF_DENIED_SETTINGS_BTN_TEXT = "设置权限";
        private static final String DEF_RATIONAL_BTN_TEXT = "我知道了";

        private String rationalMessage = DEF_RATIONAL_MESSAGE;
        private String deniedMessage = DEF_DENIED_MESSAGE;
        private String deniedCloseBtn = DEF_DENIED_CLOSE_BTN_TEXT;
        private String deniedSettingBtn = DEF_DENIED_SETTINGS_BTN_TEXT;
        private String rationalBtn = DEF_RATIONAL_BTN_TEXT;
        private String[] permissions;
        private boolean dialogCancelable = false;
        private boolean dialogCanceledOnTouchOutside = false;
        private boolean canShowDeniedDialog = true;

        /**
         * 申请权限理由框提示语
         *
         * @param rationalMessage
         * @return
         */
        public Builder setRationalMessage(String rationalMessage) {
            this.rationalMessage = rationalMessage;
            return this;
        }

        /**
         * 申请权限理由框按钮
         *
         * @param rationalBtnText
         * @return
         */
        public Builder setRationalBtn(String rationalBtnText) {
            this.rationalBtn = rationalBtnText;
            return this;
        }

        /**
         * 拒绝框提示语
         *
         * @param deniedMessage
         * @return
         */
        public Builder setDeniedMessage(String deniedMessage) {
            this.deniedMessage = deniedMessage;
            return this;
        }

        /**
         * 拒绝框关闭按钮
         *
         * @param deniedCloseBtnText
         * @return
         */
        public Builder setDeniedCloseBtn(String deniedCloseBtnText) {
            this.deniedCloseBtn = deniedCloseBtnText;
            return this;
        }

        /**
         * 拒绝框跳转设置权限按钮
         *
         * @param deniedSettingText
         * @return
         */
        public Builder setDeniedSettingBtn(String deniedSettingText) {
            this.deniedSettingBtn = deniedSettingText;
            return this;
        }

        /**
         * 需要申请的权限
         *
         * @param mPermissions {@linkplain android.Manifest.permission android.Manifest.permission}
         * @return
         */
        public Builder setPermissions(String... mPermissions) {
            this.permissions = mPermissions;
            return this;
        }

        public Builder setDialogCancelable(boolean dialogCancelable) {
            this.dialogCancelable = dialogCancelable;
            return this;
        }

        public Builder setDialogCanceledOnTouchOutside(boolean dialogCanceledOnTouchOutside) {
            this.dialogCanceledOnTouchOutside = dialogCanceledOnTouchOutside;
            return this;
        }

        public Builder setCanShowDeniedDialog(boolean canShowDeniedDialog) {
            this.canShowDeniedDialog = canShowDeniedDialog;
            return this;
        }

        public AcpOptions build() {
            if (this.permissions == null || this.permissions.length == 0) {
                throw new IllegalArgumentException("permissions no found...");
            }
            return new AcpOptions(this);
        }
    }
}
