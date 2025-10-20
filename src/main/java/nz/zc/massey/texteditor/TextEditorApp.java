package nz.ac.massey.texteditor;

import nz.ac.massey.texteditor.gui.MainFrame;
import javax.swing.*;

public class TextEditorApp {
    public static void main(String[] args) {
        // 使用SwingUtilities确保线程安全
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // 设置跨平台外观，兼容性更好
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 创建并显示主窗口
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}