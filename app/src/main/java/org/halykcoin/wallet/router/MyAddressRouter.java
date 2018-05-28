package org.halykcoin.wallet.router;

import android.content.Context;
import android.content.Intent;

import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.ui.MyAddressActivity;

import static org.halykcoin.wallet.C.Key.WALLET;

public class MyAddressRouter {

    public void open(Context context, Wallet wallet) {
        Intent intent = new Intent(context, MyAddressActivity.class);
        intent.putExtra(WALLET, wallet);
        context.startActivity(intent);
    }
}
