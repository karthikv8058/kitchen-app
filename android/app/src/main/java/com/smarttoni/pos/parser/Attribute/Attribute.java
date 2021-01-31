package com.smarttoni.pos.parser.Attribute;

public interface Attribute {
    void setValue(String value);

    void setKey(String key);

    String getValue();

    String getKey();
}
