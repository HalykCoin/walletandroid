package org.halykcoin.wallet.service;

import org.halykcoin.wallet.entity.Ticker;

import io.reactivex.Observable;

public interface TickerService {

    Observable<Ticker> fetchTickerPrice(String ticker);
}
