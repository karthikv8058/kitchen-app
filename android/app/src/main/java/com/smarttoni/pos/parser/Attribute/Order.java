package com.smarttoni.pos.parser.Attribute;

public class Order extends ParentAttribute implements Attribute {
    @Override
    public void setValue(String value) {
        // TODO Auto-generated method stub
        this.value = value;
    }

    @Override
    public void setKey(String key) {
        // TODO Auto-generated method stub
        this.key = key;
    }

    @Override
    public String getValue() {
        // TODO Auto-generated method stub
        return this.value;
    }

    @Override
    public String getKey() {
        // TODO Auto-generated method stub
        return this.key;
    }

}
