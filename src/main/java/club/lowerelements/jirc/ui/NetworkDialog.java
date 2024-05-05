package club.lowerelements.jirc;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class NetworkDialog extends JDialog {
  private Set<Listener> listeners = new HashSet<>();

  // Server tab:
  private JTextField hostField = new JTextField();
  private JSpinner portField =
      new JSpinner(new SpinnerNumberModel(6697, 1, 65535, 1));
  private JCheckBox secureToggle =
      new JCheckBox("Use secure connection (TLS)", true);

  // User tab:
  private JTextField nickField = new JTextField();
  private JTextField userField = new JTextField();
  private JTextField realNameField = new JTextField();

  public NetworkDialog(Component parent) {
    super(SwingUtilities.getWindowAncestor(parent), "Add Network");
    setLocationRelativeTo(parent);

    JComponent content = makeUI();

    var addButton = new JButton("Add");
    addButton.setMnemonic(KeyEvent.VK_A);
    addButton.addActionListener(this::onAdd);
    var cancelButton = new JButton("Cancel");
    cancelButton.setMnemonic(KeyEvent.VK_C);
    cancelButton.addActionListener(e -> this.dispose());
    Object[] buttons = {addButton, cancelButton};

    var pane = new JOptionPane(content, JOptionPane.PLAIN_MESSAGE,
                               JOptionPane.OK_CANCEL_OPTION, null, buttons,
                               buttons[0]);
    setContentPane(pane);

    pack();
    hostField.requestFocusInWindow();
  }

  private JComponent makeUI() {
    var tabs = new JTabbedPane();
    JPanel tab;

    tab = new JPanel(new GridBagLayout());
    hostField.setToolTipText("domain name or IP address of the IRC server");
    addField(tab, 0, "Host:", KeyEvent.VK_H, hostField);
    addField(tab, 1, "Port:", KeyEvent.VK_P, portField);

    secureToggle.setMnemonic(KeyEvent.VK_T);
    var c = new GridBagConstraints();
    c.gridx = 2;
    c.gridy = 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 0.7;
    c.weighty = 0.5;
    tab.add(secureToggle, c);

    tabs.addTab("Server", null, tab, "General connection information");
    tabs.setMnemonicAt(0, KeyEvent.VK_S);

    tab = new JPanel(new GridBagLayout());
    nickField.setToolTipText("You'll be referred to using this name");
    addField(tab, 0, "Nickname:", KeyEvent.VK_N, nickField);
    userField.setToolTipText(
        "Typically your username on the machine you're connecting from");
    userField.setText(System.getProperty("user.name"));
    addField(tab, 1, "Username (Ident):", KeyEvent.VK_I, userField);
    addField(tab, 2, "Real Name:", KeyEvent.VK_R, realNameField);
    tabs.addTab("User", null, tab, "User identification and credentials");
    tabs.setMnemonicAt(1, KeyEvent.VK_U);

    return tabs;
  }

  private static void addField(Container tab, int y, String lbl, int mnemonic,
                               Component component) {
    JLabel label = new JLabel(lbl);
    label.setDisplayedMnemonic(mnemonic);

    // We have to do this because ATs focus on a sub-component of a JSpinner
    if (component instanceof JSpinner spinner) {
      label.setLabelFor(spinner.getEditor());
    } else {
      label.setLabelFor(component);
    }

    var c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = y;
    c.weightx = 0.3;
    c.weighty = 0.5;
    tab.add(label, c);

    c.gridx = GridBagConstraints.REMAINDER;
    c.weightx = 0.7;
    c.fill = GridBagConstraints.HORIZONTAL;
    tab.add(component, c);
  }

  public NetworkInfo getNetworkInfo() {
    var ni = new NetworkInfo();
    ni.host = hostField.getText();
    ni.port = ((Integer)portField.getValue()).shortValue();
    ni.tls = secureToggle.isSelected();
    ni.nick = nickField.getText();
    ni.user = userField.getText();
    ni.realName = realNameField.getText();
    return ni;
  }

  private void onAdd(ActionEvent e) {
    var ev = new AddEvent(e);
    for (var l : listeners) {
      l.networkAdded(ev);
    }
    dispose();
  }

  public void addNetworkListener(Listener l) { listeners.add(l); }

  public void removeNetworkListener(Listener l) { listeners.remove(l); }

  public class AddEvent extends EventObject {
    private ActionEvent cause;

    public AddEvent(ActionEvent cause) {
      super(NetworkDialog.this);
      this.cause = cause;
    }

    public ActionEvent getCause() { return cause; }

    public NetworkDialog getNetworkDialog() { return NetworkDialog.this; }

    public NetworkInfo getNetworkInfo() {
      return NetworkDialog.this.getNetworkInfo();
    }
  }

  public interface Listener extends EventListener {
    public void networkAdded(AddEvent e);
  }
}
