package org.halykcoin.wallet.router;

import android.content.Context;
import android.content.Intent;

import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.ui.TokensActivity;

import static org.halykcoin.wallet.C.Key.WALLET;

public class MyTokensRouter {

    public void open(Context context, Wallet wallet) {
        Intent intent = new Intent(context, TokensActivity.class);
        intent.putExtra(WALLET, wallet);
        context.startActivity(intent);
    }
}
