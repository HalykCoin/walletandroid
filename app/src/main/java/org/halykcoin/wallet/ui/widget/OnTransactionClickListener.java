package org.halykcoin.wallet.ui.widget;

import android.view.View;

import org.halykcoin.wallet.entity.Transaction;

public interface OnTransactionClickListener {
    void onTransactionClick(View view, Transaction transaction);
}
