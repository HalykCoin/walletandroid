package org.halykcoin.wallet.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import org.halykcoin.wallet.R;
import org.halykcoin.wallet.entity.ErrorEnvelope;
import org.halykcoin.wallet.repository.PreferenceRepositoryType;
import org.halykcoin.wallet.ui.widget.OnImportKeystoreListener;
import org.halykcoin.wallet.ui.widget.adapter.TabPagerAdapter;
import org.halykcoin.wallet.viewmodel.ImportWalletViewModel;
import org.halykcoin.wallet.viewmodel.ImportWalletViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ImportWalletActivity extends BaseActivity implements OnImportKeystoreListener {

    private static final int KEYSTORE_FORM_INDEX = 0;
    private static final int PRIVATE_KEY_FORM_INDEX = 1;

    private final List<Pair<String, Fragment>> pages = new ArrayList<>();

    @Inject
    ImportWalletViewModelFactory importWalletViewModelFactory;
    ImportWalletViewModel importWalletViewModel;

    @Inject
    protected PreferenceRepositoryType preferenceRepositoryType;

    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_import_wallet);
        toolbar();

        pages.add(KEYSTORE_FORM_INDEX, new Pair<>(getString(R.string.tab_keystore), ImportKeystoreFragment.create()));
//        pages.add(PRIVATE_KEY_FORM_INDEX, new Pair<>(getString(R.string.tab_private_key), ImportPrivateKeyFragment.create()));
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), pages));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setVisibility(View.GONE);
        tabLayout.setupWithViewPager(viewPager);

        importWalletViewModel = ViewModelProviders.of(this, importWalletViewModelFactory)
                .get(ImportWalletViewModel.class);
        importWalletViewModel.error().observe(this, this::onError);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((ImportKeystoreFragment) pages.get(KEYSTORE_FORM_INDEX).second)
                .setOnImportKeystoreListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        hideDialog();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void onError(ErrorEnvelope errorEnvelope) {
        hideDialog();
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.title_dialog_error)
                .setMessage(R.string.error_import)
                .setPositiveButton(R.string.ok, null)
                .create();
        dialog.show();
    }

    private void onProgress(boolean shouldShowProgress) {
        hideDialog();
        if (shouldShowProgress) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.title_dialog_handling)
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

    @Override
    public void onKeystore(String keystore, String password) {
        preferenceRepositoryType.setStoredId(keystore);
        Intent result = new Intent();
        setResult(RESULT_OK, result);
        finish();
    }
}
