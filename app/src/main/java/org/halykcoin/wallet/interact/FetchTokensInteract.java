package org.halykcoin.wallet.interact;

import org.halykcoin.wallet.entity.Token;
import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.repository.TokenRepositoryType;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class FetchTokensInteract {

    private final TokenRepositoryType tokenRepository;

    public FetchTokensInteract(TokenRepositoryType tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Observable<Token[]> fetch(Wallet wallet) {
        return tokenRepository.fetch(wallet.address)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
