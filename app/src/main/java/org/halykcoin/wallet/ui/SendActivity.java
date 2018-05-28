package org.halykcoin.wallet.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import org.halykcoin.wallet.C;
import org.halykcoin.wallet.R;
import org.halykcoin.wallet.ui.barcode.BarcodeCaptureActivity;
import org.halykcoin.wallet.util.BalanceUtils;
import org.halykcoin.wallet.util.QRURLParser;
import org.halykcoin.wallet.viewmodel.SendViewModel;
import org.halykcoin.wallet.viewmodel.SendViewModelFactory;

import java.math.BigInteger;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

import static org.halykcoin.wallet.ui.barcode.BarcodeCaptureActivity.BARCODE_READER_REQUEST_CODE;

public class SendActivity extends BaseActivity {

    @Inject
    SendViewModelFactory sendViewModelFactory;
    SendViewModel viewModel;

    private EditText toAddressText;
    private EditText paymentIdText;
    private EditText amountText;

    // In case we're sending tokens
    private boolean sendingTokens = false;
    private String contractAddress;
    private int decimals;
    private String symbol;
    private TextInputLayout toInputLayout;
    private TextInputLayout amountInputLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send);
        toolbar();

        viewModel = ViewModelProviders.of(this, sendViewModelFactory)
                .get(SendViewModel.class);

        toInputLayout = findViewById(R.id.to_input_layout);
        toAddressText = findViewById(R.id.send_to_address);
        paymentIdText = findViewById(R.id.send_payment_id);
        amountInputLayout = findViewById(R.id.amount_input_layout);
        amountText = findViewById(R.id.send_amount);

        contractAddress = getIntent().getStringExtra(C.EXTRA_CONTRACT_ADDRESS);
        decimals = C.HLC_DECIMALS;
        symbol = getIntent().getStringExtra(C.EXTRA_SYMBOL);
        symbol = symbol == null ? C.HLC_SYMBOL : symbol;
        sendingTokens = getIntent().getBooleanExtra(C.EXTRA_SENDING_TOKENS, false);

        setTitle(getString(R.string.title_send) + " " + symbol);
        amountInputLayout.setHint(getString(R.string.hint_amount) + " " + symbol);

        // Populate to address if it has been passed forward
        String toAddress = getIntent().getStringExtra(C.EXTRA_ADDRESS);
        if (toAddress != null) {
            toAddressText.setText(toAddress);
        }

        ImageButton scanBarcodeButton = findViewById(R.id.scan_barcode_button);
        scanBarcodeButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
            startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next: {
                onNext();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                    QRURLParser parser = QRURLParser.getInstance();
                    Map<String, String> extracted_fields = parser.extractFieldsFromQrString(barcode.displayValue);
                    if (extracted_fields == null) {
                        Toast.makeText(this, R.string.toast_qr_code_no_data, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (extracted_fields.get(QRURLParser.QR_FIELD_ADDRESS) != null) {
                        toAddressText.setText(extracted_fields.get(QRURLParser.QR_FIELD_ADDRESS));
                    }
                    if (extracted_fields.get(QRURLParser.QR_FIELD_PAYMENT_ID) != null) {
                        paymentIdText.setText(extracted_fields.get(QRURLParser.QR_FIELD_PAYMENT_ID));
                    }
                    if (extracted_fields.get(QRURLParser.QR_FIELD_AMOUNT) != null) {
                        String amountStr = BalanceUtils.getScaledValue(extracted_fields.get(QRURLParser.QR_FIELD_AMOUNT), decimals);
                        amountText.setText(amountStr);
                    }
                }
            } else {
                Log.e("SEND", String.format(getString(R.string.barcode_error_format),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onNext() {
        // Validate input fields
        boolean inputValid = true;
        final String to = toAddressText.getText().toString();
        if (!isAddressValid(to)) {
            toInputLayout.setError(getString(R.string.error_invalid_address));
            inputValid = false;
        }
        final String amount = amountText.getText().toString();
        if (!isValidAmount(amount)) {
            amountInputLayout.setError(getString(R.string.error_invalid_amount));
            inputValid = false;
        }
        final String paymentId = paymentIdText.getText().toString();

        if (!inputValid) {
            return;
        }

        toInputLayout.setErrorEnabled(false);
        amountInputLayout.setErrorEnabled(false);

        BigInteger amountInSubunits = BalanceUtils.baseToSubunit(amount, decimals);
        Timber.d("onNext");
        viewModel.openConfirmation(this, to, paymentId, amountInSubunits, contractAddress, decimals, symbol, sendingTokens);
    }

    boolean isAddressValid(String address) {
        if (address == null || address.length() != 95) {
            return false;
        }
        return true;
    }

    boolean isValidAmount(String hlc) {
        try {
            String wei = BalanceUtils.EthToWei(hlc);
            return wei != null;
        } catch (Exception e) {
            return false;
        }
    }
}
