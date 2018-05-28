package org.halykcoin.wallet.service;

import org.halykcoin.wallet.entity.Transaction;

import io.reactivex.Observable;

public interface BlockExplorerClientType {
	Observable<Transaction[]> fetchTransactions(String forAddress);
}
