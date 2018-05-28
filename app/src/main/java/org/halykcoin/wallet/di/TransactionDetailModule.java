package org.halykcoin.wallet.di;

import org.halykcoin.wallet.interact.FindDefaultNetworkInteract;
import org.halykcoin.wallet.repository.EthereumNetworkRepositoryType;
import org.halykcoin.wallet.router.ExternalBrowserRouter;
import org.halykcoin.wallet.viewmodel.TransactionDetailViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class TransactionDetailModule {

    @Provides
    TransactionDetailViewModelFactory provideTransactionDetailViewModelFactory(
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            ExternalBrowserRouter externalBrowserRouter) {
        return new TransactionDetailViewModelFactory(findDefaultNetworkInteract, externalBrowserRouter);
    }

    @Provides
    FindDefaultNetworkInteract provideFindDefaultNetworkInteract(
            EthereumNetworkRepositoryType ethereumNetworkRepository) {
        return new FindDefaultNetworkInteract(ethereumNetworkRepository);
    }

    @Provides
    ExternalBrowserRouter externalBrowserRouter() {
        return new ExternalBrowserRouter();
    }
}
