package org.halykcoin.wallet.router;


import android.content.Context;
import android.content.Intent;

import org.halykcoin.wallet.C;
import org.halykcoin.wallet.ui.ConfirmationActivity;

import java.math.BigInteger;

public class ConfirmationRouter {
    public void open(Context context, String to, String paymentId, BigInteger amount, String contractAddress, int decimals, String symbol, boolean sendingTokens) {
        Intent intent = new Intent(context, ConfirmationActivity.class);
        intent.putExtra(C.EXTRA_TO_ADDRESS, to);
        intent.putExtra(C.EXTRA_PAYMENT_ID, paymentId);
        intent.putExtra(C.EXTRA_AMOUNT, amount.toString());
        intent.putExtra(C.EXTRA_CONTRACT_ADDRESS, contractAddress);
        intent.putExtra(C.EXTRA_DECIMALS, decimals);
        intent.putExtra(C.EXTRA_SYMBOL, symbol);
        intent.putExtra(C.EXTRA_SENDING_TOKENS, sendingTokens);
        context.startActivity(intent);
    }
}
