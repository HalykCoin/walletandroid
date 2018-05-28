package org.halykcoin.wallet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.halykcoin.wallet.R;
import org.halykcoin.wallet.ui.barcode.BarcodeCaptureActivity;
import org.halykcoin.wallet.ui.widget.OnImportKeystoreListener;
import org.halykcoin.wallet.util.BalanceUtils;
import org.halykcoin.wallet.util.QRURLParser;

import java.util.Map;

import timber.log.Timber;

import static org.halykcoin.wallet.ui.barcode.BarcodeCaptureActivity.BARCODE_READER_REQUEST_CODE;


public class ImportKeystoreFragment extends Fragment implements View.OnClickListener {

    private static final OnImportKeystoreListener dummyOnImportKeystoreListener = (k, p) -> {};

    private EditText keystore;
    private OnImportKeystoreListener onImportKeystoreListener;

    public static ImportKeystoreFragment create() {
        return new ImportKeystoreFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_import_keystore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        keystore = view.findViewById(R.id.keystore);
        view.findViewById(R.id.import_action).setOnClickListener(this);
        view.findViewById(R.id.scan_qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQRCode();
            }
        });
    }

    private void scanQRCode() {
        Intent intent = new Intent(getContext(), BarcodeCaptureActivity.class);
        startActivityForResult(intent, BarcodeCaptureActivity.BARCODE_READER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    QRURLParser parser = QRURLParser.getInstance();
                    String key = parser.extractAddressFromQrString(barcode.displayValue);
                    if (key == null) {
                        Toast.makeText(getActivity(), R.string.toast_qr_code_no_data, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    keystore.setText(key);
                }
            } else {
                Timber.e(getString(R.string.barcode_error_format,
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        this.keystore.setError(null);
        String keystore = this.keystore.getText().toString();
        String password = "";
        if (TextUtils.isEmpty(keystore)) {
            this.keystore.setError(getString(R.string.error_field_required));
        } else if ( keystore.length() != 255) {
            this.keystore.setError(getString(R.string.incorrect_private_key_error));
        }else {
            onImportKeystoreListener.onKeystore(keystore, password);
        }
    }

    public void setOnImportKeystoreListener(OnImportKeystoreListener onImportKeystoreListener) {
        this.onImportKeystoreListener = onImportKeystoreListener == null
            ? dummyOnImportKeystoreListener
            : onImportKeystoreListener;
    }
}
