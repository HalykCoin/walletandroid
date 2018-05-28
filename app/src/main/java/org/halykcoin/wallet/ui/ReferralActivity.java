package org.halykcoin.wallet.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.javiersantos.piracychecker.PiracyChecker;
import com.github.javiersantos.piracychecker.enums.PiracyCheckerCallback;
import com.github.javiersantos.piracychecker.enums.PiracyCheckerError;
import com.github.javiersantos.piracychecker.enums.PirateApp;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.halykcoin.wallet.R;
import org.halykcoin.wallet.entity.ErrorEnvelope;
import org.halykcoin.wallet.ui.barcode.BarcodeCaptureActivity;
import org.halykcoin.wallet.viewmodel.ReferralViewModel;
import org.halykcoin.wallet.viewmodel.ReferralViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

import static org.halykcoin.wallet.ui.barcode.BarcodeCaptureActivity.BARCODE_READER_REQUEST_CODE;

public class ReferralActivity extends BaseActivity {

    @Inject
    ReferralViewModelFactory mReferralViewModelFactory;
    private ReferralViewModel viewModel;
    private PiracyChecker mPiracyChecker;

    private Button mScanQRButton;
    private TextView mCodeTextView;
    private ProgressBar mReferralProgress;
    private TextView mReferralStatus;
    private ViewGroup mQRCodeBlock;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidInjection.inject(this);

        setContentView(R.layout.activity_referal);

        toolbar();

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        if (data != null) {
            Timber.d("onCreate " + data.getHost());
        }

        viewModel = ViewModelProviders.of(this, mReferralViewModelFactory)
                .get(ReferralViewModel.class);
        mPiracyChecker = new PiracyChecker(this)
                .enableGooglePlayLicensing("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgQXuvkihMHyH6cN6DKOjjlG0YSrfSLRA4LtVJWfX9buYgMm+wd808AKE1Ybqm+d/PLGT8zowhkx9uUEnzMtXsmdba6Ly7zfnTvwXxvF5W0s7iJCAM1THM3KHuKkGZMz2vslkBX9gVwqv6+PmwFS5v2rk21iePxlE7cyZGctDwqUt6mbpD89zqWpkywD2MVMC5yyaeTj7t6DOPl8NJYJQjqMRW/4nqVMM+UocXXaLdSpKPJBmstSstOY/0NdrIcWAWIRr49zBHZkFK948eEB2y4nriCYlSHgTnDt3JjJS7EE85cU7ekXHulMBkoAmJPm8VtIV0MnUY1Z2yJ8ov29WHwIDAQAB")
                .enableEmulatorCheck(false)
                .callback(new PiracyCheckerCallback() {
                    @Override
                    public void allow() {
                        Timber.d("PiracyChecker allow");
                    }

                    @Override
                    public void dontAllow(@NonNull PiracyCheckerError piracyCheckerError, @Nullable PirateApp pirateApp) {
                        Timber.d("PiracyChecker allow " + piracyCheckerError.toString());
                    }
                });
        mPiracyChecker.start();

        mScanQRButton = findViewById(R.id.referral_scan_qr);
        mScanQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });
        mCodeTextView = findViewById(R.id.referral_code);
        mReferralProgress = findViewById(R.id.referral_progress);
        mReferralStatus = findViewById(R.id.referral_status_text);
        mQRCodeBlock = findViewById(R.id.referral_qr_code_block);

        mReferralProgress.setVisibility(View.VISIBLE);
        viewModel.progress().observe(this, this::progress);
        viewModel.error().observe(this, this::onError);
        viewModel.requestReferal();
    }

    private void onError(ErrorEnvelope errorEnvelope) {
        mReferralStatus.setVisibility(View.VISIBLE);
        mReferralStatus.setText(R.string.referral_activate_error);
    }

    private void progress(boolean show) {
        Timber.d("progress " + show);
        int visibility = (show) ? View.VISIBLE : View.GONE;
        mReferralProgress.setVisibility(visibility);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPiracyChecker.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    mCodeTextView.setText(barcode.displayValue);
                }
            } else {
                Timber.e(getString(R.string.barcode_error_format,
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
