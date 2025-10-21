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

        // Set up keyboard bindings
        setupKeyBindings();

        // Listen for text changes
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }

            private void setModified(boolean modified) {
                isModified = modified;
                // Show modification status in title
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
        // Add F3 key listener for text area
        textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("F3"), "findNext");
        textArea.getActionMap().put("findNext", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findNext();
            }
        });

        // Add Ctrl+G listener (alternative)
        textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl G"), "findNextCtrlG");
        textArea.getActionMap().put("findNextCtrlG", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findNext();
            }
        });
    }

    public JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Build all menus
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createSearchMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createHelpMenu());

        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        // New menu item
        JMenuItem newItem = new JMenuItem("New");
        newItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        newItem.addActionListener(e -> newFile());

        // Open menu item
        JMenuItem openItem = new JMenuItem("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        openItem.addActionListener(e -> openFile());

        // Save menu item
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveItem.addActionListener(e -> saveFile());

        // Separator
        fileMenu.addSeparator();

        // Print menu item
        JMenuItem printItem = new JMenuItem("Print");
        printItem.setAccelerator(KeyStroke.getKeyStroke("ctrl P"));
        printItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(parentFrame, "Print functionality to be implemented");
        });

        // Separator
        fileMenu.addSeparator();

        // Exit menu item
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> exitApplication());

        // Add all items to File menu
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

        // Cut menu item
        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
        cutItem.addActionListener(e -> textArea.cut());

        // Copy menu item
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        copyItem.addActionListener(e -> textArea.copy());

        // Paste menu item
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
        pasteItem.addActionListener(e -> textArea.paste());

        // Add to Edit menu
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        return editMenu;
    }

    private JMenu createSearchMenu() {
        JMenu searchMenu = new JMenu("Search");

        // Find menu item
        JMenuItem findItem = new JMenuItem("Find");
        findItem.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
        findItem.addActionListener(e -> showFindDialog());

        // Find Next menu item
        JMenuItem findNextItem = new JMenuItem("Find Next");
        findNextItem.setAccelerator(KeyStroke.getKeyStroke("F3"));
        findNextItem.addActionListener(e -> findNext());

        // Find Next Alternative menu item
        JMenuItem findNextAltItem = new JMenuItem("Find Next (Ctrl+G)");
        findNextAltItem.setAccelerator(KeyStroke.getKeyStroke("ctrl G"));
        findNextAltItem.addActionListener(e -> findNext());

        // Replace menu item (placeholder)
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

    // ========== File Operation Methods ==========

    private void newFile() {
        // Prompt to save if there are unsaved changes
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
                    return; // Don't continue if save failed or cancelled
                }
            } else if (result == JOptionPane.CANCEL_OPTION) {
                return; // Cancel operation
            }
        }

        // Clear text and reset state
        textArea.setText("");
        currentFile = null;
        isModified = false;
        parentFrame.setTitle("Massey Text Editor - New File");
    }

    private void openFile() {
        // Check if current file has unsaved changes
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
                    return; // Don't continue if save failed or cancelled
                }
            } else if (result == JOptionPane.CANCEL_OPTION) {
                return; // Cancel operation
            }
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Text File");

        // Set file filter
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Text Files (*.txt)", "txt"));

        int userSelection = fileChooser.showOpenDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                File fileToOpen = fileChooser.getSelectedFile();

                // Read file content
                String content = new String(java.nio.file.Files.readAllBytes(fileToOpen.toPath()));

                // Display file content
                textArea.setText(content);

                // Update state
                currentFile = fileToOpen;
                isModified = false;

                // Update window title
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
        // Show save dialog if no file is associated
        if (currentFile == null) {
            return saveAsFile();
        }

        try {
            // Write to file
            java.nio.file.Files.write(
                    currentFile.toPath(),
                    textArea.getText().getBytes()
            );

            // Update state
            isModified = false;

            // Update window title
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

        // Set file filter
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Text Files (*.txt)", "txt"));

        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                File fileToSave = fileChooser.getSelectedFile();
                // Ensure file has .txt extension
                if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
                }

                // Check if file already exists
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

                // Write to file
                java.nio.file.Files.write(
                        fileToSave.toPath(),
                        textArea.getText().getBytes()
                );

                // Update state
                currentFile = fileToSave;
                isModified = false;

                // Update window title
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
        // Check for unsaved changes
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
                    return; // Don't exit if save failed or cancelled
                }
            } else if (result == JOptionPane.CANCEL_OPTION) {
                return; // Cancel exit
            }
        }

        System.exit(0);
    }

    // ========== Search Function Methods ==========

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
            // Highlight found text
            textArea.setCaretPosition(position + lastSearchText.length());
            textArea.select(position, position + lastSearchText.length());
            textArea.grabFocus();

            lastSearchPosition = position + 1;
            return true;
        } else {
            // Search from beginning or show not found
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

    // ========== Time/Date Function Methods ==========

    private void insertTimeDate() {
        String currentDateTime = java.time.LocalDateTime.now().toString();
        int caretPosition = textArea.getCaretPosition();
        textArea.insert(currentDateTime, caretPosition);
        isModified = true;
    }
}