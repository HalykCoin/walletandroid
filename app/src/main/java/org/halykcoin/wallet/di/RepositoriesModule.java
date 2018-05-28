package org.halykcoin.wallet.di;

import android.content.Context;

import com.google.gson.Gson;
import org.halykcoin.wallet.repository.EthereumNetworkRepository;
import org.halykcoin.wallet.repository.EthereumNetworkRepositoryType;
import org.halykcoin.wallet.repository.HalykRepository;
import org.halykcoin.wallet.repository.HalykRepositoryType;
import org.halykcoin.wallet.repository.PreferenceRepositoryType;
import org.halykcoin.wallet.repository.RealmTokenSource;
import org.halykcoin.wallet.repository.SharedPreferenceRepository;
import org.halykcoin.wallet.repository.TokenLocalSource;
import org.halykcoin.wallet.repository.TokenRepository;
import org.halykcoin.wallet.repository.TokenRepositoryType;
import org.halykcoin.wallet.repository.TransactionInMemorySource;
import org.halykcoin.wallet.repository.TransactionLocalSource;
import org.halykcoin.wallet.repository.TransactionRepository;
import org.halykcoin.wallet.repository.TransactionRepositoryType;
import org.halykcoin.wallet.repository.WalletRepository;
import org.halykcoin.wallet.repository.WalletRepositoryType;
import org.halykcoin.wallet.service.AccountKeystoreService;
import org.halykcoin.wallet.service.BlockExplorerClient;
import org.halykcoin.wallet.service.BlockExplorerClientType;
import org.halykcoin.wallet.service.EthplorerTokenService;
import org.halykcoin.wallet.service.GethKeystoreAccountService;
import org.halykcoin.wallet.service.HalykRetrofitClient;
import org.halykcoin.wallet.service.HalykRetrofitClientType;
import org.halykcoin.wallet.service.TickerService;
import org.halykcoin.wallet.service.TokenExplorerClientType;
import org.halykcoin.wallet.service.TrustWalletTickerService;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class RepositoriesModule {
	@Singleton
	@Provides
	PreferenceRepositoryType providePreferenceRepository(Context context) {
		return new SharedPreferenceRepository(context);
	}

	@Singleton
	@Provides
	AccountKeystoreService provideAccountKeyStoreService(Context context) {
        File file = new File(context.getFilesDir(), "keystore/keystore");
		return new GethKeystoreAccountService(file);
	}

	@Singleton
    @Provides
    TickerService provideTickerService(OkHttpClient httpClient, Gson gson) {
	    return new TrustWalletTickerService(httpClient, gson);
    }

	@Singleton
	@Provides
	EthereumNetworkRepositoryType provideEthereumNetworkRepository(
            PreferenceRepositoryType preferenceRepository,
            TickerService tickerService) {
		return new EthereumNetworkRepository(preferenceRepository, tickerService);
	}

	@Singleton
	@Provides
    WalletRepositoryType provideWalletRepository(
            OkHttpClient okHttpClient,
			PreferenceRepositoryType preferenceRepositoryType,
			AccountKeystoreService accountKeystoreService,
			EthereumNetworkRepositoryType networkRepository) {
		return new WalletRepository(
		        okHttpClient, preferenceRepositoryType, accountKeystoreService, networkRepository);
	}

	@Singleton
	@Provides
	TransactionRepositoryType provideTransactionRepository(
			EthereumNetworkRepositoryType networkRepository,
			AccountKeystoreService accountKeystoreService,
			BlockExplorerClientType blockExplorerClient) {
		TransactionLocalSource inMemoryCache = new TransactionInMemorySource();
		TransactionLocalSource inDiskCache = null;
		return new TransactionRepository(
				networkRepository,
				accountKeystoreService,
				inMemoryCache,
				inDiskCache,
				blockExplorerClient);
	}

	@Singleton
	@Provides
	BlockExplorerClientType provideBlockExplorerClient(
			OkHttpClient httpClient,
			Gson gson,
			EthereumNetworkRepositoryType ethereumNetworkRepository) {
		return new BlockExplorerClient(httpClient, gson, ethereumNetworkRepository);
	}

	@Singleton
    @Provides
    TokenRepositoryType provideTokenRepository(
            OkHttpClient okHttpClient,
            EthereumNetworkRepositoryType ethereumNetworkRepository,
            TokenExplorerClientType tokenExplorerClientType,
            TokenLocalSource tokenLocalSource) {
	    return new TokenRepository(
	            okHttpClient,
	            ethereumNetworkRepository,
	            tokenExplorerClientType,
                tokenLocalSource);
    }

	@Singleton
    @Provides
    TokenExplorerClientType provideTokenService(OkHttpClient okHttpClient, Gson gson) {
	    return new EthplorerTokenService(okHttpClient, gson);
    }

    @Singleton
    @Provides
    TokenLocalSource provideRealmTokenSource() {
	    return new RealmTokenSource();
    }

	@Singleton
	@Provides
	HalykRepositoryType provideHalykRepository(
			HalykRetrofitClientType halykClientTypee) {
		return new HalykRepository(
				halykClientTypee);
	}

	@Singleton
	@Provides
	HalykRetrofitClientType provideHalykApiService(OkHttpClient okHttpClient, Gson gson, Context context) {
		return new HalykRetrofitClient(okHttpClient, gson, context);
	}

}
