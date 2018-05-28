package org.halykcoin.wallet.repository;

import org.halykcoin.wallet.entity.HalykBalance;
import org.halykcoin.wallet.entity.HalykResponse;
import org.halykcoin.wallet.entity.HalykResponseTyped;
import org.halykcoin.wallet.entity.HalykTransaction;
import org.halykcoin.wallet.entity.HalykTransactionsInOut;
import org.halykcoin.wallet.entity.HalykTransferDetails;
import org.halykcoin.wallet.service.HalykRetrofitClientType;

import io.reactivex.Observable;

/**
 * Created by Maestro on 14.01.2018.
 */

public class HalykRepository implements HalykRepositoryType {

    private final HalykRetrofitClientType halykNetworkService;

    public HalykRepository(
            HalykRetrofitClientType halykNetworkService) {
        this.halykNetworkService = halykNetworkService;
    }

    @Override
    public Observable<HalykResponseTyped<String>> getAddress() {
        return halykNetworkService.getAddress();
    }

    @Override
    public Observable<HalykResponseTyped<HalykTransactionsInOut>> getTransactions() {
        return halykNetworkService.getTransactions();
    }

    @Override
    public Observable<HalykResponseTyped<HalykBalance>> getBalance() {
        return halykNetworkService.getBalance();
    }

    @Override
    public Observable<HalykResponseTyped<HalykTransferDetails>> transfer(HalykTransaction transaction) {
        return halykNetworkService.transfer(transaction);
    }
}
