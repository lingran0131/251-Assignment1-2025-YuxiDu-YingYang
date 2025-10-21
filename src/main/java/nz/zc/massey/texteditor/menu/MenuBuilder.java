package nz.ac.massey.texteditor.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class MenuBuilder {
    private JTextArea textArea;
    private JFrame parentFrame;
    private boolean isModified = false;
    private File currentFile = null;
    private String lastSearchText = "";
    private int lastSearchPosition = 0;

    public MenuBuilder(JTextArea textArea, JFrame parentFrame) {
        this.textArea = textArea;
        this.parentFrame = parentFrame;

        // 设置键盘绑定
        setupKeyBindings();

        // 监听文本变化
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }

            private void setModified(boolean modified) {
                isModified = modified;
                // 在标题中显示修改状态
                String title = parentFrame.getTitle();
                if (modified && !title.startsWith("*")) {
                    parentFrame.setTitle("*" + title);
                } else if (!modified && title.startsWith("*")) {
                    parentFrame.setTitle(title.substring(1));
                }
            }
        });
    }

    private void setupKeyBindings() {
        // 为文本区域添加F3键监听
        textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("F3"), "findNext");
        textArea.getActionMap().put("findNext", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findNext();
            }
        });

        // 为文本区域添加Ctrl+G监听（备用）
        textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl G"), "findNextCtrlG");
        textArea.getActionMap().put("findNextCtrlG", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findNext();
            }
        });

        // 为文本区域添加F3在ANCESTOR中的绑定（增强兼容性）
        textArea.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("F3"), "findNextAncestor");
        textArea.getActionMap().put("findNextAncestor", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findNext();
            }
        });
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
        newItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        newItem.addActionListener(e -> newFile());

        // Open 菜单项
        JMenuItem openItem = new JMenuItem("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        openItem.addActionListener(e -> openFile());

        // Save 菜单项
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveItem.addActionListener(e -> saveFile());

        // 分隔线
        fileMenu.addSeparator();

        // Print 菜单项
        JMenuItem printItem = new JMenuItem("Print");
        printItem.setAccelerator(KeyStroke.getKeyStroke("ctrl P"));
        printItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(parentFrame, "Print functionality to be implemented");
        });

        // 分隔线
        fileMenu.addSeparator();

        // Exit 菜单项
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> exitApplication());

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
        cutItem.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
        cutItem.addActionListener(e -> textArea.cut());

        // Copy 菜单项
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        copyItem.addActionListener(e -> textArea.copy());

        // Paste 菜单项
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
        pasteItem.addActionListener(e -> textArea.paste());

        // 添加到Edit菜单
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        return editMenu;
    }

    private JMenu createSearchMenu() {
        JMenu searchMenu = new JMenu("Search");

        // Find 菜单项
        JMenuItem findItem = new JMenuItem("Find");
        findItem.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
        findItem.addActionListener(e -> showFindDialog());

        // Find Next 菜单项 - 使用 F3 和 Ctrl+G 双快捷键
        JMenuItem findNextItem = new JMenuItem("Find Next");
        findNextItem.setAccelerator(KeyStroke.getKeyStroke("F3"));
        findNextItem.addActionListener(e -> findNext());

        // Find Next Alternative 菜单项
        JMenuItem findNextAltItem = new JMenuItem("Find Next (Ctrl+G)");
        findNextAltItem.setAccelerator(KeyStroke.getKeyStroke("ctrl G"));
        findNextAltItem.addActionListener(e -> findNext());

        // Replace 菜单项（预留功能）
        JMenuItem replaceItem = new JMenuItem("Replace");
        replaceItem.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
        replaceItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(parentFrame, "Replace functionality to be implemented");
        });

        searchMenu.add(findItem);
        searchMenu.add(findNextItem);
        searchMenu.add(findNextAltItem);
        searchMenu.addSeparator();
        searchMenu.add(replaceItem);

        return searchMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("View");

        JMenuItem timeDateItem = new JMenuItem("Time/Date");
        timeDateItem.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
        timeDateItem.addActionListener(e -> insertTimeDate());

        viewMenu.add(timeDateItem);
        return viewMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> {
            String aboutMessage = "Massey Text Editor\n\n" +
                    "Developed by: Yuxi Du and Ying Yang\n" +
                    "Course: 159.251 Software Design and Construction\n" +
                    "Assignment 1 - 2025\n" +
                    "Version: 1.0.0";
            JOptionPane.showMessageDialog(parentFrame, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
        });

        helpMenu.add(aboutItem);
        return helpMenu;
    }

    // ========== 文件操作方法 ==========

    private void newFile() {
        // 如果文本有更改，提示保存
        if (isModified && textArea.getText().length() > 0) {
            int result = JOptionPane.showConfirmDialog(
                    parentFrame,
                    "Do you want to save changes to the current file?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                if (!saveFile()) {
                    return; // 如果保存失败或取消，不继续新建
                }
            } else if (result == JOptionPane.CANCEL_OPTION) {
                return; // 取消操作
            }
        }

        // 清空文本并重置状态
        textArea.setText("");
        currentFile = null;
        isModified = false;
        parentFrame.setTitle("Massey Text Editor - New File");
    }

    private void openFile() {
        // 检查当前文件是否有未保存的更改
        if (isModified && textArea.getText().length() > 0) {
            int result = JOptionPane.showConfirmDialog(
                    parentFrame,
                    "Do you want to save changes to the current file?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                if (!saveFile()) {
                    return; // 如果保存失败或取消，不继续打开
                }
            } else if (result == JOptionPane.CANCEL_OPTION) {
                return; // 取消操作
            }
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Text File");

        // 设置文件过滤器
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Text Files (*.txt)", "txt"));

        int userSelection = fileChooser.showOpenDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                File fileToOpen = fileChooser.getSelectedFile();

                // 读取文件内容
                String content = new String(java.nio.file.Files.readAllBytes(fileToOpen.toPath()));

                // 显示文件内容
                textArea.setText(content);

                // 更新状态
                currentFile = fileToOpen;
                isModified = false;

                // 更新窗口标题
                parentFrame.setTitle("Massey Text Editor - " + fileToOpen.getName());

                JOptionPane.showMessageDialog(parentFrame,
                        "File opened successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (java.nio.file.NoSuchFileException ex) {
                JOptionPane.showMessageDialog(parentFrame,
                        "File not found: " + ex.getMessage(),
                        "Open Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (java.nio.file.AccessDeniedException ex) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Access denied: " + ex.getMessage(),
                        "Open Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Error opening file: " + ex.getMessage(),
                        "Open Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean saveFile() {
        // 如果当前没有关联的文件，弹出保存对话框
        if (currentFile == null) {
            return saveAsFile();
        }

        try {
            // 写入文件
            java.nio.file.Files.write(
                    currentFile.toPath(),
                    textArea.getText().getBytes()
            );

            // 更新状态
            isModified = false;

            // 更新窗口标题
            parentFrame.setTitle("Massey Text Editor - " + currentFile.getName());

            JOptionPane.showMessageDialog(parentFrame,
                    "File saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame,
                    "Error saving file: " + ex.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean saveAsFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save File As");

        // 设置文件过滤器
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Text Files (*.txt)", "txt"));

        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                File fileToSave = fileChooser.getSelectedFile();
                // 确保文件有.txt扩展名
                if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
                }

                // 检查文件是否已存在
                if (fileToSave.exists()) {
                    int overwrite = JOptionPane.showConfirmDialog(
                            parentFrame,
                            "File already exists. Overwrite?",
                            "Confirm Overwrite",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (overwrite != JOptionPane.YES_OPTION) {
                        return false;
                    }
                }

                // 写入文件
                java.nio.file.Files.write(
                        fileToSave.toPath(),
                        textArea.getText().getBytes()
                );

                // 更新状态
                currentFile = fileToSave;
                isModified = false;

                // 更新窗口标题
                parentFrame.setTitle("Massey Text Editor - " + fileToSave.getName());

                JOptionPane.showMessageDialog(parentFrame,
                        "File saved successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Error saving file: " + ex.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    private void exitApplication() {
        // 检查是否有未保存的更改
        if (isModified && textArea.getText().length() > 0) {
            int result = JOptionPane.showConfirmDialog(
                    parentFrame,
                    "You have unsaved changes. Do you want to save before exiting?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                if (!saveFile()) {
                    return; // 如果保存失败或取消，不退出
                }
            } else if (result == JOptionPane.CANCEL_OPTION) {
                return; // 取消退出
            }
        }

        System.exit(0);
    }

    // ========== 搜索功能方法 ==========

    private void showFindDialog() {
        String searchText = JOptionPane.showInputDialog(
                parentFrame,
                "Enter text to find:",
                "Find",
                JOptionPane.QUESTION_MESSAGE
        );

        if (searchText != null && !searchText.trim().isEmpty()) {
            lastSearchText = searchText.trim();
            lastSearchPosition = 0;
            boolean found = findNext();

            if (!found) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Text not found: \"" + lastSearchText + "\"",
                        "Find",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private boolean findNext() {
        if (lastSearchText.isEmpty()) {
            showFindDialog();
            return false;
        }

        String content = textArea.getText();
        int position = content.indexOf(lastSearchText, lastSearchPosition);

        if (position >= 0) {
            // 高亮显示找到的文本
            textArea.setCaretPosition(position + lastSearchText.length());
            textArea.select(position, position + lastSearchText.length());
            textArea.grabFocus();

            lastSearchPosition = position + 1;
            return true;
        } else {
            // 从头开始搜索或显示未找到
            if (lastSearchPosition > 0) {
                lastSearchPosition = 0;
                JOptionPane.showMessageDialog(parentFrame,
                        "Search wrapped to beginning.",
                        "Find",
                        JOptionPane.INFORMATION_MESSAGE);
                return findNext();
            } else {
                return false;
            }
        }
    }

    // ========== 时间日期功能 ==========

    private void insertTimeDate() {
        String currentDateTime = java.time.LocalDateTime.now().toString();
        int caretPosition = textArea.getCaretPosition();
        textArea.insert(currentDateTime, caretPosition);
        isModified = true;
    }
}