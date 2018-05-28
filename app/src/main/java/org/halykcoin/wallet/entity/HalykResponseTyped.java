package org.halykcoin.wallet.entity;

/**
 * Created by Maestro on 07.01.2018.
 */

public class HalykResponseTyped<T> {
    private String method;
    private boolean success;
    private T data;
    private String error;

    public HalykResponseTyped(String method, boolean success, T data, String error) {
        this.method = method;
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
