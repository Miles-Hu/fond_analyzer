package com.freeperch.fond_analyzer.dto.response;


import com.freeperch.fond_analyzer.dto.enums.ErrorMessageEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Miles
 * @date 2020-06-12 14:37
 */
public class ClientStatus {

    private String status;

    private String message;

    /**
     * Instantiates a new Status dto.
     */
    public ClientStatus() {
        this.status = "1";
        this.message = "请求成功";
    }

    public ClientStatus setError() {
        this.status = "0";
        this.message = "error";
        return this;
    }

    /**
     * Instantiates a new Status dto.
     *
     * @param status  the status
     * @param message the message
     */
    public ClientStatus(final String status, final String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Instantiates a new Status dto.
     *
     * @param errorMessage
     */
    public ClientStatus(ErrorMessageEnum errorMessage) {
        this.status = errorMessage.getErrorCode();
        this.message = errorMessage.getErrorMessage();
    }

    /**
     * ok status dto.
     *
     * @return the status dto
     */
    public static ClientStatus ok() {
        return new ClientStatus("1", "请求成功");
    }

    /**
     * error status dto.
     *
     * @return the status dto
     */
    public static ClientStatus error() {
        return new ClientStatus("0", "请求失败");
    }

    /**
     * Error status dto.
     *
     * @param message the message
     * @return the status dto
     */
    public static ClientStatus error(final String message) {
        return new ClientStatus("0", message);
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Sets message.
     *
     * @param errorMessage
     */
    public void setMessage(final ErrorMessageEnum errorMessage) {
        this.status = errorMessage.getErrorCode();
        this.message = errorMessage.getErrorMessage();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
