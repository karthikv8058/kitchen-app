package com.smarttoni.sync.orders;

import java.util.List;

public class SyncOrder {
    public String uuid;
    public String createdAt;
    public String updatedAt;
    public int status;
    public boolean inventoryOrder;
    public String table;
    public int type;
    public List<SyncCourse> courses;
    public int childOrderStatus;
}
