package org.halykcoin.wallet.di;

import org.halykcoin.wallet.interact.ImportWalletInteract;
import org.halykcoin.wallet.repository.PasswordStore;
import org.halykcoin.wallet.repository.WalletRepositoryType;
import org.halykcoin.wallet.viewmodel.ImportWalletViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
class ImportModule {
    @Provides
    ImportWalletViewModelFactory provideImportWalletViewModelFactory(
            ImportWalletInteract importWalletInteract) {
        return new ImportWalletViewModelFactory(importWalletInteract);
    }

    @Provides
    ImportWalletInteract provideImportWalletInteract(
            WalletRepositoryType walletRepository, PasswordStore passwordStore) {
        return new ImportWalletInteract(walletRepository, passwordStore);
    }
}
