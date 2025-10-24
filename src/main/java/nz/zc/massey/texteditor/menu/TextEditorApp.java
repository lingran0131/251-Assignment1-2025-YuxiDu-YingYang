package nz.zc.massey.texteditor;

import nz.zc.massey.texteditor.gui.MainFrame;
import javax.swing.*;

public class TextEditorApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame().setVisible(true);
        });
    }
}
