package nz.ac.massey.texteditor;

import nz.ac.massey.texteditor.gui.MainFrame;
import javax.swing.*;

public class TextEditorApp {
    public static void main(String[] args) {
        // Use SwingUtilities for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Set cross-platform look and feel for better compatibility
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Create and display main window
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}