package nz.zc.massey.texteditor.gui;

import nz.zc.massey.texteditor.config.ConfigManager;
import nz.zc.massey.texteditor.menu.MenuBuilder;
import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTextPane textPane; // 移除 final
    private JScrollPane scrollPane;
    private final ConfigManager configManager = new ConfigManager();

    public MainFrame() {
        initializeGUI();
        setupWindowProperties();
    }

    private void initializeGUI() {
        setLayout(new BorderLayout());
        createTextArea();
        createMenuBar();
    }

    private void createTextArea() {
        textPane = new JTextPane();
        textPane.setEditorKit(new HTMLEditorKit()); // 支持换行
        textPane.setFont(new Font(configManager.getFontName(), Font.PLAIN, configManager.getFontSize()));

        scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void createMenuBar() {
        MenuBuilder menuBuilder = new MenuBuilder(textPane, this, configManager);
        setJMenuBar(menuBuilder.buildMenuBar());
    }

    private void setupWindowProperties() {
        setTitle("Massey Text Editor - 159.251 Assignment 1");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JTextPane getTextPane() {
        return textPane;
    }
}
