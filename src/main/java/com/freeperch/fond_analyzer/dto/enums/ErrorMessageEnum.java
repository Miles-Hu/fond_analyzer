package com.freeperch.fond_analyzer.dto.enums;

public enum ErrorMessageEnum {
    //
    FAIL("0", "失败"),
    SUCCESS("1", "成功"),
    DATA_TOO_LARGE("100000", "请求内容过长"),
    FREQUENT_OPERATION("100001", "操作过于频繁，请稍后重试"),
    PARAM_ERROR("100002", "参数错误"),
    SYSTEM_ERROR("100003", "系统错误"),
    TOKEN_ERROR("100004", "TOKEN ERROR"),
    KEY_IS_NOT_EXIST("100005", "KEY IS NOT EXIST"),
    FREQUENT_ACTIONS("100006", "FREQUENT ACTIONS");

    private String errorCode;

    private String errorMessage;

    ErrorMessageEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
