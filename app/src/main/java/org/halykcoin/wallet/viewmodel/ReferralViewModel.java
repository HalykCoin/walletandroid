package org.halykcoin.wallet.viewmodel;

import org.halykcoin.wallet.entity.HalykResponse;
import org.halykcoin.wallet.repository.HalykRepositoryType;

import timber.log.Timber;

/**
 * Created by Igor Lakhmotkin on 10.03.2018, for HalykCoin.
 */

public class ReferralViewModel extends BaseViewModel {

    private HalykRepositoryType mRepository;

    ReferralViewModel(HalykRepositoryType repository) {
        mRepository = repository;
    }

    public void requestReferal() {
        progress.postValue(true);
    }

    private void onReferralError(Throwable throwable) {
        progress.postValue(false);
        this.onError(throwable);
    }

    private void onReferralSuccess(HalykResponse halykResponse) {
        progress.postValue(false);
        Timber.d("onReferralSuccess " + halykResponse);
    }
}
