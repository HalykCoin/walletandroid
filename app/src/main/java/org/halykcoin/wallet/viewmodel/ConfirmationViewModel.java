package org.halykcoin.wallet.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.halykcoin.wallet.entity.GasSettings;
import org.halykcoin.wallet.entity.HalykResponseTyped;
import org.halykcoin.wallet.entity.HalykTransaction;
import org.halykcoin.wallet.entity.HalykTransferDetails;
import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.interact.CreateTransactionInteract;
import org.halykcoin.wallet.interact.FetchGasSettingsInteract;
import org.halykcoin.wallet.interact.FindDefaultWalletInteract;
import org.halykcoin.wallet.repository.HalykRepositoryType;
import org.halykcoin.wallet.router.GasSettingsRouter;

import java.math.BigInteger;

public class ConfirmationViewModel extends BaseViewModel {
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<GasSettings> gasSettings = new MutableLiveData<>();
    private final MutableLiveData<HalykTransferDetails> newTransaction = new MutableLiveData<>();

    private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final FetchGasSettingsInteract fetchGasSettingsInteract;
    private final CreateTransactionInteract createTransactionInteract;

    private final GasSettingsRouter gasSettingsRouter;
    private HalykRepositoryType halykRepository;

    public ConfirmationViewModel(FindDefaultWalletInteract findDefaultWalletInteract,
                                 FetchGasSettingsInteract fetchGasSettingsInteract,
                                 CreateTransactionInteract createTransactionInteract,
                                 GasSettingsRouter gasSettingsRouter,
                                 HalykRepositoryType repository) {
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.fetchGasSettingsInteract = fetchGasSettingsInteract;
        this.createTransactionInteract = createTransactionInteract;
        this.gasSettingsRouter = gasSettingsRouter;
        halykRepository = repository;
    }

    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    public MutableLiveData<GasSettings> gasSettings() {
        return gasSettings;
    }

    public LiveData<HalykTransferDetails> sendTransaction() { return newTransaction; }

    public void prepare() {

    }

    private void onGasSettings(GasSettings gasSettings) {
        this.gasSettings.setValue(gasSettings);
    }

    public void openGasSettings(Activity context) {
        gasSettingsRouter.open(context, gasSettings.getValue());
    }

    private void onTransferError(Throwable throwable) {
        progress.postValue(false);
        this.onError(throwable);
    }

    private void onTransferSuccess(HalykResponseTyped<HalykTransferDetails> response) {
        progress.postValue(false);
        HalykTransferDetails transferDetails = response.getData();
        newTransaction.postValue(transferDetails);
    }

    public void transfer(BigInteger amount, String toAddress, String paymentId) {
        progress.postValue(true);
        HalykTransaction transaction = new HalykTransaction(amount, toAddress, paymentId);
        halykRepository.transfer(transaction)
                .subscribe(this::onTransferSuccess, this::onTransferError);
    }
}
