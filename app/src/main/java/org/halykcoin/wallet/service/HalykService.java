package org.halykcoin.wallet.service;

import org.halykcoin.wallet.entity.HalykResponse;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Maestro on 07.01.2018.
 */

public interface HalykService {
    @POST("/createWallet")
    Call<HalykResponse> createWallet();
}
