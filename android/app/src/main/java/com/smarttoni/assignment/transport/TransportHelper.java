package com.smarttoni.assignment.transport;

import com.smarttoni.assignment.order.TaskTreeBuilder;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Machine;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.Place;
import com.smarttoni.entities.Rack;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Room;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.Storage;
import com.smarttoni.entities.TransportRoute;
import com.smarttoni.entities.Work;
import com.smarttoni.models.TransportSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransportHelper {

    private final String ROOM = "room";
    private final String STATION = "station";
    private final String MACHINE = "machine";
    private final String STORAGE = "storage";
    private final String RACK = "rack";
    private final String PLACE = "place";

    private TransportHelper() {
    }

    private static TransportHelper INSTANCE = new TransportHelper();

    public static TransportHelper getInstance() {
        return INSTANCE;
    }

    public static Work createTransportationTask(Work from, Work to, Recipe recipe, int type, OrderLine orderLine, float qty) {

        TransportRoute tr = null;
        if (type == Work.TRANSPORT_TO_LOCATION) {
            tr = checkForTransport(recipe, from, to);
            if (tr == null || tr.getType() == 1) {
                if (from != null && to != null) {
                    from.addNextTask(to);
                    to.addPrevTask(from);
                }
                return null;
            }
        }

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        Work t = TaskTreeBuilder.createWork(orderLine, null);
        t.setQueueTime(System.currentTimeMillis());
        t.setTransportType(type);
        t.setTaskId("1");
        t.setRecipeId(recipe.getId());
        t.setMealsId(orderLine.getMealId());
        t.setOrderId(orderLine.getOrderId());
        t.setCourseId(orderLine.getOrderId());
        if (qty > 0) {
            t.setQuantity(qty);
        } else {
            t.setQuantity(1);
        }
        if (tr != null) {
            t.setTransportMode(tr.getType());
            t.setTransportRoute(tr.getRouteId());
        }
        //List<Modifier> modifiers = ModifierHelper.getInstance().geModifiersFromCommaSeparated(daoAdapter, orderLine.getModifiers());
        //String recipeName = modifiers == null ? recipe.getName() : recipe.getName() + "(" + ModifierHelper.getInstance().geModifierNameCommaSeparated(modifiers) + ")";
        String recipeName = recipe.getName();
        String title = "";
        switch (type) {
            case Work.TRANSPORT_FROM_INVENTORY:
                title = "Take " + recipeName + " from inventory";
                break;
            case Work.TRANSPORT_TO_LOCATION:
                title = tr.getName();
                break;
            case Work.TRANSPORT_TO_INVENTORY:
                title = "Put " + recipeName + " to inventory";
                break;
        }
        t.setTitle(title);

        if (from != null) {
            t.addPrevTask(from);
            //t.setPreviousTaskIds(from.getId());
            from.addNextTask(t);
            if (to != null) {
                to.addPrevTask(t);
                t.addNextTask(to);
                //t.setNextTaskIds(to.getId());
            }
        } else if (to != null) {
            t.addNextTask(to);
            //t.setNextTaskIds(to.getId());
            to.addPrevTask(t);
        } else {

        }
        //daoAdapter.saveWork(t);
        return t;
    }

    private static TransportRoute checkForTransport(Recipe recipe, Work from, Work to) {
        Recipe fromRecipe = null;
        Recipe toRecipe = null;
        if (from == null) {
            fromRecipe = recipe;
        } else {
            toRecipe = recipe;
        }
        if (from != null && to != null) {

            TransportRoute tr = ServiceLocator
                    .getInstance()
                    .getDatabaseAdapter()
                    .getTransportRoute(from.getTask().getStationId() + "_" + to.getTask().getStationId());
            if (tr != null) {
                return tr;
            }

            if (from.isMachineTask()) {
                tr = ServiceLocator
                        .getInstance()
                        .getDatabaseAdapter()
                        .getTransportRoute(from.getTask().getMachineId() + "_" + to.getTask().getStationId());
                if (tr != null) {
                    return tr;
                }
            }

            if (to.isMachineTask()) {
                tr = ServiceLocator
                        .getInstance()
                        .getDatabaseAdapter()
                        .getTransportRoute(from.getTask().getStationId() + "_" + to.getTask().getMachineId());
                if (tr != null) {
                    return tr;
                }
            }

            if (from.isMachineTask() && to.isMachineTask()) {
                tr = ServiceLocator
                        .getInstance()
                        .getDatabaseAdapter()
                        .getTransportRoute(from.getTask().getMachineId() + "_" + to.getTask().getMachineId());
                return tr;
            }

            return null;
        }
        //from == null >> take from inventory
        if (from == null && fromRecipe != null && to != null) {
            if (fromRecipe.getPlaceId() != null) {
                TransportRoute tr = ServiceLocator
                        .getInstance()
                        .getDatabaseAdapter()
                        .getTransportRoute(fromRecipe.getPlaceId() + "_" + to.getTask().getStationId());
                if (tr != null) {
                    return tr;
                }
            }
            if (fromRecipe.getRackId() != null) {
                TransportRoute tr = ServiceLocator
                        .getInstance()
                        .getDatabaseAdapter()
                        .getTransportRoute(fromRecipe.getRackId() + "_" + to.getTask().getStationId());
                if (tr != null) {
                    return tr;
                }
            }
            if (fromRecipe.getStorageId() != null) {
                TransportRoute tr = ServiceLocator
                        .getInstance()
                        .getDatabaseAdapter()
                        .getTransportRoute(fromRecipe.getStorageId() + "_" + to.getTask().getStationId());
                if (tr != null) {
                    return tr;
                }
            }
        }

        if (to == null && from != null && toRecipe != null) {
            if (toRecipe.getPlaceId() != null) {
                TransportRoute tr = ServiceLocator
                        .getInstance()
                        .getDatabaseAdapter()
                        .getTransportRoute(from.getTask().getStationId() + "_" + toRecipe.getPlaceId());
                if (tr != null) {
                    return tr;
                }
            }
            if (toRecipe.getRackId() != null) {
                TransportRoute tr = ServiceLocator
                        .getInstance()
                        .getDatabaseAdapter()
                        .getTransportRoute(from.getTask().getStationId() + "_" + toRecipe.getRackId());
                if (tr != null) {
                    return tr;
                }
            }
            if (toRecipe.getStorageId() != null) {
                TransportRoute tr = ServiceLocator
                        .getInstance()
                        .getDatabaseAdapter()
                        .getTransportRoute(from.getTask().getStationId() + "_" + toRecipe.getStorageId());
                return tr;
            }
        }

        return null;
    }

    public void generateTable(DaoAdapter daoAdapter, List<TransportSheet> transportSheets) {

        TransportLocationTree tree = new TransportLocationTree();
        List<Room> rooms = daoAdapter.loadRooms();
        for (Room r : rooms) {
            TransportNode tn = new TransportNode(r.getName(), r.getId(), ROOM);
            tree.roomNodes.put(r.getId(), tn);
        }

        List<Station> stations = daoAdapter.loadStations();
        for (Station s : stations) {
            TransportNode tn = new TransportNode(s.getName(), s.getId(), STATION);
            TransportNode t = tree.roomNodes.get(s.getRoom());
            if (t != null) {
                t.add(tn);
            }
            tree.stationsNodes.put(s.getId(), tn);
        }

        List<Machine> machines = daoAdapter.loadMachines();
        for (Machine m : machines) {
            TransportNode tn = new TransportNode(m.getName(), m.getId(), MACHINE);
            TransportNode t = tree.roomNodes.get(m.getRoom());
            if (t != null) {
                t.add(tn);
            }
            tree.machineNodes.put(m.getId(), tn);
        }

        List<Storage> storages = daoAdapter.loadStorage();
        for (Storage s : storages) {
            TransportNode tn = new TransportNode(s.getName(), s.getId(), STORAGE);
            TransportNode t = tree.roomNodes.get(s.getRoomId());
            if (t != null) {
                t.add(tn);
            }
            tree.storageNodes.put(s.getId(), tn);
        }

        List<Rack> racks = daoAdapter.loadRacks();
        for (Rack r : racks) {
            TransportNode tn = new TransportNode(r.getName(), r.getId(), RACK);
            TransportNode t = tree.storageNodes.get(r.getStorageId());
            if (t != null) {
                t.add(tn);
            }
            tree.rackNodes.put(r.getId(), tn);
        }

        List<Place> place = daoAdapter.loadPlaces();
        for (Place p : place) {
            TransportNode tn = new TransportNode(p.getName(), p.getId(), PLACE);
            TransportNode t = tree.rackNodes.get(p.getRackId());
            if (t != null) {
                t.add(tn);
            }
            tree.placeNodes.put(p.getId(), tn);
        }

        //generate(0,A,B);

        Map<String, Transport> map = new HashMap<>();


        TransportSheet ts1 = new TransportSheet();
        ts1.setTransport_mode(1);

        TransportSheet ts2 = new TransportSheet();
        ts2.setTransport_mode(0);


        for (TransportSheet t : transportSheets) {
            TransportNode from = getTransportNode(tree, t.getFrom_type(), t.getFrom_uuid());
            if (from == null) continue;
            TransportNode to = getTransportNode(tree, t.getTo_type(), t.getTo_uuid());
            if (to == null) continue;
            generate(getLocationLevel(t.getTo_type()), from, to, map, t);
        }

        List<TransportRoute> transportRoutes = new ArrayList<>();
        for (String s : map.keySet()) {
            Transport t = map.get(s);
            TransportSheet ts = t.ts;
            TransportRoute tr = new TransportRoute();
            tr.setType(ts.getTransport_mode());
            tr.setKey(s);
            String from = getLocationName(tree, ts.getFrom_type(), ts.getFrom_uuid());
            String to = getLocationName(tree, ts.getTo_type(), ts.getTo_uuid());
            tr.setName("Transport from " + from + " to " + to);
            tr.setRouteId(ts.getUuid());
            transportRoutes.add(tr);
        }
        daoAdapter.insertTransportRoutes(transportRoutes);
    }

    private TransportNode getTransportNode(TransportLocationTree tree, String type, String id) {
        switch (type) {
            case ROOM:
                return tree.roomNodes.get(id);
            case STATION:
                return tree.stationsNodes.get(id);
            case MACHINE:
                return tree.machineNodes.get(id);
            case STORAGE:
                return tree.storageNodes.get(id);
            case RACK:
                return tree.rackNodes.get(id);
            case PLACE:
                return tree.placeNodes.get(id);
        }
        return null;
    }

    private String getLocationName(TransportLocationTree tree, String type, String id) {
        switch (type) {
            case ROOM:
                return tree.roomNodes.get(id).name;
            case STATION:
                return tree.stationsNodes.get(id).name;
            case MACHINE:
                return tree.machineNodes.get(id).name;
            case STORAGE:
                return tree.storageNodes.get(id).name;
            case RACK:
                return tree.rackNodes.get(id).name;
            case PLACE:
                return tree.placeNodes.get(id).name;
        }
        return "";
    }

    /**
     * @param level
     * @param s     source
     * @param d     destination
     * @param map
     * @param ts
     */
    public void generate(int level, TransportNode s, TransportNode d, Map<String, Transport> map, TransportSheet ts) {
        if (s == null) {
            return;
        }
        fillMap(level, s, d, map, ts);
        if (s.subNodes == null) {
            return;
        }
        for (TransportNode transportNode : s.subNodes) {
            generate(1, transportNode, d, map, ts);
        }
    }

    public void fillMap(int level, TransportNode source, TransportNode destination, Map<String, Transport> map, TransportSheet ts) {
        if (destination == null) {
            return;
        }
        //Fill Page
        //String key= source.uuid+"_"+source.type+"_"+destination.uuid+"_"+destination.type;
        String key = source.uuid + "_" + destination.uuid;
        Transport t = map.get(key);
        int locationLevel = getLocationLevel(destination.type);
        if (t == null) {
            Transport t1 = new Transport();
            t1.transportLevel = level;
            //t1.isInherited = isInherited;
            t1.ts = ts;
            map.put(key, t1);
        } else {
            if (locationLevel >= t.transportLevel) {
                Transport t1 = new Transport();
                t1.transportLevel = level;
                //t1.isInherited = isInherited;
                t1.ts = ts;
                map.put(key, t1);
            }
        }
        if (destination.subNodes == null) {
            return;
        }
        for (TransportNode transportNode : destination.subNodes) {
            fillMap(level, source, transportNode, map, ts);
        }
    }

    public int getLocationLevel(String type) {
        switch (type) {
            case ROOM:
                return 0;
            case STATION:
            case MACHINE:
            case STORAGE:
                return 1;
            case RACK:
                return 2;
            case PLACE:
                return 3;
        }
        return -1;
    }


    class TransportNode {
        String uuid;
        String type;
        String name;

        public TransportNode(String name, String uuid, String type) {
            this.name = name;
            this.uuid = uuid;
            this.type = type;
        }

        List<TransportNode> subNodes;

        public void add(TransportNode t) {
            if (subNodes == null) {
                subNodes = new ArrayList<>();
            }
            subNodes.add(t);
        }
    }

    class Transport {
        int transportLevel;
        boolean isInherited;
        TransportSheet ts;
    }

    class TransportLocationTree {
        Map<String, TransportNode> roomNodes = new HashMap<>();
        Map<String, TransportNode> stationsNodes = new HashMap<>();
        Map<String, TransportNode> machineNodes = new HashMap<>();
        Map<String, TransportNode> storageNodes = new HashMap<>();
        Map<String, TransportNode> rackNodes = new HashMap<>();
        Map<String, TransportNode> placeNodes = new HashMap<>();
    }

}
