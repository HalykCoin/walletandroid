package org.halykcoin.wallet.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import org.halykcoin.wallet.router.ConfirmationRouter;

import io.reactivex.annotations.NonNull;

public class SendViewModelFactory implements ViewModelProvider.Factory {

    private final ConfirmationRouter confirmationRouter;

    public SendViewModelFactory(ConfirmationRouter confirmationRouter) {
        this.confirmationRouter = confirmationRouter;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SendViewModel(confirmationRouter);
    }
}
