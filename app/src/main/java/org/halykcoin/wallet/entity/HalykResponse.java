package org.halykcoin.wallet.entity;

import com.google.gson.Gson;

/**
 * Created by Maestro on 07.01.2018.
 */

public class HalykResponse {
    private String method;
    private boolean success;
    private Object data;
    private String error;

    public HalykResponse(String method, boolean success, Object data, String error) {
        this.method = method;
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public String getMethod() {
        return method;
    }

    public static HalykResponse fromString(String json) {
        final Gson gson = new Gson();
        return gson.fromJson(json, HalykResponse.class);
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
