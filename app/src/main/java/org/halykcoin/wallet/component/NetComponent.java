package org.halykcoin.wallet.component;

import android.app.Activity;

import org.halykcoin.wallet.module.AppModule;
import org.halykcoin.wallet.module.NetModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Maestro on 07.01.2018.
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(Activity activity);
}
