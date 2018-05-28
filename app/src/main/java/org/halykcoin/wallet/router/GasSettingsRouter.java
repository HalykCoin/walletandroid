package org.halykcoin.wallet.router;


import android.app.Activity;
import android.content.Intent;

import org.halykcoin.wallet.C;
import org.halykcoin.wallet.entity.GasSettings;
import org.halykcoin.wallet.ui.GasSettingsActivity;
import org.halykcoin.wallet.viewmodel.GasSettingsViewModel;

public class GasSettingsRouter {
    public void open(Activity context, GasSettings gasSettings) {
        Intent intent = new Intent(context, GasSettingsActivity.class);
        intent.putExtra(C.EXTRA_GAS_PRICE, gasSettings.gasPrice.toString());
        intent.putExtra(C.EXTRA_GAS_LIMIT, gasSettings.gasLimit.toString());
        context.startActivityForResult(intent, GasSettingsViewModel.SET_GAS_SETTINGS);
    }
}
