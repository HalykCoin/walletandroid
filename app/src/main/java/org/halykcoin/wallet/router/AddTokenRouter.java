package org.halykcoin.wallet.router;

import android.content.Context;
import android.content.Intent;

import org.halykcoin.wallet.ui.AddTokenActivity;

public class AddTokenRouter {

    public void open(Context context) {
        Intent intent = new Intent(context, AddTokenActivity.class);
        context.startActivity(intent);
    }
}
