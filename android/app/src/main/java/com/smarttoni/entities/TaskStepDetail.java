package com.smarttoni.entities;

import java.util.List;

public class TaskStepDetail {
    public TaskStep taskStep;
    public List<StepIngrediant> stepIngrediants;
    public int nextStep;
    public int currentStep;
    public String prevStepId;
    public String nextStepId;

}
