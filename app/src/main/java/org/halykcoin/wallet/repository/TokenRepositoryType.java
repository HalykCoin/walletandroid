package org.halykcoin.wallet.repository;

import org.halykcoin.wallet.entity.Token;
import org.halykcoin.wallet.entity.Wallet;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface TokenRepositoryType {

    Observable<Token[]> fetch(String walletAddress);

    Completable addToken(Wallet wallet, String address, String symbol, int decimals);
}
