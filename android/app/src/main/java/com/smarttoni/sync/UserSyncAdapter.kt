package com.smarttoni.sync


import android.content.Context
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.Options
import com.smarttoni.entities.User
import com.smarttoni.entities.UserRights
import com.smarttoni.http.HttpClient
import com.smarttoni.server_app.models.UsersResponse.UsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserSyncAdapter : AbstractSyncAdapter {
    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {
        daoAdapter.deleteAllOptions()
        daoAdapter.deleteAllRights()
        try {
            HttpClient(context).httpClient.syncUsers(restaurantId).enqueue(object : Callback<List<UsersResponse>> {
                override fun onResponse(call: Call<List<UsersResponse>>, response: Response<List<UsersResponse>>) {
                    if(response.code() != 200){
                        failListener.onFail();
                        return
                    }
                    val usersResponses = response.body() ?: return
                    Thread(Runnable {
                        val options = Options()
                        options.key = "userList"
                        options.value = "0"
                        daoAdapter.saveOptionsDao(options)
                        for (usersResponse in usersResponses) {
                            if (usersResponse.uuId == null) break
                            if (usersResponse != null) {

                                val user1 = User()
                                user1.id = usersResponse.uuId
                                user1.userType = User.TYPE_USER
                                user1.createdat = System.currentTimeMillis()
                                user1.name = usersResponse.user.firstName + " " + usersResponse.user.lastName
                                user1.password = usersResponse.user.password!!.replace("$2y", "$2a")
                                user1.username = usersResponse.user.email
                                for (setting in usersResponse.user.setting!!) {
                                    if (setting.settingsKey == "task_auto_open") {
                                        user1.taskAutoOpen = getBoolean(setting.settings_value)
                                    }
                                    if (setting.settingsKey == "auto_assign") {
                                        user1.autoAssign = getBoolean(setting.settings_value)
                                    }
                                    if (setting.settingsKey == "voice_recognition") {

                                        user1.isVROn = getBoolean(setting.settings_value)
                                    }

                                    if (setting.settingsKey == "voice_commands") {
                                        user1.isTTSOn = getBoolean(setting.settings_value)
                                    }

                                    if (setting.settingsKey == "voice_commands_task") {
                                        user1.readOnAssign = getBoolean(setting.settings_value)
                                    }

                                    if (setting.settingsKey == "voice_commands_details_description") {
                                        user1.readDescriptionInDetail = getBoolean(setting.settings_value)
                                    }

                                    if (setting.settingsKey == "voice_commands_step_description") {
                                        user1.readDescriptionInSteps = getBoolean(setting.settings_value)
                                    }

                                    if (setting.settingsKey == "voice_commands_details_ingredients") {
                                        user1.readIngredientInDetail = getBoolean(setting.settings_value)
                                    }

                                    if (setting.settingsKey == "voice_commands_step_ingredients") {
                                        user1.readIngredientInSteps = getBoolean(setting.settings_value)
                                    }
                                    if (setting.settingsKey == "step_auto_open") {
                                        user1.stepAutoOpen = getBoolean(setting.settings_value)
                                    }
                                }
                                for (right in usersResponse.userRightsList!!) {
                                    if (!daoAdapter.getUserRights(usersResponse.uuId, right)) {
                                        var userRights = UserRights()
                                        userRights.userId = usersResponse.uuId
                                        userRights.right = right
                                        daoAdapter.saveUserRight(userRights)
                                    }
                                }
                                daoAdapter.saveUser(user1)
                            }
                        }
                        successListener.onSuccess()
                    }).start()
                }

                override fun onFailure(call: Call<List<UsersResponse>>, t: Throwable) {
                    failListener.onFail()
                }
            })


        } catch (e: Exception) {
        }
    }

    fun getBoolean(value: String?): Boolean {
        return value.equals("1")
    }

}
