package com.smarttoni.pos.parser.Attribute;

import com.smarttoni.pos.parser.Parser.Pair;

import java.util.ArrayList;
import java.util.List;

public class GenericAttribute {
    public String name;
    public List<Pair> keyValuepairs = new ArrayList<>();
    public List<GenericAttribute> children = new ArrayList<GenericAttribute>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(Pair pair) {
        keyValuepairs.add(pair);
    }

    public List<Pair> getKeyValuepairs() {
        return keyValuepairs;
    }

    public void setKeyValuepairs(List<Pair> keyValuepairs) {
        this.keyValuepairs = keyValuepairs;
    }

    public List<GenericAttribute> getChildren() {
        return children;
    }

    public void setChildren(List<GenericAttribute> children) {
        this.children = children;
    }

    public void addchildren(GenericAttribute attr) {
        this.children.add(attr);
    }


}
