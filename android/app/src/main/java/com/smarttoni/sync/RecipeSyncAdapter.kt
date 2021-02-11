package com.smarttoni.sync

import android.content.Context
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.*
import com.smarttoni.logger.Logger
import com.smarttoni.models.StorageSynWrapper
import com.smarttoni.models.SyncWarpper
import com.smarttoni.http.HttpClient
import com.smarttoni.utils.HttpHelper
import com.smarttoni.utils.LocalStorage
import com.smarttoni.utils.DateUtil
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class RecipeSyncAdapter : AbstractSyncAdapter {
    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {

        val storage = LocalStorage(context)
        val time = storage.getLong(LocalStorage.LAST_SYNC_RECIPE)
        var lastCreatedAt = ""
        if (time > 0) {
            lastCreatedAt = DateUtil.formatStandardDate(Date(time))
        } else {
            lastCreatedAt = DateUtil.formatStandardDate(Date(0))
        }
        HttpClient(context).httpClient.syncRecipes(restaurantId, lastCreatedAt).enqueue(object : Callback<SyncWarpper> {
            override fun onResponse(call: Call<SyncWarpper>, response: Response<SyncWarpper>) {
                val data = response.body() ?: return
                Thread(Runnable {
                    synchRecipes(data, daoAdapter, context)
                    synchTasks(data, daoAdapter, context)
                    synchSteps(data, daoAdapter, context)
                    storage.setLong(LocalStorage.LAST_SYNC_RECIPE, Date().time)
                    Logger.write("RecipeSync",data)
                    successListener.onSuccess()
                }).start()
            }

            override fun onFailure(call: Call<SyncWarpper>, t: Throwable) {
                Logger.write("RecipeSync","Fail :  Last Sync "+lastCreatedAt)
                failListener.onFail()
            }
        })
    }

    private fun synchSteps(data: SyncWarpper, daoAdapter: DaoAdapter, context: Context) {
        val stepCreated = data.taskSteps.created
        val stepUpdated = data.taskSteps.updated
        val stepDeleted = data.taskSteps.deleted
        val stepToCreate = ArrayList<TaskStep>()
        val stepToDelete = ArrayList<String>()

        if (stepCreated != null && stepCreated.size > 0) {
            stepToCreate.addAll(stepCreated)
            for (r in stepCreated) {
                stepToDelete.add(r.id)
            }
        }
        if (stepUpdated != null && stepUpdated.size > 0) {
            stepToCreate.addAll(stepUpdated)
            for (r in stepUpdated) {
                stepToDelete.add(r.id)
            }
        }
        if (stepDeleted != null && stepDeleted.size > 0) {
            for (r in stepDeleted) {
                stepToDelete.add(r.id)
            }
        }
        daoAdapter.deleteTaskSteps(stepToDelete)
        saveStepToDb(context, stepToCreate, daoAdapter)
    }

    private fun synchTasks(data: SyncWarpper, daoAdapter: DaoAdapter, context: Context) {

        val taskCreated = data.tasks.created
        val taskUpdated = data.tasks.updated
        val taskDeleted = data.tasks.deleted
        val taskToCreate = ArrayList<Task>()
        val taskToDelete = ArrayList<String>()

        val segmentCreated = data.taskSegments.created
        val segmentUpdated = data.taskSegments.updated
        val segmentDeleted = data.taskSegments.deleted
        val segmentToCreate = ArrayList<Segment>()
        val segmentToDelete = ArrayList<String>()

        if (taskCreated != null && taskCreated.size > 0) {
            taskToCreate.addAll(taskCreated)
            for (r in taskCreated) {
                taskToDelete.add(r.id)
            }
        }
        if (taskUpdated != null && taskUpdated.size > 0) {
            taskToCreate.addAll(taskUpdated)
            for (r in taskUpdated) {
                taskToDelete.add(r.id)
            }
        }
        if (taskDeleted != null && taskDeleted.size > 0) {
            for (r in taskDeleted) {
                taskToDelete.add(r.id)
            }
        }

        if (segmentCreated != null && segmentCreated.size > 0) {
            segmentToCreate.addAll(segmentCreated)
            for (r in segmentCreated) {
                segmentToDelete.add(r.id)
            }
        }
        if (segmentUpdated != null && segmentUpdated.size > 0) {
            segmentToCreate.addAll(segmentUpdated)
            for (r in segmentUpdated) {
                segmentToDelete.add(r.id)
            }
        }
        if (segmentDeleted != null && segmentDeleted.size > 0) {
            for (r in segmentDeleted) {
                segmentToDelete.add(r.id)
            }
        }
        daoAdapter.deleteTasks(taskToDelete)
        daoAdapter.deleteSegments(segmentToDelete)
        daoAdapter.deleteInterventions(taskToDelete)
        saveTaskToDb(context, taskToCreate, daoAdapter, segmentToCreate)
    }

    private fun synchRecipes(data: SyncWarpper, daoAdapter: DaoAdapter, context: Context) {
        val recipeCreated = data.recipes.created
        val recipeUpdated = data.recipes.updated
        val recipeDeleted = data.recipes.deleted
        val storageToSynch = data.storages

        val recipeToCreate = ArrayList<Recipe>()
        val recipetoDelete = ArrayList<String>()

        if (recipeCreated != null && recipeCreated.size > 0) {
            recipeToCreate.addAll(recipeCreated)
            for (r in recipeCreated) {
                recipetoDelete.add(r.id)
            }
        }
        if (recipeUpdated != null && recipeUpdated.size > 0) {
            recipeToCreate.addAll(recipeUpdated)
            for (r in recipeUpdated) {
                recipetoDelete.add(r.id)
            }
        }
        if (recipeDeleted != null && recipeDeleted.size > 0) {
            for (r in recipeDeleted) {
                recipetoDelete.add(r.id)
            }
        }
        deleteRecipes(recipetoDelete, daoAdapter)
        saveRecipesToDb(context, recipeToCreate, daoAdapter, storageToSynch)
    }

    private fun deleteRecipes(ids: List<String>, daoAdapter: DaoAdapter) {
        daoAdapter.deleteRecipe(ids)
        daoAdapter.deleteRecipeIngredient(ids)
    }

    private fun saveStepToDb(context: Context, taskSteps: List<TaskStep>, daoAdapter: DaoAdapter) {
        if (taskSteps != null) {
            val tasksSteps = ArrayList<TaskStep>()
            val tasksStepIds = ArrayList<String>()
            for (interventionStep in taskSteps) {
                daoAdapter.deleteTaskStepById(interventionStep.id)

                interventionStep.previous = interventionStep.dependentTask
                var url: String? = interventionStep.videourl
                if (url == null) {
                    url = ""
                }
                interventionStep.videourl = HttpHelper.downloadFile(context, url, "", "")
                var imageurl: String? = interventionStep.imageurl
                if (imageurl == null) {
                    imageurl = ""
                }
                interventionStep.imageurl = HttpHelper.downloadFile(context, imageurl, "", "")

                if (interventionStep.taskIngredientComaSperatad != null) {
                    val stepIngIds = ArrayList<String>()
                    val stepIngredient = ArrayList<StepIngrediant>()
//                    val ingIds = interventionStep.taskIngredientComaSperatad.split(",")
                    val jsonArray = JSONArray(interventionStep.taskIngredientComaSperatad)
                    for (index in  0..jsonArray.length()- 1) {
                        val ing = jsonArray.optJSONObject(index)
                        daoAdapter.deleteStepIngredientById(interventionStep.id)
                        val interIngredient = StepIngrediant()
                        interIngredient.stepId = interventionStep.id
                        interIngredient.recipeUuid =  ing.optString("uuid")
                        interIngredient.quantity =  ing.optString("quantity").toInt()
                        stepIngIds.add(ing.optString("uuid"))
                        stepIngredient.add(interIngredient)
                    }
                    daoAdapter.saveStepsIngredient(stepIngredient)
                }
                tasksSteps.add(interventionStep)
                tasksStepIds.add(interventionStep.id)
            }
            daoAdapter.saveTaskSteps(tasksSteps)
        }
    }

    private fun saveTaskToDb(context: Context, tasks: List<Task>, daoAdapter: DaoAdapter, segments: List<Segment>) {
        val interventions = ArrayList<Intervention>()
        val interventionsIds = ArrayList<String>()
        val taskItem = ArrayList<Task>()
        if (segments != null) {
            for (segment in segments) {
                daoAdapter.deleteSegmentById(segment.id)
                daoAdapter.saveSegment(segment)
            }
        }
        for (task in tasks) {
            if (task.type == 1) {
                daoAdapter.deletetaskById(task.id)
                if (task.imageurl != null) {
                    task.imageurl = HttpHelper.downloadFile(context, task.imageurl, "", "")
                }
                task.numberOfSegments = getNumberofSegments(segments, task.id)
                if (task.taskIngredientComaSperatad != null) {
                    val taskIngredientsIds = ArrayList<String>()
                    val taskIng = ArrayList<TaskIngredient>()
                    val jsonArray = JSONArray(task.taskIngredientComaSperatad)

                    for (index in 0..jsonArray.length() - 1) {
                        val ing = jsonArray.optJSONObject(index)
                        daoAdapter.deleteTaskIngredient(task.id)
                        val taskIngredient = TaskIngredient()
                        taskIngredient.taskId = task.id
                        taskIngredient.recipeId = ing.optString("uuid")
                        taskIngredient.quantity = ing.optString("quantity").toFloat()
                        taskIngredientsIds.add(ing.optString("uuid"))
                        taskIng.add(taskIngredient)
                    }
                    daoAdapter.saveTaskIngredients(taskIng)
                }
                if (daoAdapter.getUnitById(task.outputUnit) != null) {
                    task.outputUnitName = (daoAdapter.getUnitById(task.outputUnit).name)
                }

                taskItem.add(task)
            } else if (task.type == 2) {
                daoAdapter.deleteInterventionById(task.id)
                val interventionsTask = Intervention()
                interventionsTask.id = task.id
                if (task.imageurl != null) {
                    interventionsTask.imageUrl = HttpHelper.downloadFile(context, task.imageurl, "", "")
                }
                interventionsTask.isDelayable = task.isDelayable
                interventionsTask.taskId = task.taskUuid
                interventionsTask.parent = task.interventionParent
                interventionsTask.interventionPosition = task.interventionPosition
                interventionsTask.time = task.interventionTime
                interventionsTask.printLabel=task.printLabel
                interventionsTask.name = task.name
                interventionsTask.description = task.description
                interventionsTask.previous = task.parentTasks
                if (task.taskIngredientComaSperatad != null) {
                    val taskIngredientsIds = ArrayList<String>()
                    val taskIng = ArrayList<TaskIngredient>()
                    val jsonArray = JSONArray(task.taskIngredientComaSperatad)
                    for (index in 0..jsonArray.length() - 1) {
                        val ing = jsonArray.optJSONObject(index)
                        daoAdapter.deleteTaskIngredient(task.id)
                        val taskIngredient = TaskIngredient()
                        taskIngredient.taskId = task.id
                        taskIngredient.recipeId = ing.optString("uuid")
                        taskIngredient.quantity = ing.optString("quantity").toFloat()
                        taskIngredientsIds.add(ing.optString("uuid"))
                        taskIng.add(taskIngredient)
                    }
                    daoAdapter.saveTaskIngredients(taskIng)
                }
                if (task.taskUuid != null && daoAdapter.getStationById(getStationId(tasks, task.taskUuid)) != null) {
                    interventionsTask.stationColor = daoAdapter.getStationById(getStationId(tasks, task.taskUuid))!!.color
                }
                var imageurl: String? = task.imageurl
                if (imageurl == null) {
                    imageurl = ""
                }
                interventionsTask.imageUrl = HttpHelper.downloadFile(context, imageurl, "", "")
                interventionsIds.add(interventionsTask.id)
                interventions.add(interventionsTask)
            }

        }
        daoAdapter.saveInterventions(interventions)
        daoAdapter.saveTasks(taskItem)
    }
}

