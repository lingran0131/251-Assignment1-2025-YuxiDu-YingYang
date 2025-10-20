package nz.ac.massey.texteditor.gui;

import nz.ac.massey.texteditor.menu.MenuBuilder;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTextArea textArea;
    private JScrollPane scrollPane;

    public MainFrame() {
        initializeGUI();
        setupWindowProperties();
    }

    private void initializeGUI() {
        // 设置布局
        setLayout(new BorderLayout());

        // 创建文本编辑区域
        createTextArea();

        // 创建菜单栏
        createMenuBar();
    }

    private void createTextArea() {
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void createMenuBar() {
        MenuBuilder menuBuilder = new MenuBuilder(textArea, this);
        JMenuBar menuBar = menuBuilder.buildMenuBar();
        setJMenuBar(menuBar);
    }

    private void setupWindowProperties() {
        setTitle("Massey Text Editor - 159.251 Assignment 1");
        setSize(1000, 700);
        setLocationRelativeTo(null); // 窗口居中
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JTextArea getTextArea() {
        return textArea;
    }
}