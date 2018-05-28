package org.halykcoin.wallet.service;

import android.content.Context;

import com.google.gson.Gson;

import org.halykcoin.wallet.entity.HalykBalance;
import org.halykcoin.wallet.entity.HalykResponse;
import org.halykcoin.wallet.entity.HalykResponseTyped;
import org.halykcoin.wallet.entity.HalykTransaction;
import org.halykcoin.wallet.entity.HalykTransactionsInOut;
import org.halykcoin.wallet.entity.HalykTransferDetails;
import org.halykcoin.wallet.repository.HalykGeneralRequestBody;
import org.halykcoin.wallet.repository.HalykTransferBodyRequest;
import org.halykcoin.wallet.repository.PreferenceRepositoryType;
import org.halykcoin.wallet.repository.SharedPreferenceRepository;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Maestro on 14.01.2018.
 */

public class HalykRetrofitClient implements HalykRetrofitClientType {

    private HalykApiClient halykApiClient;

    private static final String HALYK_API_URL = "http://api.wallet.halykcoin.org:2022/api/";
    private final PreferenceRepositoryType mPreferenceRepositoryType;

    public HalykRetrofitClient(
            OkHttpClient httpClient,
            Gson gson,
            Context context) {
        mPreferenceRepositoryType = new SharedPreferenceRepository(context);
        halykApiClient = new Retrofit.Builder()
                .baseUrl(HALYK_API_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(HalykApiClient.class);
    }

    public interface HalykApiClient {
        @Headers("Content-Type: application/json")
        @POST("getAddress")
        Observable<HalykResponseTyped<String>> getAddress(@Body HalykGeneralRequestBody body);

        @Headers("Content-Type: application/json")
        @POST("getTransfers")
        Observable<HalykResponseTyped<HalykTransactionsInOut>> getTransactions(@Body HalykGeneralRequestBody body);

        @Headers("Content-Type: application/json")
        @POST("getBalance")
        Observable<HalykResponseTyped<HalykBalance>> getBalance(@Body HalykGeneralRequestBody body);

        @Headers("Content-Type: application/json")
        @POST("transfer")
        Observable<HalykResponseTyped<HalykTransferDetails>> transfer(@Body HalykTransferBodyRequest body);
    }

    @Override
    public Observable<HalykResponseTyped<String>> getAddress() {
        HalykGeneralRequestBody body = new HalykGeneralRequestBody(mPreferenceRepositoryType.getStoredId());
        return halykApiClient
                .getAddress(body)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<HalykResponseTyped<HalykTransactionsInOut>> getTransactions() {
        HalykGeneralRequestBody body = new HalykGeneralRequestBody(mPreferenceRepositoryType.getStoredId());
        return halykApiClient
                .getTransactions(body)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<HalykResponseTyped<HalykBalance>> getBalance() {
        HalykGeneralRequestBody body = new HalykGeneralRequestBody(mPreferenceRepositoryType.getStoredId());
        return halykApiClient
                .getBalance(body)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<HalykResponseTyped<HalykTransferDetails>> transfer(HalykTransaction transaction) {
        HalykTransferBodyRequest body = new HalykTransferBodyRequest(mPreferenceRepositoryType.getStoredId(),
                transaction.getAddress(),
                transaction.getAmount(),
                transaction.getPayment_id());
        return halykApiClient
                .transfer(body)
                .subscribeOn(Schedulers.io());
    }
}
