package org.halykcoin.wallet.interact;


import org.halykcoin.wallet.entity.GasSettings;
import org.halykcoin.wallet.repository.PreferenceRepositoryType;

public class FetchGasSettingsInteract {
    private final PreferenceRepositoryType repository;

    public FetchGasSettingsInteract(PreferenceRepositoryType repository) {
        this.repository = repository;
    }

    public GasSettings fetch() {
        return repository.getGasSettings();
    }

}
