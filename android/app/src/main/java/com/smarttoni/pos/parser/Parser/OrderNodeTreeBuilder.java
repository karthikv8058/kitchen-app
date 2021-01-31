package com.smarttoni.pos.parser.Parser;

import android.content.Context;

import com.smarttoni.pos.interceptor.PrinterMessageHandler;
import com.smarttoni.pos.parser.Attribute.Attribute;
import com.smarttoni.pos.parser.Attribute.ConcreteAttribute;
import com.smarttoni.pos.parser.Attribute.Order;
import com.smarttoni.pos.parser.Attribute.Separator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class OrderNodeTreeBuilder {

    private OrderNode orderNode;
    private Context context;
    private String order;
    private String ip;

    public OrderNodeTreeBuilder(Context context, String template, String order, String ip) {
        this.order = order;
        this.ip = ip;
        this.context = context;
        this.orderNode = buildTree(template);
    }

    private OrderNode buildTree(String template) {

        Document doc = convertStringToDocument(template);

        // get the first element
        Element element = doc.getDocumentElement();

        // get all child nodes
        NodeList nodes = element.getChildNodes();

        // print the text content of each child
        Order orderElement = new Order();
        orderElement.setKey(element.getNodeName());
        orderElement.setValue(element.getTextContent());
        OrderNode node = new OrderNode<com.smarttoni.pos.parser.Attribute.Attribute>(orderElement);
        for (int i = 0; i < nodes.getLength(); i++) {
            parseNode(nodes.item(i), element, node);
        }
        return node;

    }


    public OrderNode getTree() {
        return orderNode;
    }

    private Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            new PrinterMessageHandler(this.context, this.order, this.ip).saveMessage();
            e.printStackTrace();
        }
        return null;
    }


    private void parseNode(Node node, Node parentNode, OrderNode parentOrderNode) {
        if (node.hasChildNodes()) {
            parentOrderNode = addAttributeChildToNode(parentOrderNode, node);
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                parseNode(node.getChildNodes().item(i), node, parentOrderNode);
            }
        } else {

            switch (node.getNodeName()) {
                case "#text":
                    addSeparatorStringAsChild(parentOrderNode, node);
                    break;
                default:
                    addAttributeChildToNode(parentOrderNode, node);
                    break;
            }
        }
    }

    private OrderNode addAttributeChildToNode(OrderNode<Attribute> orderNode2,
                                              Node node) {
        OrderNode addedOrderNode;
        switch (node.getNodeName()) {

            default:
                ConcreteAttribute concreteAttribute1 = new ConcreteAttribute();
                concreteAttribute1.setKey(node.getNodeName());
                concreteAttribute1.setValue(node.getTextContent());
                addedOrderNode = createAttributeNode(concreteAttribute1, orderNode2, node);
                break;
        }

        return addedOrderNode;

    }

    private OrderNode createAttributeNode(Attribute attr, OrderNode<Attribute> orderNode3, Node node) {
        OrderNode attrNode = new OrderNode<Attribute>(attr);
        orderNode3.addChild(attrNode);
        if (!orderNode3.getData().getKey().startsWith(((Attribute) attrNode.getData()).getKey())) {
            orderNode3.setIsRepeating(true);
        }

        return attrNode;
    }

    private void addSeparatorStringAsChild(OrderNode<Attribute> orderNode2,
                                           Node node) {
        // TODO Auto-generated method stub
        Separator separator = new Separator();
        separator.setKey(node.getNodeName());
        separator.setValue(node.getTextContent());
        OrderNode separatorNode = new OrderNode<Attribute>(separator);
        orderNode2.addChild(separatorNode);

    }


}
