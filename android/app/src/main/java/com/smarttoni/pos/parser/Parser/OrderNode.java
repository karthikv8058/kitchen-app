package com.smarttoni.pos.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class OrderNode<T> {
    private T data = null;

    private List<OrderNode<T>> children = new ArrayList<>();

    private boolean isRepeating = false;

    private OrderNode<T> parent = null;

    public OrderNode(T data) {
        this.data = data;
    }

    public OrderNode<T> addChild(OrderNode<T> child) {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public void addChildren(List<OrderNode<T>> children) {
        for (OrderNode<T> orderNode : children) {
            orderNode.setParent(this);
        }

        this.children.addAll(children);
    }

    public List<OrderNode<T>> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(OrderNode<T> parent) {
        this.parent = parent;
    }

    public OrderNode<T> getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "OrderNode [data=" + data + ", children=" + children + "]";
    }

    public void setIsRepeating(boolean val) {
        this.isRepeating = val;
    }

    public boolean isRepeating() {
        return this.isRepeating;
    }
}
