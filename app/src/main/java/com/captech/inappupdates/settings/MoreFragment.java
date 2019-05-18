package com.captech.inappupdates.settings;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.captech.inappupdates.MainActivity;
import com.captech.inappupdates.R;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;

import static android.app.Activity.RESULT_OK;
import static com.captech.inappupdates.MainActivity.REQUEST_CODE;

public class MoreFragment extends Fragment implements InstallStateUpdatedListener, OnSuccessListener<AppUpdateInfo> {
    AppUpdateManager appUpdateManager;

    public static String FLEXIBLE_UPDATE = "flexible_update";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_main, container, false);

        Bundle bundle = getArguments();
        boolean needsFlexibleUpdate = bundle.getBoolean(FLEXIBLE_UPDATE);

        LinearLayout flexibleUpdateLayout = view.findViewById(R.id.updateContainer);
        flexibleUpdateLayout.setVisibility(needsFlexibleUpdate ? View.VISIBLE : View.GONE);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUpdateManager = AppUpdateManagerFactory.create(requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        appUpdateManager.registerListener(this);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.i(MainActivity.class.getSimpleName(), "Update flow completed! Result code: " + resultCode);
            } else {
                Log.e(MainActivity.class.getSimpleName(), "Update flow failed! Result code: " + resultCode);
            }
        }
    }

    @Override
    public void onStateUpdate(InstallState installState) {
        int installStatus = installState.installStatus();
        switch (installStatus) {
            case InstallStatus.DOWNLOADING:
                //show downloading UI
                break;
            case InstallStatus.INSTALLING:
                //show downloading UI
                break;
            case InstallStatus.DOWNLOADED:
                //show downloading UI
                break;
            case InstallStatus.INSTALLED:
                //show downloading UI
                break;
            case InstallStatus.FAILED:
                //show downloading UI
                break;
            case InstallStatus.CANCELED:
                //show downloading UI
                break;
            case InstallStatus.PENDING:
                //show pending UI
                break;
        }
    }

    @Override
    public void onSuccess(AppUpdateInfo appUpdateInfo) {
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
            //show flexible update available UI and on response from that, start flexible update
            startUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE);
        }
    }

    private void startUpdate(AppUpdateInfo appUpdateInfo, int appUpdateType) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                    appUpdateType,
                    requireActivity(),
                    REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }
}
