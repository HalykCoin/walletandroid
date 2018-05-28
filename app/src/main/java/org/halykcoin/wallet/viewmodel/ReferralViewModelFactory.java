package org.halykcoin.wallet.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.halykcoin.wallet.repository.HalykRepositoryType;

public class ReferralViewModelFactory implements ViewModelProvider.Factory {

    private HalykRepositoryType mRepository;

    public ReferralViewModelFactory(HalykRepositoryType repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ReferralViewModel(mRepository);
    }
}
