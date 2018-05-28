package org.halykcoin.wallet.di;

import org.halykcoin.wallet.interact.FindDefaultWalletInteract;
import org.halykcoin.wallet.repository.WalletRepositoryType;
import org.halykcoin.wallet.router.ManageWalletsRouter;

import dagger.Module;
import dagger.Provides;

@Module
class SettingsFragmentModule {
    @Provides
    FindDefaultWalletInteract provideFindDefaultWalletInteract(WalletRepositoryType walletRepository) {
        return new FindDefaultWalletInteract(walletRepository);
    }

    @Provides
    ManageWalletsRouter provideManageWalletsRouter() {
        return new ManageWalletsRouter();
    }
}
