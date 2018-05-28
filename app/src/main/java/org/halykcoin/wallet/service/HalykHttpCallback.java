package org.halykcoin.wallet.service;

/**
 * Created by Maestro on 07.01.2018.
 */

public interface HalykHttpCallback {
    void onSuccess(String response);
    void onError(int code);
}
