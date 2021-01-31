package com.smarttoni.pos.parser.Parser;

import com.smarttoni.pos.parser.Attribute.Attribute;
import com.smarttoni.pos.parser.Attribute.GenericAttribute;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private GenericAttribute attr = null;

    public Parser() {
        this.attr = new GenericAttribute();
        attr.setName("ORDER");
    }

    public GenericAttribute parse(OrderNode orderNode, String order, GenericAttribute parentAttr) {
        if (parentAttr == null) parentAttr = attr;
        List<OrderNode> nodeList = orderNode.getChildren();

        for (int i = 0; i < nodeList.size(); i++) {
//            check whether a separator
            if (((Attribute) nodeList.get(i).getData()).getKey() != null) {
                OrderNode backNode, secondBackNode, frontNode, secondFrontNode;
                backNode = secondBackNode = frontNode = secondFrontNode = null;
                if (i > 0) backNode = nodeList.get(i - 1);
                if (i > 1) secondBackNode = nodeList.get(i - 2);
                if (i < nodeList.size() - 2) secondFrontNode = nodeList.get(i + 2);
                if (i < nodeList.size() - 1) frontNode = nodeList.get(i + 1);


                fetchAttributeValue(nodeList.get(i), secondBackNode, backNode, frontNode, secondFrontNode, parentAttr, order);
            }
        }
        return this.attr;
    }

    private void fetchAttributeValue(OrderNode orderNode, OrderNode secondBackNode, OrderNode backNode, OrderNode frontNode, OrderNode secondFrontNode, GenericAttribute parentAttr, String order) {
//        check whether it is a self closing attribute: if so find the value and add to parent
        if (orderNode.getChildren().size() == 0) {
//            get the value
            if (backNode == null) { //order starts with the parameter
                if (frontNode != null && ((Attribute) frontNode.getData()).getKey() == null) { //if front node is a separator :: otherwise it is an exception
//                    get string from start to the start of end separator
//                    if the secondbacknode is a separator add that to the first node
                    String lastBreak = ((Attribute) frontNode.getData()).getValue();
                    if (secondFrontNode != null) {
                        if (((Attribute) secondFrontNode.getData()).getKey() == null) {
                            lastBreak += ((Attribute) secondFrontNode.getData()).getValue();
                        }
                    }
                    String attributevalue = StringUtils.substringBefore(order, lastBreak);
                    ((Attribute) orderNode.getData()).setValue(attributevalue);
                    Pair pair = new Pair(((Attribute) orderNode.getData()).getKey(), attributevalue.trim());
                    parentAttr.add(pair);

                }
            } else {
                if (((Attribute) backNode.getData()).getKey() == null) {
                    String firstBreak = ((Attribute) backNode.getData()).getValue();

                    if (secondBackNode != null) {
                        if (((Attribute) secondBackNode.getData()).getKey() == null) {
                            firstBreak = ((Attribute) secondBackNode.getData()).getValue() + firstBreak;
                        } else {
//                            find second backnode value
                            if (((Attribute) secondBackNode.getData()).getValue() != null) {
                                firstBreak = ((Attribute) secondBackNode.getData()).getValue() + firstBreak;
                            }

                        }
                    }


                    if (frontNode != null && ((Attribute) frontNode.getData()).getKey() == null) {

                        String attributevalue = StringUtils.substringBetween(order, firstBreak, ((Attribute) frontNode.getData()).getValue());
                        if (attributevalue == null) {
                            boolean isEndWithnewLine = ((Attribute) frontNode.getData()).getValue().substring(((Attribute) frontNode.getData()).getValue().length() - 1).equals("\n");
                            if (isEndWithnewLine) {
                                String OrderWithNewLine = order + "\n";
                                attributevalue = StringUtils.substringBetween(OrderWithNewLine, firstBreak, ((Attribute) frontNode.getData()).getValue());
                            }


                        }
                        ((Attribute) orderNode.getData()).setValue(attributevalue);
                        if (attributevalue != null) {
                            Pair pair = new Pair(((Attribute) orderNode.getData()).getKey(), attributevalue.replace("\n", ""));
                            parentAttr.add(pair);
                        } else {
                            Pair pair = new Pair(((Attribute) orderNode.getData()).getKey(), attributevalue);
                            parentAttr.add(pair);
                        }


                    }
                }

            }
        } else {

            String orderString = getOrderStringForElement(orderNode, backNode, secondBackNode, frontNode, secondFrontNode, order);


            GenericAttribute attr = new GenericAttribute();
            attr.setName(((Attribute) orderNode.getData()).getKey());
            parentAttr.children.add(attr);
            //            check whther the the current string is a repeating one
            if (orderNode.getChildren().size() > 1) { //repeating attribute
//                split the repeating attributes
                List<String> attrArray = splitOrderStringForRepeatingAttributes(orderNode, orderString);
                for (String str : attrArray) {
                    parse(orderNode, str, attr);
                }
            } else {
                parse(orderNode, orderString, attr);
            }

        }
    }

    private List<String> splitOrderStringForRepeatingAttributes(OrderNode orderNode, String orderString) {
//        check whether the string starts with separator
        OrderNode firstNode = (OrderNode) orderNode.getChildren().get(0);
        if (((Attribute) firstNode.getData()).getKey() == null) {
            String[] stringList = StringUtils.splitByWholeSeparator(orderString, ((Attribute) firstNode.getData()).getValue());
            List<String> list = new ArrayList<>();
            for (String str : stringList) {
                list.add(((Attribute) firstNode.getData()).getValue() + str);
            }
            return list;
        } else {
//            check whether lastnode is separator
            OrderNode lastNode = (OrderNode) orderNode.getChildren().get(orderNode.getChildren().size() - 1);
            if (((Attribute) lastNode.getData()).getKey() == null) {
                String[] stringList = StringUtils.splitByWholeSeparator(orderString, ((Attribute) lastNode.getData()).getValue());
                List<String> list = new ArrayList<>();
                for (String str : stringList) {
                    if (!str.equals("")) {
                        list.add(str + ((Attribute) lastNode.getData()).getValue());
                    }

                }
                return list;
            }

        }

        return null;
    }

    private String getOrderStringForElement(OrderNode orderNode, OrderNode backNode, OrderNode secondBackNode, OrderNode frontNode, OrderNode secondFrontNode, String order) {
        if (backNode == null && frontNode == null && secondBackNode == null && secondFrontNode == null) {
            return order;
        }

        String firstBreak = "";
        if (backNode != null) {
            if (((Attribute) backNode.getData()).getKey() == null) {
                firstBreak = ((Attribute) backNode.getData()).getValue();
            }
        }


        if (secondBackNode != null) {
            if (((Attribute) secondBackNode.getData()).getKey() == null) {
                firstBreak = ((Attribute) secondBackNode.getData()).getValue() + firstBreak;
            }
        }

        String lastBreak = "";
        if (frontNode != null) {
            if (((Attribute) frontNode.getData()).getKey() == null) {
                lastBreak = ((Attribute) frontNode.getData()).getValue();
            }
            if (secondFrontNode != null) {
                if (((Attribute) secondFrontNode.getData()).getKey() == null) {
                    lastBreak += ((Attribute) secondFrontNode.getData()).getValue();
                }
            }
        }
        if (firstBreak == null || firstBreak.equals("")) {
            return StringUtils.substringBeforeLast(order, lastBreak);
        }
        if (lastBreak.equals("")) {
            return StringUtils.substringAfter(order, firstBreak);
        }

        return StringUtils.substringBeforeLast(StringUtils.substringAfter(order, firstBreak), lastBreak);
//        return StringUtils.substring(order, firstBreak, lastBreak);
    }


}
