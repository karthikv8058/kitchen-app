package com.smarttoni.utils;

import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;

import java.util.UUID;

public class OrderBuilder {

    Order order;

    public OrderBuilder OrderBuilder(){
        order= new Order();
        return  this;
    }


    public OrderBuilder addOrderLine(){
//        rderLine orderLine = new OrderLine();
//        orderLine.setUuid(UUID.randomUUID().toString());
//        orderLine.setRecipeId(recipeId);
//        orderLine.setQty(quantity);
//        daoAdapter.saveOrderLine(orderLine);
        return  this;
    }

}
