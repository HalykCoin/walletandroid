package org.halykcoin.wallet.di;

import org.halykcoin.wallet.interact.CreateWalletInteract;
import org.halykcoin.wallet.interact.DeleteWalletInteract;
import org.halykcoin.wallet.interact.ExportWalletInteract;
import org.halykcoin.wallet.interact.FetchWalletsInteract;
import org.halykcoin.wallet.interact.FindDefaultWalletInteract;
import org.halykcoin.wallet.interact.SetDefaultWalletInteract;
import org.halykcoin.wallet.repository.PasswordStore;
import org.halykcoin.wallet.repository.WalletRepositoryType;
import org.halykcoin.wallet.router.ImportWalletRouter;
import org.halykcoin.wallet.router.TransactionsRouter;
import org.halykcoin.wallet.viewmodel.WalletsViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
class AccountsManageModule {

	@Provides
    WalletsViewModelFactory provideAccountsManageViewModelFactory(
			CreateWalletInteract createWalletInteract,
			SetDefaultWalletInteract setDefaultWalletInteract,
			DeleteWalletInteract deleteWalletInteract,
			FetchWalletsInteract fetchWalletsInteract,
			FindDefaultWalletInteract findDefaultWalletInteract,
			ExportWalletInteract exportWalletInteract,
			ImportWalletRouter importWalletRouter,
            TransactionsRouter transactionsRouter) {
		return new WalletsViewModelFactory(createWalletInteract,
                setDefaultWalletInteract,
                deleteWalletInteract,
                fetchWalletsInteract,
                findDefaultWalletInteract,
                exportWalletInteract,
                importWalletRouter,
                transactionsRouter);
	}

	@Provides
    CreateWalletInteract provideCreateAccountInteract(
            WalletRepositoryType accountRepository, PasswordStore passwordStore) {
		return new CreateWalletInteract(accountRepository, passwordStore);
	}

	@Provides
    SetDefaultWalletInteract provideSetDefaultAccountInteract(WalletRepositoryType accountRepository) {
		return new SetDefaultWalletInteract(accountRepository);
	}

	@Provides
    DeleteWalletInteract provideDeleteAccountInteract(
            WalletRepositoryType accountRepository, PasswordStore store) {
		return new DeleteWalletInteract(accountRepository, store);
	}

	@Provides
    FetchWalletsInteract provideFetchAccountsInteract(WalletRepositoryType accountRepository) {
		return new FetchWalletsInteract(accountRepository);
	}

	@Provides
    FindDefaultWalletInteract provideFindDefaultAccountInteract(WalletRepositoryType accountRepository) {
		return new FindDefaultWalletInteract(accountRepository);
	}

	@Provides
    ExportWalletInteract provideExportWalletInteract(
            WalletRepositoryType walletRepository, PasswordStore passwordStore) {
	    return new ExportWalletInteract(walletRepository, passwordStore);
    }

	@Provides
    ImportWalletRouter provideImportAccountRouter() {
		return new ImportWalletRouter();
	}

	@Provides
    TransactionsRouter provideTransactionsRouter() {
	    return new TransactionsRouter();
    }
}
