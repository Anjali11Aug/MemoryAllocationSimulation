# MemoryAllocationSimulation
A Java-based dynamic memory allocation simulator using First-Fit and Best-Fit strategies with block splitting, coalescing, and interactive Swing GUI. Built with clean OOP and modular package design.

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

# Memory Allocator Simulator – Java + Swing

An interactive Java-based memory management simulator that mimics dynamic memory allocation using **First-Fit** and **Best-Fit** strategies. Built with clean Object-Oriented principles and a Swing-based GUI, the simulator supports **block splitting**, **coalescing**, and user-defined memory/process sizes to visualize how operating systems manage memory at runtime.

---

## Features

- Allocate and deallocate memory for simulated processes
- Choose between **First-Fit** and **Best-Fit** allocation strategies
- Accept custom process ID and memory size inputs
- GUI-based visualization of memory blocks (free/used)
- Supports **block splitting** and **coalescing** of memory
- Built with a modular package structure using OOP in Java

---

## How It Works

### 1. Memory Representation
- Memory is simulated using a `LinkedList` of `MemoryBlock` objects
- Each block tracks:
  - `startAddress`
  - `size`
  - `isFree`
  - `processId` (if allocated)

### 2. Allocation Strategies
- **First-Fit**: Allocates the first block that fits the requested size
  
  <img width="833" height="978" alt="graphviz (2)" src="https://github.com/user-attachments/assets/2068f9ac-fd9e-48d7-b023-5cb558f52dd8" />

- **Best-Fit**: Allocates the smallest block that fits to minimize waste
  
  <img width="700" height="1611" alt="graphviz (3)" src="https://github.com/user-attachments/assets/d7296622-ab77-4362-abf1-46d47a8e8678" />

- If the block is larger than required, it is **split** into two blocks

### 3. Deallocation & Coalescing
- Deallocates based on process ID
- Marks block as free
- Automatically **coalesces**:
  - Merges with previous free block (if any)
  - Merges with next free block (if any)
    

## 💡 Data Structures & Algorithms Used

| **Component**           | **Data Structure / Algorithm**         | **Purpose**                                                                 |
|-------------------------|----------------------------------------|------------------------------------------------------------------------------|
| `MemoryAllocator`       | `LinkedList<MemoryBlock>`              | Dynamic memory block management (insert, remove, split, merge)              |
| `MemoryAllocator`       | Greedy Algorithms: First-Fit, Best-Fit | Efficient block selection based on fit strategy                             |
| `MemoryAllocator`       | Manual Logic – Block Splitting         | Reduces internal fragmentation by allocating only required memory           |
| `MemoryAllocator`       | Manual Logic – Coalescing              | Merges adjacent free blocks after deallocation to reduce external fragmentation |
| `AllocationStrategy`    | `Enum`                                 | Clean implementation of strategy switching                                  |
| `SimulatorGUI`          | `Swing Components` (`JFrame`, `JPanel`) | Graphical user interface for visualization and interaction                  |
| `SimulatorGUI`          | `ArrayList` (GUI memory rendering)     | Used to iterate over memory blocks for visualization                        |


##  Project Structure

memorysimulator/
├── com.memorysimulator.Main
├── com.memorysimulator.gui.SimulatorGUI
├── com.memorysimulator.model.MemoryBlock
├── com.memorysimulator.model.Process
├── com.memorysimulator.allocator.MemoryAllocator
├── com.memorysimulator.utils.AllocationStrategy



## Class Diagram

<img width="1300" height="890" alt="graphviz (1)" src="https://github.com/user-attachments/assets/a4d4f68e-da5b-4c05-9eb1-6351a52b0819" />


## Future Enhancements
- Add Worst-Fit or Next-Fit strategy

- Export memory logs as CSV or JSON

- Add undo/redo functionality

- Convert to web-based version (JavaScript or React + Spring Boot backend)

- Add usage analytics: fragmentation %, peak memory, average load, etc.

