package org.halykcoin.wallet.ui;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.halykcoin.wallet.R;
import org.halykcoin.wallet.entity.ErrorEnvelope;
import org.halykcoin.wallet.entity.HalykResponse;
import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.repository.PreferenceRepositoryType;
import org.halykcoin.wallet.repository.SharedPreferenceRepository;
import org.halykcoin.wallet.service.HalykHttpCallback;
import org.halykcoin.wallet.service.HalykRestClient;
import org.halykcoin.wallet.ui.widget.adapter.WalletsAdapter;
import org.halykcoin.wallet.viewmodel.WalletsViewModel;
import org.halykcoin.wallet.viewmodel.WalletsViewModelFactory;
import org.halykcoin.wallet.widget.AddWalletView;
import org.halykcoin.wallet.widget.BackupView;
import org.halykcoin.wallet.widget.BackupWarningView;
import org.halykcoin.wallet.widget.SystemView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

import static org.halykcoin.wallet.C.IMPORT_REQUEST_CODE;
import static org.halykcoin.wallet.C.SHARE_REQUEST_CODE;

public class WalletsActivity extends BaseActivity implements
		View.OnClickListener,
        AddWalletView.OnNewWalletClickListener,
        AddWalletView.OnImportWalletClickListener {

	@Inject
    WalletsViewModelFactory walletsViewModelFactory;
	WalletsViewModel viewModel;

	@Inject
	protected PreferenceRepositoryType preferenceRepositoryType;

	private WalletsAdapter adapter;

	private SystemView systemView;
    private BackupWarningView backupWarning;
    private Dialog dialog;
    private boolean isSetDefault;
    private HalykRestClient mHalykRestClitnt;

    @Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		AndroidInjection.inject(this);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_wallets);
		// Init toolbar
		toolbar();
		mHalykRestClitnt = HalykRestClient.getInstance();

		adapter = new WalletsAdapter(this::onSetWalletDefault, this::onDeleteWallet, this::onExportWallet);
		SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh_layout);
		systemView = findViewById(R.id.system_view);
		backupWarning = findViewById(R.id.backup_warning);

		RecyclerView list = findViewById(R.id.list);

		list.setLayoutManager(new LinearLayoutManager(this));
		list.setAdapter(adapter);

		systemView.attachRecyclerView(list);
		systemView.attachSwipeRefreshLayout(refreshLayout);
		backupWarning.setOnPositiveClickListener(this::onNowBackup);
		backupWarning.setOnNegativeClickListener(this::onLaterBackup);

		viewModel = ViewModelProviders.of(this, walletsViewModelFactory)
				.get(WalletsViewModel.class);

		viewModel.error().observe(this, this::onError);
		viewModel.progress().observe(this, systemView::showProgress);
		viewModel.wallets().observe(this, this::onFetchWallet);
		viewModel.defaultWallet().observe(this, this::onChangeDefaultWallet);
		viewModel.createdWallet().observe(this, this::onCreatedWallet);
		viewModel.exportedStore().observe(this, this::openShareDialog);

		refreshLayout.setOnRefreshListener(viewModel::fetchWallets);
	}

	private void createWallet() {
		systemView.showProgress(true);
		mHalykRestClitnt.createWallet(new HalykHttpCallback() {
			@Override
			public void onSuccess(String response) {
				HalykResponse walletResponse = HalykResponse.fromString(response);
				String key = walletResponse.getData().toString();
				PreferenceRepositoryType preferenceRepositoryType = new SharedPreferenceRepository(WalletsActivity.this);
				preferenceRepositoryType.setStoredId(key);
				getAddress(true);
			}

			@Override
			public void onError(int code) {
				Timber.d("createWallet onError code = " + code);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						systemView.showProgress(false);
					}
				});
			}
		});
	}

	private void progress(boolean show) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				systemView.showProgress(show);
			}
		});
	}


	private void getAddress(boolean showBackup) {

		mHalykRestClitnt.getAddress(WalletsActivity.this, new HalykHttpCallback() {
			@Override
			public void onSuccess(String response) {
				progress(false);
				HalykResponse walletResponse = HalykResponse.fromString(response);
				if (walletResponse == null || !walletResponse.isSuccess()) {
					progress(false);
					Snackbar.make(systemView, R.string.error_create_wallet, Snackbar.LENGTH_INDEFINITE)
							.setAction(getString(R.string.try_again), new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									getAddress(showBackup);
								}
							})
							.show();
					return;
				}
				Timber.d("onSuccess get address: " + response);
				if (walletResponse.getData() == null) {
					return;
				}
				String address = walletResponse.getData().toString();
				PreferenceRepositoryType preferenceRepositoryType = new SharedPreferenceRepository(WalletsActivity.this);
				preferenceRepositoryType.setCurrentWalletAddress(address);
				Wallet wallet = new Wallet(address);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onCreatedWallet(wallet, showBackup);
					}
				});
			}

			@Override
			public void onError(int code) {
				Timber.d("getAddress onError code = " + code);
				progress(false);
			}
		});
	}

    private void onExportWallet(Wallet wallet) {
		shareKey();
    }

    @Override
	protected void onPause() {
		super.onPause();

		hideDialog();
	}

	@Override
	public void onBackPressed() {
//		// User can't start work without wallet.
//		if (preferenceRepositoryType.getStoredId() == null || preferenceRepositoryType.getStoredId().isEmpty()) {
//			viewModel.showTransactions(this);
//		} else {
//			finish();
//			System.exit(0);
//		}

		super.onBackPressed();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	    if (adapter.getItemCount() > 0) {
            getMenuInflater().inflate(R.menu.menu_add, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
            case R.id.action_add: {
                onAddWallet();
            } break;
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == IMPORT_REQUEST_CODE) {
			showToolbar();
		    if (resultCode == RESULT_OK) {
                Snackbar.make(systemView, getString(R.string.toast_message_wallet_imported), Snackbar.LENGTH_SHORT)
                        .show();
                progress(true);
				getAddress(false);
				viewModel.showTransactions(this);
			}
		} else if (requestCode == SHARE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(systemView, getString(R.string.toast_message_wallet_exported), Snackbar.LENGTH_SHORT)
                        .show();
                backupWarning.hide();
                showToolbar();
                hideDialog();
                if (adapter.getItemCount() <= 1) {
                    onBackPressed();
                }
            } else {
                dialog = buildDialog()
                        .setMessage(R.string.do_manage_make_backup)
                        .setPositiveButton(R.string.yes_continue, (dialog, which) -> {
                            hideDialog();
                            backupWarning.hide();
                            showToolbar();
                            if (adapter.getItemCount() <= 1) {
                                onBackPressed();
                            }
                        })
                        .setNegativeButton(R.string.no_repeat,
                                (dialog, which) -> {
                                    openShareDialog(viewModel.exportedStore().getValue());
                                    hideDialog();
                                })
                        .create();
                dialog.show();
            }
        }
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.try_again: {
				viewModel.fetchWallets();
			} break;
		}
	}

	@Override
	public void onNewWallet(View view) {
		hideDialog();
		createWallet();
//		viewModel.newWallet();
	}

	@Override
	public void onImportWallet(View view) {
		hideDialog();
		viewModel.importWallet(this);
	}

	private void onAddWallet() {
		AddWalletView addWalletView = new AddWalletView(this);
		addWalletView.setOnNewWalletClickListener(this);
		addWalletView.setOnImportWalletClickListener(this);
		dialog = new BottomSheetDialog(this);
		dialog.setContentView(addWalletView);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	private void onChangeDefaultWallet(Wallet wallet) {
        if (isSetDefault) {
            viewModel.showTransactions(this);
        } else {
            adapter.setDefaultWallet(wallet);
        }
	}

	private void onFetchWallet(Wallet[] wallets) {
		if (wallets == null || wallets.length == 0) {
			dissableDisplayHomeAsUp();
			AddWalletView addWalletView = new AddWalletView(this, R.layout.layout_empty_add_account);
			addWalletView.setOnNewWalletClickListener(this);
			addWalletView.setOnImportWalletClickListener(this);
			systemView.showEmpty(addWalletView);
			adapter.setWallets(new Wallet[0]);
            hideToolbar();
        } else {
			enableDisplayHomeAsUp();
			adapter.setWallets(wallets);
        }
		invalidateOptionsMenu();
	}

	private void onCreatedWallet(Wallet wallet, boolean showBackup) {
        hideToolbar();
        if (showBackup) {
			backupWarning.show(wallet);
		} else {
			showToolbar();
			viewModel.showTransactions(this);
		}
	}

	private void onCreatedWallet(Wallet wallet) {
		onCreatedWallet(wallet, true);
	}

	private void onLaterBackup(View view, Wallet wallet) {
        showNoBackupWarning(wallet);
    }

    private void onNowBackup(View view, Wallet wallet) {
		shareKey();
    }

	private void shareKey() {
		String key = preferenceRepositoryType.getStoredId();
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Keystore");
		sharingIntent.putExtra(Intent.EXTRA_TEXT, key);
		startActivityForResult(
				Intent.createChooser(sharingIntent, "Share via"),
				SHARE_REQUEST_CODE);
	}

    private void showNoBackupWarning(Wallet wallet) {
        dialog = buildDialog()
                .setTitle(getString(R.string.title_dialog_watch_out))
                .setMessage(getString(R.string.dialog_message_unrecoverable_message))
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton(R.string.i_understand, (dialog, whichButton) -> {
                    backupWarning.hide();
                    showToolbar();
					viewModel.showTransactions(this);
                })
                .setNegativeButton(android.R.string.cancel, null) //(dialog, whichButton) -> showBackupDialog(wallet, true)
                .create();
        dialog.show();
    }

    private void showBackupDialog(Wallet wallet, boolean isNew) {
	    BackupView view = new BackupView(this);
        dialog = buildDialog()
                .setView(view)
                .setPositiveButton(R.string.ok,
                        (dialogInterface, i) -> viewModel.exportWallet(wallet, view.getPassword()))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                    if (isNew) {
                        onCreatedWallet(wallet, true);
                    }
                })
                .create();
        dialog.show();
    }

    private void openShareDialog(String jsonData) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Keystore");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, jsonData);
        startActivityForResult(
                Intent.createChooser(sharingIntent, "Share via"),
                SHARE_REQUEST_CODE);
    }

	private void onError(ErrorEnvelope errorEnvelope) {
		systemView.showError(errorEnvelope.message, this);
	}

	private void onSetWalletDefault(Wallet wallet) {
		viewModel.setDefaultWallet(wallet);
		isSetDefault = true;
	}

	private void onDeleteWallet(Wallet wallet) {
		dialog = buildDialog()
				.setTitle(getString(R.string.title_delete_account))
				.setMessage(getString(R.string.confirm_delete_account))
				.setIcon(R.drawable.ic_warning_black_24dp)
				.setPositiveButton(android.R.string.yes, (dialog, btn) -> viewModel.deleteWallet(wallet))
				.setNegativeButton(android.R.string.no, null)
				.create();
		dialog.show();
	}

	private AlertDialog.Builder buildDialog() {
	    hideDialog();
	    return new AlertDialog.Builder(this);
    }

	private void hideDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}
}
