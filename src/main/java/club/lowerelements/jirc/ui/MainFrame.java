package club.lowerelements.jirc;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.event.*;

public class MainFrame extends JFrame {
  private JTree chatsTree;
  private JList messageList;
  private JTextField messageField;

  MainFrame(Controller controller) {
    addWindowListener(controller.getWindowListener());

    setTitle("JIRC");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    setJMenuBar(makeMenuBar(controller));
    add(makeToolBar(controller), BorderLayout.NORTH);

    chatsTree = new JTree(controller.getNetworkManager().getModel());
    chatsTree.getAccessibleContext().setAccessibleName("Channels");
    chatsTree.setRootVisible(false);
    chatsTree.addTreeSelectionListener(new ChatsSelectionListener());

    var messagePanel = new JPanel(new BorderLayout());

    messageList = new JList();
    messageList.getAccessibleContext().setAccessibleName("Messages");
    messagePanel.add(new JScrollPane(messageList), BorderLayout.CENTER);

    messageField = new JTextField();
    messageField.setEditable(false);
    messageField.getAccessibleContext().setAccessibleName("Message");
    messagePanel.add(messageField, BorderLayout.PAGE_END);

    var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                   new JScrollPane(chatsTree), messagePanel);
    splitPane.setResizeWeight(0.25);
    splitPane.setDividerLocation(0.25);
    add(splitPane, BorderLayout.CENTER);
  }

  private JMenuBar makeMenuBar(Controller controller) {
    JMenuBar menuBar = new JMenuBar();
    JMenu menu;
    JMenuItem item;

    menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_F);

    item = new JMenuItem(controller.getAddNetworkAction());
    menu.add(item);
    item = new JMenuItem(controller.getNewWindowAction());
    menu.add(item);
    item = new JMenuItem(controller.getExitAction());
    menu.add(item);

    menuBar.add(menu);

    return menuBar;
  }

  private JToolBar makeToolBar(Controller controller) {
    var tb = new JToolBar("Main");
    tb.getAccessibleContext().setAccessibleName("Main");

    tb.add(new JButton(controller.getAddNetworkAction()));

    return tb;
  }

  private class ChatsSelectionListener implements TreeSelectionListener {
    public void valueChanged(TreeSelectionEvent e) {
      Object selected = chatsTree.getLastSelectedPathComponent();
      if (selected instanceof Chat log) {
        messageList.setModel(log.getMessageList());
        messageField.getAccessibleContext().setAccessibleName("Message to " +
                                                              log.getChatName());
        setTitle(log.getChatName() + " - Jirc");
        messageField.setEditable(!log.isLogReadOnly());
        if (log.isLogReadOnly()) {
          messageField.setText("Chat is read-only");
        } else {
          messageField.setText("");
        }
      } else {
        messageList.setModel(new DefaultListModel());
        setTitle("Jirc");
        messageField.setEditable(false);
        messageField.setText("Chat is read-only");
        messageField.getAccessibleContext().setAccessibleName("Message");
      }
    }
  }
}
