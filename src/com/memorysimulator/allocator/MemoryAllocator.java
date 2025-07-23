package com.memorysimulator.allocator;

import com.memorysimulator.model.MemoryBlock;
import com.memorysimulator.model.Process;
import com.memorysimulator.utils.AllocationStrategy;

import java.util.*;

public class MemoryAllocator {
    private LinkedList<MemoryBlock> blocks;
    private AllocationStrategy strategy;

    public MemoryAllocator(List<MemoryBlock> initialBlocks, AllocationStrategy strategy) {
        this.blocks = new LinkedList<>();
        for (MemoryBlock block : initialBlocks) {
            this.blocks.add(new MemoryBlock(block.getStartAddress(), block.getSize())); // Deep copy
        }
        this.strategy = strategy;
    }

    public void setStrategy(AllocationStrategy strategy) {
        this.strategy = strategy;
    }

    public List<MemoryBlock> getBlocks() {
        return blocks;
    }

    public boolean allocate(Process p) {
        MemoryBlock selectedBlock = null;
        int minDiff = Integer.MAX_VALUE;

        for (MemoryBlock block : blocks) {
            if (block.isFree() && block.getSize() >= p.getSize()) {
                if (strategy == AllocationStrategy.FIRST_FIT) {
                    selectedBlock = block;
                    break;
                } else if (strategy == AllocationStrategy.BEST_FIT) {
                    int diff = block.getSize() - p.getSize();
                    if (diff < minDiff) {
                        minDiff = diff;
                        selectedBlock = block;
                    }
                }
            }
        }

        if (selectedBlock == null) return false;

        int originalSize = selectedBlock.getSize();
        selectedBlock.allocate(p.getId(), p.getSize());
        selectedBlock.setSize(p.getSize());

        int remaining = originalSize - p.getSize();
        if (remaining > 0) {
            int insertIndex = blocks.indexOf(selectedBlock) + 1;
            blocks.add(insertIndex, new MemoryBlock(
                    selectedBlock.getStartAddress() + p.getSize(),
                    remaining
            ));
        }

        return true;
    }

    public boolean deallocate(String processId) {
        for (int i = 0; i < blocks.size(); i++) {
            MemoryBlock block = blocks.get(i);
            if (!block.isFree() && block.getProcessId().equals(processId)) {
                block.deallocate();

                // Merge with previous block if it's free
                if (i > 0 && blocks.get(i - 1).isFree()) {
                    MemoryBlock prev = blocks.get(i - 1);
                    prev.setSize(prev.getSize() + block.getSize());
                    blocks.remove(i);
                    i--;
                    block = prev;
                }

                // Merge with next block if it's free
                if (i + 1 < blocks.size() && blocks.get(i + 1).isFree()) {
                    MemoryBlock next = blocks.get(i + 1);
                    block.setSize(block.getSize() + next.getSize());
                    blocks.remove(i + 1);
                }

                return true;
            }
        }
        return false;
    }

    public void reset(List<MemoryBlock> newBlocks) {
        this.blocks = new LinkedList<>();
        for (MemoryBlock block : newBlocks) {
            this.blocks.add(new MemoryBlock(block.getStartAddress(), block.getSize())); // deep copy
        }
    }
}
