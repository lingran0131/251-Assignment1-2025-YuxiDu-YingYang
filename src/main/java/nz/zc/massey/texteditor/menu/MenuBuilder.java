package nz.ac.massey.texteditor.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBuilder {
    private JTextArea textArea;
    private JFrame parentFrame;

    public MenuBuilder(JTextArea textArea, JFrame parentFrame) {
        this.textArea = textArea;
        this.parentFrame = parentFrame;
    }

    public JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // 构建各个菜单
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createSearchMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createHelpMenu());

        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        // New 菜单项
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(e -> textArea.setText(""));

        // Open 菜单项
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(parentFrame, "Open functionality to be implemented");
        });

        // Save 菜单项
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(parentFrame, "Save functionality to be implemented");
        });

        // 分隔线
        fileMenu.addSeparator();

        // Print 菜单项
        JMenuItem printItem = new JMenuItem("Print");
        printItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(parentFrame, "Print functionality to be implemented");
        });

        // 分隔线
        fileMenu.addSeparator();

        // Exit 菜单项
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        // 添加所有菜单项到File菜单
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(printItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        return fileMenu;
    }

    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Edit");

        // Cut 菜单项
        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.addActionListener(e -> textArea.cut());

        // Copy 菜单项
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(e -> textArea.copy());

        // Paste 菜单项
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.addActionListener(e -> textArea.paste());

        // 添加到Edit菜单
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        return editMenu;
    }

    private JMenu createSearchMenu() {
        JMenu searchMenu = new JMenu("Search");

        JMenuItem searchItem = new JMenuItem("Find");
        searchItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(parentFrame, "Search functionality to be implemented");
        });

        searchMenu.add(searchItem);
        return searchMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("View");

        JMenuItem timeDateItem = new JMenuItem("Time/Date");
        timeDateItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(parentFrame, "Time/Date functionality to be implemented");
        });

        viewMenu.add(timeDateItem);
        return viewMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> {
            String aboutMessage = "Massey Text Editor\n\n" +
                    "Developed by: [Your Name] and [Partner's Name]\n" +
                    "Course: 159.251 Software Design and Construction\n" +
                    "Assignment 1 - 2025";
            JOptionPane.showMessageDialog(parentFrame, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
        });

        helpMenu.add(aboutItem);
        return helpMenu;
    }
}