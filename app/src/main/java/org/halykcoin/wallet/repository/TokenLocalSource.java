package org.halykcoin.wallet.repository;

import org.halykcoin.wallet.entity.TokenInfo;
import org.halykcoin.wallet.entity.Wallet;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface TokenLocalSource {
    Completable put(Wallet wallet, TokenInfo tokenInfo);
    Single<TokenInfo[]> fetch(Wallet wallet);
}
