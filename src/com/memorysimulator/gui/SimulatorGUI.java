package com.memorysimulator.gui;

import com.memorysimulator.allocator.MemoryAllocator;
import com.memorysimulator.model.MemoryBlock;
import com.memorysimulator.model.Process;
import com.memorysimulator.utils.AllocationStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

public class SimulatorGUI {

    private JFrame frame;
    private JPanel memoryPanel;
    private JTextField pidField, sizeField;
    private JComboBox<AllocationStrategy> strategyBox;
    private MemoryAllocator allocator;
    private List<MemoryBlock> initialBlocks;

    public SimulatorGUI() {
        initializeMemory();
        initializeUI();
        drawMemoryBlocks(allocator.getBlocks());
    }

    private void initializeMemory() {
        initialBlocks = new LinkedList<>();
        int currentStart = 0;

        String input = JOptionPane.showInputDialog(null, "Enter block sizes (comma-separated):",
                "Setup Memory", JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            String[] parts = input.split(",");
            for (String part : parts) {
                try {
                    int size = Integer.parseInt(part.trim());
                    initialBlocks.add(new MemoryBlock(currentStart, size));
                    currentStart += size;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input: " + part);
                    System.exit(0);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No block sizes entered. Exiting.");
            System.exit(0);
        }

        allocator = new MemoryAllocator(initialBlocks, AllocationStrategy.BEST_FIT);
    }

    private void initializeUI() {
        frame = new JFrame("Memory Allocator Simulator");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(Color.WHITE);

        pidField = new JTextField(5);
        sizeField = new JTextField(5);
        strategyBox = new JComboBox<>(AllocationStrategy.values());

        JButton allocateBtn = new JButton("Allocate");
        JButton deallocateBtn = new JButton("Deallocate");
        JButton resetBtn = new JButton("Reset");

        controlPanel.add(new JLabel("PID:"));
        controlPanel.add(pidField);
        controlPanel.add(new JLabel("Size:"));
        controlPanel.add(sizeField);
        controlPanel.add(strategyBox);
        controlPanel.add(allocateBtn);
        controlPanel.add(deallocateBtn);
        controlPanel.add(resetBtn);

        frame.add(controlPanel, BorderLayout.NORTH);

        memoryPanel = new JPanel();
        memoryPanel.setBackground(Color.WHITE);
        memoryPanel.setLayout(new BoxLayout(memoryPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(memoryPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane, BorderLayout.CENTER);

        allocateBtn.addActionListener(this::allocate);
        deallocateBtn.addActionListener(this::deallocate);
        resetBtn.addActionListener(this::reset);

        frame.setVisible(true);
    }

    private void drawMemoryBlocks(List<MemoryBlock> blocks) {
        memoryPanel.removeAll();
        for (MemoryBlock block : blocks) {
            String text = "Start: " + block.getStartAddress() +
                    " | Size: " + block.getSize() +
                    " | " + (block.isFree() ? "Free" : "Allocated to " + block.getProcessId());
            JLabel label = new JLabel(text);
            label.setOpaque(true);
            label.setBackground(block.isFree() ? Color.LIGHT_GRAY : Color.DARK_GRAY);
            label.setForeground(block.isFree() ? Color.BLACK : Color.WHITE);
            label.setFont(new Font("Monospaced", Font.PLAIN, 14));
            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            memoryPanel.add(label);
        }
        memoryPanel.revalidate();
        memoryPanel.repaint();
    }

    private void allocate(ActionEvent e) {
        String pid = pidField.getText().trim();
        String sizeText = sizeField.getText().trim();

        if (pid.isEmpty() || sizeText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter PID and Size.");
            return;
        }

        try {
            int size = Integer.parseInt(sizeText);
            Process p = new Process(pid, size);
            allocator.setStrategy((AllocationStrategy) strategyBox.getSelectedItem());

            boolean success = allocator.allocate(p);
            if (!success) {
                JOptionPane.showMessageDialog(frame, "Allocation failed: No suitable block.");
            }

            drawMemoryBlocks(allocator.getBlocks());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Size must be a valid number.");
        }
    }

    private void deallocate(ActionEvent e) {
        String pid = pidField.getText().trim();
        if (pid.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Enter a process ID to deallocate.");
            return;
        }

        boolean success = allocator.deallocate(pid);
        if (!success) {
            JOptionPane.showMessageDialog(frame, "Deallocation failed: Process not found.");
        }

        drawMemoryBlocks(allocator.getBlocks());
    }

    private void reset(ActionEvent e) {
        allocator.reset(initialBlocks);
        drawMemoryBlocks(allocator.getBlocks());
    }
}
