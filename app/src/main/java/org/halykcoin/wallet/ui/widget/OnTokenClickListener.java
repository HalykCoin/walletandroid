package org.halykcoin.wallet.ui.widget;

import android.view.View;

import org.halykcoin.wallet.entity.Token;

public interface OnTokenClickListener {
    void onTokenClick(View view, Token token);
}
