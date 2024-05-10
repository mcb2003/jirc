package club.lowerelements.jirc;

import java.awt.Component;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Controller {
  // Models:
  private NetworkManager networks = new NetworkManager();

  // Views:
  private Set<MainFrame> windows = new HashSet<>();

  // Actions and other listeners:
  private WindowHandler windowAdapter = new WindowHandler();
  private ActionListener messageSendHandler = new MessageSendHandler();

  private Action exitAction = new ExitAction();
  private Action newWindowAction = new NewWindowAction();
  private Action addNetworkAction = new AddNetworkAction();

  NetworkManager getNetworkManager() { return networks; }

  public ActionListener getMessageSendHandler() { return messageSendHandler; }

  public void shutdown() { networks.shutdown(); }

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
      if (windows.isEmpty()) {
        shutdown();
      }
    }
  }

  Action getAddNetworkAction() { return addNetworkAction; }

  private class AddNetworkAction extends AbstractAction {
    public AddNetworkAction() {
      super("Add Network");
      putValue(SHORT_DESCRIPTION, "Connect to a new IRC network");
      putValue(MNEMONIC_KEY, KeyEvent.VK_A);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      var dlg = new NetworkDialog((Component)e.getSource());
      dlg.addNetworkListener(new NetworkDialog.Listener() {
        @Override
        public void networkAdded(NetworkDialog.AddEvent e) {
          var network = new Network(e.getNetworkInfo());
          networks.addNetwork(network);
        }
      });
      dlg.setVisible(true);
    }
  }

  private class MessageSendHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      var messageField = (JTextField)e.getSource();
      var msg = messageField.getText();
      var frame = MainFrame.fromChild(messageField);
      Object selected = frame.getSelectedChat();
      if (selected instanceof Messageable chat) {
        chat.getMessageReceiver().sendMessage(msg);
        var network = chat.getNetwork();
        if (!network.isCapabilityEnabled("echo-message")) {
          var self = network.getClient().getUser().get();
          var echoedMessage = new PrivMessage(self, msg);
          chat.getMessageList().addMessage(echoedMessage);
        }
        messageField.setText("");
      }
      messageField.requestFocusInWindow();
    }
  }
}
