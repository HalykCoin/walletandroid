package org.halykcoin.wallet.repository;

import org.halykcoin.wallet.entity.GasSettings;

public interface PreferenceRepositoryType {
	String getCurrentWalletAddress();
	void setCurrentWalletAddress(String address);

	String getDefaultNetwork();
	void setDefaultNetwork(String netName);

	GasSettings getGasSettings();
	void setGasSettings(GasSettings gasPrice);

	String getStoredId();
	void setStoredId(String value);
	boolean isIdAvailable();

	String getPasscode();
	boolean isPasscodeSet();

}
