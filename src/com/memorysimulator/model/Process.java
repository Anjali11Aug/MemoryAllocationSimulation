package com.memorysimulator.model;

public class Process {
    private String id;
    private int size;

    public Process(String id, int size) {
        this.id = id;
        this.size = size;
    }

    public String getId() { return id; }
    public int getSize() { return size; }
}
