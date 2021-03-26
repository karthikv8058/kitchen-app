package com.smarttoni.assignment.order;

import com.smarttoni.entities.Course;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;

import java.util.Date;
import java.util.UUID;

/**
 * Planning
 */
public class OrderBuilder {

    private Order order;

    public OrderBuilder(){
        order = new Order();
        order.setId(UUID.randomUUID().toString());
        long createAt = new Date().getTime();
        order.setCreatedAt(createAt);
        order.setUpdatedAt(createAt);
    }

    public CourseBuilder createCourse() {
        return new CourseBuilder(this);
    }


    private String getOrderId() {
        return this.order.getId();
    }

    public Order build(){
        return this.order;
    }

    class CourseBuilder {

        private OrderBuilder orderBuilder;

        private Course course;

        private CourseBuilder(OrderBuilder orderBuilder){
            this.orderBuilder = orderBuilder;
            course = new Course();
            course.setId(UUID.randomUUID().toString());
            course.setOrderId(orderBuilder.getOrderId());
        }

        public CourseBuilder setDeliveryTime(long deliveryTime){
            course.setDeliveryTime(deliveryTime);
            return this;
        }

        public MealBuilder createMeal(){
            return new MealBuilder(orderBuilder,this);
        }


        private String getCourseId() {
            return this.course.getId();
        }


        public OrderBuilder saveCourse(){
            return orderBuilder;
        }
    }


    class MealBuilder {

        private OrderBuilder orderBuilder;

        private CourseBuilder courseBuilder;

        private Meal meal;

        private MealBuilder(OrderBuilder orderBuilder,CourseBuilder courseBuilder){
            this.orderBuilder = orderBuilder;
            this.courseBuilder = courseBuilder;
            meal = new Meal();
            meal.setCourseId(courseBuilder.getCourseId());
        }

        public MealBuilder addOrderLine(String recipeId,float qty){
            OrderLine orderLine = new OrderLine();
            orderLine.setMealId(meal.getId());
            orderLine.setOrderId(orderBuilder.getOrderId());
            orderLine.setCourseId(courseBuilder.getCourseId());
            orderLine.setRecipeId(recipeId);
            orderLine.setQty(qty);
            return this;
        }

        public CourseBuilder saveMeal(){
            return courseBuilder;
        }
    }

}
