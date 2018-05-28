package org.halykcoin.wallet.interact;

import org.halykcoin.wallet.entity.Transaction;
import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.repository.TransactionRepositoryType;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class FetchTransactionsInteract {

    private final TransactionRepositoryType transactionRepository;

    public FetchTransactionsInteract(TransactionRepositoryType transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Observable<Transaction[]> fetch(Wallet wallet) {
        return transactionRepository
                .fetchTransaction(wallet)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
