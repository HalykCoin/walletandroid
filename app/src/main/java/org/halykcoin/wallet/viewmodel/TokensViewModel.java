package org.halykcoin.wallet.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import org.halykcoin.wallet.entity.NetworkInfo;
import org.halykcoin.wallet.entity.Token;
import org.halykcoin.wallet.entity.Wallet;
import org.halykcoin.wallet.interact.FetchTokensInteract;
import org.halykcoin.wallet.interact.FindDefaultNetworkInteract;
import org.halykcoin.wallet.router.AddTokenRouter;
import org.halykcoin.wallet.router.SendTokenRouter;

public class TokensViewModel extends BaseViewModel {
    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();
    private final MutableLiveData<Wallet> wallet = new MutableLiveData<>();
    private final MutableLiveData<Token[]> tokens = new MutableLiveData<>();

    private final FindDefaultNetworkInteract findDefaultNetworkInteract;
    private final FetchTokensInteract fetchTokensInteract;
    private final AddTokenRouter addTokenRouter;
    private final SendTokenRouter sendTokenRouter;

    TokensViewModel(
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            FetchTokensInteract fetchTokensInteract,
            AddTokenRouter addTokenRouter,
            SendTokenRouter sendTokenRouter) {
        this.findDefaultNetworkInteract = findDefaultNetworkInteract;
        this.fetchTokensInteract = fetchTokensInteract;
        this.addTokenRouter = addTokenRouter;
        this.sendTokenRouter = sendTokenRouter;
    }

    public void prepare() {
        progress.postValue(true);
        findDefaultNetwork();
    }

    private void findDefaultNetwork() {
        disposable = findDefaultNetworkInteract
                .find()
                .subscribe(this::onDefaultNetwork, this::onError);
    }

    private void onDefaultNetwork(NetworkInfo networkInfo) {
        defaultNetwork.postValue(networkInfo);
        fetchTokens();
    }

    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    public MutableLiveData<Wallet> wallet() {
        return wallet;
    }

    public LiveData<Token[]> tokens() {
        return tokens;
    }

    public void fetchTokens() {
        progress.postValue(true);
        if (defaultNetwork.getValue() == null) {
            findDefaultNetwork();
        }
        disposable = fetchTokensInteract
                .fetch(wallet.getValue())
                .subscribe(this::onTokens, this::onError);
    }

    private void onTokens(Token[] tokens) {
        progress.postValue(false);
        this.tokens.postValue(tokens);
    }

    public void showAddToken(Context context) {
        addTokenRouter.open(context);
    }

    public void showSendToken(Context context, String address, String symbol, int decimals) {
        sendTokenRouter.open(context, address, symbol, decimals);

    }
}
