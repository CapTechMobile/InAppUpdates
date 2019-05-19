package com.captech.inappupdates.more;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;

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
    AppUpdateInfo appUpdateInfo;
    TextView updateTextView, aboutUsText, placeholderText;
    Button updateButton;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_main, container, false);
        Bundle bundle = getArguments();
        boolean needsFlexibleUpdate = bundle.getBoolean(FLEXIBLE_UPDATE);

        LinearLayout flexibleUpdateLayout = view.findViewById(R.id.updateContainer);
        flexibleUpdateLayout.setVisibility(needsFlexibleUpdate ? View.VISIBLE : View.GONE);

        aboutUsText = view.findViewById(R.id.aboutUsText);
        placeholderText = view.findViewById(R.id.placeholderText);
        updateTextView = view.findViewById(R.id.updateView);
        updateButton = view.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appUpdateInfo != null) {
                    startUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE);
                }
            }
        });
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
                updateTextView.setText(R.string.downloading_text);
                updateButton.setVisibility(View.INVISIBLE);
                break;
            case InstallStatus.DOWNLOADED:
                updateTextView.setText(R.string.downloaded_text);
                updateButton.setVisibility(View.INVISIBLE);
                aboutUsText.setText(R.string.success_text);
                placeholderText.setText(R.string.flexible_update_installed);
                popupSnackbarForCompleteUpdate();
                break;
            case InstallStatus.FAILED:
                updateTextView.setText(R.string.download_failed_text);
                break;
            case InstallStatus.CANCELED:
                updateTextView.setText(R.string.download_canceled_text);
                break;
        }
    }

    @Override
    public void onSuccess(AppUpdateInfo appUpdateInfo) {
        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
            // If the update is downloaded but not installed,
            // notify the user to complete the update.
            popupSnackbarForCompleteUpdate();
        } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
            //show flexible update available UI and on response from that, start flexible update
            this.appUpdateInfo = appUpdateInfo;
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

    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        requireActivity().findViewById(R.id.main_activity),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.snackbarActionColor));
        snackbar.show();
    }
}
