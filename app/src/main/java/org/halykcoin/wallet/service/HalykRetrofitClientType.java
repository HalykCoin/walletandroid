package org.halykcoin.wallet.service;

import org.halykcoin.wallet.entity.HalykBalance;
import org.halykcoin.wallet.entity.HalykResponse;
import org.halykcoin.wallet.entity.HalykResponseTyped;
import org.halykcoin.wallet.entity.HalykTransaction;
import org.halykcoin.wallet.entity.HalykTransactionsInOut;
import org.halykcoin.wallet.entity.HalykTransferDetails;

import io.reactivex.Observable;

/**
 * Created by Maestro on 14.01.2018.
 */

public interface HalykRetrofitClientType {
    Observable<HalykResponseTyped<HalykTransactionsInOut>> getTransactions();
    Observable<HalykResponseTyped<HalykBalance>> getBalance();
    Observable<HalykResponseTyped<String>> getAddress();
    Observable<HalykResponseTyped<HalykTransferDetails>> transfer(HalykTransaction transaction);
}
