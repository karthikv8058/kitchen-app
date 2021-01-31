package com.smarttoni.assignment.synergy;

import com.smarttoni.entities.Work;

public class WorkDeliveryTime {
    Work work;
    long deliveryTime;
    long actualDeliveryTime;

    public WorkDeliveryTime(Work work, long deliveryTime, long actualDeliveryTime) {
        this.work = work;
        this.deliveryTime = deliveryTime;
        this.actualDeliveryTime = actualDeliveryTime;
    }
}
