package com.smarttoni.server.wrappers;

import com.smarttoni.entities.Intervention;
import com.smarttoni.entities.InterventionJob;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.Work;

import java.util.List;

public class DetailPage {

    private Station station;
    private Recipe recipe;
    private Work work;
    private InterventionMeta meta;
    private List<TaskStep> steps;

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public List<TaskStep> getSteps() {
        return steps;
    }

    public void setSteps(List<TaskStep> steps) {
        this.steps = steps;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public InterventionMeta getInterventionMeta() {
        return meta;
    }

    public void setInterventionMeta(InterventionMeta interventionMeta) {
        this.meta = interventionMeta;
    }
}
