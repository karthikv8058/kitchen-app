package com.smarttoni.server.controlles.task;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.database.DbOpenHelper;
import com.smarttoni.entities.DaoSession;
import com.smarttoni.entities.Intervention;
import com.smarttoni.entities.InterventionJob;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.RecipeTag;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.StepIngrediant;
import com.smarttoni.entities.Tag;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.TaskDao;
import com.smarttoni.entities.TaskIngredient;
import com.smarttoni.entities.TaskStep;
import com.smarttoni.entities.Units;
import com.smarttoni.entities.Work;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.server.wrappers.DetailPage;
import com.smarttoni.server.wrappers.IngredientWithQuantity;
import com.smarttoni.server.wrappers.InterventionMeta;
import com.smarttoni.server.wrappers.TaskWithQuantity;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.Strings;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GetTask extends HttpSecurityRequest {
    Context context;
    private static DaoSession daoSession;


    public GetTask(Context context) {
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {

        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            String taskId = jsonObject.getString("taskId");
            boolean isIntervention = jsonObject.getBoolean("isIntervention");
            //String interventionId = jsonObject.getString("interventionId");
            Long workId = jsonObject.getLong("workId");
            daoSession = DbOpenHelper.Companion.getDaoSession(context);
            DetailPage page = new DetailPage();

            if (isIntervention) {
                InterventionJob interventionJob = ServiceLocator
                        .getInstance()
                        .getDatabaseAdapter()
                        .getInterventionJobById(workId);

                if (interventionJob == null) {
                    Intervention intervention = ServiceLocator
                            .getInstance()
                            .getDatabaseAdapter()
                            .getInterventionByUuid(taskId);
                    interventionJob = new InterventionJob();
                    interventionJob.setId(0L);
                    interventionJob.setIntervention(intervention);
                    interventionJob.setInterventionId(intervention.getId());
                }
                if(interventionJob.getWorkId() > 0){
                    page.setWork(interventionJob.getWork().cloneWithoutNextTask());
                }
                generateInterventionPage(page, interventionJob);

            } else {
                Work w = ServiceLocator
                        .getInstance()
                        .getQueue()
                        .getTask(workId);

                if (w != null) {
                    page.setWork(w.cloneWithoutNextTask());
                }
                generateDetailPage(page, taskId, w);
            }

            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<DetailPage>() {
            }.getType();
            String json = gson.toJson(page, type);
            response.send(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void generateDetailPage(DetailPage page, String taskId, Work work) {
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        Task task = daoAdapter.getTaskById(taskId);
        if (task == null) {
            return;
        }
        Recipe recipe = task.getRecipe();

        Station station = daoAdapter
                .getStationById(task.getStationId());
        page.setRecipe(recipe);
        page.setStation(station);

        List<com.smarttoni.server.wrappers.TaskStep> taskSteps = new ArrayList<>();

        com.smarttoni.server.wrappers.TaskStep taskStep = new com.smarttoni.server.wrappers.TaskStep();

        //Check Inventory Transport task
        if (task.getId().equals("1")) {

            Map<String, TaskWithQuantity> map = new HashMap<>();

            List<Recipe> recipes = new ArrayList<>();
            recipes.add(work.getRecipe());


            List<TaskWithQuantity> tasksWithQuantities = null;
            if (work.getTransportType() == Work.TRANSPORT_TO_LOCATION && work.getPrevTasks() != null) {
                // String[] taskIds = task.getPrevious().split(",");
                //List<Task> tasks = daoAdapter.getTaskById(Arrays.asList(taskIds));
                tasksWithQuantities = new ArrayList<>();
                for (Work w : work.getPrevTasks()) {
                    TaskWithQuantity taskWithQuantity = new TaskWithQuantity();
                    taskWithQuantity.setQuantity(w.getQuantity());
                    w.getTask().getUnit();
                    taskWithQuantity.setTask(w.getTask());
                    tasksWithQuantities.add(taskWithQuantity);
                }
                taskStep.setTasks(tasksWithQuantities);
            } else {
                tasksWithQuantities = new ArrayList<>();
                TaskWithQuantity taskWithQuantity = new TaskWithQuantity();
                recipe = work.getRecipe();

                Task t = new Task();
                t.setImageurl(recipe.getImageUrl());
                t.setName(recipe.getName());
                t.setUnit(recipe.getOutputUnit());
                taskWithQuantity.setQuantity(work.getQuantity());
                taskWithQuantity.setTask(t);
                tasksWithQuantities.add(taskWithQuantity);
                StringBuilder sb = new StringBuilder();
                sb.append(recipe.getName()).append(",");

                map.put(recipe.getId(), taskWithQuantity);
            }

//TODO Why ??
//            if (work.getSynergyList() != null) {
//                for (Work w : work.getSynergyList()) {
//                    Recipe r = w.getRecipe();
//                    TaskWithQuantity tq = map.get(r.getId());
//                    if (tq == null) {
//                        tq = new TaskWithQuantity();
//                        Task t1 = new Task();
//                        t1.setImageurl(r.getImageUrl());
//                        t1.setName(r.getName());
//                        t1.setUnit(r.getOutputUnit());
//                        tq.setQuantity(w.getQuantity() * r.getOutputQuantity());
//                        tq.setTask(t1);
//                        //sb.append(r.getName()).append(",");
//                        tasksWithQuantities.add(tq);
//                        map.put(r.getId(), tq);
//                    } else {
//                        tq.setQuantity(tq.getQuantity() + (w.getQuantity()));
//                    }
//                }
//            }
// ^^^

            //String recipeNames = sb.deleteCharAt(sb.length() - 1).toString();
            if (work != null && work.getTransportType() == Work.TRANSPORT_FROM_INVENTORY) {
                taskStep.setName("Take Items from inventory");
                //taskStep.setDescription("Take " + recipeNames + " from inventory");
            } else if (work.getTransportType() == Work.TRANSPORT_TO_LOCATION) {
                taskStep.setName(work.getTitle());
                taskStep.setDescription("");
            } else if (work.getTransportType() == Work.TRANSPORT_TO_INVENTORY) {
                taskStep.setName(work.getTitle());
                taskStep.setDescription("");
            }
            taskStep.setTasks(tasksWithQuantities);
            taskStep.setUuid("1");
        } else {
            taskStep.setName(task.getName());
            taskStep.setDescription(task.getDescription());
            taskStep.setImage(task.getImageurl());
            taskStep.setUuid(task.getId());

            if (task.getPrevious() != null) {
                String[] taskIds = task.getPrevious().split(",");
                List<Task> tasks = daoAdapter.getTaskById(Arrays.asList(taskIds));
                List<TaskWithQuantity> tasksWithQuantities = new ArrayList<>();
                for (Task t : tasks) {
                    TaskWithQuantity taskWithQuantity = new TaskWithQuantity();
                    taskWithQuantity.setQuantity(work != null ? work.getQuantity() : 1);
                    if (work != null && work.getSynergyList() != null) {
                        taskWithQuantity.setQuantity(1 + work.getSynergyList().size());
                    }
                    t.getUnit();
                    taskWithQuantity.setTask(t);
                    tasksWithQuantities.add(taskWithQuantity);
                }
                taskStep.setTasks(tasksWithQuantities);
            }

        }

        if (task.getTaskIngredient() != null && task.getTaskIngredient().size() > 0) {
            List<IngredientWithQuantity> ingredients = new ArrayList<>();
            for (TaskIngredient i : task.getTaskIngredient()) {
                IngredientWithQuantity ingredient = new IngredientWithQuantity();

                Recipe r = daoAdapter.getRecipeById(i.getRecipeId());
                if (r != null) {
                    Units unit =  r.getOutputUnit();
                    ingredient.setQuantity(i.getQuantity(), unit);

                    if (work != null) {
                        ingredient.setQuantity(work.getQuantity() * ingredient.getQuantity(), unit);
                        if (work.getSynergyList() != null) {
                            ingredient.setQuantity(ingredient.getQuantity() * (1 + work.getSynergyList().size()), unit);
                        }
                    }

                    ingredient.setRecipe(r);
                }
                ingredients.add(ingredient);
            }
            taskStep.setIngredients(ingredients);
        }
        taskSteps.add(taskStep);

        for (TaskStep s : task.getTaskSteps()) {
            com.smarttoni.server.wrappers.TaskStep step = new com.smarttoni.server.wrappers.TaskStep();
            step.setDescription(s.getDescription());
            step.setImage(s.getImageurl());
            step.setUuid(s.getId());
            step.setVideo(s.getVideourl());

            //Previous Task As Step Ing]
            if (s.getPrevious() != null) {
                String[] taskIds = s.getPrevious().split(",");
                List<Task> tasks = daoAdapter.getTaskById(Arrays.asList(taskIds));

                List<TaskWithQuantity> tasksWithQuantities = new ArrayList<>();
                for (Task t : tasks) {
                    TaskWithQuantity taskWithQuantity = new TaskWithQuantity();
                    taskWithQuantity.setQuantity(1);
                    t.getUnit();
                    taskWithQuantity.setTask(t);
                    tasksWithQuantities.add(taskWithQuantity);
                }
                step.setTasks(tasksWithQuantities);
            }

            //Previous Step Ing
            List<StepIngrediant> stepIngrediants = s.getIngredients();
            if (stepIngrediants != null) {
                List<IngredientWithQuantity> ingredients = new ArrayList<>();
                for (StepIngrediant i : stepIngrediants) {
                    IngredientWithQuantity ingredient = new IngredientWithQuantity();
                    Recipe r = daoAdapter.getRecipeById(i.getRecipeUuid());
                    if (r != null) {
                        Units unit = r.getOutputUnit();
                        ingredient.setQuantity(i.getQuantity(), unit);
                        ingredient.setRecipe(r);
                    }
                    ingredients.add(ingredient);
                }
                step.setIngredients(ingredients);
            }
            taskSteps.add(step);
        }


        page.setSteps(taskSteps);
    }

    private void generateInterventionPage(DetailPage page, InterventionJob interventionJob) {
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        if (interventionJob.getId() != 0 && daoAdapter.getRestaurantSettings().getEnabledRecipeEdit()) {
            InterventionMeta meta = new InterventionMeta();
            meta.setPosition(interventionJob.getIntervention().getInterventionPosition());
            meta.setTime(interventionJob.getIntervention().getTime());
            Date startedAt = interventionJob.getStartedAt() != null ? interventionJob.getStartedAt() : new Date();
            meta.setStartedAt(startedAt.getTime());
            page.setInterventionMeta(meta);

//            Work w = ServiceLocator
//                    .getInstance()
//                    .getQueue()
//                    .getTask(interventionJob.getWorkId());
//
//            if(w != null){
//                qty = w.getActualQty() != 0 ?  w.getActualQty() : w.getQuantity();
//            }

        }


        Intervention task = daoAdapter.getInterventionByUuid(interventionJob.getInterventionId());

        Task parentTask = daoSession.getTaskDao().queryBuilder()
                .where(TaskDao.Properties.Id.eq(task.getTaskId()))
                .unique();
        Recipe recipe = parentTask.getRecipe();

        List<com.smarttoni.server.wrappers.TaskStep> taskSteps = new ArrayList<>();
        com.smarttoni.server.wrappers.TaskStep taskStep = new com.smarttoni.server.wrappers.TaskStep();
        taskStep.setName(task.getName());
        taskStep.setDescription(task.getDescription());
        taskStep.setImage(task.getImageUrl());
        taskStep.setUuid(task.getId());


        List<TaskWithQuantity> twq = new ArrayList<>();

        Intervention intervention = interventionJob.getIntervention();
        if (intervention.getPrevious() != null) {
            String[] previousTasks = intervention.getPrevious().split(",");


            for (String previous : previousTasks) {
                TaskWithQuantity taskWithQuantity = new TaskWithQuantity();
                Task t = daoAdapter.getTaskById(previous);
                taskWithQuantity.setTask(t);
                //tw.setQuantity(qty);
                twq.add(taskWithQuantity);
            }

        }

//        TaskWithQuantity tw = new TaskWithQuantity();
//        tw.setQuantity(qty);
//        parentTask.getUnit();
//        tw.setTask(parentTask);
//        twq.add(tw);
        taskStep.setTasks(twq);

        if (task.getTaskIngredients() != null && task.getTaskIngredients().size() > 0) {
            List<IngredientWithQuantity> ingredients = new ArrayList<>();
            for (TaskIngredient i : task.getTaskIngredients()) {
                IngredientWithQuantity ingredient = new IngredientWithQuantity();
                Recipe r = daoAdapter.getRecipeById(i.getRecipeId());
                if (r != null) {
                    Units unit = r.getOutputUnit();
                    ingredient.setQuantity(i.getQuantity(), unit);
                    ingredient.setRecipe(r);
                }
                ingredients.add(ingredient);
            }
            taskStep.setIngredients(ingredients);
        }

        taskSteps.add(taskStep);

        Station station = daoAdapter
                .getStationById(parentTask.getStationId());
        page.setRecipe(recipe);
        page.setStation(station);

        for (TaskStep s : task.getTaskSteps()) {
            com.smarttoni.server.wrappers.TaskStep step = new com.smarttoni.server.wrappers.TaskStep();
            step.setDescription(s.getDescription());
            step.setImage(s.getImageurl());
            step.setUuid(s.getId());
            step.setVideo(s.getVideourl());


            //Previous Task As Step Ing
            if (s.getPrevious() != null) {
                String[] taskIds = s.getPrevious().split(",");
                List<Task> tasks = daoAdapter.getTaskById(Arrays.asList(taskIds));

                List<TaskWithQuantity> tasksWithQuantities = new ArrayList<>();
                for (Task t : tasks) {
                    TaskWithQuantity taskWithQuantity = new TaskWithQuantity();
                    taskWithQuantity.setQuantity(1);
                    t.getUnit();
                    taskWithQuantity.setTask(t);
                    tasksWithQuantities.add(taskWithQuantity);
                }
                step.setTasks(tasksWithQuantities);
            }


            List<StepIngrediant> stepIngrediants = s.getIngredients();
            if (stepIngrediants != null) {
                List<IngredientWithQuantity> ingredients = new ArrayList<>();
                for (StepIngrediant i : stepIngrediants) {
                    IngredientWithQuantity ingredient = new IngredientWithQuantity();

                    //Work work =  ServiceLocator.getInstance().getQueue().getTask(task.get)
//                    if(work != null){
//                        ingredient.setQuantity(work.getQuantity() * ingredient.getQuantity());
//                    }
                    Recipe r = daoAdapter.getRecipeById(i.getRecipeUuid());
                    if (r != null) {
                        ingredient.setQuantity(i.getQuantity(), r.getOutputUnit());
                        ingredient.setRecipe(r);
                    }
                    ingredients.add(ingredient);
                }
                step.setIngredients(ingredients);
            }
            taskSteps.add(step);
        }
        page.setSteps(taskSteps);
    }
}
