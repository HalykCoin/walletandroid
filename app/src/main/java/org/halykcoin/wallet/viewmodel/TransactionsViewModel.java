package org.halykcoin.wallet.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.Uri;

import org.halykcoin.wallet.entity.HalykBalance;
import org.halykcoin.wallet.entity.HalykResponseTyped;
import org.halykcoin.wallet.entity.HalykTransaction;
import org.halykcoin.wallet.entity.HalykTransactionsInOut;
import org.halykcoin.wallet.entity.NetworkInfo;
import org.halykcoin.wallet.entity.Transaction;
import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.interact.FetchTransactionsInteract;
import org.halykcoin.wallet.interact.FindDefaultNetworkInteract;
import org.halykcoin.wallet.interact.FindDefaultWalletInteract;
import org.halykcoin.wallet.interact.GetDefaultWalletBalance;
import org.halykcoin.wallet.repository.HalykRepositoryType;
import org.halykcoin.wallet.router.ExternalBrowserRouter;
import org.halykcoin.wallet.router.ManageWalletsRouter;
import org.halykcoin.wallet.router.MyAddressRouter;
import org.halykcoin.wallet.router.MyTokensRouter;
import org.halykcoin.wallet.router.SendRouter;
import org.halykcoin.wallet.router.SettingsRouter;
import org.halykcoin.wallet.router.TransactionDetailRouter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class TransactionsViewModel extends BaseViewModel {
    private static final long GET_BALANCE_INTERVAL = 3;
    private static final long FETCH_TRANSACTIONS_INTERVAL = 5;
    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<HalykTransaction[]> transactions = new MutableLiveData<>();
    private final MutableLiveData<Map<String, String>> defaultWalletBalance = new MutableLiveData<>();
    private final MutableLiveData<HalykBalance> balance = new MutableLiveData<>();

    private final FindDefaultNetworkInteract findDefaultNetworkInteract;
    private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final GetDefaultWalletBalance getDefaultWalletBalance;
    private final FetchTransactionsInteract fetchTransactionsInteract;

    private final ManageWalletsRouter manageWalletsRouter;
    private final SettingsRouter settingsRouter;
    private final SendRouter sendRouter;
    private final TransactionDetailRouter transactionDetailRouter;
    private final MyAddressRouter myAddressRouter;
    private final MyTokensRouter myTokensRouter;
    private final ExternalBrowserRouter externalBrowserRouter;
    private HalykRepositoryType halykRepository;
    private Disposable balanceDisposable;
    private Disposable transactionDisposable;

    TransactionsViewModel(
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            FetchTransactionsInteract fetchTransactionsInteract,
            GetDefaultWalletBalance getDefaultWalletBalance,
            ManageWalletsRouter manageWalletsRouter,
            SettingsRouter settingsRouter,
            SendRouter sendRouter,
            TransactionDetailRouter transactionDetailRouter,
            MyAddressRouter myAddressRouter,
            MyTokensRouter myTokensRouter,
            ExternalBrowserRouter externalBrowserRouter,
            HalykRepositoryType repository) {
        this.findDefaultNetworkInteract = findDefaultNetworkInteract;
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.getDefaultWalletBalance = getDefaultWalletBalance;
        this.fetchTransactionsInteract = fetchTransactionsInteract;
        this.manageWalletsRouter = manageWalletsRouter;
        this.settingsRouter = settingsRouter;
        this.sendRouter = sendRouter;
        this.transactionDetailRouter = transactionDetailRouter;
        this.myAddressRouter = myAddressRouter;
        this.myTokensRouter = myTokensRouter;
        this.externalBrowserRouter = externalBrowserRouter;
        this.halykRepository = repository;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<HalykTransaction[]> transactions() {
        return transactions;
    }

    public LiveData<HalykBalance> balance() {
        return balance;
    }

    public LiveData<Map<String, String>> defaultWalletBalance() {
        return defaultWalletBalance;
    }

    public void prepare() {
        NetworkInfo ni = new NetworkInfo(
                "Halykcoin",
                "HLC",
                "",
                "",
                "",
                0,
                true
        );
        defaultNetwork.postValue(ni);
        onDefaultNetwork(ni);
    }

    public void fetchTransactions() {
        progress.postValue(true);
        halykRepository.getTransactions()
                .subscribe(this::onHalykTransactions, this::onTransactionsError);
    }

    private void onTransactionsError(Throwable throwable) {
        progress.postValue(false);
        this.onError(throwable);
        prepareBalance();
    }

    public void fetchBalance() {
        halykRepository.getBalance()
                .subscribe(this::onHalykBalance, this::onBalanceError);
    }

    private void onBalanceError(Throwable throwable) {
        this.onError(throwable);
    }

    private void onHalykBalance(HalykResponseTyped<HalykBalance> halykBalanceResponse) {
        HalykBalance balance = halykBalanceResponse.getData();
        this.balance.postValue(balance);
    }

    private void onHalykTransactions(HalykResponseTyped<HalykTransactionsInOut> halykTransactionsResponse) {
        progress.postValue(false);
        if (!halykTransactionsResponse.isSuccess()) {
            onError(new Throwable("Get Transactions error."));
        }
        if (halykTransactionsResponse.getData() != null) {
            HalykTransactionsInOut transactionsInOut = halykTransactionsResponse.getData();
            List<HalykTransaction> allTransactions = new ArrayList<>();
            if (transactionsInOut.getIn() != null) {
                allTransactions.addAll(transactionsInOut.getIn());
                Timber.d("onSuccess = " + transactionsInOut.getIn());
            }
            if (transactionsInOut.getOut() != null) {
                allTransactions.addAll(transactionsInOut.getOut());
                Timber.d("onSuccess = " + transactionsInOut.getOut());
            }
            if (transactionsInOut.getPending() != null) {
                allTransactions.addAll(transactionsInOut.getPending());
                Timber.d("onSuccess = " + transactionsInOut.getPending());
            }
            HalykTransaction[] transactionsArray = new HalykTransaction[allTransactions.size()];
            allTransactions.toArray(transactionsArray);
            Timber.d("onSuccess response allTransactions.size = " + allTransactions.size());
            onTransactions(transactionsArray);
        }
        prepareBalance();
    }

    private void onDefaultNetwork(NetworkInfo networkInfo) {
        defaultNetwork.postValue(networkInfo);
        fetchTransactions();
    }

    private void prepareBalance() {
        fetchBalance();
    }

    private void onDefaultWallet(Wallet wallet) {
        Timber.d("onDefaultWallet");
        defaultWallet.setValue(wallet);
    }

    private void onTransactions(HalykTransaction[] transactions) {
        progress.postValue(false);
        this.transactions.postValue(transactions);
    }

    public void showWallets(Context context) {
        manageWalletsRouter.open(context, false);
    }

    public void showSettings(Context context) {
        settingsRouter.open(context);
    }

    public void showSend(Context context) { sendRouter.open(context); }

    public void showDetails(Context context, Transaction transaction) {
        transactionDetailRouter.open(context, transaction);
    }

    public void showMyAddress(Context context) {
        myAddressRouter.open(context, defaultWallet.getValue());
    }

    public void showTokens(Context context) {
        myTokensRouter.open(context, defaultWallet.getValue());
    }

    public void openDeposit(Context context, Uri uri) {
        externalBrowserRouter.open(context, uri);
    }
}