private fun getStationId(tasks: List<Task>, taskId: String): String {
    var stationId = ""
    for (task in tasks) {
        if (taskId == task.id) {
            stationId = task.stationId
        }
    }
    return stationId
}

private fun getNumberofSegments(segments: List<Segment>, taskId: String): Int {
    var count = 0
    for (segment in segments) {
        if (segment.taskId == taskId) {
            count++
        }
    }
    return count
}

private fun saveRecipesToDb(context: Context, recipes: List<Recipe>, daoAdapter: DaoAdapter, storageToSynch: List<StorageSynWrapper>) {
    for (recipe in recipes) {
        for (storage in storageToSynch) {
            if (storage.recipeId == recipe.id) {
                recipe.storageId = storage.storageId
                recipe.rackId = storage.rackId
                recipe.placeId = storage.placeId
                recipe.roomId = storage.roomId
            }
        }
        var url: String? = recipe.imageUrl
        if (url == null) {
            url = ""
        }
        recipe.imageUrl = HttpHelper.downloadFile(context, url, recipe.name, recipe.description)
        if(recipe.supplier != null){
            val s = daoAdapter.getSupplierById(recipe.supplier)
            if(s != null) {
                recipe.supplierName = s.name;
            }

        }
        if (recipe.taskIngredientComaSperatad != null) {
            val recipeIngredientIds = ArrayList<String>()
            val recipeIngredient = ArrayList<RecipeIngredients>()
            val jsonArray = JSONArray(recipe.taskIngredientComaSperatad)
            for (index in 0..jsonArray.length() - 1) {
                val ing = jsonArray.optJSONObject(index)
                daoAdapter.deleteRecipeIngredientById(recipe.id)
                val recipeIng = RecipeIngredients()
                recipeIng.recipeId = recipe.id
                recipeIng.id = ing.optString("uuid")
                recipeIng.quantity = ing.optString("quantity").toFloat()
                recipeIngredientIds.add(ing.optString("uuid"))
                recipeIngredient.add(recipeIng)
            }
            daoAdapter.saveRecipeIngredient(recipeIngredient)
        }
    }
    daoAdapter.saveRecipe(recipes)
}
