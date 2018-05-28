package org.halykcoin.wallet.service;

import android.content.Context;

import com.google.gson.Gson;

import org.halykcoin.wallet.repository.HalykGeneralRequestBody;
import org.halykcoin.wallet.repository.PreferenceRepositoryType;
import org.halykcoin.wallet.repository.SharedPreferenceRepository;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;


/**
 * Created by Maestro on 07.01.2018.
 */

public class HalykRestClient {

    public static final String BASE_URL = "http://api.wallet.halykcoin.org:2022/api";
    public static final String CREATE_WALLET_URL = "/createWallet";
    public static final String GET_ADDRESS = "/getAddress";

    private static HalykRestClient mHalykRestClient = null;
    final Gson gson = new Gson();

    OkHttpClient mHttpClient;

    private HalykRestClient() {
        mHttpClient = new OkHttpClient.Builder()
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public static HalykRestClient getInstance() {
        if (mHalykRestClient == null) {
            mHalykRestClient = new HalykRestClient();
        }
        return(mHalykRestClient);
    }

    private Request createWalletRequest(){
        RequestBody reqbody = RequestBody.create(null, "");
        String url = BASE_URL + CREATE_WALLET_URL;
        return new Request.Builder()
                .method("POST",reqbody)
                .url(url)
                .build();
    }

    private Request request(String url, Context context) {
        PreferenceRepositoryType preferenceRepositoryType = new SharedPreferenceRepository(context);
        String key = preferenceRepositoryType.getStoredId();
        HalykGeneralRequestBody body = new HalykGeneralRequestBody(key);
        final Gson gson = new Gson();
        String jsonBody = gson.toJson(body);
        RequestBody reqbody = RequestBody.create(MediaType.parse("application/json"), jsonBody);
        return new Request.Builder()
                .method("POST",reqbody)
                .url(url)
                .build();
    }

    public void call(Request request, HalykHttpCallback callback){
        mHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Timber.d("onResponse = " + response + " code = " + response.code());
                if (response.isSuccessful()) {
                    String responseBody = (response.body() == null) ? "" : response.body().string();
                    callback.onSuccess(responseBody);
                } else {
                    callback.onError(response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Timber.d("onFailure ");
                callback.onError(0);
            }

        });
    }

    private Request getAddressRequest(Context context){
        return request(BASE_URL + GET_ADDRESS, context);
    }

    public void createWallet(HalykHttpCallback callback){
        Request request = createWalletRequest();
        call(request, callback);
    }

    public void getAddress(Context context, HalykHttpCallback callback){
        Request request = getAddressRequest(context);
        call(request, callback);
    }

}
