package org.halykcoin.wallet.router;

import android.content.Context;
import android.content.Intent;

import org.halykcoin.wallet.entity.Transaction;
import org.halykcoin.wallet.ui.TransactionDetailActivity;

import static org.halykcoin.wallet.C.Key.TRANSACTION;

public class TransactionDetailRouter {

    public void open(Context context, Transaction transaction) {
        Intent intent = new Intent(context, TransactionDetailActivity.class);
        intent.putExtra(TRANSACTION, transaction);
        context.startActivity(intent);
    }
}
