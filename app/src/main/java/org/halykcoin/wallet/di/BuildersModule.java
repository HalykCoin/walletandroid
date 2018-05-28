package org.halykcoin.wallet.di;

import org.halykcoin.wallet.ui.AddTokenActivity;
import org.halykcoin.wallet.ui.ConfirmationActivity;
import org.halykcoin.wallet.ui.GasSettingsActivity;
import org.halykcoin.wallet.ui.ImportWalletActivity;
import org.halykcoin.wallet.ui.MyAddressActivity;
import org.halykcoin.wallet.ui.ReferralActivity;
import org.halykcoin.wallet.ui.SendActivity;
import org.halykcoin.wallet.ui.SettingsActivity;
import org.halykcoin.wallet.ui.SplashActivity;
import org.halykcoin.wallet.ui.TokensActivity;
import org.halykcoin.wallet.ui.TransactionDetailActivity;
import org.halykcoin.wallet.ui.TransactionsActivity;
import org.halykcoin.wallet.ui.WalletsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BuildersModule {
	@ActivityScope
	@ContributesAndroidInjector(modules = SplashModule.class)
	abstract SplashActivity bindSplashModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = AccountsManageModule.class)
	abstract WalletsActivity bindManageWalletsModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = ImportModule.class)
	abstract ImportWalletActivity bindImportWalletModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = TransactionsModule.class)
	abstract TransactionsActivity bindTransactionsModule();

    @ActivityScope
    @ContributesAndroidInjector(modules = TransactionDetailModule.class)
    abstract TransactionDetailActivity bindTransactionDetailModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = SettingsModule.class)
	abstract SettingsActivity bindSettingsModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = SendModule.class)
	abstract SendActivity bindSendModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = {ConfirmationModule.class})
	abstract ConfirmationActivity bindConfirmationModule();
    @ContributesAndroidInjector
	abstract MyAddressActivity bindMyAddressModule();

	@ActivityScope
    @ContributesAndroidInjector(modules = TokensModule.class)
	abstract TokensActivity bindTokensModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = GasSettingsModule.class)
	abstract GasSettingsActivity bindGasSettingsModule();

	@ActivityScope
    @ContributesAndroidInjector(modules = AddTokenModule.class)
	abstract AddTokenActivity bindAddTokenActivity();

	@ActivityScope
	@ContributesAndroidInjector(modules = ReferralModule.class)
	abstract ReferralActivity bindReferalActivity();
}
