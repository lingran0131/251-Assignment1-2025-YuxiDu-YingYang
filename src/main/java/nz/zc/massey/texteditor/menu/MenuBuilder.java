package nz.zc.massey.texteditor.menu;

import nz.zc.massey.texteditor.config.ConfigManager;
import org.apache.poi.hwpf.HWPFDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuBuilder {
    private final JTextPane textPane;
    private final JFrame parentFrame;
    private final ConfigManager configManager;

    private boolean isModified = false;
    private File currentFile = null;
    private String lastSearchText = "";
    private int lastSearchPosition = 0;

    public MenuBuilder(JTextPane textPane, JFrame parentFrame, ConfigManager configManager) {
        this.textPane = textPane;
        this.parentFrame = parentFrame;
        this.configManager = configManager;

        setupKeyBindings();

        textPane.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void update(DocumentEvent e) {
                isModified = true;
                updateTitle();
            }
        });
    }

    private void setupKeyBindings() {
        InputMap im = textPane.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = textPane.getActionMap();

        im.put(KeyStroke.getKeyStroke("F3"), "findNext");
        am.put("findNext", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findNext();
            }
        });

        im.put(KeyStroke.getKeyStroke("ctrl G"), "findNextAlt");
        am.put("findNextAlt", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findNext();
            }
        });
    }

    private void updateTitle() {
        String title = parentFrame.getTitle();
        if (isModified && !title.startsWith("*")) {
            parentFrame.setTitle("*" + title);
        } else if (!isModified && title.startsWith("*")) {
            parentFrame.setTitle(title.substring(1));
        }
    }

    public JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();
        bar.add(createFileMenu());
        bar.add(createEditMenu());
        bar.add(createSearchMenu());
        bar.add(createViewMenu());
        bar.add(createHelpMenu());
        return bar;
    }

    // ========== 菜单创建 ==========
    private JMenu createFileMenu() {
        JMenu m = new JMenu("File");

        JMenuItem newItem = new JMenuItem("New");
        newItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        newItem.addActionListener(e -> newFile());

        JMenuItem openItem = new JMenuItem("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        openItem.addActionListener(e -> openFile());

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveItem.addActionListener(e -> saveFile());

        m.add(newItem);
        m.add(openItem);
        m.add(saveItem);

        return m;
    }

    private JMenu createEditMenu() {
        JMenu m = new JMenu("Edit");

        JMenuItem cut = new JMenuItem("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
        cut.addActionListener(e -> textPane.cut());

        JMenuItem copy = new JMenuItem("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        copy.addActionListener(e -> textPane.copy());

        JMenuItem paste = new JMenuItem("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
        paste.addActionListener(e -> textPane.paste());

        m.add(cut);
        m.add(copy);
        m.add(paste);

        return m;
    }

    private JMenu createSearchMenu() {
        JMenu m = new JMenu("Search");

        JMenuItem find = new JMenuItem("Find");
        find.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
        find.addActionListener(e -> showFindDialog());

        JMenuItem findNext = new JMenuItem("Find Next");
        findNext.setAccelerator(KeyStroke.getKeyStroke("F3"));
        findNext.addActionListener(e -> findNext());

        m.add(find);
        m.add(findNext);

        return m;
    }

    private JMenu createViewMenu() {
        JMenu m = new JMenu("View");

        JMenuItem timeDate = new JMenuItem("Time/Date");
        timeDate.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
        timeDate.addActionListener(e -> insertTimeDate());

        m.add(timeDate);
        return m;
    }

    private JMenu createHelpMenu() {
        JMenu m = new JMenu("Help");

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> {
            JOptionPane.showMessageDialog(parentFrame,
                    "Massey Text Editor\n\n" +
                            "开发者: Yuxi Du & Ying Yang\n" +
                            "课程: 159.251",
                    "关于", JOptionPane.INFORMATION_MESSAGE);
        });

        m.add(about);
        return m;
    }

    // ========== 文件操作 ==========
    private void newFile() {
        if (isModified && !textPane.getText().isEmpty()) {
            int result = JOptionPane.showConfirmDialog(parentFrame, "保存更改？", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.YES_OPTION) saveFile();
            else if (result == JOptionPane.CANCEL_OPTION) return;
        }

        textPane.setText("");
        currentFile = null;
        isModified = false;
        parentFrame.setTitle("Massey Text Editor - 新建");
    }

    private void openFile() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("文本文件", "txt", "java", "py", "js", "rtf", "odt"));

        if (fc.showOpenDialog(parentFrame) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            textPane.setText(content);
            currentFile = file;
            isModified = false;
            parentFrame.setTitle("编辑: " + file.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parentFrame, "打开失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean saveFile() {
        if (currentFile == null) return saveAsFile();

        try {
            Files.write(currentFile.toPath(), textPane.getText().getBytes());
            isModified = false;
            parentFrame.setTitle("编辑: " + currentFile.getName());
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parentFrame, "保存失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean saveAsFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(parentFrame) != JFileChooser.APPROVE_OPTION) return false;

        File file = fc.getSelectedFile();
        if (!file.getName().endsWith(".txt")) file = new File(file.getAbsolutePath() + ".txt");

        try {
            Files.write(file.toPath(), textPane.getText().getBytes());
            currentFile = file;
            isModified = false;
            parentFrame.setTitle("编辑: " + file.getName());
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parentFrame, "保存失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // ========== 搜索功能 ==========
    private void showFindDialog() {
        String txt = JOptionPane.showInputDialog(parentFrame, "查找:", "查找", JOptionPane.QUESTION_MESSAGE);
        if (txt != null && !txt.trim().isEmpty()) {
            lastSearchText = txt.trim();
            lastSearchPosition = 0;
            findNext();
        }
    }

    private boolean findNext() {
        if (lastSearchText.isEmpty()) return false;

        String content = textPane.getText();
        int pos = content.indexOf(lastSearchText, lastSearchPosition);

        if (pos >= 0) {
            textPane.setCaretPosition(pos + lastSearchText.length());
            textPane.select(pos, pos + lastSearchText.length());
            lastSearchPosition = pos + 1;
            return true;
        } else {
            lastSearchPosition = 0;
            JOptionPane.showMessageDialog(parentFrame, "已回到开头。", "查找", JOptionPane.INFORMATION_MESSAGE);
            return findNext();
        }
    }

    // ========== 时间日期 ==========
    private void insertTimeDate() {
        String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int pos = textPane.getCaretPosition();
        try {
            textPane.getDocument().insertString(pos, dt, null);
        } catch (Exception ignored) {}
    }

    // ========== 抽象监听器 ==========
    private abstract static class DocumentAdapter implements DocumentListener {
        @Override public void insertUpdate(DocumentEvent e) { update(e); }
        @Override public void removeUpdate(DocumentEvent e) { update(e); }
        @Override public void changedUpdate(DocumentEvent e) { update(e); }
        protected abstract void update(DocumentEvent e);
    }
}
