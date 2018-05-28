package org.halykcoin.wallet.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import org.halykcoin.wallet.entity.Transaction;
import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.router.ConfirmationRouter;

import java.math.BigInteger;

public class SendViewModel extends BaseViewModel {
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<Transaction> transaction = new MutableLiveData<>();
    private final ConfirmationRouter confirmationRouter;

    SendViewModel(ConfirmationRouter confirmationRouter) {
        this.confirmationRouter = confirmationRouter;
    }

    public void openConfirmation(Context context, String to, String paymentId, BigInteger amount, String contractAddress, int decimals, String symbol, boolean sendingTokens) {
        confirmationRouter.open(context, to, paymentId, amount, contractAddress, decimals, symbol, sendingTokens);
    }
}
