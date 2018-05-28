package org.halykcoin.wallet.interact;

import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.interact.rx.operator.Operators;
import org.halykcoin.wallet.repository.PasswordStore;
import org.halykcoin.wallet.repository.WalletRepositoryType;

import io.reactivex.Single;

public class CreateWalletInteract {

	private final WalletRepositoryType walletRepository;
	private final PasswordStore passwordStore;

	public CreateWalletInteract(WalletRepositoryType walletRepository, PasswordStore passwordStore) {
		this.walletRepository = walletRepository;
		this.passwordStore = passwordStore;
	}

	public Single<Wallet> create() {
		return passwordStore.generatePassword()
				.flatMap(password -> walletRepository
						.createWallet(password)
						.compose(Operators.savePassword(passwordStore, walletRepository, password)));
	}
}
