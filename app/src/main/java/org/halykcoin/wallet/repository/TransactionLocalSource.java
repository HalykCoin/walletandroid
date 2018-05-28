package org.halykcoin.wallet.repository;

import org.halykcoin.wallet.entity.Transaction;
import org.halykcoin.wallet.entity.Wallet;

import io.reactivex.Single;

public interface TransactionLocalSource {
	Single<Transaction[]> fetchTransaction(Wallet wallet);

	void putTransactions(Wallet wallet, Transaction[] transactions);

    void clear();
}
