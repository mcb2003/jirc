package club.lowerelements.jirc;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Controller {
  private Set<MainFrame> windows = new HashSet<>();

  // Actions and other listeners:
  private WindowHandler windowAdapter = new WindowHandler();
  private Action exitAction = new ExitAction();
  private Action newWindowAction = new NewWindowAction();

  Action getExitAction() { return exitAction; }

  private class ExitAction extends AbstractAction {
    public ExitAction() {
      super("Exit");
      putValue(SHORT_DESCRIPTION, "Terminate the application");
      putValue(MNEMONIC_KEY, KeyEvent.VK_X);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      // Close and dereference all open windows
      for (var w : windows) {
        w.dispose();
      }
    }
  }

  public void newWindow() {
    SwingUtilities.invokeLater(() -> {
      MainFrame w = new MainFrame(this);
      windows.add(w);
      w.setVisible(true);
    });
  }

  Action getNewWindowAction() { return newWindowAction; }

  private class NewWindowAction extends AbstractAction {
    public NewWindowAction() {
      super("New Window");
      putValue(SHORT_DESCRIPTION, "Create a new chat window");
      putValue(MNEMONIC_KEY, KeyEvent.VK_W);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      newWindow();
    }
  }

  WindowListener getWindowListener() { return windowAdapter; }

  private class WindowHandler extends WindowAdapter {
    @Override
    public void windowClosed(WindowEvent e) {
      // Remove the window from our Set
      var w = (MainFrame)e.getSource();
      windows.remove(w);
    }
  }
}
