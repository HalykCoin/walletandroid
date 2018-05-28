package org.halykcoin.wallet.di;

import org.halykcoin.wallet.repository.HalykRepositoryType;
import org.halykcoin.wallet.viewmodel.ReferralViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ReferralModule {

    @Provides
    ReferralViewModelFactory provideReferalViewModelFactory(HalykRepositoryType repository) {
        return new ReferralViewModelFactory(repository);
    }

}
