package com.smarttoni.sync;

import android.content.Context;

import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.ChefActivityLog;
import com.smarttoni.entities.Inventory;
import com.smarttoni.entities.PrinterData;
import com.smarttoni.entities.UserStationAssignment;
import com.smarttoni.entities.Work;
import com.smarttoni.http.HttpClient;
import com.smarttoni.utils.LocalStorage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IncrementalDbSync implements AbstractSyncAdapter {

    @Override
    public void onSync(@NotNull Context context, @NotNull DaoAdapter daoAdapter, @NotNull String restaurantId, @NotNull SyncSuccessListener successListener, @NotNull SyncFailListener failListener) {
        List<PrinterData> printerData = daoAdapter.loadUnUpdatedPrinterData();
        List<Inventory> inventories = daoAdapter.loadUnUpdatedInventory();
        List<UserStationAssignment> userStationAssignments = daoAdapter.loadUnUpdatedUserStation();
        List<ChefActivityLog> chefActivityLogs = daoAdapter.loadUnUpdatedChefActivity();
        List<Work> works = daoAdapter.loadUnUpdatedWorks();

        LocalStorage ls = (LocalStorage) ServiceLocator.getInstance().getService(ServiceLocator.LOCAL_STORAGE_SERVICE);

        new HttpClient(context).getHttpClient().uploadIncrementalDb(ls.getRestaurantId(), printerData, inventories, works, userStationAssignments, chefActivityLogs).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    for (PrinterData printerData1 : printerData) {
                        printerData1.setIsUpdated("1");
                        daoAdapter.updatePrinterDao(printerData1);
                    }
                    for (Inventory inventory : inventories) {
                        inventory.setIsUpdated("1");
                        daoAdapter.updateInventoryDao(inventory);
                    }
                    for (UserStationAssignment userStationAssignment : userStationAssignments) {
                        userStationAssignment.setIsUpdated("1");
                        daoAdapter.updateUserStationAssignmentDao(userStationAssignment);
                    }

                    for (ChefActivityLog chefActivityLogs : chefActivityLogs) {
                        chefActivityLogs.setIsUpdated("1");
                        daoAdapter.updateChefActivityDao(chefActivityLogs);

                    }
                    for (Work works : works) {
                        works.setIsUpdated("1");
                        daoAdapter.updateWork(works);

                    }
                }
                if (successListener != null) {
                    successListener.onSuccess();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (failListener != null) {
                    failListener.onFail();
                }
            }
        });
    }
}
