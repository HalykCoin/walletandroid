package org.halykcoin.wallet.di;

import org.halykcoin.wallet.router.ConfirmationRouter;
import org.halykcoin.wallet.viewmodel.SendViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
class SendModule {
    @Provides
    SendViewModelFactory provideSendViewModelFactory(ConfirmationRouter confirmationRouter) {
        return new SendViewModelFactory(confirmationRouter);
    }

    @Provides
    ConfirmationRouter provideConfirmationRouter() {
        return new ConfirmationRouter();
    }
}
