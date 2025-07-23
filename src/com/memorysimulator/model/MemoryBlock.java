package com.memorysimulator.model;

public class MemoryBlock {
    private int startAddress;
    private int size;
    private boolean isFree;
    private String processId;

    public MemoryBlock(int startAddress, int size) {
        this.startAddress = startAddress;
        this.size = size;
        this.isFree = true;
        this.processId = "";
    }

    public boolean allocate(String pid, int processSize) {
        if (isFree && processSize <= size) {
            this.processId = pid;
            this.isFree = false;
            return true;
        }
        return false;
    }

    public void deallocate() {
        this.isFree = true;
        this.processId = "";
    }

    public int getStartAddress() { return startAddress; }
    public int getSize() { return size; }
    public boolean isFree() { return isFree; }
    public String getProcessId() { return processId; }
   
    public void setSize(int size) {
        this.size = size;
    }

}
