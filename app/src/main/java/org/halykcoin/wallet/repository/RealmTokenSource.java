package org.halykcoin.wallet.repository;

import org.halykcoin.wallet.entity.TokenInfo;
import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.repository.entity.RealmTokenInfo;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmTokenSource implements TokenLocalSource {

    @Override
    public Completable put(Wallet wallet, TokenInfo tokenInfo) {
        return Completable.fromAction(() -> putInNeed(wallet, tokenInfo));
    }

    @Override
    public Single<TokenInfo[]> fetch(Wallet wallet) {
        return Single.fromCallable(() -> {
            Realm realm = null;
            try {
                realm = getRealmInstance(wallet);
                RealmResults<RealmTokenInfo> realmItems = realm.where(RealmTokenInfo.class)
                        .sort("addedTime", Sort.ASCENDING)
                        .findAll();
                int len = realmItems.size();
                TokenInfo[] result = new TokenInfo[len];
                for (int i = 0; i < len; i++) {
                    RealmTokenInfo realmItem = realmItems.get(i);
                    if (realmItem != null) {
                        result[i] = new TokenInfo(
                                realmItem.getAddress(),
                                realmItem.getName(),
                                realmItem.getSymbol(),
                                realmItem.getDecimals());
                    }
                }
                return result;
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }

    private Realm getRealmInstance(Wallet wallet) {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(wallet.address + ".realm")
                .schemaVersion(1)
                .build();
        return Realm.getInstance(config);
    }

    private void putInNeed(Wallet wallet, TokenInfo tokenInfo) {
        Realm realm = null;
        try {
            realm = getRealmInstance(wallet);
            RealmTokenInfo realmTokenInfo = realm.where(RealmTokenInfo.class)
                    .equalTo("address", tokenInfo.address)
                    .findFirst();
            if (realmTokenInfo == null) {
                realm.executeTransaction(r -> {
                    RealmTokenInfo obj = r.createObject(RealmTokenInfo.class, tokenInfo.address);
                    obj.setName(tokenInfo.name);
                    obj.setSymbol(tokenInfo.symbol);
                    obj.setDecimals(tokenInfo.decimals);
                    obj.setAddedTime(System.currentTimeMillis());
                });
            }
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

}
