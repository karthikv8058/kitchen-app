package com.smarttoni.pos.parser;

import android.content.Context;

import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.Recipe;
import com.smarttoni.pos.interceptor.PrinterMessageHandler;
import com.smarttoni.pos.parser.Attribute.GenericAttribute;
import com.smarttoni.pos.parser.Parser.OrderNode;
import com.smarttoni.pos.parser.Parser.OrderNodeTreeBuilder;
import com.smarttoni.pos.parser.Parser.Pair;
import com.smarttoni.pos.parser.Parser.Parser;

import java.text.Normalizer;
import java.util.Date;

public class PosOrderParser {

    private GreenDaoAdapter greenDaoAdapter;
    private Context context;

    public PosOrderParser(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    public void parse(String template, String order, String ip, String realMessage) {

        if (template != null) {
            template =
                    Normalizer
                            .normalize(template, Normalizer.Form.NFD)
                            .replaceAll("[^\\p{ASCII}]", "");
        } else {
            new PrinterMessageHandler(context, order, ip).saveMessage();
            return;
        }
        try {
            OrderNode orderNode = new OrderNodeTreeBuilder(context, template, order, ip).getTree();
            Parser parser = new Parser();
            GenericAttribute attr = parser.parse(orderNode, order, null);
            if (validateAttributeTree(attr)) {
                convertGenericAttributeTreeToOrder(attr);
            } else {
//                save it as is
                new PrinterMessageHandler(context, order, ip).saveMessage();
            }
        } catch (Exception e) {
            new PrinterMessageHandler(context, order, ip).saveMessage();
        }

//        verify the generic attribute tree if not valid return;


    }

    private boolean validateAttributeTree(GenericAttribute attr) {
//        check whether the tree has all order values
        if (attr.getName().equals("ORDER")) {
//            check whether it has one or more childrens
            if (attr.getChildren().size() > 0) {
//                if the size is one check whether iot is the courses tag
                if (attr.getChildren().size() == 1 && attr.getChildren().get(0).getName().toLowerCase().equals(ParserTags.COURSES)) {
                    GenericAttribute courses = attr.getChildren().get(0);
//                    check it has atleast one course in it
                    if (courses.getChildren().size() > 0) {
                        return validateCourses(courses);
                    } else {
                        return false;
                    }
                } else if (attr.getChildren().size() > 1) {
                    return validateCourses(attr);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    private boolean validateCourses(GenericAttribute attr) {
//        check whether courses contains course attributes
        for (GenericAttribute attribute : attr.getChildren()) {
            if (attribute.getName().toLowerCase().equals(ParserTags.COURSE)) {
                return (validateCourse(attribute));
            }
        }

        return false;

    }

    private boolean validateCourse(GenericAttribute attribute) {
//        check whether it has a meal in it
        if (attribute.getChildren().size() > 0) {
            if (attribute.getChildren().size() == 1 && attribute.getChildren().get(0).getName().toLowerCase().equals(ParserTags.MEALS)) {
                GenericAttribute meals = attribute.getChildren().get(0);
                if (meals.getChildren().size() > 0) {
                    return validateMeals(meals);
                } else {
                    return false;
                }
            } else if (attribute.getChildren().size() > 1) {
                return validateMeals(attribute);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean validateMeals(GenericAttribute meals) {
        for (GenericAttribute meal : meals.getChildren()) {
//            check whether it has atleast a recipe
            if (meal.getKeyValuepairs().size() == 0) {
                return false;
            }
        }

        return true;
    }

    private void convertGenericAttributeTreeToOrder(GenericAttribute attr) {
        Order newOrder = new Order();
        newOrder.setIsInventory(false);
        newOrder.setCreatedAt(new Date().getTime());
        newOrder.setTableNo(getAttr(attr, ParserTags.TABLE));
        newOrder.setStatus(Order.ORDER_OPEN);
        greenDaoAdapter.saveOrder(newOrder);
        GenericAttribute courses = getChild(attr, ParserTags.COURSES);
        createCourses(newOrder, courses);

//        AssignmentFactory  assignmentFactory = (AssignmentFactory) ServiceLocator.getInstance().getService(ServiceLocator.ASSIGNMENT_SERVICE);
//        assignmentFactory.processOrder(context,newOrder);
        greenDaoAdapter.deleteOrder(newOrder);


    }


    private GenericAttribute getChild(GenericAttribute attr, String name) {
        for (GenericAttribute child : attr.getChildren()) {
            if (child.getName().toLowerCase().equals(name.toLowerCase())) {
                return child;
            }
        }

        return null;
    }

    private void createCourses(Order newOrder, GenericAttribute courses) {
        for (GenericAttribute course : courses.getChildren()) {
            Course newCourse = new Course();
            newCourse.setCourseName("course 1");
            newCourse.setDeliveryTime(new Date().getTime());
            //newCourse.setOrderId(newOrder.getId());
            newCourse.setTableNo(newOrder.getTableNo());
            newCourse.setIsOnCall(true);
            greenDaoAdapter.saveCourse(newCourse);
            GenericAttribute meals = getChild(course, ParserTags.MEALS);
            createMeals(newOrder, newCourse, meals);
        }
    }

    private void createMeals(Order newOrder, Course newCourse, GenericAttribute meals) {
        for (GenericAttribute meal : meals.getChildren()) {
            Meal newMeal = new Meal();
            newMeal.setCourseId(newCourse.getId());
            newMeal.setCourseName(newCourse.getCourseName());
//            get first recipe name
            String recipeName = getAttr(meal, ParserTags.RECIPE);
            newMeal.setName(recipeName);
            newMeal.setTableNo(newCourse.getTableNo());
            greenDaoAdapter.saveMeal(newMeal);

            createRecipes(newOrder, newCourse, newMeal, meal);
        }
    }

    private void createRecipes(Order newOrder, Course newCourse, Meal newMeal, GenericAttribute meal) {
        boolean gotAmount = false;
        String amount = "1";
        boolean gotRecipe = false;
        String recipe = "";
        for (Pair pair : meal.getKeyValuepairs()) {
            if (pair.getKey().toLowerCase().equals(ParserTags.AMOUNT.toLowerCase())) {
                gotAmount = true;
                amount = pair.getValue();
            } else if (pair.getKey().toLowerCase().equals(ParserTags.RECIPE.toLowerCase())) {
                gotRecipe = true;
                recipe = pair.getValue().trim();
            }
            if (gotAmount && gotRecipe) {
//                get recipe
                Recipe actualRecipe = greenDaoAdapter.getRecipeByPrinterName(recipe);
                if (actualRecipe != null) {
                    gotAmount = false;
                    gotRecipe = false;


                    OrderLine orderLine = new OrderLine();
                    orderLine.setOrderId(newOrder.getId());
                    orderLine.setMealId(newMeal.getId());
                    orderLine.setRecipe(actualRecipe);
                    orderLine.setModifiers(newMeal.getModifier());
                    greenDaoAdapter.saveOrderLine(orderLine);
                }


            }
        }
    }


    private String getAttr(GenericAttribute attr, String key) {
        for (Pair pair : attr.getKeyValuepairs()) {
            if (pair.getKey().toLowerCase().equals(key.toLowerCase()) && !pair.getValue().equals("")) {
                return pair.getValue();
            }
        }
        return null;
    }
}


