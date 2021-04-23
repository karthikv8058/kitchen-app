package com.smarttoni.sync.orders;

import com.smarttoni.entities.InterventionJob;
import com.smarttoni.entities.Work;
import com.smarttoni.utils.DateUtil;
import com.smarttoni.utils.Strings;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

public class SyncIntervention {

    private Long interventionId;

    private String orderUuid;

    private long workId;

    private String interventionUuid;

    private String userUuid;


    private int status;

    private String waitingFrom;

    private int extraTime;

    private boolean scheduleNext;

    private boolean noStart;

    private String startedAt;


    public static SyncIntervention clone(InterventionJob job) {
        SyncIntervention i = new SyncIntervention();
        i.interventionId = job.getId();
        i.userUuid = job.getUserId() != null ? job.getUserId()  :null;
        i.orderUuid = job.getOrderId();
        i.workId = job.getWorkId();
        i.interventionUuid = job.getInterventionId();
        i.status = job.getStatus();
        i.extraTime = job.getExtraTime();
        i.scheduleNext = job.getScheduleNext();
        i.noStart = job.getNoStart();
        i.startedAt = job.getStartedAt() != null ? DateUtil.formatDate(job.getStartedAt().getTime()) : "";
        i.waitingFrom = job.getWaitingFrom() > 0 ? DateUtil.formatDate(job.getWaitingFrom()) : "";
        return i;
    }

    public static InterventionJob clone(SyncIntervention job) {
        InterventionJob i = new InterventionJob();
        i.setId(job.interventionId);
        i.setUserId(job.userUuid);
        i.setOrderId(job.orderUuid);
        i.setWorkId(job.workId);
        i.setInterventionId(job.interventionUuid);
        i.setStatus(job.status);
        i.setExtraTime(job.extraTime);
        i.setScheduleNext(job.scheduleNext);
        i.setNoStart(job.noStart);
        if(Strings.isNotEmpty(job.startedAt)){
            i.setStartedAt(new Date(DateUtil.parse(job.startedAt)));
        }
        if(Strings.isNotEmpty(job.waitingFrom)){
            i.setWaitingFrom(DateUtil.parse(job.waitingFrom));
        }
        return i;
    }
}
