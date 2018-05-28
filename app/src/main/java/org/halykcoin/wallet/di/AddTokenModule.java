package org.halykcoin.wallet.di;

import org.halykcoin.wallet.interact.AddTokenInteract;
import org.halykcoin.wallet.interact.FindDefaultWalletInteract;
import org.halykcoin.wallet.repository.TokenRepositoryType;
import org.halykcoin.wallet.repository.WalletRepositoryType;
import org.halykcoin.wallet.router.MyTokensRouter;
import org.halykcoin.wallet.viewmodel.AddTokenViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class AddTokenModule {

    @Provides
    AddTokenViewModelFactory addTokenViewModelFactory(
            AddTokenInteract addTokenInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            MyTokensRouter myTokensRouter) {
        return new AddTokenViewModelFactory(
                addTokenInteract, findDefaultWalletInteract, myTokensRouter);
    }

    @Provides
    AddTokenInteract provideAddTokenInteract(
            TokenRepositoryType tokenRepository,
            WalletRepositoryType walletRepository) {
        return new AddTokenInteract(walletRepository, tokenRepository);
    }

    @Provides
    FindDefaultWalletInteract provideFindDefaultWalletInteract(WalletRepositoryType walletRepository) {
        return new FindDefaultWalletInteract(walletRepository);
    }

    @Provides
    MyTokensRouter provideMyTokensRouter() {
        return new MyTokensRouter();
    }
}
