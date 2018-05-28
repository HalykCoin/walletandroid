package org.halykcoin.wallet.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.repository.PreferenceRepositoryType;
import org.halykcoin.wallet.repository.SharedPreferenceRepository;
import org.halykcoin.wallet.router.ManageWalletsRouter;
import org.halykcoin.wallet.router.TransactionsRouter;
import org.halykcoin.wallet.viewmodel.SplashViewModel;
import org.halykcoin.wallet.viewmodel.SplashViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SplashActivity extends AppCompatActivity {

    @Inject
    SplashViewModelFactory splashViewModelFactory;
    SplashViewModel splashViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics.Builder()
//                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

        splashViewModel = ViewModelProviders.of(this, splashViewModelFactory)
                .get(SplashViewModel.class);
        splashViewModel.wallets().observe(this, this::onWallets);
    }

    private void onWallets(Wallet[] wallets) {
        // Start home activity
        PreferenceRepositoryType preferenceRepositoryType = new SharedPreferenceRepository(this);
        if (preferenceRepositoryType.isIdAvailable() ) {
            new TransactionsRouter().open(this, true);
        } else {
            new ManageWalletsRouter().open(this, true);
        }
    }

}
