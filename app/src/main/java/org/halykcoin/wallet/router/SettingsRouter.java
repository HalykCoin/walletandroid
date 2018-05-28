package org.halykcoin.wallet.router;

import android.content.Context;
import android.content.Intent;

import org.halykcoin.wallet.ui.SettingsActivity;

public class SettingsRouter {

    public void open(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }
}
