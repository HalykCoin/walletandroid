package org.halykcoin.wallet.interact.rx.operator;

import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.repository.PasswordStore;
import org.halykcoin.wallet.repository.WalletRepositoryType;

import io.reactivex.SingleTransformer;

public class Operators {

    public static SingleTransformer<Wallet, Wallet> savePassword(
            PasswordStore passwordStore, WalletRepositoryType walletRepository, String password) {
        return new SavePasswordOperator(passwordStore, walletRepository, password);
    }
}
