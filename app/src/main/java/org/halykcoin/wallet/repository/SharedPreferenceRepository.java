package org.halykcoin.wallet.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.halykcoin.wallet.C;
import org.halykcoin.wallet.entity.GasSettings;

import java.math.BigInteger;

public class SharedPreferenceRepository implements PreferenceRepositoryType {

	private static final String CURRENT_ACCOUNT_ADDRESS_KEY = "current_account_address";
	private static final String DEFAULT_NETWORK_NAME_KEY = "default_network_name";
	private static final String GAS_PRICE_KEY  ="gas_price";
    private static final String GAS_LIMIT_KEY  ="gas_limit";
    private static final String WALLET_ID ="id";
	private static final String PASSWORD_PREFERENCE_KEY = "PASSCODE";

	private final SharedPreferences pref;

	public SharedPreferenceRepository(Context context) {
		pref = PreferenceManager.getDefaultSharedPreferences(context);
	}

	@Override
	public String getCurrentWalletAddress() {
		return pref.getString(CURRENT_ACCOUNT_ADDRESS_KEY, null);
	}

	@Override
	public void setCurrentWalletAddress(String address) {
		pref.edit().putString(CURRENT_ACCOUNT_ADDRESS_KEY, address).apply();
	}

	@Override
	public String getDefaultNetwork() {
		return pref.getString(DEFAULT_NETWORK_NAME_KEY, null);
	}

	@Override
	public void setDefaultNetwork(String netName) {
		pref.edit().putString(DEFAULT_NETWORK_NAME_KEY, netName).apply();
	}

	@Override
    public GasSettings getGasSettings() {
	    BigInteger gasPrice = new BigInteger(pref.getString(GAS_PRICE_KEY, C.DEFAULT_GAS_PRICE));
	    BigInteger gasLimit = new BigInteger(pref.getString(GAS_LIMIT_KEY, C.DEFAULT_GAS_LIMIT));

	    return new GasSettings(gasPrice, gasLimit);
    }

    @Override
    public void setGasSettings(GasSettings gasSettings) {
	    pref.edit().putString(GAS_PRICE_KEY, gasSettings.gasPrice.toString()).apply();
        pref.edit().putString(GAS_PRICE_KEY, gasSettings.gasLimit.toString()).apply();
    }

	@Override
	public String getStoredId() {
		return pref.getString(WALLET_ID, null);
	}

	@Override
	public void setStoredId(String value) {
		pref.edit().putString(WALLET_ID, value).apply();
	}

	@Override
	public boolean isIdAvailable() {
		return getStoredId() != null;
	}

	@Override
	public String getPasscode() {
		return pref.getString(PASSWORD_PREFERENCE_KEY, null);
	}

	@Override
	public boolean isPasscodeSet() {
		if (getPasscode() == null || getPasscode().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
}
