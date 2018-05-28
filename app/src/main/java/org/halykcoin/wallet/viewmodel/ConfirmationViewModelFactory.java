package org.halykcoin.wallet.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.halykcoin.wallet.interact.CreateTransactionInteract;
import org.halykcoin.wallet.interact.FetchGasSettingsInteract;
import org.halykcoin.wallet.interact.FindDefaultWalletInteract;
import org.halykcoin.wallet.repository.HalykRepositoryType;
import org.halykcoin.wallet.router.GasSettingsRouter;

public class ConfirmationViewModelFactory implements ViewModelProvider.Factory {

    private FindDefaultWalletInteract findDefaultWalletInteract;
    private FetchGasSettingsInteract fetchGasSettingsInteract;
    private CreateTransactionInteract createTransactionInteract;
    private GasSettingsRouter gasSettingsRouter;
    private HalykRepositoryType halykRepository;

    public ConfirmationViewModelFactory(FindDefaultWalletInteract findDefaultWalletInteract,
                                        FetchGasSettingsInteract fetchGasSettingsInteract,
                                        CreateTransactionInteract createTransactionInteract,
                                        GasSettingsRouter gasSettingsRouter,
                                        HalykRepositoryType repository) {
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.fetchGasSettingsInteract = fetchGasSettingsInteract;
        this.createTransactionInteract = createTransactionInteract;
        this.gasSettingsRouter = gasSettingsRouter;
        this.halykRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ConfirmationViewModel(findDefaultWalletInteract, fetchGasSettingsInteract, createTransactionInteract, gasSettingsRouter, halykRepository);
    }
}
