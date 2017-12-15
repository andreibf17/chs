package com.chs.app.entities;

/**
 * Created by Bogdan Cristian Vlad on 08-Dec-17.
 */

public class Mode {

    private String name;
    private int index;

    public int getIndex() { return index; }

    public void setIndex(int index) { this.index = index; }

    public Mode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
