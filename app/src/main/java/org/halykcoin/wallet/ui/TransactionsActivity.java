package org.halykcoin.wallet.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.halykcoin.wallet.C;
import org.halykcoin.wallet.R;
import org.halykcoin.wallet.entity.ErrorEnvelope;
import org.halykcoin.wallet.entity.HalykBalance;
import org.halykcoin.wallet.entity.HalykTransaction;
import org.halykcoin.wallet.entity.NetworkInfo;
import org.halykcoin.wallet.entity.Transaction;
import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.service.HalykRestClient;
import org.halykcoin.wallet.ui.widget.adapter.TransactionsAdapter;
import org.halykcoin.wallet.util.RootUtil;
import org.halykcoin.wallet.viewmodel.BaseNavigationActivity;
import org.halykcoin.wallet.viewmodel.TransactionsViewModel;
import org.halykcoin.wallet.viewmodel.TransactionsViewModelFactory;
import org.halykcoin.wallet.widget.DepositView;
import org.halykcoin.wallet.widget.EmptyTransactionsView;
import org.halykcoin.wallet.widget.SystemView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

public class TransactionsActivity extends BaseNavigationActivity implements View.OnClickListener {

    @Inject
    TransactionsViewModelFactory transactionsViewModelFactory;
    private TransactionsViewModel viewModel;

    private SystemView systemView;
    private TransactionsAdapter adapter;
    private Dialog dialog;
    private HalykRestClient mHalykRestClitnt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        mHalykRestClitnt = HalykRestClient.getInstance();

        setContentView(R.layout.activity_transactions);

        toolbar();
        setTitle(getString(R.string.unknown_balance_with_symbol));
        setSubtitle("");
        initBottomNavigation();
        dissableDisplayHomeAsUp();

        adapter = new TransactionsAdapter(this::onTransactionClick);
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh_layout);
        systemView = findViewById(R.id.system_view);

        RecyclerView list = findViewById(R.id.list);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        setBottomMenu(R.menu.menu_main_network);

        systemView.attachRecyclerView(list);
        systemView.attachSwipeRefreshLayout(refreshLayout);

        viewModel = ViewModelProviders.of(this, transactionsViewModelFactory)
                .get(TransactionsViewModel.class);
        viewModel.defaultNetwork().observe(this, this::onDefaultNetwork);
        viewModel.transactions().observe(this, this::onTransactions);
        viewModel.balance().observe(this, this::onBalanceChangedHLC);
        viewModel.error().observe(this, this::onError);
        viewModel.progress().observe(this, this::onProgress);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.fetchTransactions();
            }
        });
    }

    private void onProgress(Boolean show) {
        Timber.d("onProgress " + show);
        systemView.showProgress(show);
    }

    private void onError(ErrorEnvelope errorEnvelope) {
        if (errorEnvelope == null) {
            return;
        }
        systemView.showError(getString(R.string.error_fail_load_transaction), this);
    }

    private void onTransactionClick(View view, Transaction transaction) {
        viewModel.showDetails(view.getContext(), transaction);
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepare();
        checkRoot();
    }

    void prepare(){
        viewModel.prepare();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);

        NetworkInfo networkInfo = viewModel.defaultNetwork().getValue();
        if (networkInfo != null && networkInfo.symbol.equals(C.HLC_SYMBOL)) {
            //getMenuInflater().inflate(R.menu.menu_deposit, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                viewModel.showSettings(this);
            } break;
            case R.id.action_deposit: {
                openExchangeDialog();
            } break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.try_again: {
                viewModel.fetchTransactions();
            } break;
            case R.id.action_buy: {
                openExchangeDialog();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_my_address: {
                viewModel.showMyAddress(this);
                return true;
            }
            case R.id.action_send: {
                viewModel.showSend(this);
                return true;
            }
        }
        return false;
    }

    private void onBalanceChangedHLC(HalykBalance balance) {
        if (balance == null) {
            Timber.d("onBalanceChangedHLC == null");
            return;
        }
        setTitle(balance.getFormattedBalance() + " " + C.HLC_SYMBOL);
        setSubtitle(balance.getFormattedUnlockedBalance() + " " + C.HLC_SYMBOL);
    }

    private void onTransactions(HalykTransaction[] transaction) {
        if (transaction == null || transaction.length == 0) {
            Timber.d("onTransactions show empty");
            EmptyTransactionsView emptyView = new EmptyTransactionsView(this, this);
            emptyView.setNetworkInfo(viewModel.defaultNetwork().getValue());
            systemView.showEmpty(emptyView);
        }
        Timber.d("onTransactions transactions = " + transaction.length);
        adapter.clear();
        adapter.addTransactions(transaction);
        invalidateOptionsMenu();
    }


    private void onDefaultNetwork(NetworkInfo networkInfo) {
        adapter.setDefaultNetwork(networkInfo);

    }

    private void checkRoot() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (RootUtil.isDeviceRooted() && pref.getBoolean("should_show_root_warning", true)) {
            pref.edit().putBoolean("should_show_root_warning", false).apply();
            new AlertDialog.Builder(this)
                    .setTitle(R.string.root_title)
                    .setMessage(R.string.root_body)
                    .setNegativeButton(R.string.ok, (dialog, which) -> {
                    })
                    .show();
        }
    }

    private void openExchangeDialog() {
        Wallet wallet = viewModel.defaultWallet().getValue();
        if (wallet == null) {
            Toast.makeText(this, getString(R.string.error_wallet_not_selected), Toast.LENGTH_SHORT)
                    .show();
        } else {
            BottomSheetDialog dialog = new BottomSheetDialog(this);
            DepositView view = new DepositView(this, wallet);
            view.setOnDepositClickListener(this::onDepositClick);
            dialog.setContentView(view);
            dialog.show();
            this.dialog = dialog;
        }
    }

    private void onDepositClick(View view, Uri uri) {
        viewModel.openDeposit(this, uri);
    }
}
