package org.halykcoin.wallet.repository;

import org.halykcoin.wallet.entity.NetworkInfo;

public interface OnNetworkChangeListener {
	void onNetworkChanged(NetworkInfo networkInfo);
}
