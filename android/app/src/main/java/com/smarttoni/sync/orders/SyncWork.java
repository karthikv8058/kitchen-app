package com.smarttoni.sync.orders;

import com.smarttoni.entities.Work;

public class SyncWork {


    private Long workId;

    //private float quantity = 1;

    //private long timeRemaining;

    private String recipeUuid;

    private int status;

    private String taskUuid;

    private String userUuid = "1";

    private String machineUuid;

    private String orderUuid;

    private String courseUuid;

    private int transportType;

    private String title;

    private String mealsUuid;

    private String orderLineUuid;

    private boolean isEndNode;

    private String subRecipes = "";

    private boolean isUsed;

    private int interventionsCompleted;

    private long lastIntervention = 0;

    private boolean readyToStart;

    private int transportMode;

    private String transportRoute;

    private String previousTaskIds;

    private String extraQuantity;

    public static  SyncWork clone(Work work) {

        SyncWork w = new SyncWork();
        w.workId = work.getId();

        //w.quantity = work.getQuantity();

        //w.timeRemaining = work.getTimeRemaining();

        w.recipeUuid = work.getRecipeId();

        w.status = work.getStatus();

        w.taskUuid = work.getTaskId();

        w.userUuid = work.getUserId() !=  null ? work.getUserId() : "";

        w.machineUuid = work.getMachineId();

        w.orderUuid = work.getOrderId();

        w.courseUuid = work.getCourseId();

        w.transportType = work.getTransportType();

        w.title = work.getTitle();

        w.mealsUuid = work.getMealsId();

        w.orderLineUuid = work.getOrderLineId();

        w.isEndNode = work.getIsEndNode();

        w.subRecipes = work.getSubRecipes()!=  null ? work.getUserId() : "";

        w.isUsed = work.getIsUsed();

        w.interventionsCompleted = work.getInterventionsCompleted();

        w.lastIntervention = 1;//work.getLastIntervention();

        w.readyToStart = work.getReadyToStart();

        w.transportMode = work.getTransportMode();

        w.transportRoute = work.getTransportRoute();

        w.previousTaskIds = work.getPreviousTaskIds();

        w.extraQuantity = work.getExtraQuantity();

        return w;
    }

    public static  Work clone(SyncWork work) {

        Work w =new Work();

        //#w.setquantity(work.getQuantity);

        //#w.settimeRemaining(work.getTimeRemaining);

        w.setRecipeId(work.recipeUuid);

        w.setStatus(work.status);

        w.setTaskId(work.taskUuid);

        w.setUserId(work.userUuid);

        w.setMachineId(work.machineUuid);

        w.setOrderId(work.orderUuid);

        w.setCourseId(work.courseUuid);

        w.setTransportType(work.transportType);

        w.setTitle(work.title);

        w.setMealsId(work.mealsUuid);

        w.setOrderLineId(work.orderLineUuid);

        w.setIsEndNode(work.isEndNode);

        w.setSubRecipes(work.subRecipes);

        w.setIsUsed(work.isUsed);

        w.setInterventionsCompleted(work.interventionsCompleted);

        w.setLastIntervention(1);//work.getLastIntervention);

        w.setReadyToStart(work.readyToStart);

        w.setTransportMode(work.transportMode);

        w.setTransportRoute(work.transportRoute);

        w.setPreviousTaskIds(work.previousTaskIds);

        w.setExtraQuantity(work.extraQuantity);


        w.setId(work.workId);

        return w;
    }


    //subRecipes,getUserId
}
