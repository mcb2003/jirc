package club.lowerelements.jirc;

import javax.swing.SwingUtilities;

public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      var mainFrame = new MainFrame();
      mainFrame.setVisible(true);
    });
  }
}
