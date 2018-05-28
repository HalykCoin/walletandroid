package org.halykcoin.wallet;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;


import com.github.stkent.bugshaker.BugShaker;
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType;

import org.halykcoin.wallet.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.realm.Realm;
import timber.log.Timber;

public class App extends MultiDexApplication implements HasActivityInjector {

	@Inject
	DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

	@Override
	public void onCreate() {
		super.onCreate();
        Realm.init(this);
        DaggerAppComponent
				.builder()
				.application(this)
				.build()
				.inject(this);
		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		}
		BugShaker.get(this)
				.setEmailAddresses("comradapp@gmail.com")   // required
				.setEmailSubjectLine("I found a bug!") // optional
				.setAlertDialogType(AlertDialogType.NATIVE) // optional
				.setIgnoreFlagSecure(true)                  // optional
				.assemble()                                 // required
				.start();                                   // required
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
	}

	@Override
	public AndroidInjector<Activity> activityInjector() {
		return dispatchingAndroidInjector;
	}

}
