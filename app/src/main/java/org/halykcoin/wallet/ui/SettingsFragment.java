package org.halykcoin.wallet.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import org.halykcoin.wallet.R;
import org.halykcoin.wallet.interact.FindDefaultWalletInteract;
import org.halykcoin.wallet.repository.EthereumNetworkRepositoryType;
import org.halykcoin.wallet.repository.PreferenceRepositoryType;
import org.halykcoin.wallet.router.ManageWalletsRouter;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static org.halykcoin.wallet.C.SHARE_REQUEST_CODE;

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Inject
    EthereumNetworkRepositoryType ethereumNetworkRepository;
    @Inject
    FindDefaultWalletInteract findDefaultWalletInteract;
    @Inject
    ManageWalletsRouter manageWalletsRouter;
    @Inject
    protected PreferenceRepositoryType preferenceRepositoryType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.fragment_settings);
        final Preference wallets = findPreference("pref_wallet");

        wallets.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity(), WalletsActivity.class);
            getActivity().startActivity(intent);
            return false;
        });

        final Preference backup = findPreference("pref_backup");
        backup.setOnPreferenceClickListener(preference -> {
            shareKey();
            return false;
        });

//        final Preference referal = findPreference("pref_referal");
//        referal.setOnPreferenceClickListener(preference -> {
//            Intent intent = new Intent(getActivity(), ReferralActivity.class);
//            getActivity().startActivity(intent);
//            return false;
//        });

//        final Preference security = findPreference("pref_passcode");
//        security.setEnabled(preferenceRepositoryType.isPasscodeSet());
//        backup.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//                boolean enablePin = (Boolean) o;
//                Timber.d("onPreferenceChange " + enablePin);
//                if (enablePin) {
//
//                }
//                return true;
//            }
//        });

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
//        if (key.equals("pref_wallet")) {
//            Preference rpcServerPref = findPreference(key);
//            // Set summary
//            String selectedRpcServer = sharedPreferences.getString(key, "");
//            rpcServerPref.setSummary(selectedRpcServer);
//            NetworkInfo[] networks = ethereumNetworkRepository.getAvailableNetworkList();
//            for (NetworkInfo networkInfo : networks) {
//                if (networkInfo.name.equals(selectedRpcServer)) {
//                    ethereumNetworkRepository.setDefaultNetworkInfo(networkInfo);
//                    return;
//                }
//            }
//        }
    }

    private void shareKey() {
        String key = preferenceRepositoryType.getStoredId();
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Keystore");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, key);
        startActivityForResult(
                Intent.createChooser(sharingIntent, "Share via"),
                SHARE_REQUEST_CODE);
    }

    public String getVersion() {
        String version = "N/A";
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}

