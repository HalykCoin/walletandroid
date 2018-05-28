package org.halykcoin.wallet.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.halykcoin.wallet.C;
import org.halykcoin.wallet.R;
import org.halykcoin.wallet.entity.ErrorEnvelope;
import org.halykcoin.wallet.entity.HalykTransferDetails;
import org.halykcoin.wallet.repository.PreferenceRepositoryType;
import org.halykcoin.wallet.service.HalykRestClient;
import org.halykcoin.wallet.util.BalanceUtils;
import org.halykcoin.wallet.viewmodel.ConfirmationViewModel;
import org.halykcoin.wallet.viewmodel.ConfirmationViewModelFactory;

import java.math.BigInteger;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

public class ConfirmationActivity extends BaseActivity {
    AlertDialog dialog;

    @Inject
    ConfirmationViewModelFactory confirmationViewModelFactory;
    ConfirmationViewModel viewModel;

    @Inject
    protected PreferenceRepositoryType preferenceRepositoryType;

    private TextView fromAddressText;
    private TextView toAddressText;
    private TextView paymentIdText;
    private TextView valueText;
    private Button sendButton;

    private BigInteger amount;
    private int decimals;
    private HalykRestClient mHalykRestClitnt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm);
        toolbar();
        mHalykRestClitnt = HalykRestClient.getInstance();

        fromAddressText = findViewById(R.id.text_from);
        toAddressText = findViewById(R.id.text_to);
        paymentIdText = findViewById(R.id.text_payment_id);
        valueText = findViewById(R.id.text_value);
        sendButton = findViewById(R.id.send_button);

        sendButton.setOnClickListener(view -> makeTransfer());

        String fromAddress = preferenceRepositoryType.getCurrentWalletAddress();
        fromAddressText.setText(fromAddress);

        String toAddress = getIntent().getStringExtra(C.EXTRA_TO_ADDRESS);
        String paymentId = getIntent().getStringExtra(C.EXTRA_PAYMENT_ID);
        amount = new BigInteger(getIntent().getStringExtra(C.EXTRA_AMOUNT));
        Timber.d("onCreate amount = " + amount);
        decimals = C.HLC_DECIMALS;
        String symbol = C.HLC_SYMBOL;

        toAddressText.setText(toAddress);
        paymentIdText.setText(paymentId);

        String amountString = "-" + BalanceUtils.subunitToBase(amount, decimals) + " " + symbol;
        valueText.setText(amountString);
        valueText.setTextColor(ContextCompat.getColor(this, R.color.red));

        viewModel = ViewModelProviders.of(this, confirmationViewModelFactory)
                .get(ConfirmationViewModel.class);

        viewModel.sendTransaction().observe(this, this::onTransaction);
        viewModel.progress().observe(this, this::onProgress);
        viewModel.error().observe(this, this::showError);
    }

    private void onTransaction(HalykTransferDetails halykTransferDetails) {
        Timber.d("onTransaction");
        hideDialog();
        if (halykTransferDetails == null){
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.title_dialog_error)
                    .setMessage(R.string.transfer_error_message)
                    .setPositiveButton(R.string.button_ok, (dialog1, id) -> {
                        finish();
                    })
                    .create();
            dialog.show();
            return;
        }
        String hash = halykTransferDetails.getTx_hash_list()[0];
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.transaction_succeeded)
                .setMessage(hash)
                .setPositiveButton(R.string.button_ok, (dialog1, id) -> {
                    finish();
                })
                .setNeutralButton(R.string.copy, (dialog1, id) -> {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("transaction hash", hash);
                    clipboard.setPrimaryClip(clip);
                    finish();
                })
                .create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void onProgress(boolean shouldShowProgress) {
        hideDialog();
        if (shouldShowProgress) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.title_dialog_sending)
                    .setView(new ProgressBar(this))
                    .setCancelable(false)
                    .create();
            dialog.show();
        }
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void makeTransfer() {
        String toAddress = toAddressText.getText().toString();
        String paymentId = paymentIdText.getText().toString();
        viewModel.transfer(amount, toAddress, paymentId);
    }

    private void showError(ErrorEnvelope error) {
        Timber.d("showError");
        hideDialog();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.error_transaction_failed)
                .setMessage(error.message)
                .setPositiveButton(R.string.button_ok, (dialog1, id) -> {
                })
                .create();
        dialog.show();
    }

}
