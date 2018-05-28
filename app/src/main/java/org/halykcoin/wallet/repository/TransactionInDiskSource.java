package org.halykcoin.wallet.repository;

import org.halykcoin.wallet.entity.Transaction;
import org.halykcoin.wallet.entity.Wallet;

import io.reactivex.Single;

public class TransactionInDiskSource implements TransactionLocalSource {
	@Override
	public Single<Transaction[]> fetchTransaction(Wallet wallet) {
		return null;
	}

	@Override
	public void putTransactions(Wallet wallet, Transaction[] transactions) {

	}

    @Override
    public void clear() {

    }
}
