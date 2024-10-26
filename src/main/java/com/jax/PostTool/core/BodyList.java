package com.jax.PostTool.core;

import java.util.ArrayList;

public class BodyList extends ArrayList<Object> {

    public BodyList addObj(BodyObject obj) {
        add(obj);
        return this;
    }

    public BodyList addList(BodyList list) {
        add(list);
        return this;
    }

    public BodyList addString(String string) {
        add(string);
        return this;
    }

    public BodyList addInt(Integer integer) {
        add(integer);
        return this;
    }

}
