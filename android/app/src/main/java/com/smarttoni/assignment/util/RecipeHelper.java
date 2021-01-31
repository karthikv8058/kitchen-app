package com.smarttoni.assignment.util;

import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.TaskIngredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeHelper {


    public static List<Task> getPreviousTasks(DaoAdapter daoAdapter, Task task) {
        if (task == null) {
            return null;
        }
        if (task.getPrevious() != null && !task.getPrevious().equals("")) {
            List<String> list = new ArrayList<>();
            for (String s : task.getPrevious().split(","))
                list.add(s);
            return daoAdapter.getTaskById(list);
        }
        return null;
    }

    public static List<TaskIngredient> getPreviousRecipes(DaoAdapter daoAdapter, Task task) {
        return daoAdapter.getIngredientsForTask(task);
    }
}
