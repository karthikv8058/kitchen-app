package com.smarttoni.server.controlles.inventory;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.entities.ExternalAvailableQuantity;
import com.smarttoni.entities.ExternalOrderRequest;
import com.smarttoni.entities.InventoryReservation;
import com.smarttoni.utils.Strings;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.Inventory;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.Rack;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Room;
import com.smarttoni.entities.Storage;
import com.smarttoni.entities.UnitConversion;
import com.smarttoni.entities.Units;
import com.smarttoni.models.KeyValue;
import com.smarttoni.models.wrappers.InventoryWrapper;
import com.smarttoni.models.wrappers.OpenOrderWrapper;
import com.smarttoni.models.wrappers.RecipeWrapper;
import com.smarttoni.models.wrappers.UnitConversionWrapper;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.UnitHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetInventoryList extends HttpSecurityRequest {
    private Context context;

    public GetInventoryList(Context context) {
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        List<Inventory> inventories = daoAdapter.loadAvailableInventories();

        List<Order> orders = daoAdapter.listOrdersWithOpenStatus();

        //List<String> recipeIds = new ArrayList<>();
        //List<Order> ordersList = daoAdapter.loadOrders();

        List<Order> normalOrders = new ArrayList<>();
        List<Order> inventoryOrders = new ArrayList<>();
        List<Order> externalOrders = new ArrayList<>();
        for (Order o : orders) {
            if (o.getType() == Order.TYPE_EXTERNAL || o.getIsInventory()) {
                externalOrders.add(o);
            } else if (o.getType() == Order.TYPE_INTERNAL) {
                normalOrders.add(o);
            }
        }

//        for (Order order : externalOrders) {
//            order.getCourses();
//            Comparator comparator = (Comparator<Course>) (lhs, rhs) -> Long.compare(rhs.getDeliveryTime(), lhs.getDeliveryTime());
//            Collections.sort(order.getCourses(), comparator);
//            for (Course course : order.getCourses()) {
//                for (Meal meal : course.getMeals()) {
//                    for (OrderLine orderLine : meal.getOrderLine()) {
//                        orderLine.getRecipe();
//                        orderLine.getRecipeId();
//                    }
//                }
//            }
//        }

//        for (Order order : orders) {
//            order.getCourses();
//            Comparator comparator = (Comparator<Course>) (lhs, rhs) -> Long.compare(rhs.getDeliveryTime(), lhs.getDeliveryTime());
//            Collections.sort(order.getCourses(), comparator);
//            for (Course course : order.getCourses()) {
//                for (Meal meal : course.getMeals()) {
//                    for (OrderLine orderLine : meal.getOrderLine()) {
//                        orderLine.getRecipe();
//                        orderLine.getRecipeId();
//                        if (orderLine.getRecipe() != null) {
//                            recipeIds.add(orderLine.getRecipe().getId());
//                        }
//                    }
//                }
//            }
//        }

//        List<Recipe> recipes = ServiceLocator.getInstance().getDatabaseAdapter().loadRecipes();
//        List<Recipe> recipeList = new ArrayList<>();
//        for (Recipe recipe : recipes) {
//            if (recipe.getStatus() == Recipe.STATUS_PUBLISHED || recipeIds.contains(recipe.getId())) {
//                if (recipe.getOutputUnit()!=null){
//                    recipe.setOutputUnit(recipe.getOutputUnit());
//                }
//                recipeList.add(recipe);
//            }
//        }


        List<RecipeWrapper> recipeWrappers = new ArrayList<>();


        List<Recipe> recipes = ServiceLocator.getInstance().getDatabaseAdapter().loadRecipes();

//        List<ExternalAvailableQuantity> eaqs = daoAdapter.listExternalAvailableQuantity();
//
        List<ExternalOrderRequest> eors = daoAdapter.listExternalOrderRequest();
//
//
//        Map<String, Float> recipeMap = new HashMap<>();
//
//        for (ExternalOrderRequest eor : eors) {
//            Float qty = recipeMap.get(eor.getRecipe());
//            if (qty == null) {
//                qty = 0F;
//            }
//            recipeMap.put(eor.getRecipe(), qty + eor.getQuantity());
//        }

        for (Recipe recipe : recipes) {
            if (recipe.getStatus() != Recipe.STATUS_PUBLISHED) {
                continue;
            }
            RecipeWrapper recipeWrapper = new RecipeWrapper();
            recipeWrapper.setRecipeName(recipe.getName());
            recipeWrapper.setUuid(recipe.getId());
            recipeWrapper.setRecipeId(recipe.getId());
            recipeWrapper.setImage(recipe.getImageUrl());
            recipeWrapper.setStorageId(recipe.getStorageId());
            recipeWrapper.setRoomId(recipe.getRoomId());
            recipeWrapper.setRackId(recipe.getRackId());
            recipeWrapper.setPlaceId(recipe.getPlaceId());
            float inventoryQty = getInventoryOutPut(recipe.getId(), inventories, recipeWrapper);
            if (Strings.isEmpty(recipeWrapper.getCurrentInventory()) && recipe.getOutputUnit() != null) {
                recipeWrapper.setCurrentInventory(Strings.formatUnitString(0, recipe.getOutputUnit().getSymbol()));
            }

            recipeWrapper.setInventoryQuantity(inventoryQty);
            if (recipe.getProductBarcode() != null) {
                recipeWrapper.setProductBarcode(recipe.getProductBarcode());
            } else {
                recipeWrapper.setProductBarcode("");

            }

            recipeWrapper.setExpanded(false);
            recipeWrapper.setOutPutQuantity(recipe.getOutputQuantity());
            if (recipe.getOutputUnit() != null) {
                recipeWrapper.setOutPutUnit(recipe.getOutputUnit().getSymbol());
            }
            recipeWrapper.setUnitId(recipe.getOutputUnitId());
            recipeWrapper.setInventoryType(recipe.getInventoryType());

            List<OpenOrderWrapper> externalOrderWrappers = new ArrayList<>();


            for (Order externalOrder : externalOrders) {

                List<OrderLine> orderLines = daoAdapter.getOrderLinesByOrder(externalOrder.getId());


                for (Meal meal : externalOrder.getCourses().get(0).getMeals()) {
                    for (OrderLine orderLine : meal.getOrderLine()) {
                        if (recipe.getId().equals(orderLine.getRecipe().getId())) {

                            OpenOrderWrapper externalOrderWrapper = new OpenOrderWrapper();
                            externalOrderWrapper.setQty(orderLine.getRecipe().getOutputQuantity() * orderLine.getQty());
                            externalOrderWrapper.setDeliveryTime(externalOrder.getCourses().get(0).getDeliveryTime());
                            if (orderLine.getRecipe().getOutputUnit() != null) {
                                externalOrderWrapper.setUnit(orderLine.getRecipe().getOutputUnit().getSymbol());
                            }
                            if (recipe.getOutputUnit() != null) {
                                externalOrderWrapper.setQuantity(UnitHelper.convertToString(externalOrderWrapper.getQty(), recipe.getOutputUnit()));

                            }
                            externalOrderWrappers.add(externalOrderWrapper);
                        }
                    }
                }
            }

            List<OpenOrderWrapper> openOrderWrappers = new ArrayList<>();
            for (Order openOrders : normalOrders) {
//                if(openOrders.getType() == Order.TYPE_EXTERNAL || openOrders.getIsInventory()){
//                    continue;
//                }

                OpenOrderWrapper openOrderWrapper = new OpenOrderWrapper();
                openOrderWrapper.setDeliveryTime(openOrders.getCourses().get(0).getDeliveryTime());
                openOrderWrapper.setQty(0F);

                for (Meal meal : openOrders.getCourses().get(0).getMeals()) {
                    for (OrderLine orderLine : meal.getOrderLine()) {
                        if (recipe.getId().equals(orderLine.getRecipe().getId())) {
                            openOrderWrapper.setQty(openOrderWrapper.getQty() + (orderLine.getRecipe().getOutputQuantity() * orderLine.getQty()));
                        }
                    }
                }


                if (openOrderWrapper.getQty() != 0) {
                    if (recipe.getOutputUnit() != null) {
                        openOrderWrapper.setUnit(recipe.getOutputUnit().getSymbol());
                    }
                    if (recipe.getOutputUnit() != null) {
                        openOrderWrapper.setQuantity(UnitHelper.convertToString(openOrderWrapper.getQty(), recipe.getOutputUnit()));
                    }
                    openOrderWrappers.add(openOrderWrapper);
                }


            }


            outLoop: for (ExternalOrderRequest eor : eors) {
                if (recipe.getId().equals(eor.getRecipe())) {
                    OpenOrderWrapper openOrderWrapper = new OpenOrderWrapper();
                    openOrderWrapper.setQty(eor.getQuantity());
                    Order order = daoAdapter.getOrderById(eor.getParentOrder());

                    List<OrderLine> orderLines = daoAdapter.getOrderLinesByOrder(order.getId());

                    //TODO update
                    for(OrderLine orderLine : orderLines ){
                        if(orderLine.getRecipeId().equals(recipe.getId())){
                            float qty = openOrderWrapper.getQty()  - (orderLine.getQty() * recipe.getOutputQuantity());
                            if(qty > 0){
                                openOrderWrapper.setQty(qty);
                            }else{
                                openOrderWrapper.setQty(0f);
                            }
                        }
                    }

                    openOrderWrapper.setDeliveryTime(order.getCourses().get(0).getDeliveryTime());
                    openOrderWrapper.setUnit(recipe.getOutputUnit().getSymbol());
                    if (recipe.getOutputUnit() != null) {
                        openOrderWrapper.setQuantity(UnitHelper.convertToString(openOrderWrapper.getQty(), recipe.getOutputUnit()));
                    }
                    if(openOrderWrapper.getQty() > 0 ){
                        openOrderWrappers.add(openOrderWrapper);
                    }
                }
            }


            float delivery = 0;

            for (OpenOrderWrapper order : externalOrderWrappers) {
                delivery += order.getQty();
            }

            float consumption = 0;
            for (OpenOrderWrapper order : openOrderWrappers) {
                consumption += order.getQty();
            }

//            if (recipeMap.get(recipe.getId()) != null && (consumption - recipeMap.get(recipe.getId()) > 0)) {
//                consumption = consumption - recipeMap.get(recipe.getId());
//            }


            List<InventoryReservation> reservations = daoAdapter.listInventoryReservationsForRecipe(recipe.getId());


            float reserved = 0;
            for(InventoryReservation ir : reservations){
                reserved += ir.getQty();
            }

            float expected = (inventoryQty + delivery  + reserved)  - consumption;
            if (recipe.getOutputUnit() != null) {
                recipeWrapper.setExpectedInventory(UnitHelper.convertToString(expected > 0 ? expected : 0, recipe.getOutputUnit()));

            }
            recipeWrapper.setOpenOrderWrappers(openOrderWrappers);
            recipeWrapper.setExternalOrderWrappers(externalOrderWrappers);
            recipeWrappers.add(recipeWrapper);
        }

        List<Room> rooms = daoAdapter.loadRooms();
        for (Room room : rooms) {
            room.getStorage();
            for (Storage storage : room.getStorage()) {
                for (Rack rack : storage.getRacks()) {
                    rack.getPlaces();
                }
            }
        }

        InventoryWrapper inventoryWrapper = new InventoryWrapper();
        inventoryWrapper.recipes = recipeWrappers;
        inventoryWrapper.rooms = rooms;


        List<Units> units = daoAdapter.loadUnits();
        Map<String, String> map = new HashMap<>();
        for (Units u : units) {
            map.put(u.getId(), u.getSymbol());
        }

        List<UnitConversion> conversions = daoAdapter.listUnitConversions();
        List<UnitConversionWrapper> list = new ArrayList<>();
        for (UnitConversion conversion : conversions) {
            list.add(new UnitConversionWrapper(map.get(conversion.getTo()), conversion.getFrom(), conversion.getTo()));
        }

        inventoryWrapper.unitConversions = list;
        inventoryWrapper.units = units;

        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<InventoryWrapper>() {
        }.getType();
        response.send(gson.toJson(inventoryWrapper, type));
    }

    private float getInventoryOutPut(String recipeId, List<Inventory> inventories, RecipeWrapper recipeWrapper) {
        float qty = 0;
        for (Inventory inventory : inventories) {
            if (inventory.getRecipeUuid().equals(recipeId)) {
                qty = inventory.getQuantity();
                if (inventory.getRecipe().getOutputUnit() != null) {
                    recipeWrapper.setCurrentInventory(UnitHelper.convertToString(qty, inventory.getRecipe().getOutputUnit()));

                }
                return qty;
            }
        }
        return qty;
    }


}