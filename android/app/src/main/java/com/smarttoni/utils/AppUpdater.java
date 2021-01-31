package com.smarttoni.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.smarttoni.BuildConfig;
import com.smarttoni.entities.StoreRequest;
import com.smarttoni.http.HttpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppUpdater {
    public void check(Context context) {

        new HttpClient(context).getHttpClient().checkUpdate().enqueue(new Callback<StoreRequest>() {
            @Override
            public void onResponse(Call<StoreRequest> call, Response<StoreRequest> response) {
                if (response.body() != null && response.body().getLatestVersionCode() > BuildConfig.VERSION_CODE) {
                    showAlert(context);
                }
            }

            @Override
            public void onFailure(Call<StoreRequest> call, Throwable t) {

            }
        });
    }

    private void showAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("New Version available")
                .setPositiveButton("Download", (dialog, id) -> {
                    Intent launchIntent = null;
                    try {
                        launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.smarttoni.store");
                    } catch (Exception ignored) {
                    }
                    if (launchIntent == null) {
                        Toast.makeText(context, "SmartTONi Store not installed", Toast.LENGTH_SHORT).show();
                    } else {
                        context.startActivity(launchIntent);
                    }
                })
                .setNegativeButton("Not Now", (dialog, id) -> {

                }).show();
    }


}

