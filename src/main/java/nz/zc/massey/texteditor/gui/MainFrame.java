package nz.ac.massey.texteditor.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTextArea textArea;

    public MainFrame() {
        initializeGUI();
        setupWindowProperties();
    }

    private void initializeGUI() {
        setLayout(new BorderLayout());
        createTextArea();
    }

    private void createTextArea() {
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupWindowProperties() {
        setTitle("Text Editor - 159.251 Assignment 1");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JTextArea getTextArea() {
        return textArea;
    }
}